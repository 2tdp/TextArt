package com.datnt.textart.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.datnt.textart.model.ColorModel;

public class CustomColor extends View {

    private Path path;
    private Paint paint;
    private Shader shader;
    int w, h;

    public CustomColor(Context context) {
        super(context);
        init();
    }

    public CustomColor(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomColor(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        if (paint == null) paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (path == null) path = new Path();

        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        w = getWidth();
        h = getHeight();

        path.addRoundRect(0, 0, w, h, 34f, 34f, Path.Direction.CW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if (shader != null) paint.setShader(shader);

        canvas.drawPath(path, paint);
    }

    public void setColor(ColorModel color) {
        shader = new LinearGradient(setDirection(color.getDirection())[0],
                setDirection(color.getDirection())[1],
                setDirection(color.getDirection())[2],
                setDirection(color.getDirection())[3],
                new int[]{Color.parseColor(toRGBString(color.getColorStart())), Color.parseColor(toRGBString(color.getColorEnd()))}, new float[]{0, 1}, Shader.TileMode.MIRROR);
        invalidate();
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

    private int[] setDirection(int pos) {
        switch (pos) {
            case 0:
                return new int[]{0, h, w, h / 2};
            case 1:
                return new int[]{0, h, w, 0};
            case 2:
                return new int[]{w / 2, 0, w / 2, h};
            case 3:
                return new int[]{0, 0, w, h};
        }
        return new int[]{0, 0, 0, 0};
    }
}
