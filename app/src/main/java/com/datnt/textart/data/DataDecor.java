package com.datnt.textart.data;

import android.content.Context;

import com.datnt.textart.model.DecorModel;
import com.datnt.textart.model.EmojiModel;

import java.io.IOException;
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
}
