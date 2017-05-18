package com.vikingyang.localdriver.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yangj on 2017/5/18.
 */

public class BusLineDetil {

    @SerializedName("stats")
    private String content;

    private String name;

    private String info;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
