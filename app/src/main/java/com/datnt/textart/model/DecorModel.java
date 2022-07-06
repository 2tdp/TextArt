package com.datnt.textart.model;


import java.io.Serializable;
import java.util.ArrayList;

public class DecorModel implements Serializable {
    private String nameDecor;
    private String nameFolder;
    private ArrayList<String> lstPathData;
    private ColorModel colorModel;
    private boolean isSelected;

    public DecorModel(String nameDecor, String nameFolder, ArrayList<String> lstPathData, ColorModel colorModel, boolean isSelected) {
        this.nameDecor = nameDecor;
        this.nameFolder = nameFolder;
        this.lstPathData = lstPathData;
        this.colorModel = colorModel;
        this.isSelected = isSelected;
    }

    public String getNameDecor() {
        return nameDecor;
    }

    public void setNameDecor(String nameDecor) {
        this.nameDecor = nameDecor;
    }

    public String getNameFolder() {
        return nameFolder;
    }

    public void setNameFolder(String nameFolder) {
        this.nameFolder = nameFolder;
    }

    public ArrayList<String> getLstPathData() {
        return lstPathData;
    }

    public void setLstPathData(ArrayList<String> lstPathData) {
        this.lstPathData = lstPathData;
    }

    public ColorModel getColorModel() {
        return colorModel;
    }

    public void setColorModel(ColorModel colorModel) {
        this.colorModel = colorModel;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
