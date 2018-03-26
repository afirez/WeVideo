package com.afirez.wevideo.home.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.afirez.wevideo.R;

/**
 * Created by afirez on 18-3-26.
 */

public class Channel implements Parcelable {
    public static final int SHOW = 0;//电视剧
    public static final int MOVIE = 1;//电影
    public static final int COMIC = 2;//动漫
    public static final int DOCUMENTARY = 3;//纪录片
    public static final int MUSIC = 4;//音乐
    public static final int VARIETY = 5;//综艺
    public static final int LIVE = 6;//直播
    public static final int FAVORITE = 7;//收藏
    public static final int HISTORY = 8;//历史记录
    public static final int MAX_COUNT = 9;//频道数

    private int id;

    private int imgResId;

    private int nameRes;

    public Channel(int id) {
        this.id = id;
        switch (id) {
            case SHOW:
                imgResId = R.drawable.wv_main_ic_show;
                nameRes = R.string.wv_main_channel_series;
                break;
            case MOVIE:
                imgResId = R.drawable.wv_main_ic_movie;
                nameRes = R.string.wv_main_channel_movie;
                break;
            case COMIC:
                imgResId = R.drawable.wv_main_ic_comic;
                nameRes = R.string.wv_main_channel_comic;
                break;
            case DOCUMENTARY:
                imgResId = R.drawable.wv_main_ic_documentary;
                nameRes = R.string.wv_main_channel_documentary;
                break;
            case MUSIC:
                imgResId = R.drawable.wv_main_ic_music;
                nameRes = R.string.wv_main_channel_music;
                break;
            case VARIETY:
                imgResId = R.drawable.wv_main_ic_variety;
                nameRes = R.string.wv_main_channel_variety;
                break;
            case LIVE:
                imgResId = R.drawable.wv_main_ic_live;
                nameRes = R.string.wv_main_channel_live;
                break;
            case FAVORITE:
                imgResId = R.drawable.wv_main_ic_bookmark;
                nameRes = R.string.wv_main_channel_favorite;
                break;
            case HISTORY:
                imgResId = R.drawable.wv_main_ic_history;
                nameRes = R.string.wv_main_channel_history;
                break;
        }
    }

    public int getId() {
        return id;
    }

    public int getImgResId() {
        return imgResId;
    }

    public int getNameRes() {
        return nameRes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Channel channel = (Channel) o;

        if (id != channel.id) return false;
        if (imgResId != channel.imgResId) return false;
        return nameRes == channel.nameRes;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + imgResId;
        result = 31 * result + nameRes;
        return result;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id +
                ", imgResId=" + imgResId +
                ", nameRes=" + nameRes +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.imgResId);
        dest.writeInt(this.nameRes);
    }

    protected Channel(Parcel in) {
        this.id = in.readInt();
        this.imgResId = in.readInt();
        this.nameRes = in.readInt();
    }

    public static final Parcelable.Creator<Channel> CREATOR = new Parcelable.Creator<Channel>() {
        @Override
        public Channel createFromParcel(Parcel source) {
            return new Channel(source);
        }

        @Override
        public Channel[] newArray(int size) {
            return new Channel[size];
        }
    };
}
