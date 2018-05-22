package com.afirez.wevideo.api;

import com.afirez.wevideo.channel.model.Album;
import com.afirez.wevideo.channel.model.ErrorInfo;
import com.afirez.wevideo.channel.model.Site;
import com.afirez.wevideo.channel.model.Video;
import com.afirez.wevideo.channel.model.VideoWithType;
import com.afirez.wevideo.home.model.Channel;

import java.util.ArrayList;

/**
 * Created by afirez on 18-3-31.
 */

public class SiteApis {

    private static volatile SiteApis sInstance;

    public static SiteApis getInstance() {
        if (sInstance == null) {
            synchronized (SiteApis.class) {
                if (sInstance == null) {
                    sInstance = new SiteApis();
                }
            }
        }
        return sInstance;
    }

    private SiteApi letvApi = new LetvApi();
    private SiteApi sohuApi = new SohuApi();

    public void getChannelAlbums(
            int siteId,
            int channelId,
            int page,
            int pageSize,
            ApiCallback<ArrayList<Album>> callback) {
        SiteApi siteApi = null;
        switch (siteId) {
            case Site.LETV:
                siteApi = this.letvApi;
                break;
            case Site.SOHU:
                siteApi = this.sohuApi;
                break;
        }
        if (siteApi != null) {
            Channel channel = new Channel(channelId);
            siteApi.getChannelAlbums(
                    channel,
                    page,
                    pageSize,
                    callback
            );
        } else {
            if (callback != null) {
                callback.onError(new ErrorInfo(siteId, ErrorInfo.ERROR_TYPE_URL));
            }
        }
    }

    public void getAlbumDetail(Album album, ApiCallback<Album> callback) {
        int siteId = album.getSite().getSiteId();
        SiteApi siteApi = null;
        switch (siteId) {
            case Site.LETV:
                siteApi = this.letvApi;
                break;
            case Site.SOHU:
                siteApi = this.sohuApi;
                break;
        }
        if (siteApi != null) {
            siteApi.getAlbumDetail(album, callback);
        } else {
            if (callback != null) {
                callback.onError(new ErrorInfo(siteId, ErrorInfo.ERROR_TYPE_URL));
            }
        }
    }

    public void getVideos(int pageSize, int pageNo, Album album, ApiCallback<ArrayList<Video>> callback) {
        int siteId = album.getSite().getSiteId();
        SiteApi siteApi = null;
        switch (siteId) {
            case Site.LETV:
                siteApi = this.letvApi;
                break;
            case Site.SOHU:
                siteApi = this.sohuApi;
                break;
        }
        if (siteApi != null) {
            siteApi.getVideos(pageSize, pageNo, album, callback);
        } else {
            if (callback != null) {
                callback.onError(new ErrorInfo(siteId, ErrorInfo.ERROR_TYPE_URL));
            }
        }
    }

    public void getVideoUrl(Video video, ApiCallback<VideoWithType> callback) {
        int siteId = video.getSite();
        SiteApi siteApi = null;
        switch (siteId) {
            case Site.LETV:
                siteApi = this.letvApi;
                break;
            case Site.SOHU:
                siteApi = this.sohuApi;
                break;
        }
        if (siteApi != null) {
            siteApi.getVideoUrl(video, callback);
        } else {
            if (callback != null) {
                callback.onError(new ErrorInfo(siteId, ErrorInfo.ERROR_TYPE_URL));
            }
        }
    }
}
