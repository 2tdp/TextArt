package com.datnt.textart.data;

import android.content.Context;

import com.datnt.textart.model.DecorModel;
import com.datnt.textart.model.EmojiModel;
import com.datnt.textart.model.StickerModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class DataDecor {

    public static ArrayList<DecorModel> getTitleDecor(Context context, String name) {
        ArrayList<DecorModel> lstDecor = new ArrayList<>();
        try {
            String[] f = context.getAssets().list("decor/decor_" + name + "/");
            for (String s : f) lstDecor.add(new DecorModel(s, "decor_" + name, false));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lstDecor;
    }

    public static ArrayList<String> getPathDataDecor(Context context, DecorModel decor) {
        String tContents = "";
        try {
            InputStream stream = context.getAssets().open("decor_json/json_box/" + decor.getNameDecor().replace(".png", ".json"));
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            tContents = new String(buffer);
        } catch (IOException ignored) {
        }
        if (!tContents.isEmpty()) {
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            return new Gson().fromJson(tContents, type);
        }
        return new ArrayList<>();
    }
}
