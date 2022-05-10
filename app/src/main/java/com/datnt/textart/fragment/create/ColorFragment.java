package com.datnt.textart.fragment.create;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datnt.textart.R;
import com.datnt.textart.customview.ColorPicker;
import com.datnt.textart.customview.MyAlphaSlideBar;
import com.datnt.textart.customview.MyBrightnessSlideBar;
import com.datnt.textart.customview.colorpickerview.AlphaTileView;
import com.datnt.textart.customview.colorpickerview.ColorEnvelope;
import com.datnt.textart.customview.colorpickerview.flag.BubbleFlag;
import com.datnt.textart.customview.colorpickerview.flag.FlagMode;
import com.datnt.textart.customview.colorpickerview.listeners.ColorEnvelopeListener;

public class ColorFragment extends Fragment {

    private LinearLayout llColorPicker;
    private ColorPicker colorPickerView;
    private MyAlphaSlideBar alphaSlideBar;
    private MyBrightnessSlideBar brightnessSlideBar;
    private TextView textView;
    private AlphaTileView alphaTileView;

    private ViewPager2 viewPager;

    public static ColorFragment newInstance() {
        ColorFragment fragment = new ColorFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_color, container, false);

        init(view);

        return view;
    }

    private void init(View view) {
        llColorPicker = view.findViewById(R.id.llColorPicker);
        colorPickerView = view.findViewById(R.id.colorPickerView);
        alphaSlideBar = view.findViewById(R.id.alphaSlideBar);
        brightnessSlideBar = view.findViewById(R.id.brightnessSlide);
        textView = view.findViewById(R.id.textView);
        alphaTileView = view.findViewById(R.id.alphaTileView);

        llColorPicker.getLayoutParams().height = getResources().getDisplayMetrics().heightPixels * 60 / 100;

        colorPickerView.setCheckTouch(isTouch -> viewPager.setUserInputEnabled(!isTouch));
        alphaSlideBar.setCheckTouch(isTouch -> viewPager.setUserInputEnabled(!isTouch));
        brightnessSlideBar.setCheckTouch(isTouch -> viewPager.setUserInputEnabled(!isTouch));

        setUpLayout();
        evenClick();
    }

    private void evenClick() {

    }

    private void setUpLayout() {
        BubbleFlag bubbleFlag = new BubbleFlag(requireContext());
        bubbleFlag.setFlagMode(FlagMode.FADE);
        colorPickerView.setFlagView(bubbleFlag);

        colorPickerView.setColorListener((ColorEnvelopeListener) (envelope, fromUser) -> {
            setLayoutColor(envelope);
        });

        colorPickerView.attachAlphaSlider(alphaSlideBar);
        colorPickerView.attachBrightnessSlider(brightnessSlideBar);
        colorPickerView.setLifecycleOwner(this);

    }

    @SuppressLint("SetTextI18n")
    private void setLayoutColor(ColorEnvelope envelope) {
        textView.setText("#" + envelope.getHexCode());
        alphaTileView.setPaintColor(envelope.getColor());
    }

    public void setViewPager(ViewPager2 viewPager) {
        this.viewPager = viewPager;
    }
}