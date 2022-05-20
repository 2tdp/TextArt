package com.datnt.textart.model;

import java.io.Serializable;

public class TextModel implements Serializable {
    private String content;
    private QuoteModel quoteModel;
    private FontModel fontModel;
    private int typeAlign;
    private int colorText;

    public TextModel(String content, QuoteModel quoteModel, FontModel fontModel, int typeAlign, int colorText) {
        this.content = content;
        this.quoteModel = quoteModel;
        this.fontModel = fontModel;
        this.typeAlign = typeAlign;
        this.colorText = colorText;
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

    public int getColorText() {
        return colorText;
    }

    public void setColorText(int colorText) {
        this.colorText = colorText;
    }
}
