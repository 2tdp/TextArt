package com.datnt.textart.model;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.datnt.textart.model.background.BackgroundModel;
import com.datnt.textart.model.textsticker.TextModel;

import java.io.Serializable;
import java.util.ArrayList;

public class Project implements Serializable {

    private boolean isPic;
    private boolean isColor;
    private boolean isTemp;

    private BackgroundModel backgroundModel;
    private Bitmap thumb;

    private ArrayList<TextModel> lstTextModel;
    private Matrix matrix;

    public Project(BackgroundModel backgroundModel, ArrayList<TextModel> lstTextModel, Matrix matrix, Bitmap thumb) {
        this.backgroundModel = backgroundModel;
        this.lstTextModel = lstTextModel;
        this.matrix = matrix;
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

    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }
}
