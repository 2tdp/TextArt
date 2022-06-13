package com.datnt.textart.customview.stickerview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.datnt.textart.utils.UtilsAdjust;

/**
 * @author wupanjie
 */
public class DrawableSticker extends Sticker {

    private Path path;
    private Paint paint;
    private Drawable drawable;
    private int id, color;
    private float dx, dy, radiusBlur;
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
        path = new Path();
        path.addRect(0, 0, realBounds.width(), realBounds.height(), Path.Direction.CW);
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
        if (paint != null)
            canvas.drawPath(path, paint);
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

    public void setShadow(float radiusBlur, float dx, float dy, int color) {
        this.color = color;
        this.dx = dx;
        this.dy = dy;
        this.radiusBlur = radiusBlur;

        paint = new Paint();
        if (color == 0) paint.setShadowLayer(radiusBlur, dx, dy, Color.BLACK);
        else
            paint.setShadowLayer(radiusBlur, dx, dy, Color.parseColor(UtilsAdjust.toRGBString(color)));
    }

    public float getRadiusBlur() {
        return radiusBlur;
    }

    public float getDx() {
        return dx;
    }

    public float getDy() {
        return dy;
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
