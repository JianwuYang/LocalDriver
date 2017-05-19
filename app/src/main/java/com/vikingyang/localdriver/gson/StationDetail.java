package com.vikingyang.localdriver.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yangj on 2017/5/18.
 */

public class StationDetail {

    private String name;

    @SerializedName("line_names")
    private String lineNames;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLineNames() {
        return lineNames;
    }

    public void setLineNames(String lineNames) {
        this.lineNames = lineNames;
    }
}
