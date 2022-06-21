package com.datnt.textart.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.graphics.PathParser;

public class CropImage extends View implements MatrixGestureDetector.OnMatrixChangeListener {

    private Path path;
    private Paint paint;
    private RectF rectF;

    private Bitmap bitmap;
    private Matrix matrix = new Matrix();
    private Matrix maskMatrix = new Matrix();
    private MatrixGestureDetector detector;
    private RectF clip = new RectF();

    public CropImage(Context context) {
        super(context);
        init();
    }

    public CropImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CropImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    Paint paintBitmap;

    private void init() {
        path = new Path();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);

        rectF = new RectF();

        detector = new MatrixGestureDetector(maskMatrix, this);

        paintBitmap = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBitmap.setStrokeJoin(Paint.Join.ROUND);
        paintBitmap.setStrokeCap(Paint.Cap.ROUND);
//        paintBitmap.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (bitmap != null) {
            canvas.save();
            canvas.drawBitmap(bitmap, maskMatrix, null);
            canvas.restore();
        }
        path.computeBounds(rectF, true);
        int y = (int) (getWidth() * 0.5f * rectF.height() / rectF.width());

        clipPath(canvas, getWidth() / 2f, getWidth() / 4, (getHeight() - y) / 2);

        if (path != null) {
//            drawIconWithPath(canvas, path, getWidth() / 2f, getWidth() / 4, (getHeight() - y) / 2);
            clipPath(canvas, getWidth() / 2f, getWidth() / 4, (getHeight() - y) / 2);
        }
    }

    private void clipPath(Canvas canvas, float size, int x, int y) {
        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        float scale = size / rectF.width();
        canvas.save();
        canvas.translate(x, y);
        canvas.scale(scale, scale);
        canvas.clipPath(path);
        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        if (bitmap != null) {
            RectF src = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
            RectF dst = new RectF(0, 0, w, h);
            matrix.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);
            matrix.mapRect(dst, src);

            maskMatrix.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);
            setupClip();
        }
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        invalidate();
    }

    public void drawIconWithPath(Canvas canvas, Path path, float size, int x, int y) {
        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        float scale = size / rectF.width();
        canvas.save();
        canvas.translate(x, y);
        canvas.scale(scale, scale);
        canvas.drawPath(path, paintBitmap);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (bitmap != null)
            detector.onTouchEvent(event);
        return true;
    }

    private void setupClip() {
        clip.set(0, 0, getWidth(), getHeight());
        maskMatrix.mapRect(clip);
    }

    @Override
    public void onChange(Matrix matrix) {
        setupClip();
        invalidate();
    }

    public void setPath(String o) {
        path.reset();
        path.addPath(PathParser.createPathFromPathData(o));
        invalidate();
    }
}
