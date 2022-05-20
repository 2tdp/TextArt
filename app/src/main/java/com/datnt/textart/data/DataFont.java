package com.datnt.textart.data;

import android.content.Context;
import android.util.Log;

import com.datnt.textart.model.FontModel;
import com.datnt.textart.model.StyleFontModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DataFont {

    public static ArrayList<FontModel> getDataFont(Context context) {
        ArrayList<FontModel> lstFont = new ArrayList<>();
        try {
            String[] f = context.getAssets().list("fonts/");
            for (String s : f) {
                lstFont.add(new FontModel(s, getDataStyleFont(context, s), false, false, false, false));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lstFont;
    }

    public static ArrayList<StyleFontModel> getDataStyleFont(Context context, String nameFont) {
        ArrayList<StyleFontModel> lstStyle = new ArrayList<>();
        try {
            String[] f = context.getAssets().list("fonts/" + nameFont + "/");
            for (String s : f) {
                String[] arr = s.split("_");
                StringBuilder style = new StringBuilder();
                for (int i = 1; i < arr.length; i++) {
                    if (i == arr.length - 1) {
                        style.append(" ").append(arr[i].replace(".ttf", " "));
                    } else style.append(" ").append(arr[i]);

                    style = new StringBuilder(style.toString().substring(0, 2).toUpperCase() + style.toString().substring(2).toLowerCase());
                }
                lstStyle.add(new StyleFontModel(style.toString(), nameFont, false));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lstStyle;
    }
}
