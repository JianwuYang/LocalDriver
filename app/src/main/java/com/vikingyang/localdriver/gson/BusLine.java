package com.vikingyang.localdriver.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yangj on 2017/5/18.
 */

public class BusLine {

    @SerializedName("showapi_res_body")
    private BusLineBody busLineBody;

    public BusLineBody getBusLineBody() {
        return busLineBody;
    }

    public void setBusLineBody(BusLineBody busLineBody) {
        this.busLineBody = busLineBody;
    }
}
