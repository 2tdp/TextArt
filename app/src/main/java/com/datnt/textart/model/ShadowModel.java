package com.datnt.textart.model;

import java.io.Serializable;

public class ShadowModel implements Serializable {

    private float xPos;
    private float yPos;
    private float blur;
    private int colorBlur;

    public ShadowModel(float xPos, float yPos, float blur, int colorBlur) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.blur = blur;
        this.colorBlur = colorBlur;
    }

    public float getxPos() {
        return xPos;
    }

    public void setxPos(float xPos) {
        this.xPos = xPos;
    }

    public float getyPos() {
        return yPos;
    }

    public void setyPos(float yPos) {
        this.yPos = yPos;
    }

    public float getBlur() {
        return blur;
    }

    public void setBlur(float blur) {
        this.blur = blur;
    }

    public int getColorBlur() {
        return colorBlur;
    }

    public void setColorBlur(int colorBlur) {
        this.colorBlur = colorBlur;
    }
}
