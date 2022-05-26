package com.datnt.textart.model;

import java.io.Serializable;

public class TextModel implements Serializable {
    private String content;
    private QuoteModel quoteModel;
    private FontModel fontModel;
    private int typeAlign;
    private ColorModel color;

    public TextModel(TextModel textModel) {
        this.content = textModel.getContent();
        this.quoteModel = textModel.quoteModel;
        this.fontModel = textModel.fontModel;
        this.typeAlign = textModel.typeAlign;
        this.color = textModel.color;
    }

    public TextModel(String content, QuoteModel quoteModel, FontModel fontModel, int typeAlign, ColorModel color) {
        this.content = content;
        this.quoteModel = quoteModel;
        this.fontModel = fontModel;
        this.typeAlign = typeAlign;
        this.color = color;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public QuoteModel getQuoteModel() {
        return quoteModel;
    }

    public void setQuoteModel(QuoteModel quoteModel) {
        this.quoteModel = quoteModel;
    }

    public FontModel getFontModel() {
        return fontModel;
    }

    public void setFontModel(FontModel fontModel) {
        this.fontModel = fontModel;
    }

    public int getTypeAlign() {
        return typeAlign;
    }

    public void setTypeAlign(int typeAlign) {
        this.typeAlign = typeAlign;
    }

    public ColorModel getColor() {
        return color;
    }

    public void setColor(ColorModel color) {
        this.color = color;
    }
}
