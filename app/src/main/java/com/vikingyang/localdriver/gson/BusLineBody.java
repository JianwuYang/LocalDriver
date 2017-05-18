package com.vikingyang.localdriver.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yangj on 2017/5/18.
 */


public class BusLineBody {


    private List<BusLineDetil> retList;

    public List<BusLineDetil> getRetList() {
        return retList;
    }

    public void setRetList(List<BusLineDetil> retList) {
        this.retList = retList;
    }
}
