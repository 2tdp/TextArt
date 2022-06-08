package com.datnt.textart.utils;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.Log;

import com.datnt.textart.model.ColorModel;

public class UtilsAdjust {

    private static final float lumR = 0.3086f; // or  0.2125
    private static final float lumG = 0.6094f;  // or  0.7154
    private static final float lumB = 0.0820f; // or  0.0721

    public static Bitmap createFlippedBitmap(Bitmap source, boolean xFlip, boolean yFlip) {
        Matrix matrix = new Matrix();
        matrix.postScale(xFlip ? -1 : 1, yFlip ? -1 : 1, source.getWidth() / 2f, source.getHeight() / 2f);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static Bitmap changeBitmapColor(Bitmap sourceBitmap, ColorModel color) {
        Bitmap resultBitmap = sourceBitmap.copy(sourceBitmap.getConfig(), true);
        Paint paint = new Paint();
        ColorFilter filter = new LightingColorFilter(color.getColorStart(), 1);
        paint.setColorFilter(filter);
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, paint);
        return resultBitmap;
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

    public static Bitmap adjustBrightness(Bitmap bmp, float brightness) {
        brightness = cleanValue(brightness, 100);
        if (brightness == 0) {
            return bmp;
        }

        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        1, 0, 0, 0, brightness,
                        0, 1, 0, 0, brightness,
                        0, 0, 1, 0, brightness,
                        0, 0, 0, 1, 0,
                });

        Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        Canvas canvas = new Canvas(ret);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bmp, 0, 0, paint);

        return ret;
    }

    public static Bitmap adjustContrast(Bitmap bmp, float contrast) {

        contrast = cleanValue(contrast, 100);

        if (contrast == 0) {
            return bmp;
        }

        float scale = contrast + 1f;
        float translate = (-0.5f * scale + 0.5f) * 255f;

        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        scale, 0, 0, 0, translate,
                        0, scale, 0, 0, translate,
                        0, 0, scale, 0, translate,
                        0, 0, 0, 1, 0,
                });


        Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        Canvas canvas = new Canvas(ret);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bmp, 0, 0, paint);

        return ret;
    }

    public static Bitmap adjustExposure(Bitmap bmp, float exposure) {
        exposure = cleanValue(exposure, 100);

        if (exposure == 0) {
            return bmp;
        }
        exposure = (float) Math.pow(2, exposure);

        float[] mat = new float[]
                {
                        exposure, 0, 0, 0, 0,
                        0, exposure, 0, 0, 0,
                        0, 0, exposure, 0, 0,
                        0, 0, 0, 1, 0
                };

        ColorMatrix cm = new ColorMatrix(mat);

        Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        Canvas canvas = new Canvas(ret);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bmp, 0, 0, paint);

        return ret;
    }

    public static Bitmap adjustHighLight(Bitmap bmp, float highLight) {
        highLight = cleanValue(highLight, 100);

        if (highLight == 0) {
            return bmp;
        }
        float x;
        if (highLight > 0)
            x = 1 + 3 * highLight / 100;
        else
            x = 3 * highLight / 100;

        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        lumR * (1 - x) + x, 0, 0, 0, 0,
                        0, lumG * (1 - x) + x, 0, 0, 0,
                        0, 0, lumB * (1 - x) + x, 0, 0,
                        0, 0, 0, 1, 0,
                });

        Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        Canvas canvas = new Canvas(ret);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bmp, 0, 0, paint);

        return ret;
    }

    public static Bitmap adjustSaturation(Bitmap bmp, float saturation) {
        saturation = cleanValue(saturation, 100);
        if (saturation == 0) {
            return bmp;
        }

        float x = 1 + ((saturation > 0) ? 3 * saturation / 100 : saturation / 100);

        float[] mat = new float[]
                {
                        lumR * (1 - x) + x, lumG * (1 - x), lumB * (1 - x), 0, 0,
                        lumR * (1 - x), lumG * (1 - x) + x, lumB * (1 - x), 0, 0,
                        lumR * (1 - x), lumG * (1 - x), lumB * (1 - x) + x, 0, 0,
                        0, 0, 0, 1, 0,
                };

        ColorMatrix cm = new ColorMatrix(mat);

        Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        Canvas canvas = new Canvas(ret);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bmp, 0, 0, paint);

        return ret;
    }

    public static Bitmap adjustHue(Bitmap bmp, float value) {
        value = cleanValue(value, 360f) / 360f * (float) Math.PI;
        if (value == 0) {
            return bmp;
        }
        float cosVal = (float) Math.cos(value);
        float sinVal = (float) Math.sin(value);

        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        lumR + cosVal * (1 - lumR) + sinVal * (-lumR), lumG + cosVal * (-lumG) + sinVal * (-lumG), lumB + cosVal * (-lumB) + sinVal * (1 - lumB), 0, 0,
                        lumR + cosVal * (-lumR) + sinVal * (0.143f), lumG + cosVal * (1 - lumG) + sinVal * (0.140f), lumB + cosVal * (-lumB) + sinVal * (-0.283f), 0, 0,
                        lumR + cosVal * (-lumR) + sinVal * (-(1 - lumR)), lumG + cosVal * (-lumG) + sinVal * (lumG), lumB + cosVal * (1 - lumB) + sinVal * (lumB), 0, 0,
                        0f, 0f, 0f, 1f, 0f});

        Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        Canvas canvas = new Canvas(ret);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bmp, 0, 0, paint);

        return ret;
    }

    protected static float cleanValue(float p_val, float p_limit) {
        return Math.min(p_limit, Math.max(-p_limit, p_val));
    }
}
