package com.datnt.textart.activity.edit;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
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
import com.datnt.textart.activity.project.CreateProjectActivity;
import com.datnt.textart.activity.template.TemplateActivity;
import com.datnt.textart.adapter.ColorAdapter;
import com.datnt.textart.adapter.FilterBlendImageAdapter;
import com.datnt.textart.adapter.LayerAdapter;
import com.datnt.textart.adapter.OverlayAdapter;
import com.datnt.textart.adapter.TemplateAdapter;
import com.datnt.textart.adapter.TitleDecorAdapter;
import com.datnt.textart.adapter.TitleEmojiAdapter;
import com.datnt.textart.adapter.ViewPagerAddFragmentsAdapter;
import com.datnt.textart.customview.CustomSeekbarRunText;
import com.datnt.textart.customview.CustomSeekbarTwoWay;
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
import com.datnt.textart.data.DataColor;
import com.datnt.textart.data.DataDecor;
import com.datnt.textart.data.DataEmoji;
import com.datnt.textart.data.DataOverlay;
import com.datnt.textart.data.DataTemplate;
import com.datnt.textart.data.FilterBlendImage;
import com.datnt.textart.fragment.decor.DecorBoxFragment;
import com.datnt.textart.fragment.decor.DecorDrawFragment;
import com.datnt.textart.fragment.decor.DecorFrameFragment;
import com.datnt.textart.fragment.decor.DecorShapeFragment;
import com.datnt.textart.fragment.emoji.EmojiBearFragment;
import com.datnt.textart.fragment.emoji.EmojiFaceFragment;
import com.datnt.textart.model.ColorModel;
import com.datnt.textart.model.DecorModel;
import com.datnt.textart.model.EmojiModel;
import com.datnt.textart.model.FilterBlendModel;
import com.datnt.textart.model.LayerModel;
import com.datnt.textart.model.OverlayModel;
import com.datnt.textart.model.StickerModel;
import com.datnt.textart.model.StyleFontModel;
import com.datnt.textart.model.TemplateModel;
import com.datnt.textart.model.TextModel;
import com.datnt.textart.sharepref.DataLocalManager;
import com.datnt.textart.utils.Utils;
import com.datnt.textart.utils.UtilsAdjust;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.wysaid.nativePort.CGENativeLibrary;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class EditActivity extends AppCompatActivity {

    private ImageView ivOriginal, iv1_1, iv9_16, iv4_5, iv16_9, ivBack, ivTick, ivUndo, ivRedo,
            ivLayer, ivImport, ivLook, ivLock, ivColor, ivColorBlur, ivColorBlurImage, ivVignette, ivVibrance,
            ivWarmth, ivHue, ivSaturation, ivWhites, ivBlacks, ivShadows, ivHighLight, ivExposure,
            ivContrast, ivBrightness, ivColorBlurDecor, ivColorBlurTemp;
    private TextView tvOriginal, tv1_1, tv9_16, tv4_5, tv16_9, tvTitle, tvFontSize, tvCancelEdittext,
            tvTitleEditText, tvShearX, tvShearY, tvStretch, tvXPos, tvYPos, tvBlur, tvCancelEditEmoji,
            tvTitleEmoji, tvCancelEditImage, tvTitleEditImage, tvXPosImage, tvYPosImage, tvBlurImage,
            tvCancelEditBackground, tvTitleEditBackground, tvAdjustBackground, tvVignette, tvVibrance,
            tvWarmth, tvHue, tvSaturation, tvWhites, tvBlacks, tvShadows, tvHighLight, tvExposure,
            tvContrast, tvBrightness, tvTitleEditOverlay, tvCancelEditOverlay, tvCancelEditDecor,
            tvTitleEditDecor, tvCancelEditTemp, tvXPosDecor, tvYPosDecor, tvBlurDecor, tvTitleEditTemp,
            tvXPosTemp, tvYPosTemp, tvBlurTemp;
    private SeekBar sbFontSize;
    private CustomSeekbarTwoWay sbStretch, sbShearX, sbShearY, sbXPos, sbYPos, sbBlur, sbXPosImage,
            sbYPosImage, sbBlurImage, sbAdjust, sbXPosDecor, sbYPosDecor, sbBlurDecor, sbXPosTemp,
            sbYPosTemp, sbBlurTemp;
    private CustomSeekbarRunText sbOpacityText, sbOpacityEmoji, sbOpacityImage, sbOpacityBackground,
            sbOpacityOverlay, sbOpacityDecor, sbOpacityTemp;
    private RelativeLayout rlAddText, rlSticker, rlImage, rlBackground, rlBlend, rlDecor, rlCrop,
            rlDelLayer, rlDuplicateLayer, rlLook, rlLock, rlLayer, rlExpandLayer, rlExpandEditText,
            rlET, rlDuplicateText, rlFontSize, rlColor, rlTransform, rlShadow, rlOpacity, rlDelText,
            rlEditText, rlEditFontSize, rlEditColor, rlEditOpacityText, rlExpandEmoji, rlPickEmoji,
            rlExpandEditEmoji, rlEditEmoji, rlDelEmoji, rlReplaceEmoji, rlOpacityEmoji, rlFlipY, rlFlipX,
            rlEditOpacityEmoji, rlExpandEditImage, rlEditImage, rlDelImage, rlReplaceImage, rlDuplicateImage,
            rlCropImage, rlFilterImage, rlShadowImage, rlOpacityImage, rlBlendImage, rlEditFilter,
            rlEditOpacityImage, rlEditBlend, rlExpandEditBackground, rlEditBackground, rlDelBackground,
            rlReplaceBackground, rlAdjustBackground, rlFilterBackground, rlOpacityBackground, rlFlipYBackground,
            rlFlipXBackground, rlVignette, rlVibrance, rlWarmth, rlHue, rlSaturation, rlWhites, rlBlacks,
            rlShadows, rlHighLight, rlExposure, rlContrast, rlBrightness, rlAdjust, rlEditFilterBackground,
            rlEditOpacityBackground, rlPickOverlay, rlExpandOverlay, rlFlipXOverlay, rlFlipYOverlay, rlOpacityOverlay,
            rlReplaceOverlay, rlDelOverlay, rlEditOpacityOverlay, rlEditOverlay, rlExpandEditOverlay,
            rlExpandDecor, rlPickDecor, rlEditDecor, rlDelDecor, rlReplaceDecor, rlExpandEditDecor,
            rlDuplicateDecor, rlEditColorDecor, rlColorDecor, rlEditOpacityDecor, rlOpacityDecor, rlShadowDecor,
            rlFlipYDecor, rlFlipXDecor, rlExpandEditTemp, rlEditTemp, rlEditOpacityTemp, rlEditColorTemp,
            rlDelTemp, rlReplaceTemp, rlDuplicateTemp, rlColorTemp, rlBackgroundTemp,
            rlShadowTemp, rlOpacityTemp, rlFlipXTemp, rlFlipYTemp, rlExpandTemp, rlPickTextTemp;
    private LinearLayout llLayoutImport, llReUndo, llEditTransform, llEditShadow, llEditEmoji, llEditShadowImage,
            llEditOverlay, llEditShadowDecor, llEditShadowTemp;
    private HorizontalScrollView vSize, vOperation, vEditText, vEditImage, vEditBackground, vEditDecor, vEditTemp;
    private CustomView vMain;
    private StickerView stickerView;
    private RecyclerView rcvEditColor, rcvTitleEmoji, rcvEditFilter, rcvEditBlend, rcvEditFilterBackground,
            rcvOverlay, rcvTitleDecor, rcvEditColorDecor, rcvLayer, rcvEditColorTemp, rcvTextTemp;
    private ViewPager2 vpEmoji, vpDecor;

    private ViewPagerAddFragmentsAdapter addFragmentsAdapter;
    private TitleEmojiAdapter emojiTitleAdapter;
    private TitleDecorAdapter titleDecorAdapter;
    private FilterBlendImageAdapter filterBlendImageAdapter;
    private LayerAdapter layerAdapter;
    private Bitmap bitmap, bitmapDrawable, bitmapFilterBlend;
    private ArrayList<StickerModel> lstSticker;
    private ArrayList<FilterBlendModel> lstFilterBlend;
    private ArrayList<ColorModel> lstColor;
    private TextSticker textSticker;
    private DrawableSticker drawableSticker;
    private boolean check, isFirstEmoji, replaceEmoji, isReplaceImage, isFilter, isBackground,
            replaceOverlay, replaceDecor, islLayer, isFirstLayer, isTemplate;
    private int sizeMain, positionFilter = 0, positionBlend = 0, positionFilterBackground = 0, colorShadow = 0;
    private float opacityBackground = 1, radiusBlur = 5f, dx = 0f, dy = 0f;
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
        BitmapStickerIcon rotate = new BitmapStickerIcon(this, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_sticker_rotate, null), BitmapStickerIcon.LEFT_TOP);
        rotate.setIconEvent(new RotateIconEvent());

        BitmapStickerIcon flip = new BitmapStickerIcon(this, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_sticker_flip, null), BitmapStickerIcon.RIGHT_BOTTOM);
        flip.setIconEvent(new FlipHorizontallyEvent());

        BitmapStickerIcon zoom = new BitmapStickerIcon(this, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_sticker_resize, null), BitmapStickerIcon.RIGHT_TOP);
        zoom.setIconEvent(new ZoomIconEvent());

        stickerView.setIcons(Arrays.asList(rotate, flip, zoom));
        stickerView.setBackgroundColor(Color.WHITE);
        stickerView.setConstrained(true);

        stickerView.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded(@NonNull Sticker sticker) {
                int add = -1;
                if (sticker instanceof DrawableSticker) {
                    drawableSticker = (DrawableSticker) sticker;
                    textSticker = null;
                    if (drawableSticker.isDecor() && !islLayer) add = 1;
                    else if (drawableSticker.isOverlay() && !islLayer) add = 2;
                    else if (drawableSticker.isImage() && !islLayer) add = 3;
                    else if (!islLayer) add = 4;

                    switch (add) {
                        case 1:
                            seekAndHideLayout(11);
                            break;
                        case 2:
                            seekAndHideLayout(9);
                            break;
                        case 3:
                            image();
                            break;
                        case 4:
                            seekAndHideLayout(5);
                            break;
                        default:
                            layerAdapter.setData(stickerView.getListLayer());
                            layerAdapter.setCurrent(0);
                            stickerView.setCurrentSticker(stickerView.getListLayer().get(0).getSticker());
                            break;
                    }
                } else if (sticker instanceof TextSticker) {
                    textSticker = (TextSticker) sticker;
                    drawableSticker = null;
                    seekAndHideLayout(3);
                }
                Log.d("2tdp", "onStickerAdded");
            }

            @Override
            public void onStickerClicked(@NonNull Sticker sticker) {
                if (sticker instanceof DrawableSticker) {
                    drawableSticker = (DrawableSticker) sticker;
                    textSticker = null;
                    if (drawableSticker.isDecor()) seekAndHideLayout(11);
                    else if (drawableSticker.isOverlay()) seekAndHideLayout(9);
                    else if (drawableSticker.isImage()) {
                        for (StickerModel st : lstSticker) {
                            if (st.getDrawableSticker() != null && st.getDrawableSticker().getDrawable() != Utils.getDrawableTransparent(EditActivity.this))
                                if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isImage()) {
                                    clearData();
                                    bitmapFilterBlend = st.getBitmapRoot();
                                    positionFilter = st.getPositionFilter();
                                    positionBlend = st.getPositionBlend();
                                }
                        }
                        image();
                    } else if (drawableSticker.isTemplate()) seekAndHideLayout(13);
                    else seekAndHideLayout(5);
                } else if (sticker instanceof TextSticker) {
                    seekAndHideLayout(3);
                    textSticker = (TextSticker) sticker;
                    drawableSticker = null;
                    for (StickerModel t : lstSticker) {
                        if (t.getTextSticker() != null)
                            if (t.getTextSticker().getId() == textSticker.getId() && t.getTextModel().getColor() != null) {
                                ColorModel color = t.getTextModel().getColor();

                                GradientDrawable gradient;
                                if (color.getColorStart() != color.getColorEnd())
                                    gradient = new GradientDrawable(Utils.setDirection(color.getDirec()), new int[]{color.getColorStart(), color.getColorEnd()});
                                else
                                    gradient = new GradientDrawable(Utils.setDirection(0), new int[]{color.getColorStart(), color.getColorEnd()});

                                gradient.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                                gradient.setCornerRadius(10f);
                                ivColor.setBackground(gradient);
                            }
                    }
                }
                Log.d("2tdp", "onStickerClicked");
            }

            @Override
            public void onStickerDeleted(@NonNull Sticker sticker) {
                Log.d("2tdp", "onStickerDeleted");
            }

            @Override
            public void onStickerDragFinished(@NonNull Sticker sticker) {
                if (sticker instanceof DrawableSticker) {
                    drawableSticker = (DrawableSticker) sticker;
                    textSticker = null;
                    if (drawableSticker.isDecor()) seekAndHideLayout(11);
                    else if (drawableSticker.isOverlay()) seekAndHideLayout(9);
                    else if (drawableSticker.isImage()) {
                        for (StickerModel st : lstSticker) {
                            if (st.getDrawableSticker() != null && st.getDrawableSticker().getDrawable() != Utils.getDrawableTransparent(EditActivity.this))
                                if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isImage()) {
                                    clearData();
                                    bitmapFilterBlend = st.getBitmapRoot();
                                    positionFilter = st.getPositionFilter();
                                    positionBlend = st.getPositionBlend();
                                }
                        }
                        image();
                    } else if (drawableSticker.isTemplate()) seekAndHideLayout(13);
                    else seekAndHideLayout(5);
                } else if (sticker instanceof TextSticker) {
                    textSticker = (TextSticker) sticker;
                    drawableSticker = null;
                    seekAndHideLayout(3);
                }
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

        ivTick.setOnClickListener(v -> {
            if (isTemplate) seekAndHideLayout(13);
            else seekAndHideLayout(0);
        });

        rlLayer.setOnClickListener(v -> {
            seekAndHideLayout(0);
            islLayer = false;
        });
        rlEditText.setOnClickListener(v -> seekAndHideLayout(0));
        rlEditEmoji.setOnClickListener(v -> seekAndHideLayout(0));
        rlEditImage.setOnClickListener(v -> seekAndHideLayout(0));
        rlEditBackground.setOnClickListener(v -> seekAndHideLayout(0));
        rlPickOverlay.setOnClickListener(v -> seekAndHideLayout(0));
        rlEditOverlay.setOnClickListener(v -> seekAndHideLayout(0));
        rlPickDecor.setOnClickListener(v -> seekAndHideLayout(0));
        rlEditDecor.setOnClickListener(v -> seekAndHideLayout(0));
        rlEditTemp.setOnClickListener(v -> seekAndHideLayout(0));

        //size
        ivOriginal.setOnClickListener(v -> checkSize(0));
        iv1_1.setOnClickListener(v -> checkSize(1));
        iv9_16.setOnClickListener(v -> checkSize(2));
        iv4_5.setOnClickListener(v -> checkSize(3));
        iv16_9.setOnClickListener(v -> checkSize(4));

        //template
        rlDelTemp.setOnClickListener(v -> delStick());
        rlReplaceTemp.setOnClickListener(v -> replaceTemp());
        rlDuplicateTemp.setOnClickListener(v -> duplicateTextTemp());
        rlColorTemp.setOnClickListener(v -> colorTemp());
        rlShadowTemp.setOnClickListener(v -> shadowTemp());
        rlOpacityTemp.setOnClickListener(v -> opacityTemp());
        rlBackgroundTemp.setOnClickListener(v -> replaceBackground());
        rlFlipXTemp.setOnClickListener(v -> stickerView.flipCurrentSticker(0));
        rlFlipYTemp.setOnClickListener(v -> stickerView.flipCurrentSticker(1));

        //layer
        ivLayer.setOnClickListener(v -> {
            layer();
            isFirstLayer = false;
        });
        rlDelLayer.setOnClickListener(v -> {
            islLayer = true;
            isFirstLayer = false;
            delStick();
        });
        rlDuplicateLayer.setOnClickListener(v -> {
            islLayer = true;
            isFirstLayer = false;
            duplicateLayer();
        });
        rlLook.setOnClickListener(v -> lookLayer());
        rlLock.setOnClickListener(v -> lockLayer());

        //text
        rlAddText.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddTextActivity.class);
            launcherEditText.launch(intent, ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left));
            check = true;
        });
        rlDelText.setOnClickListener(v -> delStick());
        rlET.setOnClickListener(v -> editText());
        rlDuplicateText.setOnClickListener(v -> duplicateText());
        rlFontSize.setOnClickListener(v -> fontSize());
        rlColor.setOnClickListener(v -> color());
        rlTransform.setOnClickListener(v -> transform());
        rlShadow.setOnClickListener(v -> shadow());
        rlOpacity.setOnClickListener(v -> opacityText());

        //emoji
        rlSticker.setOnClickListener(v -> {
            replaceEmoji = false;
            emoji();
        });
        rlDelEmoji.setOnClickListener(v -> delStick());
        rlReplaceEmoji.setOnClickListener(v -> {
            replaceEmoji = true;
            seekAndHideLayout(4);
        });
        rlPickEmoji.setOnClickListener(v -> seekAndHideLayout(0));
        rlOpacityEmoji.setOnClickListener(v -> opacityEmoji());
        rlFlipY.setOnClickListener(v -> stickerView.flipCurrentSticker(1));
        rlFlipX.setOnClickListener(v -> stickerView.flipCurrentSticker(0));

        //image
        rlImage.setOnClickListener(v -> {
            launcherImage.launch("image/*");
            isReplaceImage = false;
        });
        rlDelImage.setOnClickListener(v -> delStick());
        rlReplaceImage.setOnClickListener(v -> {
            launcherImage.launch("image/*");
            isReplaceImage = true;
        });
        rlDuplicateImage.setOnClickListener(v -> duplicateImage());
        rlFilterImage.setOnClickListener(v -> {
            filterImage();
            isFilter = true;
        });
        rlShadowImage.setOnClickListener(v -> shadowImage());
        rlOpacityImage.setOnClickListener(v -> opacityImage());
        rlBlendImage.setOnClickListener(v -> {
            blendImage();
            isFilter = false;
        });

        //background
        rlBackground.setOnClickListener(v -> background());
        rlDelBackground.setOnClickListener(v -> vMain.setData(null, new ColorModel(Color.WHITE, Color.WHITE, 0, false)));
        rlReplaceBackground.setOnClickListener(v -> replaceBackground());
        rlAdjustBackground.setOnClickListener(v -> adjustBackground());
        rlFilterBackground.setOnClickListener(v -> filterBackground());
        rlOpacityBackground.setOnClickListener(v -> opacityBackground());
        rlFlipXBackground.setOnClickListener(v -> flipXBackground());
        rlFlipYBackground.setOnClickListener(v -> flipYBackground());

        //overlay
        rlBlend.setOnClickListener(v -> overlay());
        rlDelOverlay.setOnClickListener(v -> delStick());
        rlReplaceOverlay.setOnClickListener(v -> {
            replaceOverlay = true;
            overlay();
        });
        rlOpacityOverlay.setOnClickListener(v -> opacityOverlay());
        rlFlipXOverlay.setOnClickListener(v -> stickerView.flipCurrentSticker(0));
        rlFlipYOverlay.setOnClickListener(v -> stickerView.flipCurrentSticker(1));

        //decor
        rlDecor.setOnClickListener(v -> {
            replaceDecor = false;
            decor();
        });
        rlDelDecor.setOnClickListener(v -> delStick());
        rlReplaceDecor.setOnClickListener(v -> {
            replaceDecor = true;
            seekAndHideLayout(10);
        });
        rlDuplicateDecor.setOnClickListener(v -> duplicateDecor());
        rlColorDecor.setOnClickListener(v -> colorDecor());
        rlOpacityDecor.setOnClickListener(v -> opacityDecor());
        rlShadowDecor.setOnClickListener(v -> shadowDecor());
        rlFlipYDecor.setOnClickListener(v -> stickerView.flipCurrentSticker(1));
        rlFlipXDecor.setOnClickListener(v -> stickerView.flipCurrentSticker(0));

        //size
        rlCrop.setOnClickListener(v -> seekAndHideLayout(12));
    }

    Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what) {
                case 0:
                    Bitmap bitmap = null;
                    Drawable drawable = new BitmapDrawable(getResources(), bitmapFilterBlend);
                    DrawableSticker sticker = new DrawableSticker(getBaseContext(), drawable, new ArrayList<>(), getId(), true, false, false, false);
                    for (StickerModel st : lstSticker) {
                        if (st.getDrawableSticker() != null && st.getDrawableSticker().getDrawable() != Utils.getDrawableTransparent(EditActivity.this))
                            if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isImage()) {
                                sticker.setAlpha(st.getDrawableSticker().getAlpha());
                                positionFilter = st.getPositionFilter();
                                positionBlend = st.getPositionBlend();
                                if (positionFilter != 0) {
                                    bitmap = CGENativeLibrary.cgeFilterImage_MultipleEffects(bitmapFilterBlend, FilterBlendImage.EFFECT_CONFIGS[positionFilter], 0.8f);
                                }
                                if (positionBlend != 0) {
                                    bitmap = CGENativeLibrary.cgeFilterImage_MultipleEffects(bitmapFilterBlend, FilterBlendImage.EFFECT_CONFIGS_BLEND[positionBlend], 0.8f);
                                }
                            }
                    }
                    if (!isReplaceImage) {
                        stickerView.addSticker(sticker);
                        lstSticker.add(new StickerModel(null, null, null, bitmapFilterBlend, bitmapFilterBlend, null, sticker, null, 0, 0));
                    } else {
                        if (bitmap != null) {
                            sticker.setDrawable(new BitmapDrawable(getResources(), bitmap));
                            lstSticker.add(new StickerModel(null, null, null, bitmapFilterBlend, bitmap, null, sticker, null, positionFilter, positionBlend));
                        } else {
                            sticker.setDrawable(new BitmapDrawable(getResources(), bitmapFilterBlend));
                            lstSticker.add(new StickerModel(null, null, null, bitmapFilterBlend, bitmapFilterBlend, null, sticker, null, 0, 0));
                        }
                        stickerView.replace(sticker, true);
                    }
                    break;
                case 1:
                    if (filterBlendImageAdapter != null) {
                        filterBlendImageAdapter.setData(lstFilterBlend);
                        if (isFilter) {
                            rcvEditFilter.smoothScrollToPosition(positionFilter);
                            filterBlendImageAdapter.setCurrent(positionFilter);
                        } else if (isBackground) {
                            rcvEditFilterBackground.smoothScrollToPosition(positionFilterBackground);
                            filterBlendImageAdapter.setCurrent(positionFilterBackground);
                        } else {
                            rcvEditBlend.smoothScrollToPosition(positionBlend);
                            filterBlendImageAdapter.setCurrent(positionBlend);
                        }
                        filterBlendImageAdapter.changeNotify();
                    }
                    break;
            }
            return true;
        }
    });

    private final ActivityResultLauncher<Intent> launcherEditText = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (result.getData() != null) {
                TextModel textModel = (TextModel) result.getData().getSerializableExtra("text");
                if (check) {
                    textSticker = new TextSticker(this, getId());
                    stickerView.addSticker(textSticker);
                } else {
                    stickerView.replace(textSticker, true);
                }
                setTextSticker(textSticker, textModel);
            }
        }
    });

    private final ActivityResultLauncher<String> launcherImage = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri uri) {
            if (uri != null) {
                clearData();
                new Thread(() -> {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        if (bitmap != null) {
                            bitmapFilterBlend = Bitmap.createBitmap(Utils.modifyOrientation(getBaseContext(),
                                    Bitmap.createScaledBitmap(bitmap, 400, 400 * bitmap.getHeight() / bitmap.getWidth(), false), uri));
                        } else
                            Utils.showToast(getBaseContext(), getString(R.string.cant_get_image));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    handler.sendEmptyMessage(0);
                }).start();
            }
        }
    });

    //decor
    private void decor() {
        rlExpandDecor.getLayoutParams().height = getResources().getDisplayMetrics().heightPixels * 90 / 100;
        seekAndHideLayout(10);
        setUpTitleDecor();
        setUpDataDecor();
    }

    private void setUpDataDecor() {
        addFragmentsAdapter = new ViewPagerAddFragmentsAdapter(getSupportFragmentManager(), getLifecycle());

        createFragmentDecor();

        vpDecor.setAdapter(addFragmentsAdapter);
        vpDecor.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                titleDecorAdapter.setCurrent(position);
                rcvTitleDecor.smoothScrollToPosition(position);
            }
        });
    }

    private void createFragmentDecor() {

        //create fragment theo a - z

        DecorBoxFragment boxFragment = DecorBoxFragment.newInstance("box");
        addFragmentsAdapter.addFrag(boxFragment);
        boxFragment.getDecor((o, pos) -> {
            DecorModel decor = (DecorModel) o;
            if (replaceDecor) addNewDecor(drawableSticker.getId(), decor, 1);
            else addNewDecor(getId(), decor, 0);
            seekAndHideLayout(11);
        });

        DecorDrawFragment drawFragment = DecorDrawFragment.newInstance("draw");
        addFragmentsAdapter.addFrag(drawFragment);
        drawFragment.getDecor((o, pos) -> {
            DecorModel decor = (DecorModel) o;
            if (replaceDecor) addNewDecor(drawableSticker.getId(), decor, 1);
            else addNewDecor(getId(), decor, 0);
            seekAndHideLayout(11);
        });

        DecorFrameFragment frameFragment = DecorFrameFragment.newInstance("frame");
        addFragmentsAdapter.addFrag(frameFragment);
        frameFragment.getDecor((o, pos) -> {
            DecorModel decor = (DecorModel) o;
            if (replaceDecor) addNewDecor(drawableSticker.getId(), decor, 1);
            else addNewDecor(getId(), decor, 0);
            seekAndHideLayout(11);
        });

        DecorShapeFragment shapeFragment = DecorShapeFragment.newInstance("shape");
        addFragmentsAdapter.addFrag(shapeFragment);
        shapeFragment.getDecor((o, pos) -> {
            DecorModel decor = (DecorModel) o;
            if (replaceDecor) addNewDecor(drawableSticker.getId(), decor, 1);
            else addNewDecor(getId(), decor, 0);
            seekAndHideLayout(11);
        });


        bitmapDrawable = null;
    }

    private void setUpTitleDecor() {

        titleDecorAdapter = new TitleDecorAdapter(this, (o, pos) -> {
            titleDecorAdapter.setCurrent(pos);
            vpDecor.setCurrentItem(pos, true);
        });

        titleDecorAdapter.setData(DataDecor.getTitleDecor(this, "title"));
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvTitleDecor.setLayoutManager(manager);
        rcvTitleDecor.setAdapter(titleDecorAdapter);
    }

    //shadow
    private void shadowDecor() {
        setUpLayoutEditShadowDecor(0);
        tvCancelEditDecor.setOnClickListener(v -> setUpLayoutEditShadowDecor(1));

        dx = drawableSticker.getDx();
        dy = drawableSticker.getDy();
        radiusBlur = drawableSticker.getRadiusBlur();
        colorShadow = drawableSticker.getColorShadow();

        sbXPosDecor.setProgress((int) dx);
        tvXPosDecor.setText(String.valueOf((int) dx));
        sbYPosDecor.setProgress((int) dy);
        tvYPosDecor.setText(String.valueOf((int) dy));
        if ((int) radiusBlur == 0)
            tvBlurDecor.setText(String.valueOf(0));
        else
            tvBlurDecor.setText(String.valueOf((int) ((radiusBlur - 5) * 10f)));

        if (radiusBlur > 5)
            sbBlurDecor.setProgress((int) ((radiusBlur - 5) * 10f));
        else if (radiusBlur == 0f) {
            sbBlurDecor.setProgress(0);
            radiusBlur = 5f;
        } else sbBlurDecor.setProgress((int) ((5 - radiusBlur) * 10f));

        sbXPosDecor.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                dx = value;
                tvXPosDecor.setText(String.valueOf(value));
                drawableSticker.setShadow(radiusBlur, dx, dy, colorShadow, true);
                stickerView.invalidate();
            }

            @Override
            public void onUp(View v) {

            }
        });

        sbYPosDecor.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                dy = value;
                tvYPosDecor.setText(String.valueOf(value));
                drawableSticker.setShadow(radiusBlur, dx, dy, colorShadow, true);
                stickerView.invalidate();
            }

            @Override
            public void onUp(View v) {

            }
        });

        sbBlurDecor.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                if (value == -50) radiusBlur = 1f;
                else if (value == 50) radiusBlur = 10f;
                else if (value == 0) radiusBlur = 5f;
                else radiusBlur = 5 + (value * 5 / 50f);
                tvBlurDecor.setText(String.valueOf(value));
                drawableSticker.setShadow(radiusBlur, dx, dy, colorShadow, true);
                stickerView.invalidate();
            }

            @Override
            public void onUp(View v) {

            }
        });

        ivColorBlurDecor.setOnClickListener(v -> DataColor.pickColor(this, (color) -> {
            colorShadow = color.getColorStart();
            drawableSticker.setShadow(radiusBlur, dx, dy, colorShadow, true);
            stickerView.invalidate();
        }));
    }

    private void setUpLayoutEditShadowDecor(int pos) {
        switch (pos) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                vEditDecor.startAnimation(animation);
                if (vEditDecor.getVisibility() == View.VISIBLE) vEditDecor.setVisibility(View.GONE);

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                llEditShadowDecor.startAnimation(animation);
                if (llEditShadowDecor.getVisibility() == View.GONE) {
                    llEditShadowDecor.setVisibility(View.VISIBLE);
                    tvCancelEditDecor.setVisibility(View.VISIBLE);
                    tvTitleEditDecor.setText(getString(R.string.shadow));
                    tvXPosDecor.setText(String.valueOf(0));
                    tvYPosDecor.setText(String.valueOf(0));
                    tvBlurDecor.setText(String.valueOf(0));
                    sbXPosDecor.setMax(100);
                    sbYPosDecor.setMax(100);
                    sbBlurDecor.setMax(100);
                    colorShadow = 0;
                    radiusBlur = 5f;
                    dx = 0f;
                    dy = 0f;
                }
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                llEditShadowImage.startAnimation(animation);
                if (llEditShadowDecor.getVisibility() == View.VISIBLE) {
                    llEditShadowDecor.setVisibility(View.GONE);
                    tvCancelEditDecor.setVisibility(View.GONE);
                    tvTitleEditDecor.setText(getString(R.string.decor));
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                vEditDecor.startAnimation(animation);
                if (vEditDecor.getVisibility() == View.GONE) vEditDecor.setVisibility(View.VISIBLE);
                break;
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                vEditDecor.clearAnimation();
                llEditShadowDecor.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //opacity
    private void opacityDecor() {
        setUpLayoutEditOpacityDecor(0);
        tvCancelEditDecor.setOnClickListener(v -> setUpLayoutEditOpacityDecor(1));

        for (StickerModel st : lstSticker) {
            if (st.getDrawableSticker() != null && st.getDrawableSticker().getDrawable() != Utils.getDrawableTransparent(EditActivity.this))
                if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isDecor())
                    sbOpacityDecor.setProgress((int) (st.getDrawableSticker().getAlpha() * 100 / 255f));
        }

        sbOpacityDecor.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                drawableSticker.setShadow(drawableSticker.getRadiusBlur(), drawableSticker.getDx(), drawableSticker.getDy(), drawableSticker.getColorShadow(), true);
                drawableSticker.setAlpha((int) (value * 255 / 100f));
                stickerView.invalidate();
//                stickerView.replace(drawableSticker, true);
            }

            @Override
            public void onUp(View v) {

            }
        });
    }

    private void setUpLayoutEditOpacityDecor(int pos) {
        switch (pos) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                vEditDecor.startAnimation(animation);

                if (vEditDecor.getVisibility() == View.VISIBLE)
                    vEditDecor.setVisibility(View.GONE);

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                rlEditOpacityDecor.startAnimation(animation);
                if (rlEditOpacityDecor.getVisibility() == View.GONE) {
                    rlEditOpacityDecor.setVisibility(View.VISIBLE);
                    tvCancelEditDecor.setVisibility(View.VISIBLE);
                    tvTitleEditDecor.setText(getString(R.string.opacity));
                    sbOpacityDecor.setColorText(getResources().getColor(R.color.green));
                    sbOpacityDecor.setSizeText(com.intuit.ssp.R.dimen._10ssp);
                    sbOpacityDecor.setProgress(100);
                    sbOpacityDecor.setMax(100);
                }
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                rlEditOpacityDecor.startAnimation(animation);
                if (rlEditOpacityDecor.getVisibility() == View.VISIBLE) {
                    rlEditOpacityDecor.setVisibility(View.GONE);
                    tvCancelEditDecor.setVisibility(View.GONE);
                    tvTitleEditDecor.setText(getString(R.string.decor));
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                vEditDecor.startAnimation(animation);
                if (vEditDecor.getVisibility() == View.GONE)
                    vEditDecor.setVisibility(View.VISIBLE);
                break;
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rlEditOpacityDecor.clearAnimation();
                vEditDecor.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //color
    private void colorDecor() {
        setUpLayoutEditColorDecor(0);
        tvCancelEditDecor.setOnClickListener(v -> setUpLayoutEditColorDecor(1));

        ColorAdapter colorAdapter = new ColorAdapter(this, R.layout.item_color_edit, (o, pos) -> {
            ColorModel color = (ColorModel) o;
            if (pos == 0) DataColor.pickColor(this, this::setColorDecor);
            else {
                if (color.getColorStart() == color.getColorEnd()) {
                    setColorDecor(color);
                } else {
                    DataColor.pickDirection(this, color, this::setColorDecor);
                }
            }
        });
        if (!lstColor.isEmpty())
            colorAdapter.setData(lstColor);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvEditColorDecor.setLayoutManager(manager);
        rcvEditColorDecor.setAdapter(colorAdapter);
    }

    private void setColorDecor(ColorModel color) {
        for (StickerModel st : lstSticker) {
            if (st.getDrawableSticker() != null)
                if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isDecor()) {
                    st.getDrawableSticker().setColor(color);
                    st.setColor(color);
                    stickerView.replace(st.getDrawableSticker(), true);
//                    lstSticker.add(new StickerModel(null, st.getDecorModel(), st.getBitmapRoot(), bitmap, null, st.getDrawableSticker(), color, -1, -1));
                    break;
                }
        }
    }

    private void setUpLayoutEditColorDecor(int pos) {
        switch (pos) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                vEditDecor.startAnimation(animation);

                if (vEditDecor.getVisibility() == View.VISIBLE)
                    vEditDecor.setVisibility(View.GONE);

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                rlEditColorDecor.startAnimation(animation);
                if (rlEditColorDecor.getVisibility() == View.GONE) {
                    rlEditColorDecor.setVisibility(View.VISIBLE);
                    tvCancelEditDecor.setVisibility(View.VISIBLE);
                    tvTitleEditDecor.setText(getString(R.string.color));
                }
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                rlEditColorDecor.startAnimation(animation);
                if (rlEditColorDecor.getVisibility() == View.VISIBLE) {
                    rlEditColorDecor.setVisibility(View.GONE);
                    tvCancelEditDecor.setVisibility(View.GONE);
                    tvTitleEditDecor.setText(getString(R.string.decor));
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                vEditDecor.startAnimation(animation);
                if (vEditDecor.getVisibility() == View.GONE)
                    vEditDecor.setVisibility(View.VISIBLE);
                break;
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rlEditColorDecor.clearAnimation();
                vEditDecor.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //duplicate
    private void duplicateDecor() {
        if (drawableSticker == null) {
            Utils.showToast(this, getString(R.string.choose_sticker_text));
            return;
        }

        for (StickerModel st : lstSticker) {
            if (st.getDrawableSticker() != null)
                if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isDecor()) {
                    DrawableSticker sticker = new DrawableSticker(this, null, st.getDecorModel().getLstPathData(), getId(), false, false, true, false);

                    sticker.setAlpha(st.getDrawableSticker().getAlpha());
                    if (st.getColor() != null) sticker.setColor(st.getColor());
                    sticker.setShadow(drawableSticker.getRadiusBlur(), drawableSticker.getDx(), drawableSticker.getDy(), drawableSticker.getColorShadow(), true);

                    stickerView.addSticker(sticker);
                    if (st.getColor() != null)
                        lstSticker.add(new StickerModel(null, st.getDecorModel(), null, null, null, null, sticker, st.getColor(), -1, -1));
                    else
                        lstSticker.add(new StickerModel(null, st.getDecorModel(), null, null, null, null, sticker, null, -1, -1));

                    break;
                }
        }
    }

    private void addNewDecor(int id, DecorModel decor, int pos) {
        DrawableSticker sticker = new DrawableSticker(this, null, decor.getLstPathData(), id, false, false, true, false);

        switch (pos) {
            case 0:
                stickerView.addSticker(sticker);
                lstSticker.add(new StickerModel(null, decor, null, null, null, null, sticker, null, -1, -1));
                break;
            case 1:
                for (StickerModel st : lstSticker) {
                    if (st.getDrawableSticker() != null)
                        if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isDecor()) {
                            st.getDrawableSticker().setPathData(decor.getLstPathData());
                            sticker.setAlpha(drawableSticker.getAlpha());
                            sticker.setShadow(drawableSticker.getRadiusBlur(), drawableSticker.getDx(), drawableSticker.getDy(), drawableSticker.getColorShadow(), true);
                            if (st.getColor() != null)
                                sticker.setColor(st.getColor());

                            stickerView.replace(sticker, true);
                            st.setDecorModel(decor);
                            break;
                        }
                }
                break;
        }
    }

    //overlay
    private void overlay() {
        rlExpandOverlay.getLayoutParams().height = getResources().getDisplayMetrics().heightPixels * 90 / 100;
        seekAndHideLayout(8);
        setUpDataOverlay();
    }

    //opacity
    private void opacityOverlay() {
        setUpLayoutEditOpacityOverlay(0);
        tvCancelEditOverlay.setOnClickListener(v -> setUpLayoutEditOpacityOverlay(1));

        for (StickerModel st : lstSticker) {
            if (st.getDrawableSticker() != null && st.getDrawableSticker().getDrawable() != Utils.getDrawableTransparent(EditActivity.this))
                if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isOverlay())
                    sbOpacityOverlay.setProgress((int) (st.getDrawableSticker().getAlpha() * 100 / 255f));
        }
        sbOpacityOverlay.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                if (drawableSticker != null) drawableSticker.setAlpha((int) (value * 255 / 100f));
                Log.d("2tdp", "onMove: " + value);
                stickerView.replace(drawableSticker, true);
            }

            @Override
            public void onUp(View v) {

            }
        });
    }

    private void setUpLayoutEditOpacityOverlay(int pos) {
        switch (pos) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                llEditOverlay.startAnimation(animation);

                if (llEditOverlay.getVisibility() == View.VISIBLE)
                    llEditOverlay.setVisibility(View.GONE);

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                rlEditOpacityOverlay.startAnimation(animation);
                if (rlEditOpacityOverlay.getVisibility() == View.GONE) {
                    rlEditOpacityOverlay.setVisibility(View.VISIBLE);
                    tvCancelEditOverlay.setVisibility(View.VISIBLE);
                    tvTitleEditOverlay.setText(getString(R.string.opacity));
                    sbOpacityOverlay.setColorText(getResources().getColor(R.color.green));
                    sbOpacityOverlay.setSizeText(com.intuit.ssp.R.dimen._10ssp);
                    sbOpacityOverlay.setProgress(100);
                    sbOpacityOverlay.setMax(100);
                }
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                rlEditOpacityOverlay.startAnimation(animation);
                if (rlEditOpacityOverlay.getVisibility() == View.VISIBLE) {
                    rlEditOpacityOverlay.setVisibility(View.GONE);
                    tvCancelEditOverlay.setVisibility(View.GONE);
                    tvTitleEditOverlay.setText(getString(R.string.overlay));
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                llEditOverlay.startAnimation(animation);
                if (llEditOverlay.getVisibility() == View.GONE)
                    llEditOverlay.setVisibility(View.VISIBLE);
                break;
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rlEditOpacityOverlay.clearAnimation();
                llEditOverlay.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void addOverlay(int pos, OverlayModel overlay) {

        DrawableSticker sticker = null;
        bitmapDrawable = Utils.getBitmapFromAsset(this, overlay.getNameFolder(), overlay.getNameOverlay(), false, false);

        switch (pos) {
            case 0:
                sticker = new DrawableSticker(this, new BitmapDrawable(getResources(), bitmapDrawable), new ArrayList<>(), getId(), false, true, false, false);
                stickerView.addSticker(sticker);
                break;
            case 1:
                for (StickerModel st : lstSticker) {
                    if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isOverlay()) {
                        sticker = new DrawableSticker(this, new BitmapDrawable(getResources(), bitmapDrawable), new ArrayList<>(),
                                drawableSticker.getId(), false, true, false, false);
                        stickerView.replace(sticker, true);
                    }
                }
                break;
        }
        lstSticker.add(new StickerModel(null, null, null, null, bitmapDrawable, null, sticker, null, -1, -1));
    }

    private void setUpDataOverlay() {
        ArrayList<OverlayModel> lstOverlay = new ArrayList<>(DataOverlay.getOverlay(this, "overlay"));

        OverlayAdapter overlayAdapter = new OverlayAdapter(this, (o, pos) -> {
            OverlayModel overlay = (OverlayModel) o;

            seekAndHideLayout(9);

            if (replaceOverlay) addOverlay(1, overlay);
            else addOverlay(0, overlay);
        });
        if (!lstOverlay.isEmpty()) overlayAdapter.setData(lstOverlay);

        GridLayoutManager manager = new GridLayoutManager(this, 3);
        rcvOverlay.setLayoutManager(manager);
        rcvOverlay.setAdapter(overlayAdapter);
    }

    //background
    private void background() {
        seekAndHideLayout(7);
        if (bitmap != null) setUpDataFilterBackground();
    }

    //flip
    private void flipXBackground() {
        bitmap = UtilsAdjust.createFlippedBitmap(bitmap, true, false);
        vMain.setData(bitmap, null);
    }

    private void flipYBackground() {
        bitmap = UtilsAdjust.createFlippedBitmap(bitmap, false, true);
        vMain.setData(bitmap, null);
    }

    //opacity
    private void opacityBackground() {
        setUpLayoutEditOpacityBackground(0);
        tvCancelEditBackground.setOnClickListener(v -> setUpLayoutEditOpacityBackground(1));

        sbOpacityBackground.setProgress((int) (opacityBackground * 100));
        sbOpacityBackground.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                vMain.setAlpha(value / 100f);
                opacityBackground = value / 100f;
            }

            @Override
            public void onUp(View v) {

            }
        });
    }

    private void setUpLayoutEditOpacityBackground(int pos) {
        switch (pos) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                vEditBackground.startAnimation(animation);

                if (vEditBackground.getVisibility() == View.VISIBLE)
                    vEditBackground.setVisibility(View.GONE);

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                rlEditOpacityBackground.startAnimation(animation);
                if (rlEditOpacityBackground.getVisibility() == View.GONE) {
                    rlEditOpacityBackground.setVisibility(View.VISIBLE);
                    tvCancelEditBackground.setVisibility(View.VISIBLE);
                    tvTitleEditBackground.setText(getString(R.string.opacity));
                    sbOpacityBackground.setColorText(getResources().getColor(R.color.green));
                    sbOpacityBackground.setSizeText(com.intuit.ssp.R.dimen._10ssp);
                    sbOpacityBackground.setProgress(100);
                    sbOpacityBackground.setMax(100);
                }
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                rlEditOpacityBackground.startAnimation(animation);
                if (rlEditOpacityBackground.getVisibility() == View.VISIBLE) {
                    rlEditOpacityBackground.setVisibility(View.GONE);
                    tvCancelEditBackground.setVisibility(View.GONE);
                    tvTitleEditBackground.setText(getString(R.string.bg));
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                vEditBackground.startAnimation(animation);
                if (vEditBackground.getVisibility() == View.GONE)
                    vEditBackground.setVisibility(View.VISIBLE);
                break;
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rlEditOpacityBackground.clearAnimation();
                vEditBackground.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //filter
    private void filterBackground() {
        setUpLayoutEditFilterBackground(0);
        tvCancelEditBackground.setOnClickListener(v -> setUpLayoutEditFilterBackground(1));

        filterBlendImageAdapter = new FilterBlendImageAdapter(this, (o, pos) -> {
            FilterBlendModel filterBlend = (FilterBlendModel) o;
            filterBlend.setCheck(true);
            filterBlendImageAdapter.setCurrent(pos);
            filterBlendImageAdapter.changeNotify();

            Bitmap bm = CGENativeLibrary.cgeFilterImage_MultipleEffects(bitmap, filterBlend.getParameterFilter(), 0.8f);
            vMain.setData(bm, null);
            positionFilterBackground = pos;
        });
        if (!lstFilterBlend.isEmpty()) filterBlendImageAdapter.setData(lstFilterBlend);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvEditFilterBackground.setLayoutManager(manager);
        rcvEditFilterBackground.setAdapter(filterBlendImageAdapter);

        rcvEditFilterBackground.smoothScrollToPosition(positionFilterBackground);
        filterBlendImageAdapter.setCurrent(positionFilterBackground);
        filterBlendImageAdapter.changeNotify();
    }

    private void setUpDataFilterBackground() {
        new Thread(() -> {
            lstFilterBlend = FilterBlendImage.getDataFilter(
                    Bitmap.createScaledBitmap(bitmap, 400, 400 * bitmap.getHeight() / bitmap.getWidth(), false));

            handler.sendEmptyMessage(1);
        }).start();
    }

    private void setUpLayoutEditFilterBackground(int pos) {
        switch (pos) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                vEditBackground.startAnimation(animation);
                if (vEditBackground.getVisibility() == View.VISIBLE)
                    vEditBackground.setVisibility(View.GONE);

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                rlEditFilterBackground.startAnimation(animation);
                if (rlEditFilterBackground.getVisibility() == View.GONE) {
                    rlEditFilterBackground.setVisibility(View.VISIBLE);
                    tvCancelEditBackground.setVisibility(View.VISIBLE);
                    tvTitleEditBackground.setText(getString(R.string.filter));
                }
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                rlEditFilterBackground.startAnimation(animation);
                if (rlEditFilterBackground.getVisibility() == View.VISIBLE) {
                    rlEditFilterBackground.setVisibility(View.GONE);
                    tvCancelEditBackground.setVisibility(View.GONE);
                    tvTitleEditBackground.setText(getString(R.string.bg));
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                vEditBackground.startAnimation(animation);
                if (vEditBackground.getVisibility() == View.GONE)
                    vEditBackground.setVisibility(View.VISIBLE);
                break;
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rlEditFilterBackground.clearAnimation();
                vEditBackground.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //adjust
    private void adjustBackground() {
        setUpLayoutEditAdjustBackground(0);
        tvCancelEditBackground.setOnClickListener(v -> setUpLayoutEditAdjustBackground(1));

        setUpOptionAdjustBackground(0);

        rlBrightness.setOnClickListener(v -> setUpOptionAdjustBackground(0));
        rlContrast.setOnClickListener(v -> setUpOptionAdjustBackground(1));
        rlExposure.setOnClickListener(v -> setUpOptionAdjustBackground(2));
        rlHighLight.setOnClickListener(v -> setUpOptionAdjustBackground(3));
        rlShadows.setOnClickListener(v -> setUpOptionAdjustBackground(4));
        rlBlacks.setOnClickListener(v -> setUpOptionAdjustBackground(5));
        rlWhites.setOnClickListener(v -> setUpOptionAdjustBackground(6));
        rlSaturation.setOnClickListener(v -> setUpOptionAdjustBackground(7));
        rlHue.setOnClickListener(v -> setUpOptionAdjustBackground(8));
        rlWarmth.setOnClickListener(v -> setUpOptionAdjustBackground(9));
        rlVibrance.setOnClickListener(v -> setUpOptionAdjustBackground(10));
        rlVignette.setOnClickListener(v -> setUpOptionAdjustBackground(11));
    }

    private void adjustEditOption(int pos) {
        switch (pos) {
            case 0:
                sbAdjust.setProgress((int) vMain.getBrightness());
                break;
            case 1:
                sbAdjust.setProgress((int) vMain.getContrast());
                break;
            case 2:
                sbAdjust.setProgress((int) vMain.getExposure());
                break;
            case 3:
                sbAdjust.setProgress((int) vMain.getHighlight());
                break;
            case 4:
                sbAdjust.setProgress((int) vMain.getShadow());
                break;
            case 5:
                sbAdjust.setProgress((int) vMain.getBlack());
                break;
            case 6:
                sbAdjust.setProgress((int) vMain.getWhite());
                break;
            case 7:
                sbAdjust.setProgress((int) vMain.getSaturation());
                break;
            case 8:
                sbAdjust.setProgress((int) vMain.getHue());
                break;
            case 9:
                sbAdjust.setProgress((int) vMain.getWarmth());
                break;
            case 10:
                sbAdjust.setProgress((int) vMain.getVibrance());
                break;
            case 11:
                sbAdjust.setProgress((int) vMain.getVignette());
                break;
        }

        tvAdjustBackground.setText(String.valueOf(sbAdjust.getProgress()));

        sbAdjust.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                tvAdjustBackground.setText(String.valueOf(value));
                setValueAdjust(value, pos);
            }

            @Override
            public void onUp(View v) {

            }
        });
    }

    private void setValueAdjust(int value, int pos) {
        switch (pos) {
            case 0:
                vMain.setBrightness(value);
                break;
            case 1:
                vMain.setContrast(value);
                break;
            case 2:
                vMain.setExposure(value);
                break;
            case 3:
                vMain.setHighlight(value);
                break;
            case 4:
                vMain.setShadow(value);
                break;
            case 5:
                vMain.setBlack(value);
                break;
            case 6:
                vMain.setWhite(value);
                break;
            case 7:
                vMain.setSaturation(value);
                break;
            case 8:
                vMain.setHue(value);
                break;
            case 9:
                vMain.setWarmth(value);
                break;
            case 10:
                vMain.setVibrance(value);
                break;
            case 11:
                vMain.setVignette(value);
                break;
        }
    }

    private void setUpOptionAdjustBackground(int pos) {
        switch (pos) {
            case 0:
                ivBrightness.setImageResource(R.drawable.ic_brightness);
                tvBrightness.setTextColor(getResources().getColor(R.color.pink));

                ivContrast.setImageResource(R.drawable.ic_un_contrast);
                tvContrast.setTextColor(getResources().getColor(R.color.black));
                ivExposure.setImageResource(R.drawable.ic_un_exposure);
                tvExposure.setTextColor(getResources().getColor(R.color.black));
                ivHighLight.setImageResource(R.drawable.ic_un_hightlight);
                tvHighLight.setTextColor(getResources().getColor(R.color.black));
                ivShadows.setImageResource(R.drawable.ic_un_shadows);
                tvShadows.setTextColor(getResources().getColor(R.color.black));
                ivBlacks.setImageResource(R.drawable.ic_un_blacks);
                tvBlacks.setTextColor(getResources().getColor(R.color.black));
                ivWhites.setImageResource(R.drawable.ic_un_whites);
                tvWhites.setTextColor(getResources().getColor(R.color.black));
                ivSaturation.setImageResource(R.drawable.ic_un_saturation);
                tvSaturation.setTextColor(getResources().getColor(R.color.black));
                ivHue.setImageResource(R.drawable.ic_un_hue);
                tvHue.setTextColor(getResources().getColor(R.color.black));
                ivWarmth.setImageResource(R.drawable.ic_un_warmth);
                tvWarmth.setTextColor(getResources().getColor(R.color.black));
                ivVibrance.setImageResource(R.drawable.ic_un_vibrance);
                tvVibrance.setTextColor(getResources().getColor(R.color.black));
                ivVignette.setImageResource(R.drawable.ic_un_vignette);
                tvVignette.setTextColor(getResources().getColor(R.color.black));

                adjustEditOption(0);
                break;
            case 1:
                ivContrast.setImageResource(R.drawable.ic_contrast);
                tvContrast.setTextColor(getResources().getColor(R.color.pink));

                ivBrightness.setImageResource(R.drawable.ic_un_brightness);
                tvBrightness.setTextColor(getResources().getColor(R.color.black));
                ivExposure.setImageResource(R.drawable.ic_un_exposure);
                tvExposure.setTextColor(getResources().getColor(R.color.black));
                ivHighLight.setImageResource(R.drawable.ic_un_hightlight);
                tvHighLight.setTextColor(getResources().getColor(R.color.black));
                ivShadows.setImageResource(R.drawable.ic_un_shadows);
                tvShadows.setTextColor(getResources().getColor(R.color.black));
                ivBlacks.setImageResource(R.drawable.ic_un_blacks);
                tvBlacks.setTextColor(getResources().getColor(R.color.black));
                ivWhites.setImageResource(R.drawable.ic_un_whites);
                tvWhites.setTextColor(getResources().getColor(R.color.black));
                ivSaturation.setImageResource(R.drawable.ic_un_saturation);
                tvSaturation.setTextColor(getResources().getColor(R.color.black));
                ivHue.setImageResource(R.drawable.ic_un_hue);
                tvHue.setTextColor(getResources().getColor(R.color.black));
                ivWarmth.setImageResource(R.drawable.ic_un_warmth);
                tvWarmth.setTextColor(getResources().getColor(R.color.black));
                ivVibrance.setImageResource(R.drawable.ic_un_vibrance);
                tvVibrance.setTextColor(getResources().getColor(R.color.black));
                ivVignette.setImageResource(R.drawable.ic_un_vignette);
                tvVignette.setTextColor(getResources().getColor(R.color.black));

                adjustEditOption(1);
                break;
            case 2:
                ivExposure.setImageResource(R.drawable.ic_exposure);
                tvExposure.setTextColor(getResources().getColor(R.color.pink));

                ivContrast.setImageResource(R.drawable.ic_un_contrast);
                tvContrast.setTextColor(getResources().getColor(R.color.black));
                ivBrightness.setImageResource(R.drawable.ic_un_brightness);
                tvBrightness.setTextColor(getResources().getColor(R.color.black));
                ivHighLight.setImageResource(R.drawable.ic_un_hightlight);
                tvHighLight.setTextColor(getResources().getColor(R.color.black));
                ivShadows.setImageResource(R.drawable.ic_un_shadows);
                tvShadows.setTextColor(getResources().getColor(R.color.black));
                ivBlacks.setImageResource(R.drawable.ic_un_blacks);
                tvBlacks.setTextColor(getResources().getColor(R.color.black));
                ivWhites.setImageResource(R.drawable.ic_un_whites);
                tvWhites.setTextColor(getResources().getColor(R.color.black));
                ivSaturation.setImageResource(R.drawable.ic_un_saturation);
                tvSaturation.setTextColor(getResources().getColor(R.color.black));
                ivHue.setImageResource(R.drawable.ic_un_hue);
                tvHue.setTextColor(getResources().getColor(R.color.black));
                ivWarmth.setImageResource(R.drawable.ic_un_warmth);
                tvWarmth.setTextColor(getResources().getColor(R.color.black));
                ivVibrance.setImageResource(R.drawable.ic_un_vibrance);
                tvVibrance.setTextColor(getResources().getColor(R.color.black));
                ivVignette.setImageResource(R.drawable.ic_un_vignette);
                tvVignette.setTextColor(getResources().getColor(R.color.black));

                adjustEditOption(2);
                break;
            case 3:
                ivHighLight.setImageResource(R.drawable.ic_hightlight);
                tvHighLight.setTextColor(getResources().getColor(R.color.pink));

                ivExposure.setImageResource(R.drawable.ic_un_exposure);
                tvExposure.setTextColor(getResources().getColor(R.color.black));
                ivContrast.setImageResource(R.drawable.ic_un_contrast);
                tvContrast.setTextColor(getResources().getColor(R.color.black));
                ivBrightness.setImageResource(R.drawable.ic_un_brightness);
                tvBrightness.setTextColor(getResources().getColor(R.color.black));
                ivShadows.setImageResource(R.drawable.ic_un_shadows);
                tvShadows.setTextColor(getResources().getColor(R.color.black));
                ivBlacks.setImageResource(R.drawable.ic_un_blacks);
                tvBlacks.setTextColor(getResources().getColor(R.color.black));
                ivWhites.setImageResource(R.drawable.ic_un_whites);
                tvWhites.setTextColor(getResources().getColor(R.color.black));
                ivSaturation.setImageResource(R.drawable.ic_un_saturation);
                tvSaturation.setTextColor(getResources().getColor(R.color.black));
                ivHue.setImageResource(R.drawable.ic_un_hue);
                tvHue.setTextColor(getResources().getColor(R.color.black));
                ivWarmth.setImageResource(R.drawable.ic_un_warmth);
                tvWarmth.setTextColor(getResources().getColor(R.color.black));
                ivVibrance.setImageResource(R.drawable.ic_un_vibrance);
                tvVibrance.setTextColor(getResources().getColor(R.color.black));
                ivVignette.setImageResource(R.drawable.ic_un_vignette);
                tvVignette.setTextColor(getResources().getColor(R.color.black));

                adjustEditOption(3);
                break;
            case 4:
                ivShadows.setImageResource(R.drawable.ic_shadows);
                tvShadows.setTextColor(getResources().getColor(R.color.pink));

                ivHighLight.setImageResource(R.drawable.ic_un_hightlight);
                tvHighLight.setTextColor(getResources().getColor(R.color.black));
                ivExposure.setImageResource(R.drawable.ic_un_exposure);
                tvExposure.setTextColor(getResources().getColor(R.color.black));
                ivContrast.setImageResource(R.drawable.ic_un_contrast);
                tvContrast.setTextColor(getResources().getColor(R.color.black));
                ivBrightness.setImageResource(R.drawable.ic_un_brightness);
                tvBrightness.setTextColor(getResources().getColor(R.color.black));
                ivBlacks.setImageResource(R.drawable.ic_un_blacks);
                tvBlacks.setTextColor(getResources().getColor(R.color.black));
                ivWhites.setImageResource(R.drawable.ic_un_whites);
                tvWhites.setTextColor(getResources().getColor(R.color.black));
                ivSaturation.setImageResource(R.drawable.ic_un_saturation);
                tvSaturation.setTextColor(getResources().getColor(R.color.black));
                ivHue.setImageResource(R.drawable.ic_un_hue);
                tvHue.setTextColor(getResources().getColor(R.color.black));
                ivWarmth.setImageResource(R.drawable.ic_un_warmth);
                tvWarmth.setTextColor(getResources().getColor(R.color.black));
                ivVibrance.setImageResource(R.drawable.ic_un_vibrance);
                tvVibrance.setTextColor(getResources().getColor(R.color.black));
                ivVignette.setImageResource(R.drawable.ic_un_vignette);
                tvVignette.setTextColor(getResources().getColor(R.color.black));

                adjustEditOption(4);
                break;
            case 5:
                ivBlacks.setImageResource(R.drawable.ic_blacks);
                tvBlacks.setTextColor(getResources().getColor(R.color.pink));

                ivShadows.setImageResource(R.drawable.ic_un_shadows);
                tvShadows.setTextColor(getResources().getColor(R.color.black));
                ivHighLight.setImageResource(R.drawable.ic_un_hightlight);
                tvHighLight.setTextColor(getResources().getColor(R.color.black));
                ivExposure.setImageResource(R.drawable.ic_un_exposure);
                tvExposure.setTextColor(getResources().getColor(R.color.black));
                ivContrast.setImageResource(R.drawable.ic_un_contrast);
                tvContrast.setTextColor(getResources().getColor(R.color.black));
                ivBrightness.setImageResource(R.drawable.ic_un_brightness);
                tvBrightness.setTextColor(getResources().getColor(R.color.black));
                ivWhites.setImageResource(R.drawable.ic_un_whites);
                tvWhites.setTextColor(getResources().getColor(R.color.black));
                ivSaturation.setImageResource(R.drawable.ic_un_saturation);
                tvSaturation.setTextColor(getResources().getColor(R.color.black));
                ivHue.setImageResource(R.drawable.ic_un_hue);
                tvHue.setTextColor(getResources().getColor(R.color.black));
                ivWarmth.setImageResource(R.drawable.ic_un_warmth);
                tvWarmth.setTextColor(getResources().getColor(R.color.black));
                ivVibrance.setImageResource(R.drawable.ic_un_vibrance);
                tvVibrance.setTextColor(getResources().getColor(R.color.black));
                ivVignette.setImageResource(R.drawable.ic_un_vignette);
                tvVignette.setTextColor(getResources().getColor(R.color.black));

                adjustEditOption(5);
                break;
            case 6:
                ivWhites.setImageResource(R.drawable.ic_whites);
                tvWhites.setTextColor(getResources().getColor(R.color.pink));

                ivBlacks.setImageResource(R.drawable.ic_un_blacks);
                tvBlacks.setTextColor(getResources().getColor(R.color.black));
                ivShadows.setImageResource(R.drawable.ic_un_shadows);
                tvShadows.setTextColor(getResources().getColor(R.color.black));
                ivHighLight.setImageResource(R.drawable.ic_un_hightlight);
                tvHighLight.setTextColor(getResources().getColor(R.color.black));
                ivExposure.setImageResource(R.drawable.ic_un_exposure);
                tvExposure.setTextColor(getResources().getColor(R.color.black));
                ivContrast.setImageResource(R.drawable.ic_un_contrast);
                tvContrast.setTextColor(getResources().getColor(R.color.black));
                ivBrightness.setImageResource(R.drawable.ic_un_brightness);
                tvBrightness.setTextColor(getResources().getColor(R.color.black));
                ivSaturation.setImageResource(R.drawable.ic_un_saturation);
                tvSaturation.setTextColor(getResources().getColor(R.color.black));
                ivHue.setImageResource(R.drawable.ic_un_hue);
                tvHue.setTextColor(getResources().getColor(R.color.black));
                ivWarmth.setImageResource(R.drawable.ic_un_warmth);
                tvWarmth.setTextColor(getResources().getColor(R.color.black));
                ivVibrance.setImageResource(R.drawable.ic_un_vibrance);
                tvVibrance.setTextColor(getResources().getColor(R.color.black));
                ivVignette.setImageResource(R.drawable.ic_un_vignette);
                tvVignette.setTextColor(getResources().getColor(R.color.black));

                adjustEditOption(6);
                break;
            case 7:
                ivSaturation.setImageResource(R.drawable.ic_saturation);
                tvSaturation.setTextColor(getResources().getColor(R.color.pink));

                ivWhites.setImageResource(R.drawable.ic_un_whites);
                tvWhites.setTextColor(getResources().getColor(R.color.black));
                ivBlacks.setImageResource(R.drawable.ic_un_blacks);
                tvBlacks.setTextColor(getResources().getColor(R.color.black));
                ivShadows.setImageResource(R.drawable.ic_un_shadows);
                tvShadows.setTextColor(getResources().getColor(R.color.black));
                ivHighLight.setImageResource(R.drawable.ic_un_hightlight);
                tvHighLight.setTextColor(getResources().getColor(R.color.black));
                ivExposure.setImageResource(R.drawable.ic_un_exposure);
                tvExposure.setTextColor(getResources().getColor(R.color.black));
                ivContrast.setImageResource(R.drawable.ic_un_contrast);
                tvContrast.setTextColor(getResources().getColor(R.color.black));
                ivBrightness.setImageResource(R.drawable.ic_un_brightness);
                tvBrightness.setTextColor(getResources().getColor(R.color.black));
                ivHue.setImageResource(R.drawable.ic_un_hue);
                tvHue.setTextColor(getResources().getColor(R.color.black));
                ivWarmth.setImageResource(R.drawable.ic_un_warmth);
                tvWarmth.setTextColor(getResources().getColor(R.color.black));
                ivVibrance.setImageResource(R.drawable.ic_un_vibrance);
                tvVibrance.setTextColor(getResources().getColor(R.color.black));
                ivVignette.setImageResource(R.drawable.ic_un_vignette);
                tvVignette.setTextColor(getResources().getColor(R.color.black));

                adjustEditOption(7);
                break;
            case 8:
                ivHue.setImageResource(R.drawable.ic_hue);
                tvHue.setTextColor(getResources().getColor(R.color.pink));

                ivSaturation.setImageResource(R.drawable.ic_un_saturation);
                tvSaturation.setTextColor(getResources().getColor(R.color.black));
                ivWhites.setImageResource(R.drawable.ic_un_whites);
                tvWhites.setTextColor(getResources().getColor(R.color.black));
                ivBlacks.setImageResource(R.drawable.ic_un_blacks);
                tvBlacks.setTextColor(getResources().getColor(R.color.black));
                ivShadows.setImageResource(R.drawable.ic_un_shadows);
                tvShadows.setTextColor(getResources().getColor(R.color.black));
                ivHighLight.setImageResource(R.drawable.ic_un_hightlight);
                tvHighLight.setTextColor(getResources().getColor(R.color.black));
                ivExposure.setImageResource(R.drawable.ic_un_exposure);
                tvExposure.setTextColor(getResources().getColor(R.color.black));
                ivContrast.setImageResource(R.drawable.ic_un_contrast);
                tvContrast.setTextColor(getResources().getColor(R.color.black));
                ivBrightness.setImageResource(R.drawable.ic_un_brightness);
                tvBrightness.setTextColor(getResources().getColor(R.color.black));
                ivWarmth.setImageResource(R.drawable.ic_un_warmth);
                tvWarmth.setTextColor(getResources().getColor(R.color.black));
                ivVibrance.setImageResource(R.drawable.ic_un_vibrance);
                tvVibrance.setTextColor(getResources().getColor(R.color.black));
                ivVignette.setImageResource(R.drawable.ic_un_vignette);
                tvVignette.setTextColor(getResources().getColor(R.color.black));

                adjustEditOption(8);
                break;
            case 9:
                ivWarmth.setImageResource(R.drawable.ic_warmth);
                tvWarmth.setTextColor(getResources().getColor(R.color.pink));

                ivHue.setImageResource(R.drawable.ic_un_hue);
                tvHue.setTextColor(getResources().getColor(R.color.black));
                ivSaturation.setImageResource(R.drawable.ic_un_saturation);
                tvSaturation.setTextColor(getResources().getColor(R.color.black));
                ivWhites.setImageResource(R.drawable.ic_un_whites);
                tvWhites.setTextColor(getResources().getColor(R.color.black));
                ivBlacks.setImageResource(R.drawable.ic_un_blacks);
                tvBlacks.setTextColor(getResources().getColor(R.color.black));
                ivShadows.setImageResource(R.drawable.ic_un_shadows);
                tvShadows.setTextColor(getResources().getColor(R.color.black));
                ivHighLight.setImageResource(R.drawable.ic_un_hightlight);
                tvHighLight.setTextColor(getResources().getColor(R.color.black));
                ivExposure.setImageResource(R.drawable.ic_un_exposure);
                tvExposure.setTextColor(getResources().getColor(R.color.black));
                ivContrast.setImageResource(R.drawable.ic_un_contrast);
                tvContrast.setTextColor(getResources().getColor(R.color.black));
                ivBrightness.setImageResource(R.drawable.ic_un_brightness);
                tvBrightness.setTextColor(getResources().getColor(R.color.black));
                ivVibrance.setImageResource(R.drawable.ic_un_vibrance);
                tvVibrance.setTextColor(getResources().getColor(R.color.black));
                ivVignette.setImageResource(R.drawable.ic_un_vignette);
                tvVignette.setTextColor(getResources().getColor(R.color.black));

                adjustEditOption(9);
                break;
            case 10:
                ivVibrance.setImageResource(R.drawable.ic_vibrance);
                tvVibrance.setTextColor(getResources().getColor(R.color.pink));

                ivWarmth.setImageResource(R.drawable.ic_un_warmth);
                tvWarmth.setTextColor(getResources().getColor(R.color.black));
                ivHue.setImageResource(R.drawable.ic_un_hue);
                tvHue.setTextColor(getResources().getColor(R.color.black));
                ivSaturation.setImageResource(R.drawable.ic_un_saturation);
                tvSaturation.setTextColor(getResources().getColor(R.color.black));
                ivWhites.setImageResource(R.drawable.ic_un_whites);
                tvWhites.setTextColor(getResources().getColor(R.color.black));
                ivBlacks.setImageResource(R.drawable.ic_un_blacks);
                tvBlacks.setTextColor(getResources().getColor(R.color.black));
                ivShadows.setImageResource(R.drawable.ic_un_shadows);
                tvShadows.setTextColor(getResources().getColor(R.color.black));
                ivHighLight.setImageResource(R.drawable.ic_un_hightlight);
                tvHighLight.setTextColor(getResources().getColor(R.color.black));
                ivExposure.setImageResource(R.drawable.ic_un_exposure);
                tvExposure.setTextColor(getResources().getColor(R.color.black));
                ivContrast.setImageResource(R.drawable.ic_un_contrast);
                tvContrast.setTextColor(getResources().getColor(R.color.black));
                ivBrightness.setImageResource(R.drawable.ic_un_brightness);
                tvBrightness.setTextColor(getResources().getColor(R.color.black));
                ivVignette.setImageResource(R.drawable.ic_un_vignette);
                tvVignette.setTextColor(getResources().getColor(R.color.black));

                adjustEditOption(10);
                break;
            case 11:
                ivVignette.setImageResource(R.drawable.ic_vignette);
                tvVignette.setTextColor(getResources().getColor(R.color.pink));

                ivVibrance.setImageResource(R.drawable.ic_un_vibrance);
                tvVibrance.setTextColor(getResources().getColor(R.color.black));
                ivWarmth.setImageResource(R.drawable.ic_un_warmth);
                tvWarmth.setTextColor(getResources().getColor(R.color.black));
                ivHue.setImageResource(R.drawable.ic_un_hue);
                tvHue.setTextColor(getResources().getColor(R.color.black));
                ivSaturation.setImageResource(R.drawable.ic_un_saturation);
                tvSaturation.setTextColor(getResources().getColor(R.color.black));
                ivWhites.setImageResource(R.drawable.ic_un_whites);
                tvWhites.setTextColor(getResources().getColor(R.color.black));
                ivBlacks.setImageResource(R.drawable.ic_un_blacks);
                tvBlacks.setTextColor(getResources().getColor(R.color.black));
                ivShadows.setImageResource(R.drawable.ic_un_shadows);
                tvShadows.setTextColor(getResources().getColor(R.color.black));
                ivHighLight.setImageResource(R.drawable.ic_un_hightlight);
                tvHighLight.setTextColor(getResources().getColor(R.color.black));
                ivExposure.setImageResource(R.drawable.ic_un_exposure);
                tvExposure.setTextColor(getResources().getColor(R.color.black));
                ivContrast.setImageResource(R.drawable.ic_un_contrast);
                tvContrast.setTextColor(getResources().getColor(R.color.black));
                ivBrightness.setImageResource(R.drawable.ic_un_brightness);
                tvBrightness.setTextColor(getResources().getColor(R.color.black));

                adjustEditOption(11);
                break;
        }
    }

    private void setUpLayoutEditAdjustBackground(int pos) {
        switch (pos) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                vEditBackground.startAnimation(animation);
                if (vEditBackground.getVisibility() == View.VISIBLE)
                    vEditBackground.setVisibility(View.GONE);

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                rlAdjust.startAnimation(animation);
                if (rlAdjust.getVisibility() == View.GONE) {
                    rlAdjust.setVisibility(View.VISIBLE);
                    tvCancelEditBackground.setVisibility(View.VISIBLE);
                    tvTitleEditBackground.setText(getString(R.string.adjust));
                    tvAdjustBackground.setText(String.valueOf(0));
                    sbAdjust.setMax(100);
                }
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                rlAdjust.startAnimation(animation);
                if (rlAdjust.getVisibility() == View.VISIBLE) {
                    rlAdjust.setVisibility(View.GONE);
                    tvCancelEditBackground.setVisibility(View.GONE);
                    tvTitleEditBackground.setText(getString(R.string.bg));
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                vEditBackground.startAnimation(animation);
                if (vEditBackground.getVisibility() == View.GONE)
                    vEditBackground.setVisibility(View.VISIBLE);
                break;
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rlAdjust.clearAnimation();
                vEditBackground.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //replace
    private void replaceBackground() {
        isBackground = true;
        Intent intent = new Intent();
        intent.putExtra("pickBG", isBackground);
        intent.setComponent(new ComponentName(getPackageName(), CreateProjectActivity.class.getName()));
        startActivity(intent, ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left).toBundle());
    }

    //image
    private void image() {
        seekAndHideLayout(6);
        setUpDataFilter();
        setUpDataBlend();
    }

    //blend
    private void blendImage() {
        setUpLayoutEditBlendImage(0);
        tvCancelEditImage.setOnClickListener(v -> setUpLayoutEditBlendImage(1));

        filterBlendImageAdapter = new FilterBlendImageAdapter(this, (o, pos) -> {
            FilterBlendModel filter = (FilterBlendModel) o;
            filter.setCheck(true);
            filterBlendImageAdapter.setCurrent(pos);
            filterBlendImageAdapter.changeNotify();

            Bitmap bitmap = CGENativeLibrary.cgeFilterImage_MultipleEffects(bitmapFilterBlend, filter.getParameterFilter(), 0.8f);
            DrawableSticker sticker = new DrawableSticker(this, new BitmapDrawable(getResources(), bitmap), new ArrayList<>(), getId(), true, false, false, false);
            Bitmap bm = null;

            for (StickerModel st : lstSticker) {
                if (st.getDrawableSticker() != null && st.getDrawableSticker().getDrawable() != Utils.getDrawableTransparent(EditActivity.this))
                    if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isImage()) {
                        sticker.setAlpha(drawableSticker.getAlpha());
                        bm = st.getBitmapRoot();
                    }
            }
            stickerView.replace(sticker, true);
            drawableSticker = sticker;
            lstSticker.add(new StickerModel(null, null, null, bm, bitmap, null, sticker, null, 0, pos));

            bitmapFilterBlend = bm;
            positionBlend = pos;
        });

        if (!lstFilterBlend.isEmpty()) filterBlendImageAdapter.setData(lstFilterBlend);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvEditBlend.setLayoutManager(manager);
        rcvEditBlend.setAdapter(filterBlendImageAdapter);

        rcvEditBlend.smoothScrollToPosition(positionBlend);
        filterBlendImageAdapter.setCurrent(positionBlend);
        filterBlendImageAdapter.changeNotify();
    }

    private void setUpDataBlend() {
        new Thread(() -> {
            lstFilterBlend = FilterBlendImage.getDataBlend(
                    Bitmap.createScaledBitmap(bitmapFilterBlend, 400, 400 * bitmapFilterBlend.getHeight() / bitmapFilterBlend.getWidth(), false));

            handler.sendEmptyMessage(1);
        }).start();
    }

    private void setUpLayoutEditBlendImage(int pos) {
        switch (pos) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                vEditImage.startAnimation(animation);
                if (vEditImage.getVisibility() == View.VISIBLE) vEditImage.setVisibility(View.GONE);

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                rlEditBlend.startAnimation(animation);
                if (rlEditBlend.getVisibility() == View.GONE) {
                    rlEditBlend.setVisibility(View.VISIBLE);
                    tvCancelEditImage.setVisibility(View.VISIBLE);
                    tvTitleEditImage.setText(getString(R.string.blend));
                }
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                rlEditBlend.startAnimation(animation);
                if (rlEditBlend.getVisibility() == View.VISIBLE) {
                    rlEditBlend.setVisibility(View.GONE);
                    tvCancelEditImage.setVisibility(View.GONE);
                    tvTitleEditImage.setText(getString(R.string.image));
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                vEditImage.startAnimation(animation);
                if (vEditImage.getVisibility() == View.GONE) vEditImage.setVisibility(View.VISIBLE);
                break;
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                vEditImage.clearAnimation();
                rlEditBlend.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //opacity
    private void opacityImage() {
        setUpLayoutEditOpacityImage(0);
        tvCancelEditImage.setOnClickListener(v -> setUpLayoutEditOpacityImage(1));

        for (StickerModel st : lstSticker) {
            if (st.getDrawableSticker() != null && st.getDrawableSticker().getDrawable() != Utils.getDrawableTransparent(EditActivity.this))
                if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isImage())
                    sbOpacityImage.setProgress((int) (st.getDrawableSticker().getAlpha() * 100 / 255f));
        }
        sbOpacityImage.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                if (drawableSticker != null) drawableSticker.setAlpha((int) (value * 255 / 100f));
                Log.d("2tdp", "onMove: " + value);
                stickerView.replace(drawableSticker, true);
            }

            @Override
            public void onUp(View v) {

            }
        });
    }

    private void setUpLayoutEditOpacityImage(int pos) {
        switch (pos) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                vEditImage.startAnimation(animation);

                if (vEditImage.getVisibility() == View.VISIBLE)
                    vEditImage.setVisibility(View.GONE);

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                rlEditOpacityImage.startAnimation(animation);
                if (rlEditOpacityImage.getVisibility() == View.GONE) {
                    rlEditOpacityImage.setVisibility(View.VISIBLE);
                    tvCancelEditImage.setVisibility(View.VISIBLE);
                    tvTitleEditImage.setText(getString(R.string.opacity));
                    sbOpacityImage.setColorText(getResources().getColor(R.color.green));
                    sbOpacityImage.setSizeText(com.intuit.ssp.R.dimen._10ssp);
                    sbOpacityImage.setProgress(100);
                    sbOpacityImage.setMax(100);
                }
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                rlEditOpacityImage.startAnimation(animation);
                if (rlEditOpacityImage.getVisibility() == View.VISIBLE) {
                    rlEditOpacityImage.setVisibility(View.GONE);
                    tvCancelEditImage.setVisibility(View.GONE);
                    tvTitleEditImage.setText(getString(R.string.image));
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                vEditImage.startAnimation(animation);
                if (vEditImage.getVisibility() == View.GONE)
                    vEditImage.setVisibility(View.VISIBLE);
                break;
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rlEditOpacityImage.clearAnimation();
                vEditImage.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //shadow
    private void shadowImage() {
        setUpLayoutEditShadowImage(0);
        tvCancelEditImage.setOnClickListener(v -> setUpLayoutEditShadowImage(1));

        sbXPosImage.setProgress((int) drawableSticker.getDx());
        tvXPosImage.setText(String.valueOf((int) drawableSticker.getDx()));
        sbYPosImage.setProgress((int) drawableSticker.getDy());
        tvYPosImage.setText(String.valueOf((int) drawableSticker.getDy()));
        if ((int) drawableSticker.getRadiusBlur() == 0)
            tvBlurImage.setText(String.valueOf(0));
        else
            tvBlurImage.setText(String.valueOf((int) ((drawableSticker.getRadiusBlur() - 5) * 10f)));

        float radius = drawableSticker.getRadiusBlur();
        if (radius > 5)
            sbBlurImage.setProgress((int) ((drawableSticker.getRadiusBlur() - 5) * 10f));
        else if (radius == 0f) sbBlurImage.setProgress(0);
        else sbBlurImage.setProgress((int) ((5 - drawableSticker.getRadiusBlur()) * 10f));

        sbXPosImage.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                dx = value;
                tvXPosImage.setText(String.valueOf(value));
                drawableSticker.setShadow(radiusBlur, dx, dy, colorShadow, false);
                stickerView.invalidate();
            }

            @Override
            public void onUp(View v) {

            }
        });

        sbYPosImage.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                dy = value;
                tvYPosImage.setText(String.valueOf(value));
                drawableSticker.setShadow(radiusBlur, dx, dy, colorShadow, false);
                stickerView.invalidate();
            }

            @Override
            public void onUp(View v) {

            }
        });

        sbBlurImage.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                if (value == -50) radiusBlur = 1f;
                else if (value == 50) radiusBlur = 10f;
                else if (value == 0) radiusBlur = 5f;
                else radiusBlur = 5 + (value * 5 / 50f);
                tvBlurImage.setText(String.valueOf(value));
                drawableSticker.setShadow(radiusBlur, dx, dy, colorShadow, false);
                stickerView.invalidate();
            }

            @Override
            public void onUp(View v) {

            }
        });

        ivColorBlurImage.setOnClickListener(v -> DataColor.pickColor(this, (color) -> {
            colorShadow = color.getColorStart();
            drawableSticker.setShadow(radiusBlur, dx, dy, colorShadow, false);
            stickerView.invalidate();
        }));
    }

    private void setUpLayoutEditShadowImage(int pos) {
        switch (pos) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                vEditImage.startAnimation(animation);
                if (vEditImage.getVisibility() == View.VISIBLE) vEditImage.setVisibility(View.GONE);

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                llEditShadowImage.startAnimation(animation);
                if (llEditShadowImage.getVisibility() == View.GONE) {
                    llEditShadowImage.setVisibility(View.VISIBLE);
                    tvCancelEditImage.setVisibility(View.VISIBLE);
                    tvTitleEditImage.setText(getString(R.string.shadow));
                    tvXPosImage.setText(String.valueOf(0));
                    tvYPosImage.setText(String.valueOf(0));
                    tvBlurImage.setText(String.valueOf(0));
                    sbXPosImage.setMax(100);
                    sbYPosImage.setMax(100);
                    sbBlurImage.setMax(100);
                    radiusBlur = 5f;
                    dx = 0f;
                    dy = 0f;
                }
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                llEditShadowImage.startAnimation(animation);
                if (llEditShadowImage.getVisibility() == View.VISIBLE) {
                    llEditShadowImage.setVisibility(View.GONE);
                    tvCancelEditImage.setVisibility(View.GONE);
                    tvTitleEditImage.setText(getString(R.string.text));
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                vEditImage.startAnimation(animation);
                if (vEditImage.getVisibility() == View.GONE) vEditImage.setVisibility(View.VISIBLE);
                break;
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                vEditImage.clearAnimation();
                llEditShadowImage.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //filter
    private void filterImage() {
        setUpLayoutEditFilterImage(0);
        tvCancelEditImage.setOnClickListener(v -> setUpLayoutEditFilterImage(1));

        filterBlendImageAdapter = new FilterBlendImageAdapter(this, (o, pos) -> {
            FilterBlendModel filter = (FilterBlendModel) o;
            filter.setCheck(true);
            filterBlendImageAdapter.setCurrent(pos);
            filterBlendImageAdapter.changeNotify();

            Bitmap bitmap = CGENativeLibrary.cgeFilterImage_MultipleEffects(bitmapFilterBlend, filter.getParameterFilter(), 0.8f);
            DrawableSticker sticker = new DrawableSticker(this, new BitmapDrawable(getResources(), bitmap), new ArrayList<>(), getId(), true, false, false, false);
            Bitmap bm = null;

            for (StickerModel st : lstSticker) {
                if (st.getDrawableSticker() != null && st.getDrawableSticker().getDrawable() != Utils.getDrawableTransparent(EditActivity.this))
                    if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isImage()) {
                        sticker.setAlpha(drawableSticker.getAlpha());
                        bm = st.getBitmapRoot();
                    }
            }
            stickerView.replace(sticker, true);
            drawableSticker = sticker;
            lstSticker.add(new StickerModel(null, null, null, bm, bitmap, null, sticker, null, pos, 0));

            bitmapFilterBlend = bm;
            positionFilter = pos;
        });

        if (!lstFilterBlend.isEmpty()) filterBlendImageAdapter.setData(lstFilterBlend);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvEditFilter.setLayoutManager(manager);
        rcvEditFilter.setAdapter(filterBlendImageAdapter);

        rcvEditFilter.smoothScrollToPosition(positionFilter);
        filterBlendImageAdapter.setCurrent(positionFilter);
        filterBlendImageAdapter.changeNotify();
    }

    private void setUpDataFilter() {
        new Thread(() -> {
            lstFilterBlend = FilterBlendImage.getDataFilter(
                    Bitmap.createScaledBitmap(bitmapFilterBlend, 400, 400 * bitmapFilterBlend.getHeight() / bitmapFilterBlend.getWidth(), false));

            handler.sendEmptyMessage(1);
        }).start();
    }

    private void setUpLayoutEditFilterImage(int pos) {
        switch (pos) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                vEditImage.startAnimation(animation);
                if (vEditImage.getVisibility() == View.VISIBLE) vEditImage.setVisibility(View.GONE);

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                rlEditFilter.startAnimation(animation);
                if (rlEditFilter.getVisibility() == View.GONE) {
                    rlEditFilter.setVisibility(View.VISIBLE);
                    tvCancelEditImage.setVisibility(View.VISIBLE);
                    tvTitleEditImage.setText(getString(R.string.filter));
                }
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                rlEditFilter.startAnimation(animation);
                if (rlEditFilter.getVisibility() == View.VISIBLE) {
                    rlEditFilter.setVisibility(View.GONE);
                    tvCancelEditImage.setVisibility(View.GONE);
                    tvTitleEditImage.setText(getString(R.string.image));
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                vEditImage.startAnimation(animation);
                if (vEditImage.getVisibility() == View.GONE) vEditImage.setVisibility(View.VISIBLE);
                break;
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                vEditImage.clearAnimation();
                rlEditFilter.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //duplicate
    private void duplicateImage() {
        if (drawableSticker == null) {
            Utils.showToast(this, getString(R.string.choose_sticker_text));
            return;
        }

        for (StickerModel st : lstSticker) {
            if (st.getDrawableSticker() != null && st.getDrawableSticker().getDrawable() != Utils.getDrawableTransparent(EditActivity.this))
                if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isImage()) {
                    DrawableSticker sticker = new DrawableSticker(this, new BitmapDrawable(getResources(), st.getBitmap()), new ArrayList<>(), getId(), true, false, false, false);
                    sticker.setAlpha(st.getDrawableSticker().getAlpha());
                    stickerView.addSticker(sticker);
                    lstSticker.add(new StickerModel(null, null, null, st.getBitmapRoot(), st.getBitmap(), null, sticker, null, st.getPositionFilter(), st.getPositionBlend()));
                }
        }
    }

    //sticker
    private void emoji() {
        rlExpandEmoji.getLayoutParams().height = getResources().getDisplayMetrics().heightPixels * 90 / 100;
        seekAndHideLayout(4);
        setUpTitleEmoji();
        setUpDataEmoji();
    }

    private void opacityEmoji() {
        setUpLayoutEditOpacityEmoji(0);
        tvCancelEditEmoji.setOnClickListener(v -> setUpLayoutEditOpacityEmoji(1));

        for (StickerModel st : lstSticker) {
            if (st.getDrawableSticker() != null && st.getDrawableSticker().getDrawable() != Utils.getDrawableTransparent(EditActivity.this))
                if (st.getDrawableSticker().getId() == drawableSticker.getId())
                    sbOpacityEmoji.setProgress((int) (st.getDrawableSticker().getAlpha() * 100 / 255f));
        }
        sbOpacityEmoji.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                if (drawableSticker != null) drawableSticker.setAlpha((int) (value * 255 / 100f));
                Log.d("2tdp", "onMove: " + value);
                stickerView.replace(drawableSticker, true);
            }

            @Override
            public void onUp(View v) {

            }
        });
    }

    private void setUpLayoutEditOpacityEmoji(int pos) {
        switch (pos) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                llEditEmoji.startAnimation(animation);

                if (llEditEmoji.getVisibility() == View.VISIBLE)
                    llEditEmoji.setVisibility(View.GONE);

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                rlEditOpacityEmoji.startAnimation(animation);
                if (rlEditOpacityEmoji.getVisibility() == View.GONE) {
                    rlEditOpacityEmoji.setVisibility(View.VISIBLE);
                    tvCancelEditEmoji.setVisibility(View.VISIBLE);
                    tvTitleEmoji.setText(getString(R.string.opacity));
                    sbOpacityEmoji.setColorText(getResources().getColor(R.color.green));
                    sbOpacityEmoji.setSizeText(com.intuit.ssp.R.dimen._10ssp);
                    sbOpacityEmoji.setProgress(100);
                    sbOpacityEmoji.setMax(100);
                }
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                rlEditOpacityEmoji.startAnimation(animation);
                if (rlEditOpacityEmoji.getVisibility() == View.VISIBLE) {
                    rlEditOpacityEmoji.setVisibility(View.GONE);
                    tvCancelEditEmoji.setVisibility(View.GONE);
                    tvTitleEmoji.setText(getString(R.string.sticker));
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                llEditEmoji.startAnimation(animation);
                if (llEditEmoji.getVisibility() == View.GONE)
                    llEditEmoji.setVisibility(View.VISIBLE);
                break;
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rlEditOpacityEmoji.clearAnimation();
                llEditEmoji.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void addNewEmoji(int id, EmojiModel emoji, int pos) {
        bitmapDrawable = Utils.getBitmapFromAsset(this, emoji.getNameFolder(), emoji.getNameEmoji(), true, false);
        Drawable drawable = new BitmapDrawable(getResources(), bitmapDrawable);
        DrawableSticker sticker = new DrawableSticker(this, drawable, new ArrayList<>(), id, false, false, false, false);


        switch (pos) {
            case 0:
                stickerView.addSticker(sticker);
                lstSticker.add(new StickerModel(null, null, null, bitmapDrawable, bitmapDrawable, null, sticker, null, -1, -1));
            case 1:
                for (StickerModel st : lstSticker) {
                    if (st.getDrawableSticker() != null && st.getDrawableSticker().getDrawable() != Utils.getDrawableTransparent(EditActivity.this))
                        if (st.getDrawableSticker().getId() == drawableSticker.getId()
                                && !st.getDrawableSticker().isImage()
                                && !st.getDrawableSticker().isOverlay()
                                && !st.getDrawableSticker().isDecor()) {
                            sticker.setAlpha(st.getDrawableSticker().getAlpha());
                            st.setBitmap(bitmapDrawable);
                            st.setBitmapRoot(bitmapDrawable);
                        }
                }

                stickerView.replace(sticker, true);
                break;
        }
    }

    private void setUpDataEmoji() {
        addFragmentsAdapter = new ViewPagerAddFragmentsAdapter(getSupportFragmentManager(), getLifecycle());

        createFragment();

        vpEmoji.setAdapter(addFragmentsAdapter);
        vpEmoji.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                emojiTitleAdapter.setCurrent(position);
                rcvTitleEmoji.smoothScrollToPosition(position);
            }
        });
    }

    private void createFragment() {
        //bear
        EmojiBearFragment bearFragment = EmojiBearFragment.newInstance("bear");
        addFragmentsAdapter.addFrag(bearFragment);
        bearFragment.getEmoji((o, position) -> {
            EmojiModel emoji = (EmojiModel) o;
            if (!replaceEmoji) addNewEmoji(getId(), emoji, 0);
            else addNewEmoji(drawableSticker.getId(), emoji, 1);
            seekAndHideLayout(5);
        });

        //face
        EmojiFaceFragment faceFragment = EmojiFaceFragment.newInstance("face");
        addFragmentsAdapter.addFrag(faceFragment);
        faceFragment.getEmoji((o, position) -> {
            EmojiModel emoji = (EmojiModel) o;
            if (!replaceEmoji) addNewEmoji(getId(), emoji, 0);
            else addNewEmoji(drawableSticker.getId(), emoji, 1);
            seekAndHideLayout(5);
        });

        bitmapDrawable = null;
    }

    private void setUpTitleEmoji() {

        emojiTitleAdapter = new TitleEmojiAdapter(this, (o, pos) -> {
            emojiTitleAdapter.setCurrent(pos);
            vpEmoji.setCurrentItem(pos, true);
        });

        emojiTitleAdapter.setData(DataEmoji.getTitleEmoji(this, "title"));
        if (!isFirstEmoji) {
            emojiTitleAdapter.setCurrent(0);
            isFirstEmoji = true;
        }
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvTitleEmoji.setLayoutManager(manager);
        rcvTitleEmoji.setAdapter(emojiTitleAdapter);
    }

    //opacityText
    private void opacityText() {
        setUpLayoutEditOpacityText(0);
        tvCancelEdittext.setOnClickListener(v -> setUpLayoutEditOpacityText(1));

        if (textSticker != null)
            sbOpacityText.setProgress((int) (textSticker.getAlpha() * 100 / 255f));
        sbOpacityText.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                if (textSticker != null) textSticker.setAlpha((int) (value * 255 / 100f));
                Log.d("2tdp", "onMove: " + value);
                stickerView.replace(textSticker, true);
            }

            @Override
            public void onUp(View v) {

            }
        });
    }

    private void setUpLayoutEditOpacityText(int pos) {
        switch (pos) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                vEditText.startAnimation(animation);
                if (vEditText.getVisibility() == View.VISIBLE) vEditText.setVisibility(View.GONE);

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                rlEditOpacityText.startAnimation(animation);
                if (rlEditOpacityText.getVisibility() == View.GONE) {
                    rlEditOpacityText.setVisibility(View.VISIBLE);
                    tvCancelEdittext.setVisibility(View.VISIBLE);
                    tvTitleEditText.setText(getString(R.string.opacity));
                    sbOpacityText.setColorText(getResources().getColor(R.color.green));
                    sbOpacityText.setSizeText(com.intuit.ssp.R.dimen._10ssp);
                    sbOpacityText.setProgress(100);
                    sbOpacityText.setMax(100);
                }
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                rlEditOpacityText.startAnimation(animation);
                if (rlEditOpacityText.getVisibility() == View.VISIBLE) {
                    rlEditOpacityText.setVisibility(View.GONE);
                    tvCancelEdittext.setVisibility(View.GONE);
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
                rlEditOpacityText.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //shadow
    private void shadow() {
        setUpLayoutEditShadow(0);
        tvCancelEdittext.setOnClickListener(v -> setUpLayoutEditShadow(1));

        dx = textSticker.getDx();
        dy = textSticker.getDy();
        radiusBlur = textSticker.getRadiusBlur();
        colorShadow = textSticker.getColorShadow();

        sbXPos.setProgress((int) dx);
        tvXPos.setText(String.valueOf((int) dx));
        sbYPos.setProgress((int) dy);
        tvYPos.setText(String.valueOf((int) dy));
        if ((int) textSticker.getRadiusBlur() == 0)
            tvBlur.setText(String.valueOf(0));
        else
            tvBlur.setText(String.valueOf((int) ((radiusBlur - 5) * 10f)));

        if (radiusBlur > 5) sbBlur.setProgress((int) ((radiusBlur - 5) * 10f));
        else if (radiusBlur == 0f) {
            sbBlur.setProgress(0);
            radiusBlur = 5f;
        } else sbBlur.setProgress((int) ((5 - radiusBlur) * 10f));

        sbXPos.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                dx = value;
                tvXPos.setText(String.valueOf(value));
                textSticker.setShadow(radiusBlur, dx, dy, colorShadow);
                stickerView.invalidate();
            }

            @Override
            public void onUp(View v) {

            }
        });

        sbYPos.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                dy = value;
                tvYPos.setText(String.valueOf(value));
                textSticker.setShadow(radiusBlur, dx, dy, colorShadow);
                stickerView.invalidate();
            }

            @Override
            public void onUp(View v) {

            }
        });

        sbBlur.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                if (value == -50) radiusBlur = 1f;
                else if (value == 50) radiusBlur = 10f;
                else if (value == 0) radiusBlur = 5f;
                else radiusBlur = 5 + (value * 5 / 50f);
                tvBlur.setText(String.valueOf(value));
                textSticker.setShadow(radiusBlur, dx, dy, colorShadow);
                stickerView.invalidate();
            }

            @Override
            public void onUp(View v) {

            }
        });

        ivColorBlur.setOnClickListener(v -> DataColor.pickColor(this, (color) -> {
            colorShadow = color.getColorStart();
            textSticker.setShadow(radiusBlur, dx, dy, colorShadow);
            stickerView.invalidate();
        }));
    }

    private void setUpLayoutEditShadow(int pos) {
        switch (pos) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                vEditText.startAnimation(animation);
                if (vEditText.getVisibility() == View.VISIBLE) vEditText.setVisibility(View.GONE);

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                llEditShadow.startAnimation(animation);
                if (llEditShadow.getVisibility() == View.GONE) {
                    llEditShadow.setVisibility(View.VISIBLE);
                    tvCancelEdittext.setVisibility(View.VISIBLE);
                    tvTitleEditText.setText(getString(R.string.shadow));
                    tvXPos.setText(String.valueOf(0));
                    tvYPos.setText(String.valueOf(0));
                    tvBlur.setText(String.valueOf(0));
                    sbXPos.setMax(100);
                    sbYPos.setMax(100);
                    sbBlur.setMax(100);
                    colorShadow = 0;
                    radiusBlur = 5f;
                    dx = 0f;
                    dy = 0f;
                }
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                llEditShadow.startAnimation(animation);
                if (llEditShadow.getVisibility() == View.VISIBLE) {
                    llEditShadow.setVisibility(View.GONE);
                    tvCancelEdittext.setVisibility(View.GONE);
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
                llEditShadow.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //transform
    private void transform() {
        setUpLayoutEditTransform(0);
        tvCancelEdittext.setOnClickListener(v -> setUpLayoutEditTransform(1));

        sbShearX.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                tvShearX.setText(String.valueOf(value));
//                stickerView.shearSticker(textSticker, value / 100f, 0f);
//                textSticker.getMatrix().setSkew(value / 100f, 0f, textSticker.getWidth() / 2f, textSticker.getHeight() / 2f);
//                textSticker.setShear(textSticker, value, 0f, true);
//                stickerView.replace(textSticker, true);
//                stickerView.invalidate();
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
//                stickerView.shear(textSticker, 0f, value / 100f);
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
                    tvCancelEdittext.setVisibility(View.VISIBLE);
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
                    tvCancelEdittext.setVisibility(View.GONE);
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
                llEditTransform.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //color
    private void color() {
        setUpLayoutEditColor(0);
        tvCancelEdittext.setOnClickListener(v -> setUpLayoutEditColor(1));

        ColorAdapter colorAdapter = new ColorAdapter(this, R.layout.item_color_edit, (o, pos) -> {
            ColorModel color = (ColorModel) o;
            if (pos == 0) DataColor.pickColor(this, this::setTextColor);
            else {
                if (color.getColorStart() == color.getColorEnd()) {
                    setTextColor(color);
                } else {
                    DataColor.pickDirection(this, color, this::setTextColor);
                }
            }
        });
        if (!lstColor.isEmpty())
            colorAdapter.setData(lstColor);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvEditColor.setLayoutManager(manager);
        rcvEditColor.setAdapter(colorAdapter);
    }

    private void setTextColor(ColorModel color) {
        addColorTextModel(color);

        for (StickerModel st : lstSticker) {
            if (st.getTextSticker() != null)
                if (st.getTextSticker().getId() == textSticker.getId()) {
                    st.getTextSticker().setTextColor(color);
                    stickerView.replace(st.getTextSticker(), true);
                    break;
                }
        }

        GradientDrawable gradientDrawable = new GradientDrawable(Utils.setDirection(color.getDirec()),
                new int[]{color.getColorStart(), color.getColorEnd()});
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradientDrawable.setCornerRadius(10f);
        ivColor.setBackground(gradientDrawable);
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
                    tvCancelEdittext.setVisibility(View.VISIBLE);
                    tvTitleEditText.setText(getString(R.string.color));
                }
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                rlEditColor.startAnimation(animation);
                if (rlEditColor.getVisibility() == View.VISIBLE) {
                    rlEditColor.setVisibility(View.GONE);
                    tvCancelEdittext.setVisibility(View.GONE);
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
                rlEditColor.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void addColorTextModel(ColorModel colorModel) {
        for (StickerModel t : lstSticker) {
            if (t.getTextSticker() != null)
                if (t.getTextSticker().getId() == textSticker.getId()) {
                    t.getTextModel().setColor(colorModel);
                    break;
                }
        }
    }

    //font size
    private void fontSize() {
        setUpLayoutEditFontSize(0);
        tvCancelEdittext.setOnClickListener(v -> setUpLayoutEditFontSize(1));

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
                    tvCancelEdittext.setVisibility(View.VISIBLE);
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
                    tvCancelEdittext.setVisibility(View.GONE);
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
        TextSticker text = new TextSticker(this, getId());
        for (StickerModel t : lstSticker) {
            if (t.getTextSticker() != null)
                if (t.getTextSticker().getId() == textSticker.getId()) {
                    setTextSticker(text, new TextModel(t.getTextModel()));
                    stickerView.addSticker(text);
                    textSticker = text;

                    break;
                }
        }
    }

    //EditTextLayout
    private void setTextSticker(TextSticker sticker, TextModel textModel) {
        sticker.setText(textModel.getContent());
        sticker.resizeText();
        for (StyleFontModel f : textModel.getFontModel().getLstStyle()) {
            if (f.isSelected()) {
                textSticker.setTypeface(Utils.getTypeFace(textModel.getFontModel().getNameFont(), f.getName(), this));
                Log.d("2tdp", "font: " + textModel.getFontModel().getNameFont() + "......" + f.getName());
                break;
            }
        }
        if (textModel.getColor() != null) sticker.setTextColor(textModel.getColor());
        if (textSticker != null) {
            sticker.setAlpha(textSticker.getAlpha());
            sticker.setShadow(textSticker.getRadiusBlur(), textSticker.getDx(), textSticker.getDy(), textSticker.getColorShadow());
        }
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
        if (textModel.getColor() != null)
            lstSticker.add(new StickerModel(textModel, null, null, null, null, sticker, null, textModel.getColor(), -1, -1));
        else
            lstSticker.add(new StickerModel(textModel, null, null, null, null, sticker, null, null, -1, -1));

    }

    //edit text
    private void editText() {
        if (textSticker == null) {
            Utils.showToast(this, getResources().getString(R.string.choose_sticker_text));
            return;
        }
        Intent intent = new Intent(this, AddTextActivity.class);
        for (StickerModel t : lstSticker) {
            if (t.getTextSticker() != null)
                if (t.getTextSticker().getId() == textSticker.getId()) {
                    intent.putExtra("text", t.getTextModel());
                    break;
                }
        }
        launcherEditText.launch(intent, ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left));
        check = false;
    }

    //layer
    private void layer() {
        seekAndHideLayout(2);

        setUpDataLayer();
    }

    private void setUpDataLayer() {
        layerAdapter = new LayerAdapter(this, (o, pos) -> {
            LayerModel layer = (LayerModel) o;
            Sticker sticker = layer.getSticker();
            layerAdapter.setCurrent(pos);

            stickerView.setCurrentSticker(sticker);

            if (sticker.isLock()) setUpLayoutLockLayer(0);
            else setUpLayoutLockLayer(1);

            if (sticker.isLook()) setUpLayoutLookLayer(0);
            else setUpLayoutLookLayer(1);
        });

        layerAdapter.setData(stickerView.getListLayer());
        if (!isFirstLayer && !stickerView.getListLayer().isEmpty()) {
            layerAdapter.setCurrent(0);
            stickerView.setCurrentSticker(stickerView.getListLayer().get(0).getSticker());
        }

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvLayer.setLayoutManager(manager);
        rcvLayer.setAdapter(layerAdapter);
    }

    //lock
    private void lockLayer() {
        Sticker sticker = stickerView.getCurrentSticker();
        if (sticker != null) {
            if (sticker.isLock()) {
                sticker.setLock(false);
                setUpLayoutLockLayer(1);
            } else {
                sticker.setLock(true);
                setUpLayoutLockLayer(0);
            }
            layerAdapter.changeNotify();
        }
    }

    private void setUpLayoutLockLayer(int pos) {
        switch (pos) {
            case 0:
                ivLock.setImageResource(R.drawable.ic_lock);
                break;
            case 1:
                ivLock.setImageResource(R.drawable.ic_unlock);
                break;
        }
    }

    //look
    private void lookLayer() {
        Sticker sticker = stickerView.getCurrentSticker();
        if (sticker != null) {
            if (sticker.isLook()) {
                sticker.setLook(false);
                setUpLayoutLookLayer(1);
            } else {
                sticker.setLook(true);
                setUpLayoutLookLayer(0);
            }
            layerAdapter.changeNotify();
            stickerView.invalidate();
        }
    }

    private void setUpLayoutLookLayer(int pos) {
        switch (pos) {
            case 0:
                ivLook.setImageResource(R.drawable.ic_unlook);
                break;
            case 1:
                ivLook.setImageResource(R.drawable.ic_look);
                break;
        }
    }

    // duplicate
    private void duplicateLayer() {
        Sticker sticker = stickerView.getCurrentSticker();

        if (sticker instanceof DrawableSticker) {

            drawableSticker = (DrawableSticker) sticker;
            textSticker = null;
            if (drawableSticker.isDecor()) duplicateDecor();
            else if (drawableSticker.isImage()) duplicateImage();
            else if (drawableSticker.isTemplate()) duplicateTextTemp();
            else duplicate();
        } else if (sticker instanceof TextSticker) {
            textSticker = (TextSticker) sticker;
            drawableSticker = null;
            duplicateText();
        }
    }

    private void duplicate() {
        if (drawableSticker == null) {
            Utils.showToast(this, getString(R.string.choose_sticker_text));
            return;
        }

        for (StickerModel st : lstSticker) {
            if (st.getDrawableSticker() != null && st.getDrawableSticker().getDrawable() != Utils.getDrawableTransparent(EditActivity.this)) {
                if (st.getDrawableSticker().getId() == drawableSticker.getId()
                        && !st.getDrawableSticker().isOverlay()
                        && !st.getDrawableSticker().isImage()
                        && !st.getDrawableSticker().isDecor()
                        && !st.getDrawableSticker().isTemplate()) {

                    BitmapDrawable drawable = new BitmapDrawable(getResources(), st.getBitmap());

                    DrawableSticker sticker = new DrawableSticker(this, drawable, new ArrayList<>(), getId(), false, false, false, false);
                    sticker.setAlpha(st.getDrawableSticker().getAlpha());

                    stickerView.addSticker(sticker);
                    lstSticker.add(new StickerModel(null, null, null, st.getBitmapRoot(), st.getBitmap(), null, sticker, null, -1, -1));
                    break;
                }

                if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isOverlay()) {

                    BitmapDrawable drawable = new BitmapDrawable(getResources(), st.getBitmap());

                    DrawableSticker sticker = new DrawableSticker(this, drawable, new ArrayList<>(), getId(), false, false, true, false);
                    sticker.setAlpha(st.getDrawableSticker().getAlpha());

                    stickerView.addSticker(sticker);
                    lstSticker.add(new StickerModel(null, null, null, st.getBitmapRoot(), st.getBitmap(), null, sticker, null, -1, -1));
                    break;
                }
            }
        }
    }

    //temp
    private void replaceTemp() {
        rlExpandTemp.getLayoutParams().height = getResources().getDisplayMetrics().heightPixels * 90 / 100;
        setUpLayoutReplaceTextTemp(0);
        rlPickTextTemp.setOnClickListener(v -> setUpLayoutReplaceTextTemp(1));

        TemplateAdapter templateAdapter = new TemplateAdapter(this, R.layout.item_template_text, (o, pos) -> {
            TemplateModel template = (TemplateModel) o;

            for (StickerModel st : lstSticker) {
                if (st.getDrawableSticker() != null && st.getDrawableSticker().getDrawable() != Utils.getDrawableTransparent(EditActivity.this))
                    if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isTemplate()) {
                        DrawableSticker sticker = new DrawableSticker(this, null, template.getLstPathData(), getId(), false, false, false, true);
                        if (st.getColor() != null) sticker.setColor(st.getColor());
                        sticker.setAlpha(st.getDrawableSticker().getAlpha());

                        stickerView.replace(sticker, true);
                        stickerView.setCurrentSticker(sticker);
                        st.setTemplateModel(template);
                        lstSticker.add(st.getDrawableSticker().getId(), st);
//                        lstSticker.add(new StickerModel(null,null, template,null, null, null, sticker, null, -1, -1));
                        setUpLayoutReplaceTextTemp(1);
                        break;
                    }
            }
        });

        templateAdapter.setData(DataTemplate.getTemplate(this, ""));

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        rcvTextTemp.setLayoutManager(manager);
        rcvTextTemp.setAdapter(templateAdapter);
    }

    private void setUpLayoutReplaceTextTemp(int pos) {
        switch (pos) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                rlExpandEditTemp.startAnimation(animation);
                if (rlExpandEditTemp.getVisibility() == View.VISIBLE)
                    rlExpandEditTemp.setVisibility(View.GONE);

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                rlExpandTemp.startAnimation(animation);
                if (rlExpandTemp.getVisibility() == View.GONE)
                    rlExpandTemp.setVisibility(View.VISIBLE);
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                rlExpandTemp.startAnimation(animation);
                if (rlExpandTemp.getVisibility() == View.VISIBLE)
                    rlExpandTemp.setVisibility(View.GONE);


                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                rlExpandEditTemp.startAnimation(animation);
                if (rlExpandEditTemp.getVisibility() == View.GONE)
                    rlExpandEditTemp.setVisibility(View.VISIBLE);
                break;
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rlExpandEditTemp.clearAnimation();
                rlExpandTemp.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //shadowTemp
    private void shadowTemp() {
        setUpLayoutEditShadowTemp(0);
        tvCancelEditTemp.setOnClickListener(v -> setUpLayoutEditShadowTemp(1));

        sbXPosTemp.setProgress((int) drawableSticker.getDx());
        tvXPosTemp.setText(String.valueOf((int) drawableSticker.getDx()));
        sbYPosTemp.setProgress((int) drawableSticker.getDy());
        tvYPosTemp.setText(String.valueOf((int) drawableSticker.getDy()));
        if ((int) drawableSticker.getRadiusBlur() == 0)
            tvBlurTemp.setText(String.valueOf(0));
        else
            tvBlurTemp.setText(String.valueOf((int) ((drawableSticker.getRadiusBlur() - 5) * 10f)));

        float radius = drawableSticker.getRadiusBlur();
        if (radius > 5)
            sbBlurTemp.setProgress((int) ((drawableSticker.getRadiusBlur() - 5) * 10f));
        else if (radius == 0f) sbBlurTemp.setProgress(0);
        else sbBlurTemp.setProgress((int) ((5 - drawableSticker.getRadiusBlur()) * 10f));

        sbXPosTemp.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                dx = value;
                tvXPosTemp.setText(String.valueOf(value));
                drawableSticker.setShadow(radiusBlur, dx, dy, colorShadow, true);
                stickerView.invalidate();
            }

            @Override
            public void onUp(View v) {

            }
        });

        sbYPosTemp.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                dy = value;
                tvYPosTemp.setText(String.valueOf(value));
                drawableSticker.setShadow(radiusBlur, dx, dy, colorShadow, true);
                stickerView.invalidate();
            }

            @Override
            public void onUp(View v) {

            }
        });

        sbBlurTemp.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                if (value == -50) radiusBlur = 1f;
                else if (value == 50) radiusBlur = 10f;
                else if (value == 0) radiusBlur = 5f;
                else radiusBlur = 5 + (value * 5 / 50f);
                tvBlurTemp.setText(String.valueOf(value));
                drawableSticker.setShadow(radiusBlur, dx, dy, colorShadow, true);
                stickerView.invalidate();
            }

            @Override
            public void onUp(View v) {

            }
        });

        ivColorBlurTemp.setOnClickListener(v -> DataColor.pickColor(this, (color) -> {
            colorShadow = color.getColorStart();
            drawableSticker.setShadow(radiusBlur, dx, dy, colorShadow, true);
            stickerView.invalidate();
        }));
    }

    private void setUpLayoutEditShadowTemp(int pos) {
        switch (pos) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                vEditTemp.startAnimation(animation);
                if (vEditTemp.getVisibility() == View.VISIBLE) vEditTemp.setVisibility(View.GONE);

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                llEditShadowTemp.startAnimation(animation);
                if (llEditShadowTemp.getVisibility() == View.GONE) {
                    llEditShadowTemp.setVisibility(View.VISIBLE);
                    tvCancelEditTemp.setVisibility(View.VISIBLE);
                    tvTitleEditTemp.setText(getString(R.string.shadow));
                    tvXPosTemp.setText(String.valueOf(0));
                    tvYPosTemp.setText(String.valueOf(0));
                    tvBlurTemp.setText(String.valueOf(0));
                    sbXPosTemp.setMax(100);
                    sbYPosTemp.setMax(100);
                    sbBlurTemp.setMax(100);
                    radiusBlur = 5f;
                    dx = 0f;
                    dy = 0f;
                }
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                llEditShadowTemp.startAnimation(animation);
                if (llEditShadowTemp.getVisibility() == View.VISIBLE) {
                    llEditShadowTemp.setVisibility(View.GONE);
                    tvCancelEditTemp.setVisibility(View.GONE);
                    tvTitleEditTemp.setText(getString(R.string.temp));
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                vEditTemp.startAnimation(animation);
                if (vEditTemp.getVisibility() == View.GONE) vEditTemp.setVisibility(View.VISIBLE);
                break;
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                vEditTemp.clearAnimation();
                llEditShadowTemp.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //opacityTemp
    private void opacityTemp() {
        setUpLayoutEditOpacityTemp(0);
        tvCancelEditTemp.setOnClickListener(v -> setUpLayoutEditOpacityTemp(1));

        for (StickerModel st : lstSticker) {
            if (st.getDrawableSticker() != null)
                if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isTemplate())
                    sbOpacityTemp.setProgress((int) (st.getDrawableSticker().getAlpha() * 100 / 255f));
        }

        sbOpacityTemp.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                drawableSticker.setShadow(drawableSticker.getRadiusBlur(), drawableSticker.getDx(), drawableSticker.getDy(), drawableSticker.getColorShadow(), true);
                drawableSticker.setAlpha((int) (value * 255 / 100f));
                stickerView.invalidate();
//                stickerView.replace(drawableSticker, true);
            }

            @Override
            public void onUp(View v) {

            }
        });
    }

    private void setUpLayoutEditOpacityTemp(int pos) {
        switch (pos) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                vEditTemp.startAnimation(animation);

                if (vEditTemp.getVisibility() == View.VISIBLE)
                    vEditTemp.setVisibility(View.GONE);

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                rlEditOpacityTemp.startAnimation(animation);
                if (rlEditOpacityTemp.getVisibility() == View.GONE) {
                    rlEditOpacityTemp.setVisibility(View.VISIBLE);
                    tvCancelEditTemp.setVisibility(View.VISIBLE);
                    tvTitleEditTemp.setText(getString(R.string.opacity));
                    sbOpacityTemp.setColorText(getResources().getColor(R.color.green));
                    sbOpacityTemp.setSizeText(com.intuit.ssp.R.dimen._10ssp);
                    sbOpacityTemp.setProgress(100);
                    sbOpacityTemp.setMax(100);
                }
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                rlEditOpacityTemp.startAnimation(animation);
                if (rlEditOpacityTemp.getVisibility() == View.VISIBLE) {
                    rlEditOpacityDecor.setVisibility(View.GONE);
                    tvCancelEditTemp.setVisibility(View.GONE);
                    tvTitleEditTemp.setText(getString(R.string.temp));
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                vEditTemp.startAnimation(animation);
                if (vEditTemp.getVisibility() == View.GONE)
                    vEditTemp.setVisibility(View.VISIBLE);
                break;
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rlEditOpacityTemp.clearAnimation();
                vEditTemp.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //duplicateTemp
    private void duplicateTextTemp() {
        for (StickerModel st : lstSticker) {
            if (st.getDrawableSticker() != null)
                if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isTemplate()) {

                    DrawableSticker sticker = new DrawableSticker(this, null, st.getTemplateModel().getLstPathData(), getId(), false, false, false, true);
                    sticker.setAlpha(st.getDrawableSticker().getAlpha());
                    if (st.getColor() != null) sticker.setColor(st.getColor());

                    stickerView.addSticker(sticker);
                    stickerView.setCurrentSticker(sticker);
                    if (st.getColor() != null)
                        lstSticker.add(new StickerModel(null, null, st.getTemplateModel(), null, null, null, sticker, st.getColor(), -1, -1));
                    else
                        lstSticker.add(new StickerModel(null, null, st.getTemplateModel(), null, null, null, sticker, null, -1, -1));

                    break;
                }
        }
    }

    //colorTemp
    private void colorTemp() {
        setUpLayoutColorTextTemp(0);
        tvCancelEditTemp.setOnClickListener(v -> setUpLayoutColorTextTemp(1));

        ColorAdapter colorAdapter = new ColorAdapter(this, R.layout.item_color_edit, (o, pos) -> {
            ColorModel color = (ColorModel) o;
            if (pos == 0) DataColor.pickColor(this, this::setColorTemp);
            else {
                if (color.getColorStart() == color.getColorEnd()) {
                    setColorTemp(color);
                } else {
                    DataColor.pickDirection(this, color, this::setColorTemp);
                }
            }
        });
        if (!lstColor.isEmpty())
            colorAdapter.setData(lstColor);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvEditColorTemp.setLayoutManager(manager);
        rcvEditColorTemp.setAdapter(colorAdapter);
    }

    private void setColorTemp(ColorModel color) {
        for (StickerModel st : lstSticker) {
            if (st.getDrawableSticker() != null)
                if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isTemplate()) {

                    DrawableSticker sticker = new DrawableSticker(this, null, st.getTemplateModel().getLstPathData(), st.getDrawableSticker().getId(), false, false, false, true);
                    sticker.setColor(color);
                    drawableSticker = sticker;
                    stickerView.replace(sticker, true);
                    st.setColor(color);
                    break;
                }
        }
    }

    private void setUpLayoutColorTextTemp(int pos) {
        switch (pos) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                vEditTemp.startAnimation(animation);
                if (vEditTemp.getVisibility() == View.VISIBLE)
                    vEditTemp.setVisibility(View.GONE);

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                rlEditColorTemp.startAnimation(animation);
                if (rlEditColorTemp.getVisibility() == View.GONE) {
                    rlEditColorTemp.setVisibility(View.VISIBLE);
                    tvCancelEditTemp.setVisibility(View.VISIBLE);
                }
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                rlEditColorTemp.startAnimation(animation);
                if (rlEditColorTemp.getVisibility() == View.VISIBLE) {
                    rlEditColorTemp.setVisibility(View.GONE);
                    tvCancelEditTemp.setVisibility(View.GONE);
                }


                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                vEditTemp.startAnimation(animation);
                if (vEditTemp.getVisibility() == View.GONE)
                    vEditTemp.setVisibility(View.VISIBLE);
                break;
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                vEditTemp.clearAnimation();
                rlEditColorTemp.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //del
    private void delStick() {
        if (textSticker == null && drawableSticker == null) {
            Utils.showToast(this, getResources().getString(R.string.choose_sticker_text));
            return;
        }

        StickerModel stickerModel = null;
        for (StickerModel st : lstSticker) {
            if (st.getDrawableSticker() != null && st.getDrawableSticker().getDrawable() != Utils.getDrawableTransparent(getBaseContext()))
                if (st.getDrawableSticker().getId() == drawableSticker.getId()) stickerModel = st;
        }
        lstSticker.remove(stickerModel);
        stickerView.remove(stickerView.getCurrentSticker());
        if (!islLayer) seekAndHideLayout(0);
        else {
            layerAdapter.setData(stickerView.getListLayer());
            layerAdapter.setCurrent(0);
            stickerView.setCurrentSticker(stickerView.getListLayer().get(0).getSticker());
        }
    }

    //SetUpData
    private void getData() {
        String strUri = DataLocalManager.getOption("bitmap");
        TemplateModel template = DataLocalManager.getTemp("temp");
        if (!isBackground) vMain.setSize(0);
        else {
            vMain.setSize(sizeMain);
            vMain.setAlpha(opacityBackground);
        }
        if (!strUri.equals("")) {
            try {
                bitmap = Utils.modifyOrientation(getBaseContext(), MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(strUri)), Uri.parse(strUri));
                if (bitmap != null && isBackground) setUpDataFilterBackground();
                if (isBackground)
                    bitmap = CGENativeLibrary.cgeFilterImage_MultipleEffects(bitmap, FilterBlendImage.EFFECT_CONFIGS[positionFilterBackground], 0.8f);
                if (bitmap != null) vMain.setData(bitmap, null);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (template != null) {
            bitmap = Utils.getBitmapFromAsset(this, "template/template_background", template.getBackground(), false, false);

            DrawableSticker drawableSticker = new DrawableSticker(this, null, template.getLstPathData(), getId(), false, false, false, true);

            stickerView.addSticker(drawableSticker);
            lstSticker.add(new StickerModel(null, null, template, null, null, null, drawableSticker, null, -1, -1));
            isTemplate = true;
            if (bitmap != null) vMain.setData(bitmap, null);
        } else {
            ColorModel color = DataLocalManager.getColor("color");
            if (color != null) vMain.setData(null, color);
        }
    }

    private int getId() {
        if (lstSticker.isEmpty()) return 0;
        else if (lstSticker.get(lstSticker.size() - 1).getTextSticker() != null)
            return lstSticker.get(lstSticker.size() - 1).getTextSticker().getId() + 1;
        else
            return lstSticker.get(lstSticker.size() - 1).getDrawableSticker().getId() + 1;
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

                if (rlExpandEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEmoji.startAnimation(animation);
                    rlExpandEmoji.setVisibility(View.GONE);
                }

                if (rlExpandEditEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEditEmoji.startAnimation(animation);
                    rlExpandEditEmoji.setVisibility(View.GONE);
                }

                if (rlExpandEditImage.getVisibility() == View.VISIBLE) {
                    rlExpandEditImage.startAnimation(animation);
                    rlExpandEditImage.setVisibility(View.GONE);
                }

                if (rlExpandEditBackground.getVisibility() == View.VISIBLE) {
                    rlExpandEditBackground.startAnimation(animation);
                    rlExpandEditBackground.setVisibility(View.GONE);
                }

                if (rlExpandOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandOverlay.startAnimation(animation);
                    rlExpandOverlay.setVisibility(View.GONE);
                }

                if (rlExpandEditOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandEditOverlay.startAnimation(animation);
                    rlExpandEditOverlay.setVisibility(View.GONE);
                }

                if (rlExpandDecor.getVisibility() == View.VISIBLE) {
                    rlExpandDecor.startAnimation(animation);
                    rlExpandDecor.setVisibility(View.GONE);
                }

                if (rlExpandEditDecor.getVisibility() == View.VISIBLE) {
                    rlExpandEditDecor.startAnimation(animation);
                    rlExpandEditDecor.setVisibility(View.GONE);
                }

                if (rlExpandEditTemp.getVisibility() == View.VISIBLE) {
                    rlExpandEditTemp.startAnimation(animation);
                    rlExpandEditTemp.setVisibility(View.GONE);
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

                if (rlExpandEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEmoji.startAnimation(animation);
                    rlExpandEmoji.setVisibility(View.GONE);
                }

                if (rlExpandEditEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEditEmoji.startAnimation(animation);
                    rlExpandEditEmoji.setVisibility(View.GONE);
                }

                if (rlExpandEditImage.getVisibility() == View.VISIBLE) {
                    rlExpandEditImage.startAnimation(animation);
                    rlExpandEditImage.setVisibility(View.GONE);
                }

                if (rlExpandEditBackground.getVisibility() == View.VISIBLE) {
                    rlExpandEditBackground.startAnimation(animation);
                    rlExpandEditBackground.setVisibility(View.GONE);
                }

                if (rlExpandOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandOverlay.startAnimation(animation);
                    rlExpandOverlay.setVisibility(View.GONE);
                }

                if (rlExpandEditOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandEditOverlay.startAnimation(animation);
                    rlExpandEditOverlay.setVisibility(View.GONE);
                }

                if (rlExpandDecor.getVisibility() == View.VISIBLE) {
                    rlExpandDecor.startAnimation(animation);
                    rlExpandDecor.setVisibility(View.GONE);
                }

                if (rlExpandEditDecor.getVisibility() == View.VISIBLE) {
                    rlExpandEditDecor.startAnimation(animation);
                    rlExpandEditDecor.setVisibility(View.GONE);
                }

                if (rlExpandEditTemp.getVisibility() == View.VISIBLE) {
                    rlExpandEditTemp.startAnimation(animation);
                    rlExpandEditTemp.setVisibility(View.GONE);
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

                if (rlExpandEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEmoji.startAnimation(animation);
                    rlExpandEmoji.setVisibility(View.GONE);
                }

                if (rlExpandEditEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEditEmoji.startAnimation(animation);
                    rlExpandEditEmoji.setVisibility(View.GONE);
                }

                if (rlExpandEditImage.getVisibility() == View.VISIBLE) {
                    rlExpandEditImage.startAnimation(animation);
                    rlExpandEditImage.setVisibility(View.GONE);
                }

                if (rlExpandEditBackground.getVisibility() == View.VISIBLE) {
                    rlExpandEditBackground.startAnimation(animation);
                    rlExpandEditBackground.setVisibility(View.GONE);
                }

                if (rlExpandOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandOverlay.startAnimation(animation);
                    rlExpandOverlay.setVisibility(View.GONE);
                }

                if (rlExpandEditOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandEditOverlay.startAnimation(animation);
                    rlExpandEditOverlay.setVisibility(View.GONE);
                }

                if (rlExpandDecor.getVisibility() == View.VISIBLE) {
                    rlExpandDecor.startAnimation(animation);
                    rlExpandDecor.setVisibility(View.GONE);
                }

                if (rlExpandEditDecor.getVisibility() == View.VISIBLE) {
                    rlExpandEditDecor.startAnimation(animation);
                    rlExpandEditDecor.setVisibility(View.GONE);
                }

                if (rlExpandEditTemp.getVisibility() == View.VISIBLE) {
                    rlExpandEditTemp.startAnimation(animation);
                    rlExpandEditTemp.setVisibility(View.GONE);
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

                if (rlExpandEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEmoji.startAnimation(animation);
                    rlExpandEmoji.setVisibility(View.GONE);
                }

                if (rlExpandEditEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEditEmoji.startAnimation(animation);
                    rlExpandEditEmoji.setVisibility(View.GONE);
                }

                if (rlExpandEditImage.getVisibility() == View.VISIBLE) {
                    rlExpandEditImage.startAnimation(animation);
                    rlExpandEditImage.setVisibility(View.GONE);
                }

                if (rlExpandEditBackground.getVisibility() == View.VISIBLE) {
                    rlExpandEditBackground.startAnimation(animation);
                    rlExpandEditBackground.setVisibility(View.GONE);
                }

                if (rlExpandOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandOverlay.startAnimation(animation);
                    rlExpandOverlay.setVisibility(View.GONE);
                }

                if (rlExpandEditOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandEditOverlay.startAnimation(animation);
                    rlExpandEditOverlay.setVisibility(View.GONE);
                }

                if (rlExpandDecor.getVisibility() == View.VISIBLE) {
                    rlExpandDecor.startAnimation(animation);
                    rlExpandDecor.setVisibility(View.GONE);
                }

                if (rlExpandEditDecor.getVisibility() == View.VISIBLE) {
                    rlExpandEditDecor.startAnimation(animation);
                    rlExpandEditDecor.setVisibility(View.GONE);
                }

                if (rlExpandEditTemp.getVisibility() == View.VISIBLE) {
                    rlExpandEditTemp.startAnimation(animation);
                    rlExpandEditTemp.setVisibility(View.GONE);
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlExpandEditText.getVisibility() == View.GONE) {
                    rlExpandEditText.startAnimation(animation);
                    rlExpandEditText.setVisibility(View.VISIBLE);
                }
                break;
            case 4:
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

                if (rlExpandLayer.getVisibility() == View.VISIBLE) {
                    rlExpandLayer.startAnimation(animation);
                    rlExpandLayer.setVisibility(View.GONE);
                }

                if (rlExpandEditEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEditEmoji.startAnimation(animation);
                    rlExpandEditEmoji.setVisibility(View.GONE);
                }

                if (rlExpandEditImage.getVisibility() == View.VISIBLE) {
                    rlExpandEditImage.startAnimation(animation);
                    rlExpandEditImage.setVisibility(View.GONE);
                }

                if (rlExpandEditBackground.getVisibility() == View.VISIBLE) {
                    rlExpandEditBackground.startAnimation(animation);
                    rlExpandEditBackground.setVisibility(View.GONE);
                }

                if (rlExpandOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandOverlay.startAnimation(animation);
                    rlExpandOverlay.setVisibility(View.GONE);
                }

                if (rlExpandEditOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandEditOverlay.startAnimation(animation);
                    rlExpandEditOverlay.setVisibility(View.GONE);
                }

                if (rlExpandDecor.getVisibility() == View.VISIBLE) {
                    rlExpandDecor.startAnimation(animation);
                    rlExpandDecor.setVisibility(View.GONE);
                }

                if (rlExpandEditDecor.getVisibility() == View.VISIBLE) {
                    rlExpandEditDecor.startAnimation(animation);
                    rlExpandEditDecor.setVisibility(View.GONE);
                }

                if (rlExpandEditTemp.getVisibility() == View.VISIBLE) {
                    rlExpandEditTemp.startAnimation(animation);
                    rlExpandEditTemp.setVisibility(View.GONE);
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlExpandEmoji.getVisibility() == View.GONE) {
                    rlExpandEmoji.startAnimation(animation);
                    rlExpandEmoji.setVisibility(View.VISIBLE);
                }
                break;
            case 5:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);

                if (vOperation.getVisibility() == View.VISIBLE) {
                    vOperation.startAnimation(animation);
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

                if (rlExpandEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEmoji.startAnimation(animation);
                    rlExpandEmoji.setVisibility(View.GONE);
                }

                if (rlExpandEditImage.getVisibility() == View.VISIBLE) {
                    rlExpandEditImage.startAnimation(animation);
                    rlExpandEditImage.setVisibility(View.GONE);
                }

                if (rlExpandEditBackground.getVisibility() == View.VISIBLE) {
                    rlExpandEditBackground.startAnimation(animation);
                    rlExpandEditBackground.setVisibility(View.GONE);
                }

                if (rlExpandOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandOverlay.startAnimation(animation);
                    rlExpandOverlay.setVisibility(View.GONE);
                }

                if (rlExpandEditOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandEditOverlay.startAnimation(animation);
                    rlExpandEditOverlay.setVisibility(View.GONE);
                }

                if (rlExpandDecor.getVisibility() == View.VISIBLE) {
                    rlExpandDecor.startAnimation(animation);
                    rlExpandDecor.setVisibility(View.GONE);
                }

                if (rlExpandEditDecor.getVisibility() == View.VISIBLE) {
                    rlExpandEditDecor.startAnimation(animation);
                    rlExpandEditDecor.setVisibility(View.GONE);
                }

                if (rlExpandEditTemp.getVisibility() == View.VISIBLE) {
                    rlExpandEditTemp.startAnimation(animation);
                    rlExpandEditTemp.setVisibility(View.GONE);
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlExpandEditEmoji.getVisibility() == View.GONE) {
                    rlExpandEditEmoji.startAnimation(animation);
                    rlExpandEditEmoji.setVisibility(View.VISIBLE);
                }
                break;
            case 6:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);

                if (vOperation.getVisibility() == View.VISIBLE) {
                    vOperation.startAnimation(animation);
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

                if (rlExpandEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEmoji.startAnimation(animation);
                    rlExpandEmoji.setVisibility(View.GONE);
                }

                if (rlExpandEditEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEditEmoji.startAnimation(animation);
                    rlExpandEditEmoji.setVisibility(View.GONE);
                }

                if (rlExpandEditBackground.getVisibility() == View.VISIBLE) {
                    rlExpandEditBackground.startAnimation(animation);
                    rlExpandEditBackground.setVisibility(View.GONE);
                }

                if (rlExpandOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandOverlay.startAnimation(animation);
                    rlExpandOverlay.setVisibility(View.GONE);
                }

                if (rlExpandEditOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandEditOverlay.startAnimation(animation);
                    rlExpandEditOverlay.setVisibility(View.GONE);
                }

                if (rlExpandDecor.getVisibility() == View.VISIBLE) {
                    rlExpandDecor.startAnimation(animation);
                    rlExpandDecor.setVisibility(View.GONE);
                }

                if (rlExpandEditDecor.getVisibility() == View.VISIBLE) {
                    rlExpandEditDecor.startAnimation(animation);
                    rlExpandEditDecor.setVisibility(View.GONE);
                }

                if (rlExpandEditTemp.getVisibility() == View.VISIBLE) {
                    rlExpandEditTemp.startAnimation(animation);
                    rlExpandEditTemp.setVisibility(View.GONE);
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlExpandEditImage.getVisibility() == View.GONE) {
                    rlExpandEditImage.startAnimation(animation);
                    rlExpandEditImage.setVisibility(View.VISIBLE);
                }
                break;
            case 7:
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

                if (rlExpandLayer.getVisibility() == View.VISIBLE) {
                    rlExpandLayer.startAnimation(animation);
                    rlExpandLayer.setVisibility(View.GONE);
                }

                if (rlExpandEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEmoji.startAnimation(animation);
                    rlExpandEmoji.setVisibility(View.GONE);
                }

                if (rlExpandEditEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEditEmoji.startAnimation(animation);
                    rlExpandEditEmoji.setVisibility(View.GONE);
                }

                if (rlExpandEditImage.getVisibility() == View.VISIBLE) {
                    rlExpandEditImage.startAnimation(animation);
                    rlExpandEditImage.setVisibility(View.GONE);
                }

                if (rlExpandOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandOverlay.startAnimation(animation);
                    rlExpandOverlay.setVisibility(View.GONE);
                }

                if (rlExpandEditOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandEditOverlay.startAnimation(animation);
                    rlExpandEditOverlay.setVisibility(View.GONE);
                }

                if (rlExpandDecor.getVisibility() == View.VISIBLE) {
                    rlExpandDecor.startAnimation(animation);
                    rlExpandDecor.setVisibility(View.GONE);
                }

                if (rlExpandEditDecor.getVisibility() == View.VISIBLE) {
                    rlExpandEditDecor.startAnimation(animation);
                    rlExpandEditDecor.setVisibility(View.GONE);
                }

                if (rlExpandEditTemp.getVisibility() == View.VISIBLE) {
                    rlExpandEditTemp.startAnimation(animation);
                    rlExpandEditTemp.setVisibility(View.GONE);
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlExpandEditBackground.getVisibility() == View.GONE) {
                    rlExpandEditBackground.startAnimation(animation);
                    rlExpandEditBackground.setVisibility(View.VISIBLE);
                }
                break;
            case 8:
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

                if (rlExpandEditText.getVisibility() == View.VISIBLE) {
                    rlExpandEditText.startAnimation(animation);
                    rlExpandEditText.setVisibility(View.GONE);
                }

                if (rlExpandLayer.getVisibility() == View.VISIBLE) {
                    rlExpandLayer.startAnimation(animation);
                    rlExpandLayer.setVisibility(View.GONE);
                }

                if (rlExpandEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEmoji.startAnimation(animation);
                    rlExpandEmoji.setVisibility(View.GONE);
                }

                if (rlExpandEditEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEditEmoji.startAnimation(animation);
                    rlExpandEditEmoji.setVisibility(View.GONE);
                }

                if (rlExpandEditImage.getVisibility() == View.VISIBLE) {
                    rlExpandEditImage.startAnimation(animation);
                    rlExpandEditImage.setVisibility(View.GONE);
                }

                if (rlExpandEditBackground.getVisibility() == View.VISIBLE) {
                    rlExpandEditBackground.startAnimation(animation);
                    rlExpandEditBackground.setVisibility(View.GONE);
                }

                if (rlExpandEditOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandEditOverlay.startAnimation(animation);
                    rlExpandEditOverlay.setVisibility(View.GONE);
                }

                if (rlExpandDecor.getVisibility() == View.VISIBLE) {
                    rlExpandDecor.startAnimation(animation);
                    rlExpandDecor.setVisibility(View.GONE);
                }

                if (rlExpandEditDecor.getVisibility() == View.VISIBLE) {
                    rlExpandEditDecor.startAnimation(animation);
                    rlExpandEditDecor.setVisibility(View.GONE);
                }

                if (rlExpandEditTemp.getVisibility() == View.VISIBLE) {
                    rlExpandEditTemp.startAnimation(animation);
                    rlExpandEditTemp.setVisibility(View.GONE);
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlExpandOverlay.getVisibility() == View.GONE) {
                    rlExpandOverlay.startAnimation(animation);
                    rlExpandOverlay.setVisibility(View.VISIBLE);
                }
                break;
            case 9:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);

                if (vOperation.getVisibility() == View.VISIBLE) {
                    vOperation.startAnimation(animation);
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

                if (rlExpandEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEmoji.startAnimation(animation);
                    rlExpandEmoji.setVisibility(View.GONE);
                }

                if (rlExpandEditEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEditEmoji.startAnimation(animation);
                    rlExpandEditEmoji.setVisibility(View.GONE);
                }

                if (rlExpandEditImage.getVisibility() == View.VISIBLE) {
                    rlExpandEditImage.startAnimation(animation);
                    rlExpandEditImage.setVisibility(View.GONE);
                }

                if (rlExpandEditBackground.getVisibility() == View.VISIBLE) {
                    rlExpandEditBackground.startAnimation(animation);
                    rlExpandEditBackground.setVisibility(View.GONE);
                }

                if (rlExpandOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandOverlay.startAnimation(animation);
                    rlExpandOverlay.setVisibility(View.GONE);
                }

                if (rlExpandDecor.getVisibility() == View.VISIBLE) {
                    rlExpandDecor.startAnimation(animation);
                    rlExpandDecor.setVisibility(View.GONE);
                }

                if (rlExpandEditDecor.getVisibility() == View.VISIBLE) {
                    rlExpandEditDecor.startAnimation(animation);
                    rlExpandEditDecor.setVisibility(View.GONE);
                }

                if (rlExpandEditTemp.getVisibility() == View.VISIBLE) {
                    rlExpandEditTemp.startAnimation(animation);
                    rlExpandEditTemp.setVisibility(View.GONE);
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlExpandEditOverlay.getVisibility() == View.GONE) {
                    rlExpandEditOverlay.startAnimation(animation);
                    rlExpandEditOverlay.setVisibility(View.VISIBLE);
                }
                break;
            case 10:
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

                if (rlExpandLayer.getVisibility() == View.VISIBLE) {
                    rlExpandLayer.startAnimation(animation);
                    rlExpandLayer.setVisibility(View.GONE);
                }

                if (rlExpandEditEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEditEmoji.startAnimation(animation);
                    rlExpandEditEmoji.setVisibility(View.GONE);
                }

                if (rlExpandEditImage.getVisibility() == View.VISIBLE) {
                    rlExpandEditImage.startAnimation(animation);
                    rlExpandEditImage.setVisibility(View.GONE);
                }

                if (rlExpandEditBackground.getVisibility() == View.VISIBLE) {
                    rlExpandEditBackground.startAnimation(animation);
                    rlExpandEditBackground.setVisibility(View.GONE);
                }

                if (rlExpandOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandOverlay.startAnimation(animation);
                    rlExpandOverlay.setVisibility(View.GONE);
                }

                if (rlExpandEditOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandEditOverlay.startAnimation(animation);
                    rlExpandEditOverlay.setVisibility(View.GONE);
                }

                if (rlExpandEditDecor.getVisibility() == View.VISIBLE) {
                    rlExpandEditDecor.startAnimation(animation);
                    rlExpandEditDecor.setVisibility(View.GONE);
                }

                if (rlExpandEditTemp.getVisibility() == View.VISIBLE) {
                    rlExpandEditTemp.startAnimation(animation);
                    rlExpandEditTemp.setVisibility(View.GONE);
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlExpandDecor.getVisibility() == View.GONE) {
                    rlExpandDecor.startAnimation(animation);
                    rlExpandDecor.setVisibility(View.VISIBLE);
                }
                break;
            case 11:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);

                if (vOperation.getVisibility() == View.VISIBLE) {
                    vOperation.startAnimation(animation);
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

                if (rlExpandEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEmoji.startAnimation(animation);
                    rlExpandEmoji.setVisibility(View.GONE);
                }

                if (rlExpandEditEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEditEmoji.startAnimation(animation);
                    rlExpandEditEmoji.setVisibility(View.GONE);
                }

                if (rlExpandEditImage.getVisibility() == View.VISIBLE) {
                    rlExpandEditImage.startAnimation(animation);
                    rlExpandEditImage.setVisibility(View.GONE);
                }

                if (rlExpandEditBackground.getVisibility() == View.VISIBLE) {
                    rlExpandEditBackground.startAnimation(animation);
                    rlExpandEditBackground.setVisibility(View.GONE);
                }

                if (rlExpandOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandOverlay.startAnimation(animation);
                    rlExpandOverlay.setVisibility(View.GONE);
                }

                if (rlExpandDecor.getVisibility() == View.VISIBLE) {
                    rlExpandDecor.startAnimation(animation);
                    rlExpandDecor.setVisibility(View.GONE);
                }

                if (rlExpandEditOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandEditOverlay.startAnimation(animation);
                    rlExpandEditOverlay.setVisibility(View.GONE);
                }

                if (rlExpandEditTemp.getVisibility() == View.VISIBLE) {
                    rlExpandEditTemp.startAnimation(animation);
                    rlExpandEditTemp.setVisibility(View.GONE);
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlExpandEditDecor.getVisibility() == View.GONE) {
                    rlExpandEditDecor.startAnimation(animation);
                    rlExpandEditDecor.setVisibility(View.VISIBLE);
                }
                break;
            case 12:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);

                if (vOperation.getVisibility() == View.VISIBLE) {
                    vOperation.startAnimation(animation);
                    vOperation.setVisibility(View.GONE);
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

                if (rlExpandEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEmoji.startAnimation(animation);
                    rlExpandEmoji.setVisibility(View.GONE);
                }

                if (rlExpandEditEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEditEmoji.startAnimation(animation);
                    rlExpandEditEmoji.setVisibility(View.GONE);
                }

                if (rlExpandEditImage.getVisibility() == View.VISIBLE) {
                    rlExpandEditImage.startAnimation(animation);
                    rlExpandEditImage.setVisibility(View.GONE);
                }

                if (rlExpandEditBackground.getVisibility() == View.VISIBLE) {
                    rlExpandEditBackground.startAnimation(animation);
                    rlExpandEditBackground.setVisibility(View.GONE);
                }

                if (rlExpandOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandOverlay.startAnimation(animation);
                    rlExpandOverlay.setVisibility(View.GONE);
                }

                if (rlExpandDecor.getVisibility() == View.VISIBLE) {
                    rlExpandDecor.startAnimation(animation);
                    rlExpandDecor.setVisibility(View.GONE);
                }

                if (rlExpandEditOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandEditOverlay.startAnimation(animation);
                    rlExpandEditOverlay.setVisibility(View.GONE);
                }

                if (rlExpandEditDecor.getVisibility() == View.VISIBLE) {
                    rlExpandEditDecor.startAnimation(animation);
                    rlExpandEditDecor.setVisibility(View.GONE);
                }

                if (rlExpandEditTemp.getVisibility() == View.VISIBLE) {
                    rlExpandEditTemp.startAnimation(animation);
                    rlExpandEditTemp.setVisibility(View.GONE);
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (vSize.getVisibility() == View.GONE) {
                    vSize.startAnimation(animation);
                    tvTitle.setVisibility(View.VISIBLE);
                    vSize.setVisibility(View.VISIBLE);
                    ivTick.setVisibility(View.VISIBLE);
                }
                break;
            case 13:
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

                if (rlExpandLayer.getVisibility() == View.VISIBLE) {
                    rlExpandLayer.startAnimation(animation);
                    rlExpandLayer.setVisibility(View.GONE);
                }

                if (rlExpandEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEmoji.startAnimation(animation);
                    rlExpandEmoji.setVisibility(View.GONE);
                }

                if (rlExpandEditEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEditEmoji.startAnimation(animation);
                    rlExpandEditEmoji.setVisibility(View.GONE);
                }

                if (rlExpandEditImage.getVisibility() == View.VISIBLE) {
                    rlExpandEditImage.startAnimation(animation);
                    rlExpandEditImage.setVisibility(View.GONE);
                }

                if (rlExpandEditBackground.getVisibility() == View.VISIBLE) {
                    rlExpandEditBackground.startAnimation(animation);
                    rlExpandEditBackground.setVisibility(View.GONE);
                }

                if (rlExpandOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandOverlay.startAnimation(animation);
                    rlExpandOverlay.setVisibility(View.GONE);
                }

                if (rlExpandDecor.getVisibility() == View.VISIBLE) {
                    rlExpandDecor.startAnimation(animation);
                    rlExpandDecor.setVisibility(View.GONE);
                }

                if (rlExpandEditOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandEditOverlay.startAnimation(animation);
                    rlExpandEditOverlay.setVisibility(View.GONE);
                }

                if (rlExpandEditDecor.getVisibility() == View.VISIBLE) {
                    rlExpandEditDecor.startAnimation(animation);
                    rlExpandEditDecor.setVisibility(View.GONE);
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlExpandEditTemp.getVisibility() == View.GONE) {
                    rlExpandEditTemp.startAnimation(animation);
                    rlExpandEditTemp.setVisibility(View.VISIBLE);
                    llLayoutImport.setVisibility(View.VISIBLE);
                    llReUndo.setVisibility(View.VISIBLE);
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
                    rlExpandEmoji.clearAnimation();
                    rlExpandEditEmoji.clearAnimation();
                    vEditImage.clearAnimation();
                    vEditBackground.clearAnimation();
                    rlExpandEditBackground.clearAnimation();
                    rlExpandOverlay.clearAnimation();
                    rlExpandEditOverlay.clearAnimation();
                    rlExpandDecor.clearAnimation();
                    rlExpandEditTemp.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
    }

    private void checkSize(int pos) {
        sizeMain = pos;
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
        tvCancelEdittext = findViewById(R.id.tvCancelEditText);
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
        llEditShadow = findViewById(R.id.llEditShadow);
        tvXPos = findViewById(R.id.tvXPos);
        tvYPos = findViewById(R.id.tvYPos);
        tvBlur = findViewById(R.id.tvBlur);
        sbXPos = findViewById(R.id.sbXPos);
        sbYPos = findViewById(R.id.sbYPos);
        sbBlur = findViewById(R.id.sbBlur);
        ivColorBlur = findViewById(R.id.ivColorBlur);
        rlEditOpacityText = findViewById(R.id.rlEditOpacityText);
        sbOpacityText = findViewById(R.id.sbOpacityText);
        rlExpandEmoji = findViewById(R.id.rlExpandEmoji);
        rcvTitleEmoji = findViewById(R.id.rcvTitleEmoji);
        vpEmoji = findViewById(R.id.vpEmoji);
        rlPickEmoji = findViewById(R.id.rlPickEmoji);
        rlExpandEditEmoji = findViewById(R.id.rlExpandEditEmoji);
        rlEditEmoji = findViewById(R.id.rlEditEmoji);
        tvCancelEditEmoji = findViewById(R.id.tvCancelEditEmoji);
        tvTitleEmoji = findViewById(R.id.tvTitleEmoji);
        rlDelEmoji = findViewById(R.id.rlDelEmoji);
        rlReplaceEmoji = findViewById(R.id.rlReplaceEmoji);
        rlOpacityEmoji = findViewById(R.id.rlOpacityEmoji);
        rlFlipY = findViewById(R.id.rlFlipY);
        rlFlipX = findViewById(R.id.rlFlipX);
        llEditEmoji = findViewById(R.id.llEditEmoji);
        rlEditOpacityEmoji = findViewById(R.id.rlEditOpacityEmoji);
        sbOpacityEmoji = findViewById(R.id.sbOpacityEmoji);
        rlExpandEditImage = findViewById(R.id.rlExpandEditImage);
        rlEditImage = findViewById(R.id.rlEditImage);
        tvCancelEditImage = findViewById(R.id.tvCancelEditImage);
        tvTitleEditImage = findViewById(R.id.tvTitleEditImage);
        vEditImage = findViewById(R.id.vEditImage);
        rlDelImage = findViewById(R.id.rlDelImage);
        rlReplaceImage = findViewById(R.id.rlReplaceImage);
        rlDuplicateImage = findViewById(R.id.rlDuplicateImage);
        rlCropImage = findViewById(R.id.rlCropImage);
        rlFilterImage = findViewById(R.id.rlFilterImage);
        rlShadowImage = findViewById(R.id.rlShadowImage);
        rlOpacityImage = findViewById(R.id.rlOpacityImage);
        rlBlendImage = findViewById(R.id.rlBlendImage);
        rlEditFilter = findViewById(R.id.rlEditFilter);
        rcvEditFilter = findViewById(R.id.rcvEditFilter);
        llEditShadowImage = findViewById(R.id.llEditShadowImage);
        tvXPosImage = findViewById(R.id.tvXPosImage);
        tvYPosImage = findViewById(R.id.tvYPosImage);
        tvBlurImage = findViewById(R.id.tvBlurImage);
        sbXPosImage = findViewById(R.id.sbXPosImage);
        sbYPosImage = findViewById(R.id.sbYPosImage);
        sbBlurImage = findViewById(R.id.sbBlurImage);
        ivColorBlurImage = findViewById(R.id.ivColorBlurImage);
        rlEditOpacityImage = findViewById(R.id.rlEditOpacityImage);
        sbOpacityImage = findViewById(R.id.sbOpacityImage);
        rlEditBlend = findViewById(R.id.rlEditBlend);
        rcvEditBlend = findViewById(R.id.rcvEditBlend);
        rlExpandEditBackground = findViewById(R.id.rlExpandEditBackground);
        rlEditBackground = findViewById(R.id.rlEditBackground);
        tvCancelEditBackground = findViewById(R.id.tvCancelEditBackground);
        tvTitleEditBackground = findViewById(R.id.tvTitleEditBackground);
        vEditBackground = findViewById(R.id.vEditBackground);
        rlDelBackground = findViewById(R.id.rlDelBackground);
        rlReplaceBackground = findViewById(R.id.rlReplaceBackground);
        rlAdjustBackground = findViewById(R.id.rlAdjustBackground);
        rlFilterBackground = findViewById(R.id.rlFilterBackground);
        rlOpacityBackground = findViewById(R.id.rlOpacityBackground);
        rlFlipYBackground = findViewById(R.id.rlFlipYBackground);
        rlFlipXBackground = findViewById(R.id.rlFlipXBackground);
        rlVignette = findViewById(R.id.rlVignette);
        rlVibrance = findViewById(R.id.rlVibrance);
        rlWarmth = findViewById(R.id.rlWarmth);
        rlHue = findViewById(R.id.rlHue);
        rlSaturation = findViewById(R.id.rlSaturation);
        rlWhites = findViewById(R.id.rlWhites);
        rlBlacks = findViewById(R.id.rlBlacks);
        rlShadows = findViewById(R.id.rlShadows);
        rlHighLight = findViewById(R.id.rlHighLight);
        rlExposure = findViewById(R.id.rlExposure);
        rlContrast = findViewById(R.id.rlContrast);
        rlBrightness = findViewById(R.id.rlBrightness);
        sbAdjust = findViewById(R.id.sbAdjust);
        rlAdjust = findViewById(R.id.rlAdjust);
        tvAdjustBackground = findViewById(R.id.tvAdjustBackground);
        ivBrightness = findViewById(R.id.ivBrightness);
        tvBrightness = findViewById(R.id.tvBrightness);
        ivContrast = findViewById(R.id.ivContrast);
        tvContrast = findViewById(R.id.tvContrast);
        ivExposure = findViewById(R.id.ivExposure);
        tvExposure = findViewById(R.id.tvExposure);
        ivHighLight = findViewById(R.id.ivHighLight);
        tvHighLight = findViewById(R.id.tvHighLight);
        ivShadows = findViewById(R.id.ivShadows);
        tvShadows = findViewById(R.id.tvShadows);
        ivBlacks = findViewById(R.id.ivBlacks);
        tvBlacks = findViewById(R.id.tvBlacks);
        ivWhites = findViewById(R.id.ivWhites);
        tvWhites = findViewById(R.id.tvWhites);
        ivSaturation = findViewById(R.id.ivSaturation);
        tvSaturation = findViewById(R.id.tvSaturation);
        ivHue = findViewById(R.id.ivHue);
        tvHue = findViewById(R.id.tvHue);
        ivWarmth = findViewById(R.id.ivWarmth);
        tvWarmth = findViewById(R.id.tvWarmth);
        ivVibrance = findViewById(R.id.ivVibrance);
        tvVibrance = findViewById(R.id.tvVibrance);
        ivVignette = findViewById(R.id.ivVignette);
        tvVignette = findViewById(R.id.tvVignette);
        rcvEditFilterBackground = findViewById(R.id.rcvEditFilterBackground);
        rlEditFilterBackground = findViewById(R.id.rlEditFilterBackground);
        sbOpacityBackground = findViewById(R.id.sbOpacityBackground);
        rlEditOpacityBackground = findViewById(R.id.rlEditOpacityBackground);
        rcvOverlay = findViewById(R.id.rcvOverlay);
        rlPickOverlay = findViewById(R.id.rlPickOverlay);
        rlExpandOverlay = findViewById(R.id.rlExpandOverlay);
        tvTitleEditOverlay = findViewById(R.id.tvTitleEditOverlay);
        tvCancelEditOverlay = findViewById(R.id.tvCancelEditOverlay);
        rlFlipXOverlay = findViewById(R.id.rlFlipXOverlay);
        rlFlipYOverlay = findViewById(R.id.rlFlipYOverlay);
        rlOpacityOverlay = findViewById(R.id.rlOpacityOverlay);
        rlReplaceOverlay = findViewById(R.id.rlReplaceOverlay);
        rlDelOverlay = findViewById(R.id.rlDelOverlay);
        rlEditOpacityOverlay = findViewById(R.id.rlEditOpacityOverlay);
        rlEditOverlay = findViewById(R.id.rlEditOverlay);
        rlExpandEditOverlay = findViewById(R.id.rlExpandEditOverlay);
        llEditOverlay = findViewById(R.id.llEditOverlay);
        sbOpacityOverlay = findViewById(R.id.sbOpacityOverlay);
        rlExpandDecor = findViewById(R.id.rlExpandDecor);
        rcvTitleDecor = findViewById(R.id.rcvTitleDecor);
        vpDecor = findViewById(R.id.vpDecor);
        rlPickDecor = findViewById(R.id.rlPickDecor);
        vEditDecor = findViewById(R.id.vEditDecor);
        tvCancelEditDecor = findViewById(R.id.tvCancelEditDecor);
        rlEditDecor = findViewById(R.id.rlEditDecor);
        rlDelDecor = findViewById(R.id.rlDelDecor);
        rlReplaceDecor = findViewById(R.id.rlReplaceDecor);
        rlExpandEditDecor = findViewById(R.id.rlExpandEditDecor);
        rlDuplicateDecor = findViewById(R.id.rlDuplicateDecor);
        rlEditColorDecor = findViewById(R.id.rlEditColorDecor);
        rlColorDecor = findViewById(R.id.rlColorDecor);
        tvTitleEditDecor = findViewById(R.id.tvTitleEditDecor);
        sbOpacityDecor = findViewById(R.id.sbOpacityDecor);
        rlEditOpacityDecor = findViewById(R.id.rlEditOpacityDecor);
        rlOpacityDecor = findViewById(R.id.rlOpacityDecor);
        rlShadowDecor = findViewById(R.id.rlShadowDecor);
        llEditShadowDecor = findViewById(R.id.llEditShadowDecor);
        tvXPosDecor = findViewById(R.id.tvXPosDecor);
        sbXPosDecor = findViewById(R.id.sbXPosDecor);
        tvYPosDecor = findViewById(R.id.tvYPosDecor);
        sbYPosDecor = findViewById(R.id.sbYPosDecor);
        tvBlurDecor = findViewById(R.id.tvBlurDecor);
        sbBlurDecor = findViewById(R.id.sbBlurDecor);
        ivColorBlurDecor = findViewById(R.id.ivColorBlurDecor);
        rlFlipXDecor = findViewById(R.id.rlFlipXDecor);
        rlFlipYDecor = findViewById(R.id.rlFlipYDecor);
        rcvEditColorDecor = findViewById(R.id.rcvEditColorDecor);
        rcvLayer = findViewById(R.id.rcvLayer);
        rlExpandEditTemp = findViewById(R.id.rlExpandEditTemp);
        rlEditTemp = findViewById(R.id.rlEditTemp);
        rlEditOpacityTemp = findViewById(R.id.rlEditOpacityTemp);
        rlEditColorTemp = findViewById(R.id.rlEditColorTemp);
        rlDelTemp = findViewById(R.id.rlDelTemp);
        rlReplaceTemp = findViewById(R.id.rlReplaceTemp);
        rlDuplicateTemp = findViewById(R.id.rlDuplicateTemp);
        rlColorTemp = findViewById(R.id.rlColorTemp);
        rlBackgroundTemp = findViewById(R.id.rlBackgroundTemp);
        rlShadowTemp = findViewById(R.id.rlShadowTemp);
        rlOpacityTemp = findViewById(R.id.rlOpacityTemp);
        rlFlipXTemp = findViewById(R.id.rlFlipXTemp);
        rlFlipYTemp = findViewById(R.id.rlFlipYTemp);
        tvCancelEditTemp = findViewById(R.id.tvCancelEditTemp);
        tvTitleEditTemp = findViewById(R.id.tvTitleEditTemp);
        tvXPosTemp = findViewById(R.id.tvXPosTemp);
        tvYPosTemp = findViewById(R.id.tvYPosTemp);
        tvBlurTemp = findViewById(R.id.tvBlurTemp);
        ivColorBlurTemp = findViewById(R.id.ivColorBlurTemp);
        sbXPosTemp = findViewById(R.id.sbXPosTemp);
        sbYPosTemp = findViewById(R.id.sbYPosTemp);
        sbBlurTemp = findViewById(R.id.sbBlurTemp);
        sbOpacityTemp = findViewById(R.id.sbOpacityTemp);
        llEditShadowTemp = findViewById(R.id.llEditShadowTemp);
        vEditTemp = findViewById(R.id.vEditTemp);
        rcvEditColorTemp = findViewById(R.id.rcvEditColorTemp);
        rcvTextTemp = findViewById(R.id.rcvTextTemp);
        rlExpandTemp = findViewById(R.id.rlExpandTemp);
        rlPickTextTemp = findViewById(R.id.rlPickTextTemp);

        lstSticker = new ArrayList<>();
        lstFilterBlend = new ArrayList<>();
        lstColor = new ArrayList<>();
        lstColor.addAll(DataColor.setListColor(this));

        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.parseColor("#575757"), Color.parseColor("#575757")});
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradientDrawable.setCornerRadius(10f);
        ivColor.setBackground(gradientDrawable);
    }

    private void clearData() {
        if (bitmap != null) bitmap = null;
        if (bitmapDrawable != null) bitmapDrawable = null;
        if (bitmapFilterBlend != null) bitmapFilterBlend = null;
        if (lstFilterBlend != null) lstFilterBlend = null;
        lstFilterBlend = new ArrayList<>();
        DataLocalManager.setOption("", "bitmap");
        DataLocalManager.setTemp(null, "temp");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isBackground) getData();
    }

    @Override
    public void onBackPressed() {
        clearData();
        super.onBackPressed();
        Utils.setAnimExit(this);
    }

    @Override
    protected void onStop() {
        clearData();
        super.onStop();
    }
}