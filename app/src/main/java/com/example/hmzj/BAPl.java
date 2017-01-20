package com.example.hmzj;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by zchao on 2017/1/19.
 */

public class BAPl extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID + "=588030de");
    }

}
