package com.afirez.wevideo.channel.model;

public class VideoWithType {

    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_HIGH = 2;
    public static final int TYPE_SUPER = 3;

    private Video video;

    private int type;

    private String url;

    public VideoWithType(Video video, int type, String url) {
        this.video = video;
        this.type = type;
        this.url = url;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "VideoWithType{" +
                "video=" + video +
                ", type=" + type +
                ", url=" + url +
                '}';
    }
}
