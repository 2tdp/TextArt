package com.datnt.textart.activity.edit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.datnt.textart.R;
import com.datnt.textart.customview.CustomView;
import com.datnt.textart.customview.stickerview.BitmapStickerIcon;
import com.datnt.textart.customview.stickerview.DeleteIconEvent;
import com.datnt.textart.customview.stickerview.FlipHorizontallyEvent;
import com.datnt.textart.customview.stickerview.Sticker;
import com.datnt.textart.customview.stickerview.StickerView;
import com.datnt.textart.customview.stickerview.TextSticker;
import com.datnt.textart.customview.stickerview.ZoomIconEvent;
import com.datnt.textart.model.ColorModel;
import com.datnt.textart.sharepref.DataLocalManager;
import com.datnt.textart.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;
import java.util.Arrays;

public class EditActivity extends AppCompatActivity {

    private ImageView ivOriginal, iv1_1, iv9_16, iv4_5, iv16_9, ivBack, ivTick, ivUndo, ivRedo, ivLayer, ivImport, ivLook, ivLock;
    private TextView tvOriginal, tv1_1, tv9_16, tv4_5, tv16_9, tvTitle;
    private RelativeLayout rlAddText, rlSticker, rlImage, rlBackground, rlBlend, rlDecor, rlCrop, rlDel, rlDuplicate, rlLook, rlLock, rlLayer, rlExpand;
    private LinearLayout llLayoutImport, llReUndo;
    private HorizontalScrollView vSize, vOperation;
    private CustomView vMain;
    private StickerView stickerView;
    private Bitmap bitmap;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_size);

        init();
    }

    private void init() {
        setUpLayout();
        getData();
        evenClick();
        setUpStickerView();
    }

    private void setUpStickerView() {
        BitmapStickerIcon deleteIcon = new BitmapStickerIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.sticker_ic_close_white_18dp, null), BitmapStickerIcon.LEFT_TOP);
        deleteIcon.setIconEvent(new DeleteIconEvent());

        BitmapStickerIcon zoomIcon = new BitmapStickerIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.sticker_ic_scale_white_18dp, null), BitmapStickerIcon.RIGHT_BOTTOM);
        zoomIcon.setIconEvent(new ZoomIconEvent());

        BitmapStickerIcon flipIcon = new BitmapStickerIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.sticker_ic_flip_white_18dp, null), BitmapStickerIcon.RIGHT_TOP);
        flipIcon.setIconEvent(new FlipHorizontallyEvent());

        stickerView.setIcons(Arrays.asList(deleteIcon, zoomIcon, flipIcon));
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
                if (sticker instanceof TextSticker) {
                    ((TextSticker) sticker).setTextColor(Color.RED);
                    stickerView.replace(sticker);
                    stickerView.invalidate();
                }
                Log.d("2tdp", "onStickerClicked");
            }

            @Override
            public void onStickerDeleted(@NonNull Sticker sticker) {
                Log.d("2tdp", "onStickerDeleted");
            }

            @Override
            public void onStickerDragFinished(@NonNull Sticker sticker) {
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

    private void evenClick() {
        ivBack.setOnClickListener(v -> onBackPressed());
        ivTick.setOnClickListener(v -> clickTick());

        ivOriginal.setOnClickListener(v -> checkSize(0));
        iv1_1.setOnClickListener(v -> checkSize(1));
        iv9_16.setOnClickListener(v -> checkSize(2));
        iv4_5.setOnClickListener(v -> checkSize(3));
        iv16_9.setOnClickListener(v -> checkSize(4));

        ivLayer.setOnClickListener(v -> clickLayer());
        rlLayer.setOnClickListener(v -> {
            vOperation.setVisibility(View.VISIBLE);
            vOperation.setAnimation(AnimationUtils.makeInChildBottomAnimation(this));
            rlExpand.setVisibility(View.GONE);
        });

        rlAddText.setOnClickListener(v -> Utils.setIntent(this, AddTextActivity.class.getName()));
    }

    private void clickLayer() {
        if (vOperation.getVisibility() == View.VISIBLE) vOperation.setVisibility(View.GONE);
        rlExpand.setVisibility(View.VISIBLE);
        rlExpand.setAnimation(AnimationUtils.makeInChildBottomAnimation(this));
    }

    private void clickTick() {
        animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        vSize.startAnimation(animation);
        tvTitle.setVisibility(View.GONE);
        vSize.setVisibility(View.GONE);
        ivTick.setVisibility(View.GONE);

        animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
        vOperation.startAnimation(animation);
        llLayoutImport.setVisibility(View.VISIBLE);
        llReUndo.setVisibility(View.VISIBLE);
        vOperation.setVisibility(View.VISIBLE);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                vSize.clearAnimation();
                vOperation.clearAnimation();
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
        rlExpand = findViewById(R.id.rlExpand);
        rlDel = findViewById(R.id.rlDel);
        rlDuplicate = findViewById(R.id.rlDuplicate);
        rlLock = findViewById(R.id.rlLock);
        rlLook = findViewById(R.id.rlLook);
        ivLock = findViewById(R.id.ivLock);
        ivLook = findViewById(R.id.ivLook);
        rlLayer = findViewById(R.id.rlLayer);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.setAnimExit(this);
    }
}