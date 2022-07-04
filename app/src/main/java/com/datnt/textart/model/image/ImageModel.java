package com.datnt.textart.model.image;

import android.graphics.Bitmap;

import com.datnt.textart.model.ShadowModel;

import java.io.Serializable;

public class ImageModel implements Serializable {

    private Bitmap image;
    private Bitmap imageRoot;
    private ShadowModel shadowModel;
    private int posFilter;
    private int posBlend;

    public ImageModel(Bitmap image, Bitmap imageRoot, ShadowModel shadowModel, int posFilter, int posBlend) {
        this.image = image;
        this.imageRoot = imageRoot;
        this.shadowModel = shadowModel;
        this.posFilter = posFilter;
        this.posBlend = posBlend;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImageRoot() {
        return imageRoot;
    }

    public void setImageRoot(Bitmap imageRoot) {
        this.imageRoot = imageRoot;
    }

    public ShadowModel getShadowModel() {
        return shadowModel;
    }

    public void setShadowModel(ShadowModel shadowModel) {
        this.shadowModel = shadowModel;
    }

    public int getPosFilter() {
        return posFilter;
    }

    public void setPosFilter(int posFilter) {
        this.posFilter = posFilter;
    }

    public int getPosBlend() {
        return posBlend;
    }

    public void setPosBlend(int posBlend) {
        this.posBlend = posBlend;
    }
}
