package com.datnt.textart.model;

import java.io.Serializable;

public class OverlayModel implements Serializable {
    private String nameOverlay;
    private String nameFolder;
    private boolean isSelected;

    public OverlayModel(String nameOverlay, String nameFolder, boolean isSelected) {
        this.nameOverlay = nameOverlay;
        this.nameFolder = nameFolder;
        this.isSelected = isSelected;
    }

    public String getNameOverlay() {
        return nameOverlay;
    }

    public void setNameOverlay(String nameEmoji) {
        this.nameOverlay = nameEmoji;
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
