package com.datnt.textart.sharepref;

import android.content.Context;

import com.datnt.textart.adapter.ProjectAdapter;
import com.datnt.textart.model.ColorModel;
import com.datnt.textart.model.textsticker.FontModel;
import com.datnt.textart.model.Project;
import com.datnt.textart.model.TemplateModel;
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

    public static void setListFont(ArrayList<FontModel> lstFont, String key) {
        Gson gson = new Gson();
        JsonArray jsonArray = gson.toJsonTree(lstFont).getAsJsonArray();
        String json = jsonArray.toString();

        DataLocalManager.getInstance().mySharedPreferences.putStringwithKey(key, json);
    }

    public static ArrayList<FontModel> getListFont(String key) {
        Gson gson = new Gson();
        JSONObject jsonObject;
        ArrayList<FontModel> lstFont = new ArrayList<>();

        String strJson = DataLocalManager.getInstance().mySharedPreferences.getStringwithKey(key, "");
        try {
            JSONArray jsonArray = new JSONArray(strJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                lstFont.add(gson.fromJson(jsonObject.toString(), FontModel.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return lstFont;
    }

    public static void setColor(ColorModel color, String key) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.toJsonTree(color).getAsJsonObject();
        String json = jsonObject.toString();

        DataLocalManager.getInstance().mySharedPreferences.putStringwithKey(key, json);
    }

    public static ColorModel getColor(String key) {
        String strJson = DataLocalManager.getInstance().mySharedPreferences.getStringwithKey(key, "");
        ColorModel color = null;

        Gson gson = new Gson();

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(strJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            color = gson.fromJson(jsonObject.toString(), ColorModel.class);
        }

        return color;
    }

    public static void setTemp(TemplateModel temp, String key) {
        Gson gson = new Gson();
        JsonObject jsonObject = null;
        if (temp != null) jsonObject = gson.toJsonTree(temp).getAsJsonObject();

        String json;
        if (jsonObject != null) json = jsonObject.toString();
        else json = "";

        DataLocalManager.getInstance().mySharedPreferences.putStringwithKey(key, json);
    }

    public static TemplateModel getTemp(String key) {
        String strJson = DataLocalManager.getInstance().mySharedPreferences.getStringwithKey(key, "");
        TemplateModel temp = null;

        Gson gson = new Gson();

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(strJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            temp = gson.fromJson(jsonObject.toString(), TemplateModel.class);
        }

        return temp;
    }

    public static void setListProject(ArrayList<Project> lstProject, String key) {
        Gson gson = new Gson();
        JsonArray jsonArray = gson.toJsonTree(lstProject).getAsJsonArray();
        String json = jsonArray.toString();

        DataLocalManager.getInstance().mySharedPreferences.putStringwithKey(key, json);
    }

    public static ArrayList<Project> getListProject(String key) {
        Gson gson = new Gson();
        JSONObject jsonObject;
        ArrayList<Project> lstProject = new ArrayList<>();

        String strJson = DataLocalManager.getInstance().mySharedPreferences.getStringwithKey(key, "");
        try {
            JSONArray jsonArray = new JSONArray(strJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                lstProject.add(gson.fromJson(jsonObject.toString(), Project.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return lstProject;
    }

    public static void setProject(Project project, String key) {
        Gson gson = new Gson();
        JsonObject jsonObject = null;
        if (project != null) jsonObject = gson.toJsonTree(project).getAsJsonObject();

        String json;
        if (jsonObject != null) json = jsonObject.toString();
        else json = "";

        DataLocalManager.getInstance().mySharedPreferences.putStringwithKey(key, json);
    }

    public static Project getProject(String key) {
        String strJson = DataLocalManager.getInstance().mySharedPreferences.getStringwithKey(key, "");
        Project project = null;

        Gson gson = new Gson();

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(strJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {
            project = gson.fromJson(jsonObject.toString(), Project.class);
        }

        return project;
    }
}
