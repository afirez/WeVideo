package com.afirez.wevideo.channel.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by afirez on 18-3-27.
 */

public class Album implements Parcelable {
    private String albumId;//专辑id
    private int videoTotal;//集数
    private String title;//专辑名称
    private String subTitle;//专辑子标题
    private String director;//导演
    private String mainActor;//主演
    private String verImgUrl;//专辑竖图
    private String horImgUrl;//专辑横图
    private String albumDesc;//专辑描述
    private Site site;//网站
    private String tip;//提示
    private boolean isCompleted;//专辑是否更新完
    private String letvStyle;//乐视特殊字段

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public int getVideoTotal() {
        return videoTotal;
    }

    public void setVideoTotal(int videoTotal) {
        this.videoTotal = videoTotal;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getMainActor() {
        return mainActor;
    }

    public void setMainActor(String mainActor) {
        this.mainActor = mainActor;
    }

    public String getVerImgUrl() {
        return verImgUrl;
    }

    public void setVerImgUrl(String verImgUrl) {
        this.verImgUrl = verImgUrl;
    }

    public String getHorImgUrl() {
        return horImgUrl;
    }

    public void setHorImgUrl(String horImgUrl) {
        this.horImgUrl = horImgUrl;
    }

    public String getAlbumDesc() {
        return albumDesc;
    }

    public void setAlbumDesc(String albumDesc) {
        this.albumDesc = albumDesc;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getLetvStyle() {
        return letvStyle;
    }

    public void setLetvStyle(String letvStyle) {
        this.letvStyle = letvStyle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Album album = (Album) o;

        if (videoTotal != album.videoTotal) return false;
        if (isCompleted != album.isCompleted) return false;
        if (albumId != null ? !albumId.equals(album.albumId) : album.albumId != null) return false;
        if (title != null ? !title.equals(album.title) : album.title != null) return false;
        if (subTitle != null ? !subTitle.equals(album.subTitle) : album.subTitle != null)
            return false;
        if (director != null ? !director.equals(album.director) : album.director != null)
            return false;
        if (mainActor != null ? !mainActor.equals(album.mainActor) : album.mainActor != null)
            return false;
        if (verImgUrl != null ? !verImgUrl.equals(album.verImgUrl) : album.verImgUrl != null)
            return false;
        if (horImgUrl != null ? !horImgUrl.equals(album.horImgUrl) : album.horImgUrl != null)
            return false;
        if (albumDesc != null ? !albumDesc.equals(album.albumDesc) : album.albumDesc != null)
            return false;
        if (site != null ? !site.equals(album.site) : album.site != null) return false;
        if (tip != null ? !tip.equals(album.tip) : album.tip != null) return false;
        return letvStyle != null ? letvStyle.equals(album.letvStyle) : album.letvStyle == null;
    }

    @Override
    public int hashCode() {
        int result = albumId != null ? albumId.hashCode() : 0;
        result = 31 * result + videoTotal;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (subTitle != null ? subTitle.hashCode() : 0);
        result = 31 * result + (director != null ? director.hashCode() : 0);
        result = 31 * result + (mainActor != null ? mainActor.hashCode() : 0);
        result = 31 * result + (verImgUrl != null ? verImgUrl.hashCode() : 0);
        result = 31 * result + (horImgUrl != null ? horImgUrl.hashCode() : 0);
        result = 31 * result + (albumDesc != null ? albumDesc.hashCode() : 0);
        result = 31 * result + (site != null ? site.hashCode() : 0);
        result = 31 * result + (tip != null ? tip.hashCode() : 0);
        result = 31 * result + (isCompleted ? 1 : 0);
        result = 31 * result + (letvStyle != null ? letvStyle.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Album{" +
                "albumId='" + albumId + '\'' +
                ", videoTotal=" + videoTotal +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", director='" + director + '\'' +
                ", mainActor='" + mainActor + '\'' +
                ", verImgUrl='" + verImgUrl + '\'' +
                ", horImgUrl='" + horImgUrl + '\'' +
                ", albumDesc='" + albumDesc + '\'' +
                ", site=" + site +
                ", tip='" + tip + '\'' +
                ", isCompleted=" + isCompleted +
                ", letvStyle='" + letvStyle + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.albumId);
        dest.writeInt(this.videoTotal);
        dest.writeString(this.title);
        dest.writeString(this.subTitle);
        dest.writeString(this.director);
        dest.writeString(this.mainActor);
        dest.writeString(this.verImgUrl);
        dest.writeString(this.horImgUrl);
        dest.writeString(this.albumDesc);
        dest.writeInt(this.site.getSiteId());
        dest.writeString(this.tip);
        dest.writeByte(this.isCompleted ? (byte) 1 : (byte) 0);
        dest.writeString(this.letvStyle);
    }

    public Album(int siteId) {
        site =  new Site(siteId);
    }

    protected Album(Parcel in) {
        this.albumId = in.readString();
        this.videoTotal = in.readInt();
        this.title = in.readString();
        this.subTitle = in.readString();
        this.director = in.readString();
        this.mainActor = in.readString();
        this.verImgUrl = in.readString();
        this.horImgUrl = in.readString();
        this.albumDesc = in.readString();
        this.site = new Site(in.readInt());
        this.tip = in.readString();
        this.isCompleted = in.readByte() != 0;
        this.letvStyle = in.readString();
    }

    public static final Parcelable.Creator<Album> CREATOR = new Parcelable.Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel source) {
            return new Album(source);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };
}
