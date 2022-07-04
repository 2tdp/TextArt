package com.datnt.textart.model.textsticker;

import com.datnt.textart.model.ColorModel;
import com.datnt.textart.model.ShadowModel;

import java.io.Serializable;

public class TextModel implements Serializable {

    private String content;
    private QuoteModel quoteModel;
    private FontModel fontModel;
    private int typeAlign;
    private ColorModel color;
    private ShadowModel shadowModel;
    private ShearTextModel shearTextModel;
    private int opacity;

    public TextModel(TextModel textModel) {
        this.content = textModel.getContent();
        this.quoteModel = textModel.getQuoteModel();
        this.fontModel = textModel.getFontModel();
        this.typeAlign = textModel.getTypeAlign();
        this.color = textModel.getColor();
        this.shadowModel = textModel.getShadowModel();
        this.shearTextModel = textModel.getShearTextModel();
        this.opacity = textModel.getOpacity();
    }

    public TextModel(String content, QuoteModel quoteModel, FontModel fontModel, int typeAlign,
                     ColorModel color, ShadowModel shadowModel, ShearTextModel shearTextModel, int opacity) {
        this.content = content;
        this.quoteModel = quoteModel;
        this.fontModel = fontModel;
        this.typeAlign = typeAlign;
        this.color = color;
        this.shadowModel = shadowModel;
        this.shearTextModel = shearTextModel;
        this.opacity = opacity;
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

    public ShadowModel getShadowModel() {
        return shadowModel;
    }

    public void setShadowModel(ShadowModel shadowModel) {
        this.shadowModel = shadowModel;
    }

    public ShearTextModel getShearTextModel() {
        return shearTextModel;
    }

    public void setShearTextModel(ShearTextModel shearTextModel) {
        this.shearTextModel = shearTextModel;
    }

    public int getOpacity() {
        return opacity;
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }
}
