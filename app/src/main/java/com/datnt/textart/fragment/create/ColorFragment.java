package com.datnt.textart.fragment.create;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.datnt.textart.R;
import com.datnt.textart.activity.edit.EditActivity;
import com.datnt.textart.adapter.ColorAdapter;
import com.datnt.textart.callback.ICheckTouch;
import com.datnt.textart.model.ColorModel;
import com.datnt.textart.sharepref.DataLocalManager;
import com.datnt.textart.utils.Utils;
import com.flask.colorpicker.ColorPickerView;

import java.util.ArrayList;

public class ColorFragment extends Fragment {

    private RecyclerView rcvColor;
    private GradientDrawable gradientDrawable;
    private int direc;
    private ICheckTouch clickTouch;

    private boolean isBackground;

    public static ColorFragment newInstance(boolean isBG) {
        ColorFragment fragment = new ColorFragment();
        Bundle args = new Bundle();
        args.putBoolean("isBG", isBG);
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
        rcvColor = view.findViewById(R.id.rcvColor);

        if (getArguments() != null) isBackground = getArguments().getBoolean("isBG");

        setUpLayout();
        evenClick();
    }

    private void evenClick() {

    }

    private void setUpLayout() {
        ArrayList<ColorModel> lstColor = new ArrayList<>(setListColor());

        ColorAdapter colorAdapter = new ColorAdapter(requireContext(), R.layout.item_color, (o, pos) -> {
            if (pos == 0) pickColor();
            else {
                ColorModel color = (ColorModel) o;
                if (color.getColorStart() == color.getColorEnd()) {
                    DataLocalManager.setColor(color, "color");
                    DataLocalManager.setOption("", "bitmap");
                    if (!isBackground)
                        Utils.setIntent(requireActivity(), EditActivity.class.getName());
                    else clickTouch.checkTouch(true);
                } else {
                    pickDirection(color);
                }
            }
        });
        colorAdapter.setData(lstColor);

        GridLayoutManager manager = new GridLayoutManager(requireContext(), 4);
        rcvColor.setLayoutManager(manager);
        rcvColor.setAdapter(colorAdapter);
    }

    private ArrayList<ColorModel> setListColor() {
        ArrayList<ColorModel> lstColor = new ArrayList<>();

        int[] arrColor = getResources().getIntArray(R.array.lstColor);

        int direction = 0;
        for (int i = arrColor.length - 1; i >= 0; i--) {
            if (i < 70)
                lstColor.add(new ColorModel(arrColor[i], arrColor[i], 0, false));
            else {
                lstColor.add(new ColorModel(arrColor[i], arrColor[i - 1], direction, false));
                i--;
                if (direction < 5) direction++;
                else direction = 0;
            }
        }

        lstColor.add(0, new ColorModel(0, 0, 0, false));

        return lstColor;
    }

    private void pickDirection(ColorModel color) {
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_pick_direction, null);
        ImageView iv_TB = view.findViewById(R.id.iv_TB);
        ImageView ivTL_BR = view.findViewById(R.id.ivTL_BR);
        ImageView ivLR = view.findViewById(R.id.ivLR);
        ImageView ivBL_TR = view.findViewById(R.id.ivBL_TR);
        ImageView ivBT = view.findViewById(R.id.ivBT);
        ImageView ivRL = view.findViewById(R.id.ivRL);

        ImageView ivShow = view.findViewById(R.id.ivShow);

        TextView tvNo = view.findViewById(R.id.tvNo);
        TextView tvYes = view.findViewById(R.id.tvYes);

        AlertDialog dialog = new AlertDialog.Builder(requireContext(), R.style.SheetDialog).create();
        dialog.setView(view);
        dialog.setCancelable(false);
        dialog.show();

        tvNo.setOnClickListener(v -> dialog.cancel());
        tvYes.setOnClickListener(v -> {
            color.setDirec(direc);
            DataLocalManager.setColor(color, "color");
            DataLocalManager.setOption("", "bitmap");
            if (!isBackground) Utils.setIntent(requireActivity(), EditActivity.class.getName());
            else clickTouch.checkTouch(true);
            dialog.cancel();
        });

        direc = color.getDirec();
        clickDirec(color.getDirec(), color, ivShow);
        checkPickDirec(color.getDirec(), iv_TB, ivTL_BR, ivLR, ivBL_TR, ivBT, ivRL);

        iv_TB.setOnClickListener(v -> {
            direc = 0;
            clickDirec(0, color, ivShow);
            checkPickDirec(0, iv_TB, ivTL_BR, ivLR, ivBL_TR, ivBT, ivRL);
        });
        ivTL_BR.setOnClickListener(v -> {
            direc = 1;
            clickDirec(1, color, ivShow);
            checkPickDirec(1, iv_TB, ivTL_BR, ivLR, ivBL_TR, ivBT, ivRL);
        });
        ivLR.setOnClickListener(v -> {
            direc = 2;
            clickDirec(2, color, ivShow);
            checkPickDirec(2, iv_TB, ivTL_BR, ivLR, ivBL_TR, ivBT, ivRL);
        });
        ivBL_TR.setOnClickListener(v -> {
            direc = 3;
            clickDirec(3, color, ivShow);
            checkPickDirec(3, iv_TB, ivTL_BR, ivLR, ivBL_TR, ivBT, ivRL);
        });
        ivBT.setOnClickListener(v -> {
            direc = 4;
            clickDirec(4, color, ivShow);
            checkPickDirec(4, iv_TB, ivTL_BR, ivLR, ivBL_TR, ivBT, ivRL);
        });
        ivRL.setOnClickListener(v -> {
            direc = 5;
            clickDirec(5, color, ivShow);
            checkPickDirec(5, iv_TB, ivTL_BR, ivLR, ivBL_TR, ivBT, ivRL);
        });
    }

    private void clickDirec(int pos, ColorModel color, ImageView ivShow) {
        switch (pos) {
            case 0:
                createGradient(GradientDrawable.Orientation.TOP_BOTTOM, color.getColorStart(), color.getColorEnd());
                ivShow.setBackground(gradientDrawable);
                break;
            case 1:
                createGradient(GradientDrawable.Orientation.TL_BR, color.getColorStart(), color.getColorEnd());
                ivShow.setBackground(gradientDrawable);
                break;
            case 2:
                createGradient(GradientDrawable.Orientation.LEFT_RIGHT, color.getColorStart(), color.getColorEnd());
                ivShow.setBackground(gradientDrawable);
                break;
            case 3:
                createGradient(GradientDrawable.Orientation.BL_TR, color.getColorStart(), color.getColorEnd());
                ivShow.setBackground(gradientDrawable);
                break;
            case 4:
                createGradient(GradientDrawable.Orientation.BOTTOM_TOP, color.getColorStart(), color.getColorEnd());
                ivShow.setBackground(gradientDrawable);
                break;
            case 5:
                createGradient(GradientDrawable.Orientation.RIGHT_LEFT, color.getColorStart(), color.getColorEnd());
                ivShow.setBackground(gradientDrawable);
                break;
        }
    }

    private void createGradient(GradientDrawable.Orientation direc, int start, int end) {
        gradientDrawable = new GradientDrawable(direc, new int[]{start, end});
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradientDrawable.setCornerRadius(34f);
    }

    private void checkPickDirec(int pos, ImageView iv_tb, ImageView ivTL_br, ImageView ivLR, ImageView ivBL_tr, ImageView ivBT, ImageView ivRL) {
        switch (pos) {
            case 0:
                iv_tb.setImageResource(R.drawable.ic_top_bottom_check);
                ivTL_br.setImageResource(R.drawable.ic_tl_br_uncheck);
                ivLR.setImageResource(R.drawable.ic_left_right_uncheck);
                ivBL_tr.setImageResource(R.drawable.ic_bl_tr_uncheck);
                ivBT.setImageResource(R.drawable.ic_bottom_top_uncheck);
                ivRL.setImageResource(R.drawable.ic_right_left_uncheck);
                break;
            case 1:
                iv_tb.setImageResource(R.drawable.ic_top_bottom_uncheck);
                ivTL_br.setImageResource(R.drawable.ic_tl_br_check);
                ivLR.setImageResource(R.drawable.ic_left_right_uncheck);
                ivBL_tr.setImageResource(R.drawable.ic_bl_tr_uncheck);
                ivBT.setImageResource(R.drawable.ic_bottom_top_uncheck);
                ivRL.setImageResource(R.drawable.ic_right_left_uncheck);
                break;
            case 2:
                iv_tb.setImageResource(R.drawable.ic_top_bottom_uncheck);
                ivTL_br.setImageResource(R.drawable.ic_tl_br_uncheck);
                ivLR.setImageResource(R.drawable.ic_left_right_check);
                ivBL_tr.setImageResource(R.drawable.ic_bl_tr_uncheck);
                ivBT.setImageResource(R.drawable.ic_bottom_top_uncheck);
                ivRL.setImageResource(R.drawable.ic_right_left_uncheck);
                break;
            case 3:
                iv_tb.setImageResource(R.drawable.ic_top_bottom_uncheck);
                ivTL_br.setImageResource(R.drawable.ic_tl_br_uncheck);
                ivLR.setImageResource(R.drawable.ic_left_right_uncheck);
                ivBL_tr.setImageResource(R.drawable.ic_bl_tr_check);
                ivBT.setImageResource(R.drawable.ic_bottom_top_uncheck);
                ivRL.setImageResource(R.drawable.ic_right_left_uncheck);
                break;
            case 4:
                iv_tb.setImageResource(R.drawable.ic_top_bottom_uncheck);
                ivTL_br.setImageResource(R.drawable.ic_tl_br_uncheck);
                ivLR.setImageResource(R.drawable.ic_left_right_uncheck);
                ivBL_tr.setImageResource(R.drawable.ic_bl_tr_uncheck);
                ivBT.setImageResource(R.drawable.ic_bottom_top_check);
                ivRL.setImageResource(R.drawable.ic_right_left_uncheck);
                break;
            case 5:
                iv_tb.setImageResource(R.drawable.ic_top_bottom_uncheck);
                ivTL_br.setImageResource(R.drawable.ic_tl_br_uncheck);
                ivLR.setImageResource(R.drawable.ic_left_right_uncheck);
                ivBL_tr.setImageResource(R.drawable.ic_bl_tr_uncheck);
                ivBT.setImageResource(R.drawable.ic_bottom_top_uncheck);
                ivRL.setImageResource(R.drawable.ic_right_left_check);
                break;
        }
    }

    private void pickColor() {
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_pick_color, null);
        ColorPickerView colorPicker = view.findViewById(R.id.pickColor);
        TextView tvNo = view.findViewById(R.id.tvNo);
        TextView tvYes = view.findViewById(R.id.tvYes);

        AlertDialog dialog = new AlertDialog.Builder(requireContext(), R.style.SheetDialog).create();
        dialog.setView(view);
        dialog.setCancelable(false);
        dialog.show();

        tvNo.setOnClickListener(v -> dialog.cancel());
        tvYes.setOnClickListener(view1 -> {
            ColorModel color = new ColorModel(colorPicker.getSelectedColor(), colorPicker.getSelectedColor(), 0, false);
            DataLocalManager.setColor(color, "color");
            DataLocalManager.setOption("", "bitmap");
            if (!isBackground)
                Utils.setIntent(requireActivity(), EditActivity.class.getName());
            else clickTouch.checkTouch(true);
            dialog.cancel();
        });
    }

    public void finish(ICheckTouch clickTouch) {
        this.clickTouch = clickTouch;
    }
}