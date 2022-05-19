package com.datnt.textart.sharepref;

import android.content.Context;

import com.datnt.textart.model.ColorModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataLocalManager {

    private static DataLocalManager instance;
    private MySharedPreferences mySharedPreferences;

    public static void init(Context context) {
        instance = new DataLocalManager();
        instance.mySharedPreferences = new MySharedPreferences(context);
    }

    public static DataLocalManager getInstance() {

        if (instance == null) {
            instance = new DataLocalManager();
        }
        return instance;
    }

    public static void setFirstInstall(String key, boolean isFirst) {
        DataLocalManager.getInstance().mySharedPreferences.putBooleanValue(key, isFirst);
    }

    public static boolean getFirstInstall(String key) {
        return DataLocalManager.getInstance().mySharedPreferences.getBooleanValue(key);
    }

    public static void setCheck(String key, boolean volumeOn) {
        DataLocalManager.getInstance().mySharedPreferences.putBooleanValue(key, volumeOn);
    }

    public static boolean getCheck(String key) {
        return DataLocalManager.getInstance().mySharedPreferences.getBooleanValue(key);
    }

    public static void setOption(String option, String key) {
        DataLocalManager.getInstance().mySharedPreferences.putStringwithKey(key, option);
    }

    public static String getOption(String key) {
        return DataLocalManager.getInstance().mySharedPreferences.getStringwithKey(key, "");
    }

//    public static void setColor(ColorModel color, String key) {
//        Gson gson = new Gson();
//        JsonObject jsonObject = gson.toJsonTree(color).getAsJsonObject();
//        String json = jsonObject.toString();
//
//        DataLocalManager.getInstance().mySharedPreferences.putStringwithKey(key, json);
//    }
//
//    public static ColorModel getColor(String key) {
//        String strJson = DataLocalManager.getInstance().mySharedPreferences.getStringwithKey(key, "");
//        ColorModel color = null;
//
//        Gson gson = new Gson();
//
//        JSONObject jsonObject = null;
//        try {
//            jsonObject = new JSONObject(strJson);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        if (jsonObject != null) {
//            color = gson.fromJson(jsonObject.toString(), ColorModel.class);
//        }
//
//        return color;
////    }
}
