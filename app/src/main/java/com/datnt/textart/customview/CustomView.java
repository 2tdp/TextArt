package com.datnt.textart.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.datnt.textart.model.ColorModel;
import com.datnt.textart.sharepref.DataLocalManager;

public class CustomView extends View {

    private Path path, pathClear;
    private Paint paint;
    private Shader shader;
    private Bitmap bitmap;
    private ColorModel color;
    private int size;
    int w, h;

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
        if (pathClear == null) pathClear = new Path();

        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        w = getWidth();
        h = getHeight();

        pathClear.addRect(0, 0, w / 2f, h / 2f, Path.Direction.CW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (DataLocalManager.getOption("bitmap").equals("")) {
            resetSize(size);
            resetColor();
            canvas.clipPath(path, Region.Op.INTERSECT);
            canvas.drawPath(path, paint);
        } else {
            DataLocalManager.setOption("", "bitmap");
            canvas.drawBitmap(resetBitmap(size), 0, 0, paint);
        }
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        invalidate();
    }

    public void setColor(ColorModel color) {
        this.color = color;
        invalidate();
    }

    public void setSize(int size) {
        this.size = size;
        invalidate();
    }

    private Bitmap resetBitmap(int size) {

        return bitmap;
    }

    private void resetSize(int pos) {
        switch (pos) {
            case 1:
                path.addRect(0, 0, w, w, Path.Direction.CW);
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            default:
                path.addRect(0, 0, w, h, Path.Direction.CW);
                break;
        }
    }

    private void resetColor() {
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
        }
        if (shader != null) paint.setShader(shader);
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
                return new int[]{w / 2, 0, w / 2, h};
            case 1:
                return new int[]{0, 0, w, h};
            case 2:
                return new int[]{0, h / 2, w, h / 2};
            case 3:
                return new int[]{0, h, w, 0};
        }
        return new int[]{0, 0, 0, 0};
    }
}
