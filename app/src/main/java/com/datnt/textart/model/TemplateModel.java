package com.datnt.textart.model;

import java.io.Serializable;
import java.util.ArrayList;

public class TemplateModel implements Serializable {
    private String name;
    private String background;
    private String text;
    private ArrayList<String> lstPathData;

    public TemplateModel(String name, String background, String text, ArrayList<String> lstPathData) {
        this.name = name;
        this.background = background;
        this.text = text;
        this.lstPathData = lstPathData;
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

    public ArrayList<String> getLstPathData() {
        return lstPathData;
    }

    public void setLstPathData(ArrayList<String> lstPathData) {
        this.lstPathData = lstPathData;
    }
}
