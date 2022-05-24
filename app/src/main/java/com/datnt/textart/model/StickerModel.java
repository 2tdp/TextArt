package com.datnt.textart.model;

import com.datnt.textart.customview.stickerview.DrawableSticker;
import com.datnt.textart.customview.stickerview.TextSticker;

import java.io.Serializable;

public class StickerModel implements Serializable {

    private TextModel textModel;
    private TextSticker textSticker;
    private DrawableSticker drawableSticker;

    public StickerModel(TextModel textModel, TextSticker textSticker, DrawableSticker drawableSticker) {
        this.textModel = textModel;
        this.textSticker = textSticker;
        this.drawableSticker = drawableSticker;
    }

    public TextModel getTextModel() {
        return textModel;
    }

    public void setTextModel(TextModel textModel) {
        this.textModel = textModel;
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
}
