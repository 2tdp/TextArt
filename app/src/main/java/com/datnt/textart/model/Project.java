package com.datnt.textart.model;

import android.graphics.Bitmap;
import android.graphics.BlendMode;
import android.graphics.Matrix;

import com.datnt.textart.model.background.BackgroundModel;
import com.datnt.textart.model.image.ImageModel;
import com.datnt.textart.model.textsticker.TextModel;

import java.io.Serializable;
import java.util.ArrayList;

public class Project implements Serializable {

    private BackgroundModel backgroundModel;
    private Bitmap thumb;

    private ArrayList<TextModel> lstTextModel;
    private ArrayList<EmojiModel> lstEmojiModel;
    private ArrayList<ImageModel> lstImageModel;
    private ArrayList<OverlayModel> lstOverlayModel;
    private ArrayList<DecorModel> lstDecorModel;
    private ArrayList<TemplateModel> lstTempModel;
    private ArrayList<Matrix> lstMatrix;

    public Project(BackgroundModel backgroundModel,
                   ArrayList<TextModel> lstTextModel,
                   ArrayList<EmojiModel> lstEmojiModel,
                   ArrayList<ImageModel> lstImageModel,
                   ArrayList<OverlayModel> lstOverlayModel,
                   ArrayList<DecorModel> lstDecorModel,
                   ArrayList<TemplateModel> lstTempModel,
                   ArrayList<Matrix> lstMatrix, Bitmap thumb) {
        this.backgroundModel = backgroundModel;
        this.lstTextModel = lstTextModel;
        this.lstMatrix = lstMatrix;
        this.thumb = thumb;
    }

    public BackgroundModel getBitmap() {
        return backgroundModel;
    }

    public void setBitmap(BackgroundModel backgroundModel) {
        this.backgroundModel = backgroundModel;
    }

    public Bitmap getThumb() {
        return thumb;
    }

    public void setThumb(Bitmap thumb) {
        this.thumb = thumb;
    }

    public ArrayList<TextModel> getListTextModel() {
        return lstTextModel;
    }

    public void setTextModel(ArrayList<TextModel> lstTextModel) {
        this.lstTextModel = lstTextModel;
    }

    public ArrayList<EmojiModel> getLstEmojiModel() {
        return lstEmojiModel;
    }

    public void setLstEmojiModel(ArrayList<EmojiModel> lstEmojiModel) {
        this.lstEmojiModel = lstEmojiModel;
    }

    public ArrayList<ImageModel> getLstImageModel() {
        return lstImageModel;
    }

    public void setLstImageModel(ArrayList<ImageModel> lstImageModel) {
        this.lstImageModel = lstImageModel;
    }

    public ArrayList<OverlayModel> getLstOverlayModel() {
        return lstOverlayModel;
    }

    public void setLstOverlayModel(ArrayList<OverlayModel> lstOverlayModel) {
        this.lstOverlayModel = lstOverlayModel;
    }

    public ArrayList<DecorModel> getLstDecorModel() {
        return lstDecorModel;
    }

    public void setLstDecorModel(ArrayList<DecorModel> lstDecorModel) {
        this.lstDecorModel = lstDecorModel;
    }

    public ArrayList<TemplateModel> getLstTempModel() {
        return lstTempModel;
    }

    public void setLstTempModel(ArrayList<TemplateModel> lstTempModel) {
        this.lstTempModel = lstTempModel;
    }

    public ArrayList<Matrix> getLstMatrix() {
        return lstMatrix;
    }

    public void setLstMatrix(ArrayList<Matrix> lstMatrix) {
        this.lstMatrix = lstMatrix;
    }
}
