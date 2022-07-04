package com.datnt.textart.model.background;

import android.graphics.Bitmap;

import java.io.Serializable;

public class BackgroundModel implements Serializable {

    private Bitmap background;
    private Bitmap backgroundRoot;
    private AdjustModel adjustModel;
    private int positionFilter;
    private int positionBlend;
}
