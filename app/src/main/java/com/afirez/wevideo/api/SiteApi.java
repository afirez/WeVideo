package com.afirez.wevideo.api;

import com.afirez.wevideo.channel.model.Album;
import com.afirez.wevideo.channel.model.VideoWithType;
import com.afirez.wevideo.channel.model.Video;
import com.afirez.wevideo.home.model.Channel;

import java.util.ArrayList;

/**
 * Created by afirez on 18-3-31.
 */

public interface SiteApi {
    void getChannelAlbums(
            Channel channel,
            int page,
            int pageSize,
            ApiCallback<ArrayList<Album>> callback
    );

    void getAlbumDetail(Album album, ApiCallback<Album> callback);

    void getVideos(int pageSize, int pageNo, Album album, ApiCallback<ArrayList<Video>> callback);

    void getVideoUrl(Video video, ApiCallback<VideoWithType> callback);
}
