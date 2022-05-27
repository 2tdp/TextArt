package com.datnt.textart.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.datnt.textart.R;

public class CustomSeekbarRunText extends View {

    private Paint paint, paintProgress, paintText;
    private Rect rectText;
    private int progress, max, colorText, sizeText;
    private float sizeThumb, sizeBg, sizePos;
    private OnSeekbarResult onSeekbarResult;

    public void setOnSeekbarResult(OnSeekbarResult onSeekbarResult) {
        this.onSeekbarResult = onSeekbarResult;
    }

    public CustomSeekbarRunText(Context context) {
        super(context);
        init();
    }

    public CustomSeekbarRunText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomSeekbarRunText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        sizeThumb = 30f;
        sizeBg = 10f;
        sizePos = 13f;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintProgress = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paintText.setStrokeJoin(Paint.Join.ROUND);
        paintText.setStrokeCap(Paint.Cap.ROUND);

        paint.setStyle(Paint.Style.FILL);
        paintProgress.setStyle(Paint.Style.FILL);
        paintProgress.setStrokeJoin(Paint.Join.ROUND);
        paintProgress.setStrokeCap(Paint.Cap.ROUND);

        rectText = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.clearShadowLayer();
        paint.setColor(getResources().getColor(R.color.gray_2));
        paint.setStrokeWidth(sizeBg);
        int temp = 45;
        canvas.drawLine(sizeThumb / 2 + temp, getHeight() / 2f, getWidth() - sizeThumb / 2 - temp, getHeight() / 2f, paint);

        paintProgress.setColor(getResources().getColor(R.color.green));
        paintProgress.setStrokeWidth(sizePos);
        float p = (getWidth() - sizeThumb - 2 * temp) * progress / max + sizeThumb / 2 + temp;
        canvas.drawLine(sizeThumb / 2 + temp, getHeight() / 2f, p, getHeight() / 2f, paintProgress);

        paint.setColor(getResources().getColor(R.color.green));
        paint.setShadowLayer(sizeThumb / 8, 0, 0, Color.WHITE);
        canvas.drawCircle(p, getHeight() / 2f, sizeThumb / 2, paint);

        String str = String.valueOf(progress);
        paintText.setTextSize(getResources().getDimension(sizeText));
        paintText.setColor(Color.parseColor(CustomView.toRGBString(colorText)));
        paintText.getTextBounds(str, 0, str.length(), rectText);
        str = progress + getResources().getString(R.string.percent);
        canvas.drawText(str, p - rectText.width() / 2f, getHeight() / 2f - rectText.height(), paintText);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        int value;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (onSeekbarResult != null) onSeekbarResult.onDown(this);
                break;
            case MotionEvent.ACTION_MOVE:
                progress = (int) ((x - sizeThumb / 2) * max / ((getWidth() - sizeThumb)));

                if (progress < 0) progress = 0;
                else if (progress > max) progress = max;
                invalidate();
                if (onSeekbarResult != null) onSeekbarResult.onMove(this, progress);
                break;
            case MotionEvent.ACTION_UP:
                if (onSeekbarResult != null) onSeekbarResult.onUp(this);
                break;
        }
        return true;
    }

    public void setColorText(int colorText) {
        this.colorText = colorText;
    }

    public void setSizeText(int sizeText) {
        this.sizeText = sizeText;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    public int getProgress() {
        return progress;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
        invalidate();
    }
}
