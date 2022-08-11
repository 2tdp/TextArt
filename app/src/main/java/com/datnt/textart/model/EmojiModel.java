package com.datnt.textart.model;

import java.io.Serializable;

public class EmojiModel implements Serializable {

    private String nameEmoji;
    private String nameFolder;
    private int opacity;
    private boolean isSelected;

    public EmojiModel(String nameEmoji, String nameFolder, int opacity, boolean isSelected) {
        this.nameEmoji = nameEmoji;
        this.nameFolder = nameFolder;
        this.opacity = opacity;
        this.isSelected = isSelected;
    }

    public String getNameEmoji() {
        return nameEmoji;
    }

    public void setNameEmoji(String nameEmoji) {
        this.nameEmoji = nameEmoji;
    }

    public String getNameFolder() {
        return nameFolder;
    }

    public void setNameFolder(String nameFolder) {
        this.nameFolder = nameFolder;
    }

    public int getOpacity() {
        return opacity;
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
