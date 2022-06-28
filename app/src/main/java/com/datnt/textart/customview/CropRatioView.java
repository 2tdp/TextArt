package com.datnt.textart.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;

import com.datnt.textart.model.ColorModel;
import com.datnt.textart.utils.UtilsAdjust;
import com.edmodo.cropper.CropImageView;

public class CropRatioView extends CropImageView {

    private int pos;
    private float w, h;

    private ColorModel color;
    private Bitmap bmRoot;

    private Paint paintColor;
    private Path pathColor;

    public CropRatioView(Context context) {
        super(context);
        init();
    }

    public CropRatioView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paintColor = new Paint(Paint.ANTI_ALIAS_FLAG);

        pathColor = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        w = getWidth();
        h = getHeight();

        if (color != null) {
            setSizeColor(pos);
            resetColor();
            canvas.drawPath(pathColor, paintColor);
        }
    }

    public void setSize(int pos) {
        this.pos = pos;
        switch (pos) {
            case 0:
                setAspectRatio(bmRoot.getWidth(), bmRoot.getHeight());
                break;
            case 1:
                setAspectRatio(1, 1);
                break;
            case 2:
                setAspectRatio(9, 16);
                break;
            case 3:
                setAspectRatio(4, 5);
                break;
            case 4:
                setAspectRatio(16, 9);
                break;
        }
        setFixedAspectRatio(true);
        setGuidelines(2);

        invalidate();
    }

    public void setData(Bitmap bitmap, ColorModel color) {
        if (bitmap != null) {
            this.bmRoot = bitmap;
            setImageBitmap(bitmap);
            this.color = null;
        } else {
            this.color = color;
        }
        invalidate();
    }

    private void setSizeColor(int pos) {
        pathColor.reset();
        switch (pos) {
            case 1:
//                if (w / h > 1) {
//                    pathColor.addRect((w - h) / 2, 0.0f, (w + h) / 2, h, Path.Direction.CW);
//                } else if (w / h < 1) {
//                    pathColor.addRect(0, (h - w) / 2, w, (h + w) / 2, Path.Direction.CW);
//                }
                setPath(1f);
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
                pathColor.addRect(0, 0, w, h, Path.Direction.CW);
                break;
        }
        invalidate();
    }

    private void setPath(float scale) {
        if (w / h > scale) {
            pathColor.addRect((w * (1 - scale)) / 2, 0, (w * (1 + scale)) / 2, h, Path.Direction.CW);
        } else if (w / h < scale) {
            pathColor.addRect(0, (h * (1 - 1 / scale)) / 2, w, (h * (1 + 1 / scale)) / 2, Path.Direction.CW);
        }
    }

    private void resetColor() {
        if (color != null)
            if (color.getColorStart() == color.getColorEnd()) {
                paintColor.setColor(color.getColorStart());
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

                Shader shader = new LinearGradient(setDirection(color.getDirec())[0],
                        setDirection(color.getDirec())[1],
                        setDirection(color.getDirec())[2],
                        setDirection(color.getDirec())[3],
                        new int[]{Color.parseColor(UtilsAdjust.toRGBString(color.getColorStart())), Color.parseColor(UtilsAdjust.toRGBString(color.getColorEnd()))},
                        new float[]{0, 1}, Shader.TileMode.MIRROR);

                paintColor.setShader(shader);
            }
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

    public Bitmap getBitmap() {
        return bmRoot;
    }
}
