package com.example.radio.util;

import com.google.gson.annotations.SerializedName;

public class Shoutcast {

    private String name;
    private String player;
    @SerializedName("stream")
    private String url;

    public String getName() {
        return name;
    }

    public String getplayer() {
        return player;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
