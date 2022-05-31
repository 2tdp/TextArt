package com.datnt.textart.model;

import android.graphics.Bitmap;

import com.datnt.textart.customview.stickerview.DrawableSticker;
import com.datnt.textart.customview.stickerview.TextSticker;

import java.io.Serializable;

public class StickerModel implements Serializable {

    private TextModel textModel;
    private Bitmap bitmapRoot;
    private Bitmap bitmap;
    private TextSticker textSticker;
    private DrawableSticker drawableSticker;
    private int positionFilter;
    private int positionBlend;

    public StickerModel(TextModel textModel, Bitmap bitmapRoot, Bitmap bitmap, TextSticker textSticker, DrawableSticker drawableSticker, int positionFilter, int positionBlend) {
        this.textModel = textModel;
        this.bitmapRoot = bitmapRoot;
        this.bitmap = bitmap;
        this.textSticker = textSticker;
        this.drawableSticker = drawableSticker;
        this.positionFilter = positionFilter;
        this.positionBlend = positionBlend;
    }

    public TextModel getTextModel() {
        return textModel;
    }

    public void setTextModel(TextModel textModel) {
        this.textModel = textModel;
    }

    public Bitmap getBitmapRoot() {
        return bitmapRoot;
    }

    public void setBitmapRoot(Bitmap bitmapRoot) {
        this.bitmapRoot = bitmapRoot;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public TextSticker getTextSticker() {
        return textSticker;
    }

    public void setTextSticker(TextSticker textSticker) {
        this.textSticker = textSticker;
    }

    public DrawableSticker getDrawableSticker() {
        return drawableSticker;
    }

    public void setDrawableSticker(DrawableSticker drawableSticker) {
        this.drawableSticker = drawableSticker;
    }

    public int getPositionFilter() {
        return positionFilter;
    }

    public void setPositionFilter(int positionFilter) {
        this.positionFilter = positionFilter;
    }

    public int getPositionBlend() {
        return positionBlend;
    }

    public void setPositionBlend(int positionBlend) {
        this.positionBlend = positionBlend;
    }
}
