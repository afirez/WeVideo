package com.afirez.wevideo.api;

import android.text.TextUtils;
import android.util.Log;

import com.afirez.wevideo.channel.model.Album;
import com.afirez.wevideo.channel.model.ErrorInfo;
import com.afirez.wevideo.channel.model.Site;
import com.afirez.wevideo.common.utils.OkHttpUtils;
import com.afirez.wevideo.home.model.Channel;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by afirez on 18-3-31.
 */

public class LetvApi implements SiteApi {

    private static final String TAG = LetvApi.class.getSimpleName();
    private static final int LETV_CHANNELID_MOVIE = 1; //乐视电影频道ID
    private static final int LETV_CHANNELID_SERIES = 2; //乐视电视剧频道ID
    private static final int LETV_CHANNELID_VARIETY = 11; //乐视综艺频道ID
    private static final int LETV_CHANNELID_DOCUMENTRY = 16; //乐视纪录片频道ID
    private static final int LETV_CHANNELID_COMIC = 5; //乐视动漫频道ID
    private static final int LETV_CHANNELID_MUSIC = 9; //乐视音乐频道ID
    private static final int BITSTREAM_SUPER = 100;
    private static final int BITSTREAM_NORMAL = 101;
    private static final int BITSTREAM_HIGH = 102;
    //http://static.meizi.app.m.letv.com/android/mod/mob/ctl/listalbum/act/index/src/1/cg/2/or/20/vt/180001/ph/420003,420004/pt/-141003/pn/1/ps/30/pcode/010110263/version/5.6.2.mindex.html
    private final static String ALBUM_LIST_URL_FORMAT = "http://static.meizi.app.m.letv.com/android/" +
            "mod/mob/ctl/listalbum/act/index/src/1/cg/%s/ph/420003,420004/pn/%s/ps/%s/pcode/010110263/version/5.6.2.mindex.html";

    private final static String ALBUM_LIST_URL_DOCUMENTARY_FORMAT = "http://static.meizi.app.m.letv.com/android/" +
            "mod/mob/ctl/listalbum/act/index/src/1/cg/%s/or/3/ph/420003,420004/pn/%s/ps/%s/pcode/010110263/version/5.6.2.mindex.html";

    private final static String ALBUM_LIST_URL_SHOW_FORMAT = "http://static.meizi.app.m.letv.com/android/" +
            "mod/mob/ctl/listalbum/act/index/src/1/cg/%s/or/20/vt/180001/ph/420003,420004/pt/-141003/pn/%s/ps/%s/pcode/010110263/version/5.6.2.mindex.html";

    //http://static.meizi.app.m.letv.com/android/mod/mob/ctl/album/act/detail/id/10026309/pcode/010410000/version/2.1.mindex.html
    private final static String ALBUM_DESC_URL_FORMAT = "http://static.meizi.app.m.letv.com/" +
            "android/mod/mob/ctl/album/act/detail/id/%s/pcode/010410000/version/2.1.mindex.html";
    //key : bh65OzqYYYmHRQ
    private final static String SEVER_TIME_URL = "http://dynamic.meizi.app.m.letv.com/android/dynamic.php?mod=mob&ctl=timestamp&act=timestamp&pcode=010410000&version=5.4";

    //http://static.app.m.letv.com/android/mod/mob/ctl/videolist/act/detail/id/10026309/vid/0/b/1/s/30/o/-1/m/1/pcode/010410000/version/2.1.mindex.html
    private final static String ALBUM_VIDEOS_URL_FORMAT = "http://static.app.m.letv.com/" +
            "android/mod/mob/ctl/videolist/act/detail/id/%s/vid/0/b/%s/s/%s/o/%s/m/%s/pcode/010410000/version/2.1.mindex.html";

    //arg: mmsid currentServerTime key vid
    private final static String VIDEO_FILE_URL_FORMAT = "http://dynamic.meizi.app.m.letv.com/android/dynamic.php?mmsid=" +
            "%s&playid=0&tss=ios&pcode=010410000&version=2.1&tm=%s&key=%s&vid=" +
            "%s&ctl=videofile&mod=minfo&act=index";

    private final static String VIDEO_REAL_LINK_APPENDIX = "&format=1&expect=1&termid=2&pay=0&ostype=android&hwtype=iphone";

    //http://play.g3proxy.lecloud.com/vod/v2/MjYwLzkvNTIvbGV0di11dHMvMTQvdmVyXzAwXzIyLTEwOTczMjQ5NzUtYXZjLTE5OTY1OS1hYWMtNDgwMDAtMjU4NjI0MC04Mzk3NjQ4OC04MmQxMGVlM2I3ZTdkMGU5ZjE4YzM1NDViMWI4MzI4Yi0xNDkyNDA2MDE2MTg4Lm1wNA==?b=259&mmsid=64244666&tm=1492847915&key=22f2f114ed643e0d08596659e5834cd6&platid=3&splatid=347&playid=0&tss=ios&vtype=21&cvid=711590995389&payff=0&pip=83611a86979ddb3df8ef0fb41034f39c&format=1&sign=mb&dname=mobile&expect=3&p1=0&p2=00&p3=003&tag=mobile&pid=10031263&format=1&expect=1&termid=2&pay=0&ostype=android&hwtype=iphone

    private Long mTimeOffSet = Long.MAX_VALUE;

    public LetvApi() {
    }

    private void fetchServerTime() {
        if (mTimeOffSet != Long.MAX_VALUE) {
            return;
        }
        OkHttpUtils.execute(SEVER_TIME_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, ">> onFailure !!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                if (body == null || !response.isSuccessful()) {
                    Log.d(TAG, ">> onResponse failed!!");
                    return;
                }
                String result = body.string();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String time = jsonObject.optString("time");
                    updateServerTime(Long.parseLong(time));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void updateServerTime(Long time) {
        if (mTimeOffSet == Long.MAX_VALUE) {
            mTimeOffSet = System.currentTimeMillis() / 1000 - time;
        }
    }

    @Override
    public void getChannelAlbums(
            Channel channel,
            int page,
            int pageSize,
            ApiCallback<ArrayList<Album>> callback) {
        fetchServerTime();
        String url = buildChannelAlbumUrl(channel, page, pageSize);
        fetchChannelAlbums(url, callback);
    }

    private String buildChannelAlbumUrl(Channel channel, int page, int pageSize) {
        if (channel.getId() == Channel.DOCUMENTARY) {
            return String.format(ALBUM_LIST_URL_DOCUMENTARY_FORMAT, convertChannelId(channel), page, pageSize);
        } else if (channel.getId() == Channel.SHOW) {
            return String.format(ALBUM_LIST_URL_SHOW_FORMAT, convertChannelId(channel), page, pageSize);
        }
        return String.format(ALBUM_LIST_URL_FORMAT, convertChannelId(channel), page, pageSize);

    }

    private int convertChannelId(Channel channel) {
        int channelId = -1;//-1 无效值
        switch (channel.getId()) {
            case Channel.SHOW:
                channelId = LETV_CHANNELID_SERIES;
                break;
            case Channel.MOVIE:
                channelId = LETV_CHANNELID_MOVIE;
                break;
            case Channel.COMIC:
                channelId = LETV_CHANNELID_COMIC;
                break;
            case Channel.MUSIC:
                channelId = LETV_CHANNELID_MUSIC;
                break;
            case Channel.DOCUMENTARY:
                channelId = LETV_CHANNELID_DOCUMENTRY;
                break;
            case Channel.VARIETY:
                channelId = LETV_CHANNELID_VARIETY;
                break;
        }
        return channelId;
    }

    private void fetchChannelAlbums(final String url, final ApiCallback<ArrayList<Album>> callback) {
        if (url == null || url.length() == 0 || callback == null) {
            return;
        }
        OkHttpUtils.execute(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ErrorInfo info = buildErrorInfo(url, "fetchChannelAlbums", e, ErrorInfo.ERROR_TYPE_URL);
                callback.onError(info);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                if (body == null || !response.isSuccessful()) {
                    ErrorInfo info = buildErrorInfo(url, "fetchChannelAlbums", null, ErrorInfo.ERROR_TYPE_HTTP);
                    callback.onError(info);
                    return;
                }
                String json = body.string();
                try {
                    JSONObject resultJson = new JSONObject(json);
                    JSONObject bodyJson = resultJson.optJSONObject("body");
                    if (bodyJson.optInt("album_count") > 0) {
                        ArrayList<Album> list = new ArrayList<>();
                        JSONArray albumListJosn = bodyJson.optJSONArray("album_list");
                        for (int i = 0; i < albumListJosn.length(); i++) {
                            Album album = new Album(Site.LETV);
                            JSONObject albumJson = albumListJosn.getJSONObject(i);
                            album.setAlbumId(albumJson.getString("aid"));
                            album.setAlbumDesc(albumJson.getString("subname"));
                            album.setTitle(albumJson.getString("name"));
                            album.setTip(albumJson.getString("subname"));
                            JSONObject jsonImage = albumJson.getJSONObject("images");
                            //读取【400*300】字符
                            String imageurl = StringEscapeUtils.unescapeJava(jsonImage.getString("400*300"));
                            album.setHorImgUrl(imageurl);
                            list.add(album);
                        }
                        if (list.size() > 0) {
                            callback.onSuccess(list);
                        } else {
                            ErrorInfo info = buildErrorInfo(url, "fetchChannelAlbums", null, ErrorInfo.ERROR_TYPE_DATA_CONVERT);
                            callback.onError(info);
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    ErrorInfo info = buildErrorInfo(url, "fetchChannelAlbums", null, ErrorInfo.ERROR_TYPE_PARSE_JSON);
                    callback.onError(info);

                }
            }
        });
    }

    private ErrorInfo buildErrorInfo(String url, String functionName, IOException e, int type) {
        ErrorInfo info = new ErrorInfo(Site.LETV, type);
        info.setExceptionString(e.getMessage());
        info.setFunctionName(functionName);
        info.setUrl(url);
        info.setTag(TAG);
        info.setClassName(TAG);
        return info;
    }

    @Override
    public void getAlbumDetail(final Album album, final ApiCallback<Album> callback) {
        final String url = String.format(ALBUM_DESC_URL_FORMAT, album.getAlbumId());
        if (url == null || url.length() == 0 || callback == null) {
            return;
        }
        OkHttpUtils.execute(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ErrorInfo errorInfo = buildErrorInfo(url, "getAlbumDetail", null, ErrorInfo.ERROR_TYPE_URL);
                callback.onError(errorInfo);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                if (body == null || !response.isSuccessful()) {
                    ErrorInfo errorInfo = buildErrorInfo(url, "getAlbumDetail", null, ErrorInfo.ERROR_TYPE_HTTP);
                    callback.onError(errorInfo);
                    return;
                }

                String json = body.string();
                try {
                    JSONObject albumJson = new JSONObject(json);
                    if (albumJson.optJSONObject("body") != null) {
                        JSONObject albumJsonBody = albumJson.optJSONObject("body");
                        if (albumJsonBody.optJSONObject("picCollections") != null) {
                            JSONObject jsonImg = albumJsonBody.optJSONObject("picCollections");
                            if (!TextUtils.isEmpty(jsonImg.optString("150*200"))) {
                                album.setHorImgUrl(StringEscapeUtils.unescapeJava(jsonImg.optString("150*200")));
                            }
                        }
                        if (!TextUtils.isEmpty(albumJsonBody.optString("description"))) {
                            album.setAlbumDesc(albumJsonBody.optString("description"));
                        }
                        if (!TextUtils.isEmpty(albumJsonBody.optString("nowEpisodes"))) {
                            album.setVideoTotal(Integer.parseInt(albumJsonBody.optString("nowEpisodes")));
                        }
                        //directory starring
                        if (!TextUtils.isEmpty(albumJsonBody.optString("directory"))) {
                            album.setDirector(albumJsonBody.optString("directory"));
                        }
                        if (!TextUtils.isEmpty(albumJsonBody.optString("starring"))) {
                            album.setMainActor(albumJsonBody.optString("starring"));
                        }
                        callback.onSuccess(album);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    ErrorInfo info = buildErrorInfo(url, "getAlbumDetail", null, ErrorInfo.ERROR_TYPE_PARSE_JSON);
                    callback.onError(info);
                }
            }
        });
    }
}
