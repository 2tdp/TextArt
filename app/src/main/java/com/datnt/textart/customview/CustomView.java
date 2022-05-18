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
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.datnt.textart.model.ColorModel;
import com.datnt.textart.sharepref.DataLocalManager;

public class CustomView extends View {

    private Path path;
    private Paint paint;
    private Shader shader;
    private Bitmap bitmap;
    private ColorModel color;
    private Rect src;
    private int size;
    private float w, h;

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
        if (bitmap != null) this.bitmap = bitmap;
        else this.color = color;
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
                    src = new Rect( (wB - hB) / 2, 0,  (wB + hB) / 2,  hB);
                } else if ((float) wB / hB < 1) {
                    src = new Rect(0,  (hB - wB) / 2,  wB,  (hB + wB) / 2);
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
                    return new RectF( (w - h) / 2, 0, (w + h) / 2, h);
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
            dst.set(0, (h * (1 - 1 / scale)) / 2, w,  (h * (1 + 1 / scale)) / 2);
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
