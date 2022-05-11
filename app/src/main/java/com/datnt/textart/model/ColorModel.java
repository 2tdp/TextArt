package com.datnt.textart.model;

import java.io.Serializable;

public class ColorModel implements Serializable {
    private int direction;
    private int colorStart;
    private int colorEnd;
    private boolean isColor;

    public ColorModel() {
    }

    public ColorModel(int direction, int colorStart, int colorEnd, boolean isColor) {
        this.direction= direction;
        this.colorStart = colorStart;
        this.colorEnd = colorEnd;
        this.isColor = isColor;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getColorStart() {
        return colorStart;
    }

    public void setColorStart(int colorStart) {
        this.colorStart = colorStart;
    }

    public int getColorEnd() {
        return colorEnd;
    }

    public void setColorEnd(int colorEnd) {
        this.colorEnd = colorEnd;
    }

    public boolean isColor() {
        return isColor;
    }

    public void setColor(boolean color) {
        isColor = color;
    }
}
