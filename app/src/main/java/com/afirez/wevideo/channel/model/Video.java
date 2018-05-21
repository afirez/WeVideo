package com.afirez.wevideo.channel.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Video implements Parcelable {

    @Expose
    private Long vid; //视频id

    @Expose
    @SerializedName("video_name")
    private String videoName;//视频名字

    @Expose
    @SerializedName("hor_high_pic")
    private String horHighPic;//视频横图

    @Expose
    @SerializedName("ver_high_pic")
    private String verHighPic;//视频竖图

    @Expose
    private String title;

    @Expose
    private int site;

    @Expose
    private long aid; //专辑id

    public Long getVid() {
        return vid;
    }

    public void setVid(Long vid) {
        this.vid = vid;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getHorHighPic() {
        return horHighPic;
    }

    public void setHorHighPic(String horHighPic) {
        this.horHighPic = horHighPic;
    }

    public String getVerHighPic() {
        return verHighPic;
    }

    public void setVerHighPic(String verHighPic) {
        this.verHighPic = verHighPic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSite() {
        return site;
    }

    public void setSite(int site) {
        this.site = site;
    }

    public long getAid() {
        return aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }

    @Override
    public String toString() {
        return "Video{" +
                "vid=" + vid +
                ", videoName='" + videoName + '\'' +
                ", horHighPic='" + horHighPic + '\'' +
                ", verHighPic='" + verHighPic + '\'' +
                ", title='" + title + '\'' +
                ", site=" + site +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.vid);
        dest.writeString(this.videoName);
        dest.writeString(this.horHighPic);
        dest.writeString(this.verHighPic);
        dest.writeString(this.title);
        dest.writeInt(this.site);
        dest.writeLong(this.aid);
    }

    public Video() {
    }

    protected Video(Parcel in) {
        this.vid = (Long) in.readValue(Long.class.getClassLoader());
        this.videoName = in.readString();
        this.horHighPic = in.readString();
        this.verHighPic = in.readString();
        this.title = in.readString();
        this.site = in.readInt();
        this.aid = in.readLong();
    }

    public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}
