package com.datnt.textart.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.datnt.textart.model.ColorModel;
import com.datnt.textart.sharepref.DataLocalManager;
import com.datnt.textart.utils.Utils;
import com.datnt.textart.utils.UtilsAdjust;

public class CustomView extends View {

    private Path path;
    private Paint paint;
    private Shader shader;
    private Bitmap bitmap, bitmapRoot;
    private ColorModel color;
    private Rect src;
    private int size;
    private float w, h;
    private float brightness = 0f, contrast = 0f, exposure = 0f, highlight = 0f, shadow = 0f,
            black = 0f, white = 0f, saturation = 0f, hue = 0f, warmth = 0f, vibrance = 0f, vignette = 0f;

    public CustomView(Context context) {
        super(context);
        init();
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        if (paint == null) paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (path == null) path = new Path();

        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getWidth() == 0 || getHeight() == 0) return;
        w = getWidth();
        h = getHeight();

        if (bitmap != null) {
            setSRC(size);
            canvas.drawBitmap(bitmap, src, setDST(size), paint);
        } else {
            setSizeColor(size);
            resetColor();
            canvas.drawPath(path, paint);
        }
    }

    public void setData(Bitmap bitmap, ColorModel color) {
        if (bitmap != null) {
            this.bitmap = bitmap;
            this.bitmapRoot = bitmap;
        } else {
            this.bitmap = null;
            this.color = color;
        }
        invalidate();
    }

    public void setSize(int size) {
        this.size = size;
        invalidate();
    }

    private void setSRC(int size) {
        switch (size) {
            case 1:
                int wB = bitmap.getWidth();
                int hB = bitmap.getHeight();

                if ((float) wB / hB > 1) {
                    src = new Rect((wB - hB) / 2, 0, (wB + hB) / 2, hB);
                } else if ((float) wB / hB < 1) {
                    src = new Rect(0, (hB - wB) / 2, wB, (hB + wB) / 2);
                }
                break;
            case 2:
                createSRC(9 / 16f);
                break;
            case 3:
                createSRC(4 / 5f);
                break;
            case 4:
                createSRC(16 / 9f);
                break;
            default:
                src = null;
                break;
        }
    }

    private void createSRC(float scale) {

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        if ((float) w / h > scale) {
            src = new Rect((int) (w * (1 - scale)) / 2, 0, (int) (w * (1 + scale)) / 2, (int) h);
        } else if ((float) w / h < scale) {
            src = new Rect(0, (int) (h * (1 - 1 / scale)) / 2, (int) w, (int) (h * (1 + 1 / scale)) / 2);
        }
    }

    private RectF setDST(int size) {
        switch (size) {
            case 1:
                if (w / h > 1) {
                    return new RectF((w - h) / 2, 0, (w + h) / 2, h);
                } else if (w / h < 1) {
                    return new RectF(0, (h - w) / 2, w, (h + w) / 2);
                }
            case 2:
                return createDST(9 / 16f);
            case 3:
                return createDST(4 / 5f);
            case 4:
                return createDST(16 / 9f);
            default:
                float sourceWidth = bitmap.getWidth();
                float sourceHeight = bitmap.getHeight();

                float xScale = w / sourceWidth;
                float yScale = h / sourceHeight;
                float scale = Math.max(xScale, yScale);

                float scaledWidth = scale * sourceWidth;
                float scaledHeight = scale * sourceHeight;
                if (scale == xScale) {
                    return new RectF(0, -(scaledHeight - h) / 2, w, (scaledHeight + h) / 2);
                } else {
                    return new RectF(-(scaledWidth - w) / 2, 0, (w + scaledWidth) / 2, h);
                }
        }
    }

    private RectF createDST(float scale) {
        RectF dst = new RectF();

        if (w / h < scale) {
            dst.set(0, (h * (1 - 1 / scale)) / 2, w, (h * (1 + 1 / scale)) / 2);
        } else if (w / h > scale) {
            dst.set((w * (1 - scale)) / 2, 0, (w * (1 + scale)) / 2, h);
        }

        return dst;
    }

    private void setSizeColor(int pos) {
        path.reset();
        switch (pos) {
            case 1:
                if (w / h > 1) {
                    path.addRect((w - h) / 2, 0.0f, (w + h) / 2, h, Path.Direction.CW);
                } else if (w / h < 1) {
                    path.addRect(0, (h - w) / 2, w, (h + w) / 2, Path.Direction.CW);
                }
                break;
            case 2:
                setPath(9 / 16f);
                break;
            case 3:
                setPath(4 / 5f);
                break;
            case 4:
                setPath(16 / 9f);
                break;
            default:
                path.addRect(0, 0, w, h, Path.Direction.CW);
                break;
        }
        invalidate();
    }

    private void setPath(float scale) {
        if (w / h > scale) {
            path.addRect((w * (1 - scale)) / 2, 0, (w * (1 + scale)) / 2, h, Path.Direction.CW);
        } else if (w / h < scale) {
            path.addRect(0, (h * (1 - 1 / scale)) / 2, w, (h * (1 + 1 / scale)) / 2, Path.Direction.CW);
        }
    }

    private void resetColor() {
        if (color != null)
            if (color.getColorStart() == color.getColorEnd()) {
                paint.setColor(color.getColorStart());
            } else {
                if (color.getDirec() == 4) {
                    int c = color.getColorStart();
                    color.setColorStart(color.getColorEnd());
                    color.setColorEnd(c);

                    color.setDirec(0);
                } else if (color.getDirec() == 5) {
                    int c = color.getColorStart();
                    color.setColorStart(color.getColorEnd());
                    color.setColorEnd(c);

                    color.setDirec(2);
                }

                shader = new LinearGradient(setDirection(color.getDirec())[0],
                        setDirection(color.getDirec())[1],
                        setDirection(color.getDirec())[2],
                        setDirection(color.getDirec())[3],
                        new int[]{Color.parseColor(toRGBString(color.getColorStart())), Color.parseColor(toRGBString(color.getColorEnd()))},
                        new float[]{0, 1}, Shader.TileMode.MIRROR);

                paint.setShader(shader);
            }
    }

    public static String toRGBString(int color) {
        // format: #RRGGBB
        String red = Integer.toHexString(Color.red(color));
        String green = Integer.toHexString(Color.green(color));
        String blue = Integer.toHexString(Color.blue(color));
        if (red.length() == 1)
            red = "0" + red;
        if (green.length() == 1)
            green = "0" + green;
        if (blue.length() == 1)
            blue = "0" + blue;
        return "#" + red + green + blue;
    }

    private int[] setDirection(int direc) {
        switch (direc) {
            case 0:
                return new int[]{(int) w / 2, 0, (int) w / 2, (int) h};
            case 1:
                return new int[]{0, 0, (int) w, (int) h};
            case 2:
                return new int[]{0, (int) h / 2, (int) w, (int) h / 2};
            case 3:
                return new int[]{0, (int) h, (int) w, 0};
        }
        return new int[]{0, 0, 0, 0};
    }

    public float getBrightness() {
        if (brightness == 0) return 0;
        else return brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
        bitmap = UtilsAdjust.adjustBrightness(bitmapRoot, brightness * 255 / 100f);
        invalidate();
    }

    public float getContrast() {
        if (contrast == 0) return 0;
        else return contrast;
    }

    public void setContrast(float contrast) {
        this.contrast = contrast;
        if (contrast > 1) {
            contrast = contrast / 50 + 1;
        } else if (contrast < 1) {
            contrast = 1 + contrast / 50;
        }
        bitmap = UtilsAdjust.adjustContrast(bitmapRoot, contrast);
        invalidate();
    }

    public float getExposure() {
        if (exposure == 0) return 0;
        else return exposure;
    }

    public void setExposure(float exposure) {
        this.exposure = exposure;
        if (exposure > 1) {
            exposure = exposure / 50 + 1;
        } else if (exposure < 1) {
            exposure = 1 + exposure / 50;
        }
        bitmap = UtilsAdjust.adjustExposure(bitmapRoot, exposure);
        invalidate();
    }

    public float getHighlight() {
        if (highlight == 0) return 0;
        else return highlight;
    }

    public void setHighlight(float highlight) {
        this.highlight = highlight;
        if (highlight > 1) {
            highlight = highlight / 50 + 1;
        } else if (highlight < 1) {
            highlight = 1 + highlight / 50;
        }
        bitmap = UtilsAdjust.adjustHighLight(bitmapRoot, highlight);
        invalidate();
    }

    public float getShadow() {
        return shadow;
    }

    public void setShadow(float shadow) {
        this.shadow = shadow;
    }

    public float getBlack() {
        return black;
    }

    public void setBlack(float black) {
        this.black = black;
    }

    public float getWhite() {
        return white;
    }

    public void setWhite(float white) {
        this.white = white;
    }

    public float getSaturation() {
        if (saturation == 0) return 0;
        else return saturation;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
//        if (saturation > 1) {
//            saturation = saturation / 50 + 1;
//        } else if (saturation < 1) {
//            saturation = 1 + saturation / 50;
//        }
        Log.d("2tdp", "setSaturation: " + saturation);
        bitmap = UtilsAdjust.adjustSaturation(bitmapRoot, saturation * 255 / 100f);
        invalidate();
    }

    public float getHue() {
        if (hue == 0) return 0;
        else return hue;
    }

    public void setHue(float hue) {
        this.hue = hue;
        bitmap = UtilsAdjust.adjustHue(bitmapRoot, hue * 255 / 100f);
        invalidate();
    }

    public float getWarmth() {
        return warmth;
    }

    public void setWarmth(float warmth) {
        this.warmth = warmth;
    }

    public float getVibrance() {
        return vibrance;
    }

    public void setVibrance(float vibrance) {
        this.vibrance = vibrance;
    }

    public float getVignette() {
        return vignette;
    }

    public void setVignette(float vignette) {
        this.vignette = vignette;
    }
}
