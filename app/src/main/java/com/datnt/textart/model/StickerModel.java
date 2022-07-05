package com.datnt.textart.model;

import com.datnt.textart.customview.stickerview.DrawableSticker;
import com.datnt.textart.customview.stickerview.TextSticker;
import com.datnt.textart.model.background.BackgroundModel;
import com.datnt.textart.model.image.ImageModel;
import com.datnt.textart.model.textsticker.TextModel;

import java.io.Serializable;

public class StickerModel implements Serializable {

    private TextModel textModel;
    private EmojiModel emojiModel;
    private ImageModel imageModel;
    private OverlayModel overlayModel;
    private DecorModel decorModel;
    private TemplateModel templateModel;
    private BackgroundModel backgroundModel;
    private TextSticker textSticker;
    private DrawableSticker drawableSticker;
    private ColorModel color;

    public StickerModel(TextModel textModel, EmojiModel emojiModel, OverlayModel overlayModel, DecorModel decorModel,
                        ImageModel imageModel, TemplateModel templateModel, BackgroundModel backgroundModel,
                        TextSticker textSticker, DrawableSticker drawableSticker, ColorModel color) {
        this.textModel = textModel;
        this.emojiModel = emojiModel;
        this.imageModel = imageModel;
        this.overlayModel = overlayModel;
        this.decorModel = decorModel;
        this.templateModel = templateModel;
        this.backgroundModel = backgroundModel;
        this.textSticker = textSticker;
        this.drawableSticker = drawableSticker;
        this.color = color;
    }

    public TextModel getTextModel() {
        return textModel;
    }

    public void setTextModel(TextModel textModel) {
        this.textModel = textModel;
    }

    public EmojiModel getEmojiModel() {
        return emojiModel;
    }

    public void setEmojiModel(EmojiModel emojiModel) {
        this.emojiModel = emojiModel;
    }

    public ImageModel getImageModel() {
        return imageModel;
    }

    public void setImageModel(ImageModel imageModel) {
        this.imageModel = imageModel;
    }

    public OverlayModel getOverlayModel() {
        return overlayModel;
    }

    public void setOverlayModel(OverlayModel overlayModel) {
        this.overlayModel = overlayModel;
    }

    public DecorModel getDecorModel() {
        return decorModel;
    }

    public void setDecorModel(DecorModel decorModel) {
        this.decorModel = decorModel;
    }

    public TemplateModel getTemplateModel() {
        return templateModel;
    }

    public void setTemplateModel(TemplateModel templateModel) {
        this.templateModel = templateModel;
    }

    public BackgroundModel getBackgroundModel() {
        return backgroundModel;
    }

    public void setBackgroundModel(BackgroundModel backgroundModel) {
        this.backgroundModel = backgroundModel;
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

    public ColorModel getColor() {
        return color;
    }

    public void setColor(ColorModel color) {
        this.color = color;
    }
}
