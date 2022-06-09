package com.datnt.textart.model;

import java.io.Serializable;

public class TemplateModel implements Serializable {
    private String name;
    private String background;
    private String text;

    public TemplateModel(String name, String background, String text) {
        this.name = name;
        this.background = background;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
