package com.alarm.manger.util;

import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by Rana Shahid Bashir on 10/28/2016.
 */
public  class JsonUtil {


    public static AlarmInfo convertJsonToAlarmObject(String str){
        Gson gson = new Gson();

        AlarmInfo alarmInfo = gson.fromJson(str, AlarmInfo.class);
        return alarmInfo;


    }

    public static String convertAlarmObjectIntoJson(AlarmInfo alarmInfo){
        Gson gson = new Gson();
        String json = gson.toJson(alarmInfo);

        return json;
    }
}
