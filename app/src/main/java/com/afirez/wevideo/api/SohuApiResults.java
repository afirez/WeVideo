package com.afirez.wevideo.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by afirez on 18-3-31.
 */

public class SohuApiResults<T> {
    private int status;  // 200,
    private String statusText; //"OK",
    private Data<T> data;

    public static class Data<T> {
        private int count;

        @SerializedName("videos")
        private ArrayList<T> albums;

        public Data() {
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public ArrayList<T> getAlbums() {
            return albums;
        }

        public void setAlbums(ArrayList<T> albums) {
            this.albums = albums;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "count=" + count +
                    ", albums=" + albums +
                    '}';
        }
    }

    public SohuApiResults() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public Data<T> getData() {
        return data;
    }

    public void setData(Data<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SohuApiResults{" +
                "status=" + status +
                ", statusText='" + statusText + '\'' +
                ", Data=" + data +
                '}';
    }
}
