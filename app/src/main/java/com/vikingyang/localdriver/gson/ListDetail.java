package com.vikingyang.localdriver.gson;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yangj on 2017/5/19.
 */

public class ListDetail implements Serializable {

    private String title;

    private List<String> list;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
