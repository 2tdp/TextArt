package com.datnt.textart.model;

import java.io.Serializable;

public class EmojiModel implements Serializable {
    private String nameEmoji;
    private String nameFolder;
    private boolean isSelected;

    public EmojiModel(String nameEmoji, String nameFolder, boolean isSelected) {
        this.nameEmoji = nameEmoji;
        this.nameFolder = nameFolder;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
