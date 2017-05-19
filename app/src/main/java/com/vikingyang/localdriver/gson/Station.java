package com.vikingyang.localdriver.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yangj on 2017/5/18.
 */

public class Station {

    @SerializedName("showapi_res_body")
    private StationBody stationBody;

    public StationBody getStationBody() {
        return stationBody;
    }

    public void setStationBody(StationBody stationBody) {
        this.stationBody = stationBody;
    }
}
