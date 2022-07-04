package com.datnt.textart.model.background;

import android.graphics.Bitmap;

import java.io.Serializable;

public class BackgroundModel implements Serializable {

    private Bitmap background;
    private Bitmap backgroundRoot;
    private AdjustModel adjustModel;
    private int sizeMain;
    private int positionFilterBackground = 0;
    private float opacity = 1f;

    public BackgroundModel() {
    }

    public BackgroundModel(Bitmap background, Bitmap backgroundRoot, AdjustModel adjustModel, int sizeMain,
                           int positionFilterBackground, float opacity) {
        this.background = background;
        this.backgroundRoot = backgroundRoot;
        this.adjustModel = adjustModel;
        this.sizeMain = sizeMain;
        this.positionFilterBackground = positionFilterBackground;
        this.opacity = opacity;
    }

    public Bitmap getBackground() {
        return background;
    }

    public void setBackground(Bitmap background) {
        this.background = background;
    }

    public Bitmap getBackgroundRoot() {
        return backgroundRoot;
    }

    public void setBackgroundRoot(Bitmap backgroundRoot) {
        this.backgroundRoot = backgroundRoot;
    }

    public AdjustModel getAdjustModel() {
        return adjustModel;
    }

    public void setAdjustModel(AdjustModel adjustModel) {
        this.adjustModel = adjustModel;
    }

    public int getSizeMain() {
        return sizeMain;
    }

    public void setSizeMain(int sizeMain) {
        this.sizeMain = sizeMain;
    }

    public int getPositionFilterBackground() {
        return positionFilterBackground;
    }

    public void setPositionFilterBackground(int positionFilterBackground) {
        this.positionFilterBackground = positionFilterBackground;
    }

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }
}
