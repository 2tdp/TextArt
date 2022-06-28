package com.datnt.textart.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View;


import androidx.annotation.Nullable;

import com.datnt.textart.R;
import com.datnt.textart.model.ColorModel;
import com.datnt.textart.utils.UtilsAdjust;

public class CutView extends View {

    private int size;
    private float w, h, l, t, r, b, lastY = 0;

    private Bitmap bitmap;
    private ColorModel color;
    private Paint paintGrid, paintBitmap, paintColor;
    private Path pathColor;
    private RectF rectGrid, rectBitmap;

    public CutView(Context context) {
        super(context);
        init();
    }

    public CutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {
        paintColor = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBitmap = new Paint(Paint.FILTER_BITMAP_FLAG);
        paintGrid = new Paint();

        pathColor = new Path();

        paintGrid.setStrokeWidth(10);
        paintGrid.setStyle(Paint.Style.STROKE);
        paintGrid.setColor(Color.WHITE);

        rectGrid = new RectF();
        rectBitmap = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        w = getWidth();
        h = getHeight();

        if (bitmap == null) {
            setSizeColor(size);
            resetColor();
            canvas.drawPath(pathColor, paintColor);
        } else {

            canvas.drawColor(Color.BLACK);
            rectGrid.set(0, (h - w) / 2f, getWidth(), (h + w) / 2f);
//        canvas.drawRect(rectGrid,paintGrid);

            canvas.drawBitmap(bitmap, null, rectBitmap, paintBitmap);
            drawGrid(canvas, rectGrid);
        }
    }

    public void setData(Bitmap bitmap, ColorModel color) {
        if (bitmap != null) {
            this.bitmap = bitmap;
            this.color = null;
        } else {
            this.bitmap = null;
            this.color = color;
        }
        invalidate();
    }

    void drawGrid(Canvas canvas, RectF rect) {
        canvas.drawLine(rect.left, rect.top, rect.left, rect.bottom, paintGrid);
        canvas.drawLine(rect.left + rect.width() / 3f, rect.top, rect.left + rect.width() / 3f, rect.bottom, paintGrid);
        canvas.drawLine(rect.left + rect.width() * 2 / 3f, rect.top, rect.left + rect.width() * 2 / 3f, rect.bottom, paintGrid);
        canvas.drawLine(rect.right, rect.top, rect.right, rect.bottom, paintGrid);

        canvas.drawLine(rect.left, rect.top, rect.right, rect.top, paintGrid);
        canvas.drawLine(rect.left, rect.top + rect.height() / 3f, rect.right, rect.top + rect.height() / 3f, paintGrid);
        canvas.drawLine(rect.left, rect.top + rect.height() * 2 / 3f, rect.right, rect.top + rect.height() * 2 / 3f, paintGrid);
        canvas.drawLine(rect.left, rect.bottom, rect.right, rect.bottom, paintGrid);
    }

    public Bitmap getBitmap() {
        Bitmap bmTemp1 = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmTemp1);
        canvas.drawColor(Color.RED);
        canvas.clipRect(rectGrid);
        canvas.drawBitmap(bitmap, null, rectBitmap, paintBitmap);
        Bitmap bmResult = Bitmap.createBitmap(bmTemp1.getWidth(), bmTemp1.getWidth(), Bitmap.Config.ARGB_8888);
        Canvas canvas1 = new Canvas(bmResult);
        Rect rect = new Rect();
        rect.set((int) rectGrid.left, (int) rectGrid.top, (int) rectGrid.right, (int) rectGrid.bottom);
        canvas1.drawBitmap(bmTemp1, 0, -rectGrid.top, paintBitmap);
        return bmResult;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float tempY = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                tempY = event.getY();
            case MotionEvent.ACTION_MOVE:
                t = event.getY();
                if ((t - 300) > rectGrid.top || w * 1f * bitmap.getHeight() / bitmap.getWidth() + t - 300 < rectGrid.bottom) {

                } else {
                    rectBitmap.set(0, t - 300, w, w * 1f * bitmap.getHeight() / bitmap.getWidth() + t - 300);
                    invalidate();
                }

            case MotionEvent.ACTION_UP:
                lastY = t;
        }
        return true;
    }

    public void setSize(int size) {
        this.size = size;
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

}
