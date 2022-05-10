package com.datnt.textart.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.datnt.textart.callback.ICheckTouch;
import com.datnt.textart.customview.colorpickerview.sliders.AlphaSlideBar;

public class MyAlphaSlideBar extends AlphaSlideBar {

    private ICheckTouch checkTouch;

    public MyAlphaSlideBar(Context context) {
        super(context);
    }

    public MyAlphaSlideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyAlphaSlideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (checkTouch != null)
                    checkTouch.checkTouch(true);
                break;
            case MotionEvent.ACTION_UP:
                if (checkTouch != null)
                    checkTouch.checkTouch(false);
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setCheckTouch(ICheckTouch checkTouch) {
        this.checkTouch = checkTouch;
    }
}
