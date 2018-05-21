package com.afirez.wevideo.api;

import android.util.Log;

import com.afirez.wevideo.channel.model.Album;
import com.afirez.wevideo.channel.model.ErrorInfo;
import com.afirez.wevideo.channel.model.Site;
import com.afirez.wevideo.channel.model.SohuAlbum;
import com.afirez.wevideo.common.utils.GsonSerializer;
import com.afirez.wevideo.common.utils.OkHttpUtils;
import com.afirez.wevideo.home.model.Channel;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by afirez on 18-3-31.
 */

public class SohuApi implements SiteApi {
    private static final String TAG = SohuApi.class.getSimpleName();
    private static final int SOHU_CHANNELID_MOVIE = 1; //搜狐电影频道ID
    private static final int SOHU_CHANNELID_SERIES = 2; //搜狐电视剧频道ID
    private static final int SOHU_CHANNELID_VARIETY = 7; //搜狐综艺频道ID
    private static final int SOHU_CHANNELID_DOCUMENTARY = 8; //搜狐纪录片频道ID
    private static final int SOHU_CHANNELID_COMIC = 16; //搜狐动漫频道ID
    private static final int SOHU_CHANNELID_MUSIC = 24; //搜狐音乐频道ID

    //某一专辑详情
    //http://api.tv.sohu.com/v4/album/info/9112373.json?plat=6&poid=1&api_key=9854b2afa779e1a6bff1962447a09dbd&sver=6.2.0&sysver=4.4.2&partner=47
    private final static String API_KEY = "plat=6&poid=1&api_key=9854b2afa779e1a6bff1962447a09dbd&sver=6.2.0&sysver=4.4.2&partner=47";
    private final static String API_ALBUM_INFO = "http://api.tv.sohu.com/v4/album/info/";
    //http://api.tv.sohu.com/v4/search/channel.json?cid=2&o=1&plat=6&poid=1&api_key=9854b2afa779e1a6bff1962447a09dbd&sver=6.2.0&sysver=4.4.2&partner=47&page=1&page_size=1
    private final static String API_CHANNEL_ALBUM_FORMAT = "http://api.tv.sohu.com/v4/search/channel.json" +
            "?cid=%s&o=1&plat=6&poid=1&api_key=9854b2afa779e1a6bff1962447a09dbd&" +
            "sver=6.2.0&sysver=4.4.2&partner=47&page=%s&page_size=%s";
    //http://api.tv.sohu.com/v4/album/videos/9112373.json?page=1&page_size=50&order=0&site=1&with_trailer=1&plat=6&poid=1&api_key=9854b2afa779e1a6bff1962447a09dbd&sver=6.2.0&sysver=4.4.2&partner=47
    private final static String API_ALBUM_VIDOES_FORMAT = "http://api.tv.sohu.com/v4/album/videos/%s.json?page=%s&page_size=%s&order=0&site=1&with_trailer=1&plat=6&poid=1&api_key=9854b2afa779e1a6bff1962447a09dbd&sver=6.2.0&sysver=4.4.2&partner=47";
    // 播放url
    //http://api.tv.sohu.com/v4/video/info/3669315.json?site=1&plat=6&poid=1&api_key=9854b2afa779e1a6bff1962447a09dbd&sver=4.5.1&sysver=4.4.2&partner=47&aid=9112373
    private final static String API_VIDEO_PLAY_URL_FORMAT = "http://api.tv.sohu.com/v4/video/info/%s.json?site=1&plat=6&poid=1&api_key=9854b2afa779e1a6bff1962447a09dbd&sver=4.5.1&sysver=4.4.2&partner=47&aid=%s";
    //真实url格式 m3u8
    //http://hot.vrs.sohu.com/ipad3669271_4603585256668_6870592.m3u8?plat=6uid=f5dbc7b40dad477c8516885f6c681c01&pt=5&prod=app&pg=1

    @Override
    public void getChannelAlbums(
            Channel channel,
            int page,
            int pageSize,
            ApiCallback<ArrayList<Album>> callback) {
        String url = buildChannelAlbumUrl(channel, page, pageSize);
        fetchChannelAlbums(url, callback);
    }

    private void fetchChannelAlbums(final String url, final ApiCallback<ArrayList<Album>> callback) {
        if (url == null || url.length() == 0 || callback == null) {
            return;
        }
        Log.i(TAG, "fetchChannelAlbums: " + url);
        OkHttpUtils.execute(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ErrorInfo info = buildErrorInfo(
                        url,
                        "fetchChannelAlbums",
                        e,
                        ErrorInfo.ERROR_TYPE_URL
                );
                callback.onError(info);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                if (body == null || !response.isSuccessful()) {
                    ErrorInfo info = buildErrorInfo(
                            url,
                            "fetchChannelAlbums",
                            null,
                            ErrorInfo.ERROR_TYPE_URL
                    );
                    callback.onError(info);
                    return;
                }
                Gson gson = GsonSerializer.getInstance().getGson();
                String json = body.string();
                Log.i(TAG, "onResponse: " + json);
                SohuApiResults<SohuAlbum> apiResult = null;
                try {
                    apiResult = gson.fromJson(
                            json,
                            new TypeToken<SohuApiResults<SohuAlbum>>() {
                            }.getType()
                    );
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

                if (apiResult == null) {
                    ErrorInfo info = buildErrorInfo(
                            url,
                            "fetchChannelAlbums",
                            null,
                            ErrorInfo.ERROR_TYPE_DATA_CONVERT
                    );
                    callback.onError(info);
                    return;
                }
                ArrayList<Album> albums = albumsOf(apiResult);
                if (albums == null) {
                    ErrorInfo info = buildErrorInfo(
                            url,
                            "fetchChannelAlbums",
                            null,
                            ErrorInfo.ERROR_TYPE_DATA_CONVERT
                    );
                    callback.onError(info);
                    return;
                }
                callback.onSuccess(albums);
            }
        });
    }

    private String buildChannelAlbumUrl(Channel channel, int page, int pageSize) {
        return String.format(API_CHANNEL_ALBUM_FORMAT, channelIdOf(channel), page, pageSize);
    }

    //自定义频道ID与真实频道id转换
    private int channelIdOf(Channel channel) {
        int channelId = -1;//-1 无效值
        switch (channel.getId()) {
            case Channel.SHOW:
                channelId = SOHU_CHANNELID_SERIES;
                break;
            case Channel.MOVIE:
                channelId = SOHU_CHANNELID_MOVIE;
                break;
            case Channel.COMIC:
                channelId = SOHU_CHANNELID_COMIC;
                break;
            case Channel.MUSIC:
                channelId = SOHU_CHANNELID_MUSIC;
                break;
            case Channel.DOCUMENTARY:
                channelId = SOHU_CHANNELID_DOCUMENTARY;
                break;
            case Channel.VARIETY:
                channelId = SOHU_CHANNELID_VARIETY;
                break;
        }
        return channelId;
    }

    private ErrorInfo buildErrorInfo(String url, String functionName, Exception e, int type) {
        ErrorInfo info = new ErrorInfo(Site.SOHU, type);
        info.setExceptionString(e == null ? "" : e.getMessage());
        info.setFunctionName(functionName);
        info.setUrl(url);
        info.setTag(TAG);
        info.setClassName(TAG);
        return info;
    }

    private ArrayList<Album> albumsOf(SohuApiResults<SohuAlbum> result) {
        SohuApiResults.Data<SohuAlbum> sohuAlbumData = result.getData();
        if (sohuAlbumData != null) {
            ArrayList<SohuAlbum> sohuAlbums = sohuAlbumData.getAlbums();
            if (sohuAlbums != null && sohuAlbums.size() > 0) {
                ArrayList<Album> albums = new ArrayList<>();
                Album album;
                for (SohuAlbum sohuAlbum : sohuAlbums) {
                    if (sohuAlbum != null) {
                        album = new Album(Site.SOHU);
                        album.setAlbumDesc(sohuAlbum.getTvDesc());
                        album.setAlbumId(sohuAlbum.getAlbumId());
                        album.setHorImgUrl(sohuAlbum.getHorHighPic());
                        album.setMainActor(sohuAlbum.getMainActor());
                        album.setTip(sohuAlbum.getTip());
                        album.setTitle(sohuAlbum.getAlbumName());
                        album.setVerImgUrl(sohuAlbum.getVerHighPic());
                        album.setDirector(sohuAlbum.getDirector());
                        albums.add(album);
                    }
                }
                return albums;
            }
        }
        return null;
    }

    @Override
    public void getAlbumDetail(final Album album, final ApiCallback<Album> callback) {
        final String url = API_ALBUM_INFO + album.getAlbumId() + ".json?" + API_KEY;
        if (callback == null) {
            return;
        }
        OkHttpUtils.execute(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ErrorInfo info = buildErrorInfo(url, "onGetAlbumDetail", e, ErrorInfo.ERROR_TYPE_URL);
                callback.onError(info);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                if (body == null || !response.isSuccessful()) {
                    ErrorInfo info = buildErrorInfo(url, "onGetAlbumDetail", null, ErrorInfo.ERROR_TYPE_HTTP);
                    callback.onError(info);
                    return;
                }
                String json = body.string();
                Gson gson = GsonSerializer.getInstance().getGson();
                SohuApiResult<SohuAlbum> result = null;
                try {
                    result = gson.fromJson(json, new TypeToken<SohuApiResult<SohuAlbum>>() {
                    }.getType());
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }

                if (result == null) {
                    ErrorInfo info = buildErrorInfo(
                            url,
                            "getAlbumDetail",
                            null,
                            ErrorInfo.ERROR_TYPE_DATA_CONVERT
                    );
                    callback.onError(info);
                    return;
                }
                if (result.getData() != null) {
                    if (result.getData().getLastVideoCount() > 0) {
                        album.setVideoTotal(result.getData().getLastVideoCount());
                    } else {
                        album.setVideoTotal(result.getData().getTotalVideoCount());
                    }
                }
                callback.onSuccess(album);

            }
        });

    }
}
