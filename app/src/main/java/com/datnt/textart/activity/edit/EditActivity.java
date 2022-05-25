package com.datnt.textart.activity.edit;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.datnt.textart.R;
import com.datnt.textart.adapter.ColorAdapter;
import com.datnt.textart.customview.CustomSeekbar;
import com.datnt.textart.customview.CustomView;
import com.datnt.textart.customview.OnSeekbarResult;
import com.datnt.textart.customview.stickerview.BitmapStickerIcon;
import com.datnt.textart.customview.stickerview.DrawableSticker;
import com.datnt.textart.customview.stickerview.FlipHorizontallyEvent;
import com.datnt.textart.customview.stickerview.RotateIconEvent;
import com.datnt.textart.customview.stickerview.Sticker;
import com.datnt.textart.customview.stickerview.StickerView;
import com.datnt.textart.customview.stickerview.TextSticker;
import com.datnt.textart.customview.stickerview.ZoomIconEvent;
import com.datnt.textart.model.ColorModel;
import com.datnt.textart.model.StickerModel;
import com.datnt.textart.model.StyleFontModel;
import com.datnt.textart.model.TextModel;
import com.datnt.textart.utils.Utils;
import com.flask.colorpicker.ColorPickerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class EditActivity extends AppCompatActivity {

    private ImageView ivOriginal, iv1_1, iv9_16, iv4_5, iv16_9, ivBack, ivTick, ivUndo, ivRedo,
            ivLayer, ivImport, ivLook, ivLock, ivColor;
    private TextView tvOriginal, tv1_1, tv9_16, tv4_5, tv16_9, tvTitle, tvFontSize, tvCancel,
            tvTitleEditText, tvShearX, tvShearY, tvStretch;
    private SeekBar sbFontSize;
    private CustomSeekbar sbStretch, sbShearX, sbShearY;
    private RelativeLayout rlAddText, rlSticker, rlImage, rlBackground, rlBlend, rlDecor, rlCrop,
            rlDelLayer, rlDuplicateLayer, rlLook, rlLock, rlLayer, rlExpandLayer, rlExpandEditText,
            rlET, rlDuplicateText, rlFontSize, rlColor, rlTransform, rlShadow, rlOpacity, rlDelText,
            rlEditText, rlEditFontSize, rlEditColor;
    private LinearLayout llLayoutImport, llReUndo, llEditTransform;
    private HorizontalScrollView vSize, vOperation, vEditText;
    private CustomView vMain;
    private StickerView stickerView;
    private RecyclerView rcvEditColor;

    private TextModel textModel;
    private Bitmap bitmap;
    private ArrayList<StickerModel> lstSticker;
    private TextSticker textSticker;
    private DrawableSticker drawableSticker;
    private GradientDrawable gradientDrawable;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        init();
    }

    private void init() {
        setUpLayout();
        getData();
        evenClick();
        setUpStickerView();
    }

    private void setUpStickerView() {
        BitmapStickerIcon rotate = new BitmapStickerIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_sticker_rotate, null), BitmapStickerIcon.LEFT_TOP);
        rotate.setIconEvent(new RotateIconEvent());

        BitmapStickerIcon flip = new BitmapStickerIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_sticker_flip, null), BitmapStickerIcon.RIGHT_BOTTOM);
        flip.setIconEvent(new FlipHorizontallyEvent());

        BitmapStickerIcon zoom = new BitmapStickerIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_sticker_resize, null), BitmapStickerIcon.RIGHT_TOP);
        zoom.setIconEvent(new ZoomIconEvent());

        stickerView.setIcons(Arrays.asList(rotate, flip, zoom));
        stickerView.setBackgroundColor(Color.WHITE);
        stickerView.setLocked(false);
        stickerView.setConstrained(true);

        stickerView.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded(@NonNull Sticker sticker) {
                Log.d("2tdp", "onStickerAdded");
            }

            @Override
            public void onStickerClicked(@NonNull Sticker sticker) {
                //stickerView.removeAllSticker();
//                if (sticker instanceof TextSticker) {
//                    ((TextSticker) sticker).setTextColor(Color.RED);
//                    stickerView.replace(sticker);
//                    stickerView.invalidate();
//                }
                if (sticker instanceof TextSticker) textSticker = (TextSticker) sticker;
                for (StickerModel t : lstSticker) {
                    if (t.getTextSticker() == textSticker && t.getTextModel().getColor() != null) {
                        ColorModel color = t.getTextModel().getColor();
                        GradientDrawable gradient;
                        if (color.getColorStart() != color.getColorEnd())
                            gradient = new GradientDrawable(Utils.setDirection(color.getDirec()), new int[]{color.getColorStart(), color.getColorEnd()});
                        else
                            gradient = new GradientDrawable(Utils.setDirection(0), new int[]{color.getColorStart(), color.getColorEnd()});
                        Log.d("2tdp", "onStickerClicked: " + color.getColorStart());
                        gradient.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                        gradient.setCornerRadius(10f);
                        ivColor.setBackground(gradient);

                    }
                }
                seekAndHideLayout(3);
                Log.d("2tdp", "onStickerClicked");
            }

            @Override
            public void onStickerDeleted(@NonNull Sticker sticker) {
                Log.d("2tdp", "onStickerDeleted");
            }

            @Override
            public void onStickerDragFinished(@NonNull Sticker sticker) {
                seekAndHideLayout(3);
                Log.d("2tdp", "onStickerDragFinished");
            }

            @Override
            public void onStickerTouchedDown(@NonNull Sticker sticker) {
                Log.d("2tdp", "onStickerTouchedDown");
            }

            @Override
            public void onStickerZoomFinished(@NonNull Sticker sticker) {
                Log.d("2tdp", "onStickerZoomFinished");
            }

            @Override
            public void onStickerFlipped(@NonNull Sticker sticker) {
                Log.d("2tdp", "onStickerFlipped");
            }

            @Override
            public void onStickerDoubleTapped(@NonNull Sticker sticker) {
                Log.d("2tdp", "onDoubleTapped: double tap will be with two click");
            }
        });
    }

    private void evenClick() {
        ivBack.setOnClickListener(v -> onBackPressed());
        ivTick.setOnClickListener(v -> seekAndHideLayout(0));

        ivOriginal.setOnClickListener(v -> checkSize(0));
        iv1_1.setOnClickListener(v -> checkSize(1));
        iv9_16.setOnClickListener(v -> checkSize(2));
        iv4_5.setOnClickListener(v -> checkSize(3));
        iv16_9.setOnClickListener(v -> checkSize(4));

        ivLayer.setOnClickListener(v -> seekAndHideLayout(2));
        rlLayer.setOnClickListener(v -> seekAndHideLayout(0));
        rlEditText.setOnClickListener(v -> seekAndHideLayout(0));

        rlDelText.setOnClickListener(v -> delStick());
        rlET.setOnClickListener(v -> editText());
        rlDuplicateText.setOnClickListener(v -> duplicateText());
        rlFontSize.setOnClickListener(v -> fontSize());
        rlColor.setOnClickListener(v -> color());
        rlTransform.setOnClickListener(v -> transform());

        rlAddText.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddTextActivity.class);
            launcher.launch(intent, ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left));
        });
    }

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (result.getData() != null) {
                textModel = (TextModel) result.getData().getSerializableExtra("text");
                if (textSticker == null) {
                    textSticker = new TextSticker(this);
                    stickerView.addSticker(textSticker);
                } else {
                    stickerView.replace(textSticker, true);
                }
                setTextSticker(textModel, textSticker);
            }
        }
    });

    private void transform() {
        setUpLayoutEditTransform(0);
        tvCancel.setOnClickListener(v -> setUpLayoutEditTransform(1));

        sbShearX.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                tvShearX.setText(String.valueOf(value));
            }

            @Override
            public void onUp(View v) {

            }
        });
        sbShearY.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                tvShearY.setText(String.valueOf(value));
            }

            @Override
            public void onUp(View v) {

            }
        });
        sbStretch.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                tvStretch.setText(String.valueOf(value));
            }

            @Override
            public void onUp(View v) {

            }
        });
    }

    private void setUpLayoutEditTransform(int pos) {
        switch (pos) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                vEditText.startAnimation(animation);
                if (vEditText.getVisibility() == View.VISIBLE) vEditText.setVisibility(View.GONE);

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                llEditTransform.startAnimation(animation);
                if (llEditTransform.getVisibility() == View.GONE) {
                    llEditTransform.setVisibility(View.VISIBLE);
                    tvCancel.setVisibility(View.VISIBLE);
                    tvTitleEditText.setText(getString(R.string.transform));
                    tvShearX.setText(String.valueOf(0));
                    tvShearY.setText(String.valueOf(0));
                    tvStretch.setText(String.valueOf(0));
                    sbShearX.setMax(100);
                    sbShearY.setMax(100);
                    sbStretch.setMax(200);
                }
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                llEditTransform.startAnimation(animation);
                if (llEditTransform.getVisibility() == View.VISIBLE) {
                    llEditTransform.setVisibility(View.GONE);
                    tvCancel.setVisibility(View.GONE);
                    tvTitleEditText.setText(getString(R.string.text));
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                vEditText.startAnimation(animation);
                if (vEditText.getVisibility() == View.GONE) vEditText.setVisibility(View.VISIBLE);
                break;
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                vEditText.clearAnimation();
                rlEditFontSize.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //color
    private void color() {
        setUpLayoutEditColor(0);
        tvCancel.setOnClickListener(v -> setUpLayoutEditColor(1));

        ArrayList<ColorModel> lstColor = new ArrayList<>(setListColor());

        ColorAdapter colorAdapter = new ColorAdapter(this, R.layout.item_color_edit, (o, pos) -> {
            ColorModel color = (ColorModel) o;
            if (pos == 0) pickColor();
            else {
                if (color.getColorStart() == color.getColorEnd()) {
                    addColorTextModel(textSticker, color);
                    textSticker.setTextColor(color);
                    stickerView.replace(textSticker, true);
                    GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                            new int[]{color.getColorStart(), color.getColorStart()});
                    gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                    gradientDrawable.setCornerRadius(10f);
                    ivColor.setBackground(gradientDrawable);
                } else {
                    pickDirection(color);
                }
            }
        });
        colorAdapter.setData(lstColor);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvEditColor.setLayoutManager(manager);
        rcvEditColor.setAdapter(colorAdapter);
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

    private void setUpLayoutEditColor(int pos) {
        switch (pos) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                vEditText.startAnimation(animation);
                if (vEditText.getVisibility() == View.VISIBLE) vEditText.setVisibility(View.GONE);

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                rlEditColor.startAnimation(animation);
                if (rlEditColor.getVisibility() == View.GONE) {
                    rlEditColor.setVisibility(View.VISIBLE);
                    tvCancel.setVisibility(View.VISIBLE);
                    tvTitleEditText.setText(getString(R.string.color));
                }
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                rlEditColor.startAnimation(animation);
                if (rlEditColor.getVisibility() == View.VISIBLE) {
                    rlEditColor.setVisibility(View.GONE);
                    tvCancel.setVisibility(View.GONE);
                    tvTitleEditText.setText(getString(R.string.text));
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                vEditText.startAnimation(animation);
                if (vEditText.getVisibility() == View.GONE) vEditText.setVisibility(View.VISIBLE);
                break;
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                vEditText.clearAnimation();
                rlEditFontSize.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void pickDirection(ColorModel color) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_pick_direction, null);
        ImageView iv_TB = view.findViewById(R.id.iv_TB);
        ImageView ivTL_BR = view.findViewById(R.id.ivTL_BR);
        ImageView ivLR = view.findViewById(R.id.ivLR);
        ImageView ivBL_TR = view.findViewById(R.id.ivBL_TR);
        ImageView ivBT = view.findViewById(R.id.ivBT);
        ImageView ivRL = view.findViewById(R.id.ivRL);

        ImageView ivShow = view.findViewById(R.id.ivShow);

        TextView tvNo = view.findViewById(R.id.tvNo);
        TextView tvYes = view.findViewById(R.id.tvYes);

        AlertDialog dialog = new AlertDialog.Builder(this, R.style.SheetDialog).create();
        dialog.setView(view);
        dialog.setCancelable(false);
        dialog.show();

        tvNo.setOnClickListener(v -> dialog.cancel());
        tvYes.setOnClickListener(v -> {
            addColorTextModel(textSticker, color);
            textSticker.setTextColor(color);
            stickerView.replace(textSticker, true);

            GradientDrawable gradientDrawable = new GradientDrawable(Utils.setDirection(color.getDirec()),
                    new int[]{color.getColorStart(), color.getColorEnd()});
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
            gradientDrawable.setCornerRadius(10f);
            ivColor.setBackground(gradientDrawable);
            dialog.cancel();
        });

        clickDirec(color.getDirec(), color, ivShow);
        checkPickDirec(color.getDirec(), iv_TB, ivTL_BR, ivLR, ivBL_TR, ivBT, ivRL);

        iv_TB.setOnClickListener(v -> {
            clickDirec(0, color, ivShow);
            checkPickDirec(0, iv_TB, ivTL_BR, ivLR, ivBL_TR, ivBT, ivRL);
        });
        ivTL_BR.setOnClickListener(v -> {
            clickDirec(1, color, ivShow);
            checkPickDirec(1, iv_TB, ivTL_BR, ivLR, ivBL_TR, ivBT, ivRL);
        });
        ivLR.setOnClickListener(v -> {
            clickDirec(2, color, ivShow);
            checkPickDirec(2, iv_TB, ivTL_BR, ivLR, ivBL_TR, ivBT, ivRL);
        });
        ivBL_TR.setOnClickListener(v -> {
            clickDirec(3, color, ivShow);
            checkPickDirec(3, iv_TB, ivTL_BR, ivLR, ivBL_TR, ivBT, ivRL);
        });
        ivBT.setOnClickListener(v -> {
            clickDirec(4, color, ivShow);
            checkPickDirec(4, iv_TB, ivTL_BR, ivLR, ivBL_TR, ivBT, ivRL);
        });
        ivRL.setOnClickListener(v -> {
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
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_pick_color, null);
        ColorPickerView colorPicker = view.findViewById(R.id.pickColor);
        TextView tvNo = view.findViewById(R.id.tvNo);
        TextView tvYes = view.findViewById(R.id.tvYes);

        AlertDialog dialog = new AlertDialog.Builder(this, R.style.SheetDialog).create();
        dialog.setView(view);
        dialog.setCancelable(false);
        dialog.show();

        tvNo.setOnClickListener(v -> dialog.cancel());
        tvYes.setOnClickListener(view1 -> {
            ColorModel color = new ColorModel(colorPicker.getSelectedColor(), colorPicker.getSelectedColor(), 0, false);
            addColorTextModel(textSticker, color);
            textSticker.setTextColor(color);
            stickerView.replace(textSticker, true);
            GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{colorPicker.getSelectedColor(), colorPicker.getSelectedColor()});
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
            gradientDrawable.setCornerRadius(10f);
            ivColor.setBackground(gradientDrawable);
            dialog.cancel();
        });
    }

    private void addColorTextModel(TextSticker textSticker, ColorModel colorModel) {
        for (StickerModel t : lstSticker) {
            if (t.getTextSticker() == textSticker) t.getTextModel().setColor(colorModel);
            break;
        }
    }

    //font size
    private void fontSize() {
        setUpLayoutEditFontSize(0);
        tvCancel.setOnClickListener(v -> setUpLayoutEditFontSize(1));

        sbFontSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvFontSize.setText(String.valueOf(i));
                textSticker.setTextSize((float) i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setUpLayoutEditFontSize(int pos) {
        switch (pos) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                vEditText.startAnimation(animation);
                if (vEditText.getVisibility() == View.VISIBLE) vEditText.setVisibility(View.GONE);

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                rlEditFontSize.startAnimation(animation);
                if (rlEditFontSize.getVisibility() == View.GONE) {
                    rlEditFontSize.setVisibility(View.VISIBLE);
                    tvCancel.setVisibility(View.VISIBLE);
                    tvTitleEditText.setText(getString(R.string.font_size));
                    tvFontSize.setText(String.valueOf((int) textSticker.getTextSize()));
                    sbFontSize.setProgress((int) textSticker.getTextSize());
                }
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                rlEditFontSize.startAnimation(animation);
                if (rlEditFontSize.getVisibility() == View.VISIBLE) {
                    rlEditFontSize.setVisibility(View.GONE);
                    tvCancel.setVisibility(View.GONE);
                    tvTitleEditText.setText(getString(R.string.text));
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                vEditText.startAnimation(animation);
                if (vEditText.getVisibility() == View.GONE) vEditText.setVisibility(View.VISIBLE);
                break;
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                vEditText.clearAnimation();
                rlEditFontSize.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //duplicate
    private void duplicateText() {
        if (textSticker == null) {
            Utils.showToast(this, getResources().getString(R.string.choose_sticker_text));
            return;
        }
        TextSticker text = new TextSticker(this);
        for (StickerModel t : lstSticker) {
            if (t.getTextSticker() == textSticker) {
                setTextSticker(t.getTextModel(), text);
                stickerView.addSticker(text);
                textSticker = text;
                break;
            }
        }
    }

    private void setTextSticker(TextModel textModel, TextSticker textSticker) {
        textSticker.setText(textModel.getContent());
        textSticker.resizeText();
        for (StyleFontModel f : textModel.getFontModel().getLstStyle()) {
            if (f.isSelected()) {
                textSticker.setTypeface(Utils.getTypeFace(textModel.getFontModel().getNameFont(), f.getName(), this));
                Log.d("2tdp", "font: " + textModel.getFontModel().getNameFont() + "......" + f.getName());
                break;
            }
        }
        if (textModel.getColor() != null) textSticker.setTextColor(textModel.getColor());
            switch (textModel.getTypeAlign()) {
                case Gravity.START:
                    textSticker.setTextAlign(Layout.Alignment.ALIGN_NORMAL);
                    break;
                case Gravity.CENTER:
                    textSticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
                    break;
                case Gravity.END:
                    textSticker.setTextAlign(Layout.Alignment.ALIGN_OPPOSITE);
                    break;
            }
        lstSticker.add(new StickerModel(textModel, textSticker, null));
        seekAndHideLayout(3);
        Log.d("2tdp", "lstSticker: " + lstSticker.size());
    }

    //edit text
    private void editText() {
        if (textSticker == null) {
            Utils.showToast(this, getResources().getString(R.string.choose_sticker_text));
            return;
        }
        Intent intent = new Intent(this, AddTextActivity.class);
        intent.putExtra("text", textModel);
        launcher.launch(intent, ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left));
    }

    //del
    private void delStick() {
        if (textSticker == null && drawableSticker == null) {
            Utils.showToast(this, getResources().getString(R.string.choose_sticker_text));
            return;
        }
        if (textSticker != null) {
            StickerModel stickerModel = null;
            for (StickerModel st : lstSticker) {
                if (st.getTextSticker() == textSticker) stickerModel = st;
            }
            lstSticker.remove(stickerModel);
            stickerView.remove(textSticker);
            textSticker = null;
            seekAndHideLayout(0);
            Log.d("2tdp", "lstSticker: " + lstSticker.size());
            return;
        }
        if (drawableSticker != null) {
            StickerModel stickerModel = null;
            for (StickerModel st : lstSticker) {
                if (st.getDrawableSticker() == drawableSticker) stickerModel = st;
            }
            lstSticker.remove(stickerModel);
            stickerView.remove(drawableSticker);
            Log.d("2tdp", "lstSticker: " + lstSticker.size());
            return;
        }
    }

    private void getData() {
        String strUri = getIntent().getStringExtra("bitmap");
        vMain.setSize(0);
        if (strUri != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(strUri));
                if (bitmap != null) vMain.setData(bitmap, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ColorModel color = (ColorModel) getIntent().getSerializableExtra("color");
            if (color != null) color.setDirec(getIntent().getIntExtra("direc", 0));
            vMain.setData(null, color);
        }
    }

    private void seekAndHideLayout(int pos) {
        switch (pos) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                if (vSize.getVisibility() == View.VISIBLE) {
                    vSize.startAnimation(animation);
                    tvTitle.setVisibility(View.GONE);
                    vSize.setVisibility(View.GONE);
                    ivTick.setVisibility(View.GONE);
                }

                if (rlExpandEditText.getVisibility() == View.VISIBLE) {
                    rlExpandEditText.startAnimation(animation);
                    rlExpandEditText.setVisibility(View.GONE);
                }

                if (rlExpandLayer.getVisibility() == View.VISIBLE) {
                    rlExpandLayer.startAnimation(animation);
                    rlExpandLayer.setVisibility(View.GONE);
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (vOperation.getVisibility() == View.GONE) {
                    vOperation.startAnimation(animation);
                    llLayoutImport.setVisibility(View.VISIBLE);
                    llReUndo.setVisibility(View.VISIBLE);
                    vOperation.setVisibility(View.VISIBLE);
                }
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                if (vOperation.getVisibility() == View.VISIBLE) {
                    vOperation.startAnimation(animation);
                    llLayoutImport.setVisibility(View.GONE);
                    llReUndo.setVisibility(View.GONE);
                    vOperation.setVisibility(View.GONE);
                }

                if (rlExpandEditText.getVisibility() == View.VISIBLE) {
                    rlExpandEditText.startAnimation(animation);
                    rlExpandEditText.setVisibility(View.GONE);
                }

                if (rlExpandLayer.getVisibility() == View.VISIBLE) {
                    rlExpandLayer.startAnimation(animation);
                    rlExpandLayer.setVisibility(View.GONE);
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (vSize.getVisibility() == View.GONE) {
                    vSize.startAnimation(animation);
                    tvTitle.setVisibility(View.VISIBLE);
                    vSize.setVisibility(View.VISIBLE);
                    ivTick.setVisibility(View.VISIBLE);
                }
                break;
            case 2:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                if (vSize.getVisibility() == View.VISIBLE) {
                    vSize.startAnimation(animation);
                    tvTitle.setVisibility(View.GONE);
                    vSize.setVisibility(View.GONE);
                    ivTick.setVisibility(View.GONE);
                }

                if (vOperation.getVisibility() == View.VISIBLE) {
                    vOperation.startAnimation(animation);
                    vOperation.setVisibility(View.GONE);
                }

                if (rlExpandEditText.getVisibility() == View.VISIBLE) {
                    rlExpandEditText.startAnimation(animation);
                    rlExpandEditText.setVisibility(View.GONE);
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlExpandLayer.getVisibility() == View.GONE) {
                    rlExpandLayer.startAnimation(animation);
                    rlExpandLayer.setVisibility(View.VISIBLE);
                }
                break;
            case 3:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                if (vSize.getVisibility() == View.VISIBLE) {
                    vSize.startAnimation(animation);
                    tvTitle.setVisibility(View.GONE);
                    vSize.setVisibility(View.GONE);
                    ivTick.setVisibility(View.GONE);
                }

                if (vOperation.getVisibility() == View.VISIBLE) {
                    vOperation.startAnimation(animation);
                    vOperation.setVisibility(View.GONE);
                }

                if (rlExpandLayer.getVisibility() == View.VISIBLE) {
                    rlExpandLayer.startAnimation(animation);
                    rlExpandLayer.setVisibility(View.GONE);
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlExpandEditText.getVisibility() == View.GONE) {
                    rlExpandEditText.startAnimation(animation);
                    rlExpandEditText.setVisibility(View.VISIBLE);
                }
                break;
        }
        if (animation != null)
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    vSize.clearAnimation();
                    vOperation.clearAnimation();
                    rlExpandEditText.clearAnimation();
                    rlExpandLayer.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
    }

    private void checkSize(int pos) {
        switch (pos) {
            case 0:
                ivOriginal.setBackgroundResource(R.drawable.boder_size_check);
                tvOriginal.setTextColor(getResources().getColor(R.color.black));
                iv1_1.setBackgroundResource(R.drawable.boder_size_uncheck);
                tv1_1.setTextColor(getResources().getColor(R.color.gray));
                iv9_16.setBackgroundResource(R.drawable.boder_size_uncheck);
                tv9_16.setTextColor(getResources().getColor(R.color.gray));
                iv4_5.setBackgroundResource(R.drawable.boder_size_uncheck);
                tv4_5.setTextColor(getResources().getColor(R.color.gray));
                iv16_9.setBackgroundResource(R.drawable.boder_size_uncheck);
                tv16_9.setTextColor(getResources().getColor(R.color.gray));

                vMain.setSize(0);
                if (bitmap != null) vMain.setData(bitmap, null);
                break;
            case 1:
                ivOriginal.setBackgroundResource(R.drawable.boder_size_uncheck);
                tvOriginal.setTextColor(getResources().getColor(R.color.gray));
                iv1_1.setBackgroundResource(R.drawable.boder_size_check);
                tv1_1.setTextColor(getResources().getColor(R.color.black));
                iv9_16.setBackgroundResource(R.drawable.boder_size_uncheck);
                tv9_16.setTextColor(getResources().getColor(R.color.gray));
                iv4_5.setBackgroundResource(R.drawable.boder_size_uncheck);
                tv4_5.setTextColor(getResources().getColor(R.color.gray));
                iv16_9.setBackgroundResource(R.drawable.boder_size_uncheck);
                tv16_9.setTextColor(getResources().getColor(R.color.gray));

                vMain.setSize(1);
                if (bitmap != null) vMain.setData(bitmap, null);
                break;
            case 2:
                ivOriginal.setBackgroundResource(R.drawable.boder_size_uncheck);
                tvOriginal.setTextColor(getResources().getColor(R.color.gray));
                iv1_1.setBackgroundResource(R.drawable.boder_size_uncheck);
                tv1_1.setTextColor(getResources().getColor(R.color.gray));
                iv9_16.setBackgroundResource(R.drawable.boder_size_check);
                tv9_16.setTextColor(getResources().getColor(R.color.black));
                iv4_5.setBackgroundResource(R.drawable.boder_size_uncheck);
                tv4_5.setTextColor(getResources().getColor(R.color.gray));
                iv16_9.setBackgroundResource(R.drawable.boder_size_uncheck);
                tv16_9.setTextColor(getResources().getColor(R.color.gray));

                vMain.setSize(2);
                if (bitmap != null) vMain.setData(bitmap, null);
                break;
            case 3:
                ivOriginal.setBackgroundResource(R.drawable.boder_size_uncheck);
                tvOriginal.setTextColor(getResources().getColor(R.color.gray));
                iv1_1.setBackgroundResource(R.drawable.boder_size_uncheck);
                tv1_1.setTextColor(getResources().getColor(R.color.gray));
                iv9_16.setBackgroundResource(R.drawable.boder_size_uncheck);
                tv9_16.setTextColor(getResources().getColor(R.color.gray));
                iv4_5.setBackgroundResource(R.drawable.boder_size_check);
                tv4_5.setTextColor(getResources().getColor(R.color.black));
                iv16_9.setBackgroundResource(R.drawable.boder_size_uncheck);
                tv16_9.setTextColor(getResources().getColor(R.color.gray));

                vMain.setSize(3);
                if (bitmap != null) vMain.setData(bitmap, null);
                break;
            case 4:
                ivOriginal.setBackgroundResource(R.drawable.boder_size_uncheck);
                tvOriginal.setTextColor(getResources().getColor(R.color.gray));
                iv1_1.setBackgroundResource(R.drawable.boder_size_uncheck);
                tv1_1.setTextColor(getResources().getColor(R.color.gray));
                iv9_16.setBackgroundResource(R.drawable.boder_size_uncheck);
                tv9_16.setTextColor(getResources().getColor(R.color.gray));
                iv4_5.setBackgroundResource(R.drawable.boder_size_uncheck);
                tv4_5.setTextColor(getResources().getColor(R.color.gray));
                iv16_9.setBackgroundResource(R.drawable.boder_size_check);
                tv16_9.setTextColor(getResources().getColor(R.color.black));

                vMain.setSize(4);
                if (bitmap != null) vMain.setData(bitmap, null);
                break;
        }
    }

    private void setUpLayout() {
        ivBack = findViewById(R.id.ivBack);
        ivTick = findViewById(R.id.ivTick);
        stickerView = findViewById(R.id.stickerView);
        vMain = findViewById(R.id.vMain);
        ivOriginal = findViewById(R.id.ivOriginal);
        iv1_1 = findViewById(R.id.iv1_1);
        iv9_16 = findViewById(R.id.iv9_16);
        iv4_5 = findViewById(R.id.iv4_5);
        iv16_9 = findViewById(R.id.iv16_9);
        tvOriginal = findViewById(R.id.tvOriginal);
        tv1_1 = findViewById(R.id.tv1_1);
        tv9_16 = findViewById(R.id.tv9_16);
        tv4_5 = findViewById(R.id.tv4_5);
        tv16_9 = findViewById(R.id.tv16_9);
        ivUndo = findViewById(R.id.ivUndo);
        ivRedo = findViewById(R.id.ivRedo);
        ivLayer = findViewById(R.id.ivLayer);
        ivImport = findViewById(R.id.ivImport);
        rlAddText = findViewById(R.id.rlAddText);
        rlSticker = findViewById(R.id.rlStick);
        rlImage = findViewById(R.id.rlImage);
        rlBackground = findViewById(R.id.rlBackground);
        rlBlend = findViewById(R.id.rlBlend);
        rlDecor = findViewById(R.id.rlDecor);
        rlCrop = findViewById(R.id.rlCrop);
        llLayoutImport = findViewById(R.id.llLayerImport);
        vSize = findViewById(R.id.vSize);
        vOperation = findViewById(R.id.vOperation);
        llReUndo = findViewById(R.id.lLReUndo);
        tvTitle = findViewById(R.id.tvTitle);
        rlExpandLayer = findViewById(R.id.rlExpandLayer);
        rlDelLayer = findViewById(R.id.rlDelLayer);
        rlDuplicateLayer = findViewById(R.id.rlDuplicateLayer);
        rlLock = findViewById(R.id.rlLock);
        rlLook = findViewById(R.id.rlLook);
        ivLock = findViewById(R.id.ivLock);
        ivLook = findViewById(R.id.ivLook);
        rlLayer = findViewById(R.id.rlLayer);
        rlExpandEditText = findViewById(R.id.rlExpandEditText);
        rlDelText = findViewById(R.id.rlDelText);
        rlDuplicateText = findViewById(R.id.rlDuplicateText);
        rlFontSize = findViewById(R.id.rlFontSize);
        rlColor = findViewById(R.id.rlColor);
        rlTransform = findViewById(R.id.rlTransform);
        rlShadow = findViewById(R.id.rlShadow);
        rlOpacity = findViewById(R.id.rlOpacity);
        ivColor = findViewById(R.id.ivColor);
        rlEditText = findViewById(R.id.rlEditText);
        rlET = findViewById(R.id.rlET);
        rlEditFontSize = findViewById(R.id.rlEditFontSize);
        sbFontSize = findViewById(R.id.sbFontSize);
        tvFontSize = findViewById(R.id.tvFontSize);
        vEditText = findViewById(R.id.vEditText);
        tvCancel = findViewById(R.id.tvCancel);
        tvTitleEditText = findViewById(R.id.tvTitleEditText);
        rlEditColor = findViewById(R.id.rlEditColor);
        rcvEditColor = findViewById(R.id.rcvEditColor);
        tvShearX = findViewById(R.id.tvShearX);
        tvShearY = findViewById(R.id.tvShearY);
        tvStretch = findViewById(R.id.tvStretch);
        sbShearX = findViewById(R.id.sbShearX);
        sbShearY = findViewById(R.id.sbShearY);
        sbStretch = findViewById(R.id.sbStretch);
        llEditTransform = findViewById(R.id.llEditTransform);

        lstSticker = new ArrayList<>();

        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.parseColor("#575757"), Color.parseColor("#575757")});
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradientDrawable.setCornerRadius(10f);
        ivColor.setBackground(gradientDrawable);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.setAnimExit(this);
    }
}