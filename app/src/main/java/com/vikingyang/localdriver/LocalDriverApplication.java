package com.vikingyang.localdriver;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

import org.litepal.LitePalApplication;

/**
 * Created by yangj on 2017/5/20.
 */

public class LocalDriverApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        context = getApplicationContext();
        SDKInitializer.initialize(context);
        SDKInitializer.setCoordType(CoordType.BD09LL);
        LitePalApplication.initialize(context);
    }

    public static Context getContext(){
        return context;
    }
}
