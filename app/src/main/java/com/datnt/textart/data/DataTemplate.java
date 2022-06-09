package com.datnt.textart.data;

import android.content.Context;

import com.datnt.textart.model.DecorModel;
import com.datnt.textart.model.TemplateModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DataTemplate {

    public static ArrayList<TemplateModel> getTemplate(Context context, String name) {
        ArrayList<TemplateModel> lstTemp = new ArrayList<>();
        try {
            String[] f = context.getAssets().list("template/template" + name + "/");
            for (String s : f) {
                String background = "template_background" + s.split("template")[1];
                String text = "template_text" + s.split("template")[1];
                lstTemp.add(new TemplateModel(s, background, text));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lstTemp;
    }


}
