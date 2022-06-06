package com.datnt.textart.model;

import java.io.Serializable;

public class DecorModel implements Serializable {
    private String nameDecor;
    private String nameFolder;
    private boolean isSelected;

    public DecorModel(String nameDecor, String nameFolder, boolean isSelected) {
        this.nameDecor = nameDecor;
        this.nameFolder = nameFolder;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
