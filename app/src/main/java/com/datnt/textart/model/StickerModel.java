package com.datnt.textart.model;

import java.io.Serializable;

public class StickerModel implements Serializable {
    private String name;
    private TextModel text;

    public StickerModel(String name, TextModel text) {
        this.name = name;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TextModel getText() {
        return text;
    }

    public void setText(TextModel text) {
        this.text = text;
    }
}
