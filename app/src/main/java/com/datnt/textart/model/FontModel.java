package com.datnt.textart.model;

import java.io.Serializable;
import java.util.ArrayList;

public class FontModel implements Serializable {
    private String nameFont;
    private ArrayList<StyleFontModel> lstStyle;
    private boolean isSelected;
    private boolean isFavorite;
    private boolean isYourFont;
    private boolean isNewFont;

    public FontModel(String nameFont, ArrayList<StyleFontModel> lstStyle, boolean isSelected, boolean isFavorite, boolean isYourFont, boolean isNewFont) {
        this.nameFont = nameFont;
        this.lstStyle = lstStyle;
        this.isSelected = isSelected;
        this.isFavorite = isFavorite;
        this.isYourFont = isYourFont;
        this.isNewFont = isNewFont;
    }

    public String getNameFont() {
        return nameFont;
    }

    public void setNameFont(String nameFont) {
        this.nameFont = nameFont;
    }

    public ArrayList<StyleFontModel> getLstStyle() {
        return lstStyle;
    }

    public void setLstStyle(ArrayList<StyleFontModel> lstStyle) {
        this.lstStyle = lstStyle;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isYourFont() {
        return isYourFont;
    }

    public void setYourFont(boolean yourFont) {
        isYourFont = yourFont;
    }

    public boolean isNewFont() {
        return isNewFont;
    }

    public void setNewFont(boolean newFont) {
        isNewFont = newFont;
    }
}
