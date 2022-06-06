package com.datnt.textart.data;

import android.content.Context;

import com.datnt.textart.model.EmojiModel;
import com.datnt.textart.model.OverlayModel;

import java.io.IOException;
import java.util.ArrayList;

public class DataOverlay {

    public static ArrayList<OverlayModel> getOverlay(Context context, String name) {
        ArrayList<OverlayModel> lstOverlay = new ArrayList<>();
        try {
            String[] f = context.getAssets().list(name + "/");
            for (String s : f) lstOverlay.add(new OverlayModel(s, name, false));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lstOverlay;
    }
}
