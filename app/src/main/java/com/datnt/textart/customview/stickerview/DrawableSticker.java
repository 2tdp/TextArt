package com.datnt.textart.customview.stickerview;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author wupanjie
 */
public class DrawableSticker extends Sticker {

    private Drawable drawable;
    private int id;
    private boolean isImage, isOverlay, isDecor, isTemplate;
    private Rect realBounds;

    public DrawableSticker(Drawable drawable, int id, boolean isImage, boolean isOverlay, boolean isDecor, boolean isTemplate) {
        this.drawable = drawable;
        this.id = id;
        this.isImage = isImage;
        this.isOverlay = isOverlay;
        this.isDecor = isDecor;
        this.isTemplate = isTemplate;
        realBounds = new Rect(0, 0, getWidth(), getHeight());
    }

    @NonNull
    public Drawable getDrawable() {
        return drawable;
    }

    public DrawableSticker setDrawable(@Nullable Drawable drawable) {
        this.drawable = drawable;
        return this;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.save();
        canvas.concat(getMatrix());
        drawable.setBounds(realBounds);
        drawable.draw(canvas);
        canvas.restore();
    }

    @NonNull
    @Override
    public DrawableSticker setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        drawable.setAlpha(alpha);
        return this;
    }

    public int getAlpha() {
        return drawable.getAlpha();
    }

    @Override
    public int getWidth() {
        return drawable.getIntrinsicWidth();
    }

    @Override
    public int getHeight() {
        return drawable.getIntrinsicHeight();
    }

    public boolean isTemplate() {
        return isTemplate;
    }

    public void setTemplate(boolean template) {
        isTemplate = template;
    }

    public boolean isDecor() {
        return isDecor;
    }

    public void setDecor(boolean decor) {
        isDecor = decor;
    }

    public boolean isOverlay() {
        return isOverlay;
    }

    public void setOverlay(boolean overlay) {
        isOverlay = overlay;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void release() {
        super.release();
        if (drawable != null) {
            drawable = null;
        }
    }
}
