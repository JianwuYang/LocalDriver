package com.vikingyang.localdriver.util;

import android.os.Handler;

import com.show.api.ShowApiRequest;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by yangj on 2017/5/18.
 */

public class HttpUtil {


    public static void sendHttpRequestBusLine(String city, String busNo, String station, Handler.Callback callback){
        String appId = "38394";
        String secret ="76a067eab2964b0482e08229436abc99";
        final String res = new ShowApiRequest("http://route.showapi.com/844-2",appId,secret).addTextPara("city",city).addTextPara("busNo",busNo).post();

    }

    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

}
