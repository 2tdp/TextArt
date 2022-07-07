package com.datnt.textart.activity.edit;

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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
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
import com.datnt.textart.activity.project.CreateProjectActivity;
import com.datnt.textart.adapter.ColorAdapter;
import com.datnt.textart.adapter.CropImageAdapter;
import com.datnt.textart.adapter.FilterBlendImageAdapter;
import com.datnt.textart.adapter.LayerAdapter;
import com.datnt.textart.adapter.OverlayAdapter;
import com.datnt.textart.adapter.TemplateAdapter;
import com.datnt.textart.adapter.TitleDecorAdapter;
import com.datnt.textart.adapter.TitleEmojiAdapter;
import com.datnt.textart.adapter.ViewPagerAddFragmentsAdapter;
import com.datnt.textart.customview.ColorView;
import com.datnt.textart.customview.CropImage;
import com.datnt.textart.customview.CropRatioView;
import com.datnt.textart.customview.CustomSeekbarRunText;
import com.datnt.textart.customview.CustomSeekbarTwoWay;
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
import com.datnt.textart.data.DataPic;
import com.datnt.textart.data.DataTemplate;
import com.datnt.textart.data.FilterBlendImage;
import com.datnt.textart.fragment.DecorFragment;
import com.datnt.textart.fragment.EmojiFragment;
import com.datnt.textart.fragment.ImageFragment;
import com.datnt.textart.model.ColorModel;
import com.datnt.textart.model.DecorModel;
import com.datnt.textart.model.EmojiModel;
import com.datnt.textart.model.FilterBlendModel;
import com.datnt.textart.model.image.ImageModel;
import com.datnt.textart.model.LayerModel;
import com.datnt.textart.model.OverlayModel;
import com.datnt.textart.model.Project;
import com.datnt.textart.model.background.AdjustModel;
import com.datnt.textart.model.background.BackgroundModel;
import com.datnt.textart.model.ShadowModel;
import com.datnt.textart.model.picture.PicModel;
import com.datnt.textart.model.textsticker.ShearTextModel;
import com.datnt.textart.model.StickerModel;
import com.datnt.textart.model.textsticker.StyleFontModel;
import com.datnt.textart.model.TemplateModel;
import com.datnt.textart.model.textsticker.TextModel;
import com.datnt.textart.sharepref.DataLocalManager;
import com.datnt.textart.utils.Utils;
import com.datnt.textart.utils.UtilsAdjust;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.wysaid.nativePort.CGENativeLibrary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class EditActivity extends AppCompatActivity {

    private ImageView ivOriginal, iv1_1, iv9_16, iv4_5, iv16_9, ivBack, ivTick, ivUndo, ivRedo,
            ivLayer, ivExport, ivLook, ivLock, ivColor, ivColorBlur, ivColorBlurImage, ivVignette, ivVibrance,
            ivWarmth, ivHue, ivSaturation, ivWhites, ivBlacks, ivShadows, ivHighLight, ivExposure,
            ivContrast, ivBrightness, ivColorBlurDecor, ivColorBlurTemp, ivTickCropImage, vMain;
    private TextView tvOriginal, tv1_1, tv9_16, tv4_5, tv16_9, tvTitle, tvFontSize, tvCancelEdittext,
            tvTitleEditText, tvShearX, tvShearY, tvStretch, tvXPos, tvYPos, tvBlur, tvCancelEditEmoji,
            tvTitleEmoji, tvCancelEditImage, tvTitleEditImage, tvXPosImage, tvYPosImage, tvBlurImage,
            tvCancelEditBackground, tvTitleEditBackground, tvAdjustBackground, tvVignette, tvVibrance,
            tvWarmth, tvHue, tvSaturation, tvWhites, tvBlacks, tvShadows, tvHighLight, tvExposure,
            tvContrast, tvBrightness, tvTitleEditOverlay, tvCancelEditOverlay, tvCancelEditDecor,
            tvTitleEditDecor, tvCancelEditTemp, tvXPosDecor, tvYPosDecor, tvBlurDecor, tvTitleEditTemp,
            tvXPosTemp, tvYPosTemp, tvBlurTemp, tvCancelCropImage, tvDiscard, tvSave, tvCancel;
    private SeekBar sbFontSize;
    private CustomSeekbarTwoWay sbStretch, sbShearX, sbShearY, sbXPos, sbYPos, sbBlur, sbXPosImage,
            sbYPosImage, sbBlurImage, sbAdjust, sbXPosDecor, sbYPosDecor, sbBlurDecor, sbXPosTemp,
            sbYPosTemp, sbBlurTemp;
    private CustomSeekbarRunText sbOpacityText, sbOpacityEmoji, sbOpacityImage, sbOpacityBackground,
            sbOpacityOverlay, sbOpacityDecor, sbOpacityTemp;
    private CropImage pathCrop;
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
            rlDelTemp, rlReplaceTemp, rlDuplicateTemp, rlColorTemp, rlBackgroundTemp, rlShadowTemp,
            rlOpacityTemp, rlFlipXTemp, rlFlipYTemp, rlExpandTemp, rlPickTextTemp, rlEditCrop;
    private LinearLayout llLayerExport, llReUndo, llEditTransform, llEditShadow, llEditEmoji, llEditShadowImage,
            llEditOverlay, llEditShadowDecor, llEditShadowTemp;
    private HorizontalScrollView vSize, vOperation, vEditText, vEditImage, vEditBackground, vEditDecor, vEditTemp;
    private CropRatioView vCrop;
    private ColorView vColor;
    private StickerView stickerView;
    private RecyclerView rcvEditColor, rcvTitleEmoji, rcvEditFilter, rcvEditBlend, rcvEditFilterBackground,
            rcvOverlay, rcvTitleDecor, rcvEditColorDecor, rcvLayer, rcvEditColorTemp, rcvTextTemp, rcvEditCrop;
    private ViewPager2 vpEmoji, vpDecor;

    private ViewPagerAddFragmentsAdapter addFragmentsAdapter;
    private TitleEmojiAdapter emojiTitleAdapter;
    private TitleDecorAdapter titleDecorAdapter;
    private FilterBlendImageAdapter filterBlendImageAdapter;
    private LayerAdapter layerAdapter;
    private Bitmap bitmapDrawable, bitmapFilterBlend, bitmapAjust;
    private BackgroundModel backgroundModel;
    private ArrayList<StickerModel> lstSticker;
    private ArrayList<FilterBlendModel> lstFilterBlend;
    private ArrayList<ColorModel> lstColor;
    private TextSticker textSticker;
    private DrawableSticker drawableSticker;
    private boolean isAddText, isFirstEmoji, replaceEmoji, isReplaceImage, isFilter, isReplaceBackground,
            replaceOverlay, replaceDecor, islLayer, isFirstLayer, isTemplate, isColor;
    private int positionFilter = 0, positionBlend = 0, colorShadow = 0, opacity = 0;
    private float radiusBlur = 5f, dx = 0f, dy = 0f;
    private float brightness = 0f, contrast = 0f, exposure = 0f, highlight = 0f, shadow = 0f, black = 0f,
            white = 0f, saturation = 0f, hue = 0f, warmth = 0f, vibrance = 0f, vignette = 0f;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        init();
    }

    private void init() {
        setUpLayout();
        if (!DataLocalManager.getCheck("isProject")) getData();
        else setUpDataProject();
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
                    else if (drawableSticker.isImage()) seekAndHideLayout(6);
                    else if (drawableSticker.isTemplate()) seekAndHideLayout(13);
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

        ivExport.setOnClickListener(v -> exportPhoto());
        ivTick.setOnClickListener(v -> clickTick());

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
            if (stickerView.getStickerCount() > 0) {
                layer();
                isFirstLayer = false;
            }
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
            isAddText = true;
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
            pickImage();
            isReplaceImage = false;
        });
        rlDelImage.setOnClickListener(v -> delStick());
        rlReplaceImage.setOnClickListener(v -> {
            pickImage();
            isReplaceImage = true;
        });
        rlDuplicateImage.setOnClickListener(v -> duplicateImage());
        rlCropImage.setOnClickListener(v -> cropImage());
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
        ivTickCropImage.setOnClickListener(v -> afterCropImage());

        //background
        rlBackground.setOnClickListener(v -> background());
        rlDelBackground.setOnClickListener(v -> {
            if (vMain.getVisibility() == View.VISIBLE) vMain.setVisibility(View.GONE);
            if (vCrop.getVisibility() == View.VISIBLE) vCrop.setVisibility(View.GONE);
            if (vColor.getVisibility() == View.GONE) vColor.setVisibility(View.VISIBLE);

            vColor.setAlpha(255);
            vColor.setData(new ColorModel(Color.WHITE, Color.WHITE, 0, false));
            seekAndHideLayout(0);
        });
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
        rlCrop.setOnClickListener(v -> {
            seekAndHideLayout(12);
            stickerView.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
            stickerView.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
        });
        ivOriginal.setOnClickListener(v -> checkSize(0));
        iv1_1.setOnClickListener(v -> checkSize(1));
        iv9_16.setOnClickListener(v -> checkSize(2));
        iv4_5.setOnClickListener(v -> checkSize(3));
        iv16_9.setOnClickListener(v -> checkSize(4));
    }

    private void saveProject() {
        ArrayList<Project> lstProject = DataLocalManager.getListProject("lstProject");
        ArrayList<TextModel> lstTextModel = new ArrayList<>();
        ArrayList<EmojiModel> lstEmojiModel = new ArrayList<>();
        ArrayList<ImageModel> lstImageModel = new ArrayList<>();
        ArrayList<OverlayModel> lstOverlayModel = new ArrayList<>();
        ArrayList<DecorModel> lstDecorModel = new ArrayList<>();
        ArrayList<TemplateModel> lstTemplateModel = new ArrayList<>();
        ArrayList<Matrix> lstMatrix = new ArrayList<>();

        Bitmap thumb = stickerView.getThumb();

        for (Sticker sticker : stickerView.getListStickers()) {

            if (sticker instanceof TextSticker) {
                TextSticker textSticker = (TextSticker) sticker;
                for (StickerModel st : lstSticker) {
                    if (st.getTextSticker() != null)
                        if (st.getTextSticker().getId() == textSticker.getId()) {
                            lstTextModel.add(st.getTextModel());
                            lstMatrix.add(textSticker.getMatrix());
                            Log.d("2tdp", "saveProject: Text");
                            break;
                        }
                }
            }

            if (sticker instanceof DrawableSticker) {
                DrawableSticker drawableSticker = (DrawableSticker) sticker;
                for (StickerModel st : lstSticker) {
                    if (st.getDrawableSticker() != null) {
                        if (st.getDrawableSticker().getId() == drawableSticker.getId() && drawableSticker.isImage()) {
                            lstImageModel.add(st.getImageModel());
                            lstMatrix.add(drawableSticker.getMatrix());
                            Log.d("2tdp", "saveProject: Image");
                            break;
                        } else if (st.getDrawableSticker().getId() == drawableSticker.getId() && drawableSticker.isOverlay()) {
                            lstOverlayModel.add(st.getOverlayModel());
                            lstMatrix.add(drawableSticker.getMatrix());
                            Log.d("2tdp", "saveProject: OverLay");
                            break;
                        } else if (st.getDrawableSticker().getId() == drawableSticker.getId() && drawableSticker.isDecor()) {
                            lstDecorModel.add(st.getDecorModel());
                            lstMatrix.add(drawableSticker.getMatrix());
                            Log.d("2tdp", "saveProject: Decor");
                            break;
                        } else if (st.getDrawableSticker().getId() == drawableSticker.getId() && drawableSticker.isTemplate()) {
                            lstTemplateModel.add(st.getTemplateModel());
                            lstMatrix.add(drawableSticker.getMatrix());
                            Log.d("2tdp", "saveProject: Template");
                            break;
                        } else {
                            lstEmojiModel.add(st.getEmojiModel());
                            lstMatrix.add(drawableSticker.getMatrix());
                            Log.d("2tdp", "saveProject: Emoji");
                            break;
                        }

                    }

                }
            }
        }

        lstProject.add(new Project(backgroundModel, lstTextModel, lstEmojiModel, lstImageModel, lstOverlayModel, lstDecorModel, lstTemplateModel, lstMatrix, thumb));
        DataLocalManager.setListProject(lstProject, "lstProject");
    }

    private void clickTick() {
        if (isTemplate) seekAndHideLayout(13);
        else seekAndHideLayout(0);

        int wMain = getResources().getDisplayMetrics().widthPixels;
        int hMain = (int) (getResources().getDisplayMetrics().heightPixels - getResources().getDimension(com.intuit.sdp.R.dimen._215sdp));

        float scaleScreen = (float) wMain / hMain;

        if (!isColor) {
            Bitmap bitmap;
            Bitmap bm = vCrop.getCroppedImage();
            if ((float) bm.getWidth() / bm.getHeight() > scaleScreen)
                bitmap = Bitmap.createScaledBitmap(bm, wMain, wMain * bm.getHeight() / bm.getWidth(), false);
            else
                bitmap = Bitmap.createScaledBitmap(bm, hMain * bm.getWidth() / bm.getHeight(), hMain, false);
            backgroundModel.setBackground(bitmap);
            stickerView.getLayoutParams().width = bitmap.getWidth();
            vMain.getLayoutParams().width = bitmap.getWidth();

            stickerView.getLayoutParams().height = bitmap.getHeight();
            vMain.getLayoutParams().height = bitmap.getHeight();

            vCrop.setVisibility(View.GONE);
            vMain.setImageBitmap(bitmap);
            vMain.setVisibility(View.VISIBLE);
            vMain.setAlpha(backgroundModel.getOpacity());
        } else {
            stickerView.getLayoutParams().height = (int) vColor.getH();
            stickerView.getLayoutParams().width = (int) vColor.getW();
        }
    }

    private void exportPhoto() {
        @SuppressLint("InflateParams")
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_export, null);
        RelativeLayout rlExport = v.findViewById(R.id.rlExport);
        LinearLayout llSavePhoto = v.findViewById(R.id.llSavePhoto);
        LinearLayout llRemove = v.findViewById(R.id.llRemovePhoto);

        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.SheetDialog);
        dialog.setContentView(v);
        dialog.setCancelable(true);
        dialog.create();
        dialog.show();

        rlExport.setOnClickListener(vCancel -> dialog.cancel());
        llSavePhoto.setOnClickListener(vSave -> {
            stickerView.saveImage(this);
            dialog.cancel();
        });
        llRemove.setOnClickListener(vRemove -> {
            dialog.cancel();
            Utils.showToast(this, "Remove");
        });
    }

    Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what) {
                case 0:
                    image();
                    if (!isReplaceImage) {
                        Drawable drawable = new BitmapDrawable(getResources(), bitmapFilterBlend);
                        DrawableSticker sticker = new DrawableSticker(getBaseContext(), drawable, new ArrayList<>(), getId(), true, false, false, false);

                        stickerView.addSticker(sticker);
                        lstSticker.add(new StickerModel(null, null, null, null,
                                new ImageModel(bitmapFilterBlend, bitmapFilterBlend, null, 255, 0, 0),
                                null, backgroundModel, null, sticker));
                    } else {
                        if (stickerView.getCurrentSticker() == null) {
                            Utils.showToast(EditActivity.this, getResources().getString(R.string.choose_sticker_text));
                        } else
                            for (StickerModel st : lstSticker) {
                                if (st.getDrawableSticker() != null)
                                    if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isImage()) {
                                        st.getImageModel().setImageRoot(bitmapFilterBlend);

                                        if (st.getImageModel().getPosFilter() != 0) {
                                            bitmapFilterBlend = CGENativeLibrary.cgeFilterImage_MultipleEffects(bitmapFilterBlend,
                                                    FilterBlendImage.EFFECT_CONFIGS[st.getImageModel().getPosFilter()], 0.8f);
                                        }

                                        if (st.getImageModel().getPosBlend() != 0) {
                                            bitmapFilterBlend = CGENativeLibrary.cgeFilterImage_MultipleEffects(bitmapFilterBlend,
                                                    FilterBlendImage.EFFECT_CONFIGS_BLEND[st.getImageModel().getPosBlend()], 0.8f);
                                        }

                                        st.getImageModel().setImage(bitmapFilterBlend);
                                        Drawable drawable = new BitmapDrawable(getResources(), bitmapFilterBlend);

                                        drawable.setAlpha(st.getImageModel().getOpacity());
                                        stickerView.getCurrentSticker().setDrawable(drawable);
                                        stickerView.invalidate();
                                        break;
                                    }
                            }
                    }
                    break;
                case 1:
                    if (filterBlendImageAdapter != null) {
                        filterBlendImageAdapter.setData(lstFilterBlend);
                        if (isFilter) {
                            rcvEditFilter.smoothScrollToPosition(positionFilter);
                            filterBlendImageAdapter.setCurrent(positionFilter);
                        } else if (isReplaceBackground) {
                            rcvEditFilterBackground.smoothScrollToPosition(backgroundModel.getPositionFilterBackground());
                            filterBlendImageAdapter.setCurrent(backgroundModel.getPositionFilterBackground());
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
                if (isAddText) {
                    textSticker = new TextSticker(this, getId());
                    stickerView.addSticker(textSticker);

                    setTextSticker(textSticker, textModel);

                    lstSticker.add(new StickerModel(textModel, null, null, null, null,
                            null, backgroundModel, textSticker, null));
                } else {
                    for (StickerModel st : lstSticker) {
                        if (st.getTextSticker() != null)
                            if (st.getTextSticker().getId() == textSticker.getId()) {
                                setTextSticker(textSticker, textModel);

                                st.getTextModel().setContent(textModel.getContent());
                                st.getTextModel().setFontModel(textModel.getFontModel());
                                st.getTextModel().setTypeAlign(textModel.getTypeAlign());
                                break;
                            }
                    }
                }
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

        String[] nameDecor = new String[]{"box", "draw", "frame", "shape"};
        for (String s : nameDecor) {
            DecorFragment decorFragment = DecorFragment.newInstance(s, (o, pos) -> {
                DecorModel decor = (DecorModel) o;
                if (replaceDecor) addNewDecor(drawableSticker.getId(), decor, 1);
                else addNewDecor(getId(), decor, 0);
                seekAndHideLayout(11);
            });
            addFragmentsAdapter.addFrag(decorFragment);
        }

        bitmapDrawable = null;

        vpDecor.setAdapter(addFragmentsAdapter);
        vpDecor.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                titleDecorAdapter.setCurrent(position);
                rcvTitleDecor.smoothScrollToPosition(position);
            }
        });
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
            public void onUp(View v, int value) {

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
            public void onUp(View v, int value) {

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
            public void onUp(View v, int value) {

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
            public void onUp(View v, int value) {

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
                    st.getDecorModel().setColorModel(color);
                    stickerView.replace(st.getDrawableSticker(), true);
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
                    if (st.getDecorModel().getColorModel() != null)
                        sticker.setColor(st.getDecorModel().getColorModel());

                    sticker.setShadow(drawableSticker.getRadiusBlur(), drawableSticker.getDx(), drawableSticker.getDy(), drawableSticker.getColorShadow(), true);

                    stickerView.addSticker(sticker);
                    lstSticker.add(new StickerModel(null, null, null, new DecorModel(st.getDecorModel()), null, null, backgroundModel, null, sticker));
                    break;
                }
        }
    }

    private void addNewDecor(int id, DecorModel decor, int pos) {
        DrawableSticker sticker = new DrawableSticker(this, null, decor.getLstPathData(), id, false, false, true, false);

        switch (pos) {
            case 0:
                stickerView.addSticker(sticker);
                lstSticker.add(new StickerModel(null, null, null, decor, null, null, backgroundModel, null, sticker));
                break;
            case 1:
                for (StickerModel st : lstSticker) {
                    if (st.getDrawableSticker() != null)
                        if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isDecor()) {
                            drawableSticker.setPathData(decor.getLstPathData());
                            st.getDecorModel().setLstPathData(decor.getLstPathData());
                            stickerView.invalidate();
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
            if (st.getDrawableSticker() != null)
                if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isOverlay()) {
                    opacity = (int) (st.getDrawableSticker().getAlpha() * 100 / 255f);
                    sbOpacityOverlay.setProgress(opacity);
                    break;
                }
        }
        sbOpacityOverlay.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                opacity = (int) (value * 255 / 100f);
                if (drawableSticker != null) drawableSticker.setAlpha(opacity);
                stickerView.replace(drawableSticker, true);
            }

            @Override
            public void onUp(View v, int value) {

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

                addOpacityOverlayModel(opacity);
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

    private void addOpacityOverlayModel(int opacity) {
        for (StickerModel t : lstSticker) {
            if (t.getDrawableSticker() != null)
                if (t.getDrawableSticker().getId() == drawableSticker.getId() && t.getDrawableSticker().isOverlay()) {
                    t.getOverlayModel().setOpacity(opacity);
                    break;
                }
        }
    }

    private void addOverlay(int pos, OverlayModel overlay) {

        Bitmap bitmap = Utils.getBitmapFromAsset(this, overlay.getNameFolder(), overlay.getNameOverlay(), false, false);
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);

        switch (pos) {
            case 0:
                DrawableSticker sticker = new DrawableSticker(this, drawable, new ArrayList<>(), getId(), false, true, false, false);
                stickerView.addSticker(sticker);

                lstSticker.add(new StickerModel(null, null, overlay, null, null, null, backgroundModel, null, sticker));
                break;
            case 1:
                if (stickerView.getCurrentSticker() == null)
                    Utils.showToast(this, getResources().getString(R.string.choose_sticker_text));
                else
                    for (StickerModel st : lstSticker) {
                        if (st.getDrawableSticker() != null)
                            if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isOverlay()) {
                                drawable.setAlpha(st.getOverlayModel().getOpacity());
                                overlay.setOpacity(st.getOverlayModel().getOpacity());
                                st.setOverlayModel(overlay);
                                stickerView.getCurrentSticker().setDrawable(drawable);
                                stickerView.invalidate();
                                break;
                            }
                    }
        }
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
        if (backgroundModel.getBackground() != null) setUpDataFilterBackground();
    }

    //flip
    private void flipXBackground() {
        if (!isColor) {
            backgroundModel.setBackground(UtilsAdjust.createFlippedBitmap(backgroundModel.getBackground(), true, false));
            vMain.setImageBitmap(backgroundModel.getBackground());
        }
    }

    private void flipYBackground() {
        if (!isColor) {
            backgroundModel.setBackground(UtilsAdjust.createFlippedBitmap(backgroundModel.getBackground(), false, true));
            vMain.setImageBitmap(backgroundModel.getBackground());
        }
    }

    //opacity
    private void opacityBackground() {
        setUpLayoutEditOpacityBackground(0);
        tvCancelEditBackground.setOnClickListener(v -> setUpLayoutEditOpacityBackground(1));

        sbOpacityBackground.setProgress((int) (backgroundModel.getOpacity() * 100));

        sbOpacityBackground.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                if (!isColor)
                    vMain.setAlpha(value / 100f);
                else
                    vColor.setAlpha(value / 100f);

                backgroundModel.setOpacity(value / 100f);
            }

            @Override
            public void onUp(View v, int value) {

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

            Bitmap bm = CGENativeLibrary.cgeFilterImage_MultipleEffects(backgroundModel.getBackground(), filterBlend.getParameterFilter(), 0.8f);
            vMain.setImageBitmap(bm);
            backgroundModel.setPositionFilterBackground(pos);
        });
        if (!lstFilterBlend.isEmpty()) filterBlendImageAdapter.setData(lstFilterBlend);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvEditFilterBackground.setLayoutManager(manager);
        rcvEditFilterBackground.setAdapter(filterBlendImageAdapter);

        rcvEditFilterBackground.smoothScrollToPosition(backgroundModel.getPositionFilterBackground());
        filterBlendImageAdapter.setCurrent(backgroundModel.getPositionFilterBackground());
        filterBlendImageAdapter.changeNotify();
    }

    private void setUpDataFilterBackground() {
        new Thread(() -> {
            Bitmap bitmap = backgroundModel.getBackground();
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
                sbAdjust.setProgress((int) brightness / 2);
                break;
            case 1:
                sbAdjust.setProgress((int) contrast);
                break;
            case 2:
                sbAdjust.setProgress((int) exposure / 4);
                break;
            case 3:
                if (highlight > 0) sbAdjust.setProgress((int) highlight / 4);
                else sbAdjust.setProgress((int) highlight / 2);
                break;
            case 4:
                if (shadow > 0) sbAdjust.setProgress((int) shadow / 4);
                else sbAdjust.setProgress((int) shadow / 2);
                break;
            case 5:
                sbAdjust.setProgress((int) black / 2);
                break;
            case 6:
                sbAdjust.setProgress((int) white / 2);
                break;
            case 7:
                sbAdjust.setProgress((int) saturation / 2);
                break;
            case 8:
                sbAdjust.setProgress((int) hue * 100 / 360);
                break;
            case 9:
                sbAdjust.setProgress((int) warmth / 2);
                break;
            case 10:
                sbAdjust.setProgress((int) vibrance / 2);
                break;
            case 11:
                sbAdjust.setProgress((int) vignette / 2);
                break;
        }

        tvAdjustBackground.setText(String.valueOf(sbAdjust.getProgress()));

        bitmapAjust = backgroundModel.getBackground();
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
            public void onUp(View v, int value) {
//                setValueAdjust(value, pos);
            }
        });
    }

    private void setValueAdjust(int value, int pos) {
        switch (pos) {
            case 0:
                brightness = value * 2f;
                backgroundModel.setBackground(UtilsAdjust.adjustBrightness(bitmapAjust, brightness));
                break;
            case 1:
                contrast = value;
                backgroundModel.setBackground(UtilsAdjust.adjustContrast(bitmapAjust, contrast));
                break;
            case 2:
                exposure = value * 4f;
                backgroundModel.setBackground(UtilsAdjust.adjustExposure(bitmapAjust, exposure));
                break;
            case 3:
                if (value > 0) highlight = value * 4f;
                else if (value < 0) highlight = value * 2f;
                backgroundModel.setBackground(UtilsAdjust.adjustHighLight(bitmapAjust, highlight));
                break;
            case 4:
                if (value > 0) shadow = value * 4f;
                else if (value < 0) shadow = value * 2f;
                backgroundModel.setBackground(UtilsAdjust.adjustShadow(bitmapAjust, shadow));
                break;
            case 5:
                black = value * 2f;
                backgroundModel.setBackground(UtilsAdjust.adjustBlacks(bitmapAjust, black));
                break;
            case 6:
                white = value * 2f;
                backgroundModel.setBackground(UtilsAdjust.adjustWhites(bitmapAjust, white));
                break;
            case 7:
                saturation = value * 2f;
                backgroundModel.setBackground(UtilsAdjust.adjustSaturation(bitmapAjust, saturation));
                break;
            case 8:
                hue = value * 360 / 100f;
                backgroundModel.setBackground(UtilsAdjust.adjustHue(bitmapAjust, hue));
                break;
            case 9:
                warmth = value * 2f;
                backgroundModel.setBackground(UtilsAdjust.adjustWarmth(bitmapAjust, warmth));
                break;
            case 10:
                vibrance = value * 2f;
                backgroundModel.setBackground(UtilsAdjust.adjustVibrance(bitmapAjust, vibrance));
                break;
            case 11:
                vignette = value * 2f;
                backgroundModel.setBackground(UtilsAdjust.adjustVignette(bitmapAjust, vignette));
                break;
        }
        vMain.setImageBitmap(backgroundModel.getBackground());
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

                addAdjustBackground(new AdjustModel(brightness, contrast, exposure, highlight, shadow,
                        black, white, saturation, hue, warmth, vibrance, vignette));
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

    private void addAdjustBackground(AdjustModel adjustModel) {
        for (StickerModel t : lstSticker) {
            if (t.getTextSticker() != null)
                if (t.getTextSticker().getId() == textSticker.getId()) {
                    t.getTextModel().setOpacity(opacity);
                    break;
                }
        }
    }

    //replace
    private void replaceBackground() {
        isReplaceBackground = true;
        Intent intent = new Intent();
        intent.putExtra("pickBG", isReplaceBackground);
        intent.setComponent(new ComponentName(getPackageName(), CreateProjectActivity.class.getName()));
        startActivity(intent, ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left).toBundle());
    }

    //image
    private void image() {
        seekAndHideLayout(6);
        setUpDataFilter();
        setUpDataBlend();
    }

    private void pickImage() {
        ImageFragment imageFragment = ImageFragment.newInstance((o, pos) -> {
            PicModel picModel = (PicModel) o;
            if (!picModel.getUri().equals("")) {
                clearData();
                Uri uri = Uri.parse(picModel.getUri());
                new Thread(() -> {
                    try {
                        Bitmap bitmap = Utils.getBitmapFromUri(EditActivity.this, uri);
                        if (bitmap != null)
                            bitmapFilterBlend = Bitmap.createBitmap(Utils.modifyOrientation(EditActivity.this,
                                    Bitmap.createScaledBitmap(bitmap, 512, 512 * bitmap.getHeight() / bitmap.getWidth(), false), uri));
                        else Utils.showToast(getBaseContext(), getString(R.string.cant_get_image));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(0);
                }).start();
            }
        });
        Utils.replaceFragment(getSupportFragmentManager(), imageFragment, false, true);
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

            if (stickerView.getCurrentSticker() == null)
                Utils.showToast(this, getResources().getString(R.string.choose_sticker_text));
            else
                for (StickerModel st : lstSticker) {
                    if (st.getDrawableSticker() != null)
                        if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isImage()) {
                            Drawable drawable = new BitmapDrawable(getResources(), bitmap);

                            st.getImageModel().setImage(bitmap);
                            st.getImageModel().setPosBlend(pos);
                            st.getImageModel().setPosFilter(0);
                            stickerView.getCurrentSticker().setDrawable(drawable);
                            stickerView.invalidate();
                            break;
                        }
                }

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
        if (bitmapFilterBlend == null) return;
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
            if (st.getDrawableSticker() != null)
                if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isImage()) {
                    opacity = (int) (st.getDrawableSticker().getAlpha() * 100 / 255f);
                    sbOpacityImage.setProgress(opacity);
                }
        }
        sbOpacityImage.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                opacity = (int) (value * 255 / 100f);
                if (drawableSticker != null) drawableSticker.setAlpha(opacity);
                stickerView.replace(drawableSticker, true);
            }

            @Override
            public void onUp(View v, int value) {

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

                addOpacityImageModel(opacity);
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

    private void addOpacityImageModel(int opacity) {
        for (StickerModel t : lstSticker) {
            if (t.getDrawableSticker() != null)
                if (t.getDrawableSticker().getId() == drawableSticker.getId() && t.getDrawableSticker().isImage()) {
                    t.getImageModel().setOpacity(opacity);
                    break;
                }
        }
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
            public void onUp(View v, int value) {

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
            public void onUp(View v, int value) {

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
            public void onUp(View v, int value) {

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
                    tvTitleEditImage.setText(getString(R.string.image));
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                vEditImage.startAnimation(animation);
                if (vEditImage.getVisibility() == View.GONE) vEditImage.setVisibility(View.VISIBLE);

                addShadowImageModel(new ShadowModel(dx, dy, radiusBlur, colorShadow));
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

    private void addShadowImageModel(ShadowModel shadowModel) {
        for (StickerModel t : lstSticker) {
            if (t.getDrawableSticker() != null)
                if (t.getDrawableSticker().getId() == drawableSticker.getId()) {
                    t.getImageModel().setShadowModel(shadowModel);
                    break;
                }
        }
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

            if (stickerView.getCurrentSticker() == null)
                Utils.showToast(this, getResources().getString(R.string.choose_sticker_text));
            else
                for (StickerModel st : lstSticker) {
                    if (st.getDrawableSticker() != null)
                        if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isImage()) {

                            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                            st.getImageModel().setImage(bitmap);
                            st.getImageModel().setPosFilter(pos);
                            st.getImageModel().setPosBlend(0);

                            stickerView.getCurrentSticker().setDrawable(drawable);
                            stickerView.invalidate();
                            break;
                        }
                }

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
        if (bitmapFilterBlend == null) return;
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
            if (st.getDrawableSticker() != null)
                if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isImage()) {
                    Drawable drawable = st.getDrawableSticker().getDrawable();

                    DrawableSticker sticker = new DrawableSticker(this, drawable, new ArrayList<>(), getId(), true, false, false, false);

                    drawable.setAlpha(st.getImageModel().getOpacity());
                    if (drawableSticker.getColorShadow() != 0)
                        sticker.setShadow(st.getImageModel().getShadowModel().getBlur(), st.getImageModel().getShadowModel().getxPos(),
                                st.getImageModel().getShadowModel().getyPos(), st.getImageModel().getShadowModel().getColorBlur(), false);

                    stickerView.addSticker(sticker);
                    lstSticker.add(new StickerModel(null, null, null, null, st.getImageModel(), null, backgroundModel, null, sticker));
                    break;
                }
        }
    }

    private void cropImage() {
        rlEditCrop.getLayoutParams().height = getResources().getDisplayMetrics().heightPixels * 90 / 100;
        setUpLayoutEditCropImage(0);
        tvCancelCropImage.setOnClickListener(v -> setUpLayoutEditCropImage(1));

        for (StickerModel st : lstSticker) {
            if (st.getDrawableSticker() != null)
                if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isImage()) {
                    pathCrop.setBitmap(st.getImageModel().getImage());
                    pathCrop.setPath(DataPic.getPathDataCrop(0));
                    break;
                }
        }

        CropImageAdapter cropImageAdapter = new CropImageAdapter(this, (o, pos) -> pathCrop.setPath((String) o));

        GridLayoutManager manager = new GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false);
        cropImageAdapter.setData(Arrays.asList(DataPic.lstPathData));

        rcvEditCrop.setLayoutManager(manager);
        rcvEditCrop.setAdapter(cropImageAdapter);
    }

    private void afterCropImage() {
        setUpLayoutEditCropImage(1);

        Bitmap bitmap = pathCrop.getBitmapCreate();
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        if (stickerView.getCurrentSticker() == null)
            Utils.showToast(this, getResources().getString(R.string.choose_sticker_text));
        else {
            for (StickerModel st : lstSticker) {
                if (st.getDrawableSticker() != null)
                    if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isImage()) {
                        st.getImageModel().setImage(bitmap);

                        stickerView.getCurrentSticker().setDrawable(drawable);
                        stickerView.invalidate();
                    }
            }
        }
    }

    private void setUpLayoutEditCropImage(int pos) {
        switch (pos) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                vEditImage.startAnimation(animation);
                if (vEditImage.getVisibility() == View.VISIBLE) vEditImage.setVisibility(View.GONE);

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                rlEditCrop.startAnimation(animation);
                if (rlEditCrop.getVisibility() == View.GONE) {
                    rlEditCrop.setVisibility(View.VISIBLE);
                    tvCancelEditImage.setVisibility(View.VISIBLE);
                    tvTitleEditImage.setText(getString(R.string.crop));
                }
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                rlEditCrop.startAnimation(animation);
                if (rlEditCrop.getVisibility() == View.VISIBLE) {
                    rlEditCrop.setVisibility(View.GONE);
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
                rlEditCrop.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
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
            if (st.getDrawableSticker() != null)
                if (st.getDrawableSticker().getId() == drawableSticker.getId()) {
                    opacity = (int) (st.getDrawableSticker().getAlpha() * 100 / 255f);
                    sbOpacityEmoji.setProgress((int) (st.getDrawableSticker().getAlpha() * 100 / 255f));
                }
        }
        sbOpacityEmoji.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                opacity = (int) (value * 255 / 100f);
                if (drawableSticker != null) drawableSticker.setAlpha(opacity);
                stickerView.replace(drawableSticker, true);

            }

            @Override
            public void onUp(View v, int value) {

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

                addOpacityEmojiModel(opacity);
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

    private void addOpacityEmojiModel(int opacity) {
        for (StickerModel t : lstSticker) {
            if (t.getDrawableSticker() != null)
                if (t.getDrawableSticker().getId() == drawableSticker.getId()
                        && !t.getDrawableSticker().isImage()
                        && !t.getDrawableSticker().isDecor()
                        && !t.getDrawableSticker().isTemplate()
                        && !t.getDrawableSticker().isTemplate()) {
                    t.getEmojiModel().setOpacity(opacity);
                    break;
                }
        }
    }

    private void addNewEmoji(int id, EmojiModel emoji, int pos) {
        Bitmap bitmap = Utils.getBitmapFromAsset(this, emoji.getNameFolder(), emoji.getNameEmoji(), true, false);
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);

        switch (pos) {
            case 0:
                DrawableSticker sticker = new DrawableSticker(this, drawable, new ArrayList<>(), id, false, false, false, false);

                stickerView.addSticker(sticker);
                lstSticker.add(new StickerModel(null, emoji, null, null, null, null, backgroundModel, null, sticker));
            case 1:
                if (stickerView.getCurrentSticker() == null)
                    Utils.showToast(this, getResources().getString(R.string.choose_sticker_text));
                else
                    for (StickerModel st : lstSticker) {
                        if (st.getDrawableSticker() != null)
                            if (st.getDrawableSticker().getId() == drawableSticker.getId()
                                    && !st.getDrawableSticker().isImage()
                                    && !st.getDrawableSticker().isOverlay()
                                    && !st.getDrawableSticker().isDecor()) {

                                st.getEmojiModel().setNameEmoji(emoji.getNameEmoji());
                                st.getEmojiModel().setNameFolder(emoji.getNameFolder());
                                drawable.setAlpha(st.getEmojiModel().getOpacity());
                                drawableSticker.setDrawable(drawable);
                                stickerView.invalidate();
                            }
                    }
                break;
        }
    }

    private void setUpDataEmoji() {
        addFragmentsAdapter = new ViewPagerAddFragmentsAdapter(getSupportFragmentManager(), getLifecycle());

//        createFragment();
        String[] arrNameEmoji = new String[]{"bear", "cat", "chicken", "face", "fox", "frankenstein",
                "ghost", "heart", "icon", "pillow", "pumpkin", "santa", "tiger"};
        for (String s : arrNameEmoji) {
            EmojiFragment emojiFragment = EmojiFragment.newInstance(s, (o, pos) -> {
                EmojiModel emoji = (EmojiModel) o;
                if (!replaceEmoji) addNewEmoji(getId(), emoji, 0);
                else addNewEmoji(drawableSticker.getId(), emoji, 1);
                seekAndHideLayout(5);
            });
            addFragmentsAdapter.addFrag(emojiFragment);
        }

        bitmapDrawable = null;

        vpEmoji.setAdapter(addFragmentsAdapter);
        vpEmoji.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                emojiTitleAdapter.setCurrent(position);
                rcvTitleEmoji.smoothScrollToPosition(position);
            }
        });
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
                opacity = (int) (value * 255 / 100f);
                if (textSticker != null) textSticker.setAlpha(opacity);

                stickerView.replace(textSticker, true);
            }

            @Override
            public void onUp(View v, int value) {

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

                addOpacityTextModel(opacity);

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

    private void addOpacityTextModel(int opacity) {
        for (StickerModel t : lstSticker) {
            if (t.getTextSticker() != null)
                if (t.getTextSticker().getId() == textSticker.getId()) {
                    t.getTextModel().setOpacity(opacity);
                    break;
                }
        }
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
            public void onUp(View v, int value) {

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
            public void onUp(View v, int value) {

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
            public void onUp(View v, int value) {

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

                addShadowTextModel(new ShadowModel(dx, dy, radiusBlur, colorShadow));
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

    private void addShadowTextModel(ShadowModel shadowModel) {
        for (StickerModel t : lstSticker) {
            if (t.getTextSticker() != null)
                if (t.getTextSticker().getId() == textSticker.getId()) {
                    t.getTextModel().setShadowModel(shadowModel);
                    break;
                }
        }
    }

    //transform
    private void transform() {
        setUpLayoutEditTransform(0);
        tvCancelEdittext.setOnClickListener(v -> setUpLayoutEditTransform(1));

        int shearX = (int) (stickerView.getShearX() * 100);
        int shearY = (int) (stickerView.getShearY() * 100);
        int stretch = (int) (stickerView.getStretch() * 100);

        tvShearX.setText(String.valueOf(shearX));
        sbShearX.setProgress(shearX);
        tvShearY.setText(String.valueOf(shearY));
        sbShearY.setProgress(shearY);
        tvStretch.setText(String.valueOf(stretch));
        sbStretch.setProgress(stretch);

        sbShearX.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                tvShearX.setText(String.valueOf(value));
                stickerView.shearSticker(value / 100f, true);
            }

            @Override
            public void onUp(View v, int value) {

            }
        });
        sbShearY.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                tvShearY.setText(String.valueOf(value));
                stickerView.shearSticker(value / 100f, false);
            }

            @Override
            public void onUp(View v, int value) {

            }
        });
        sbStretch.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                tvStretch.setText(String.valueOf(value));
                stickerView.stretchSticker(value / 100f);
            }

            @Override
            public void onUp(View v, int value) {

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

                addShearTextModel(new ShearTextModel(sbShearX.getProgress() / 100f, sbShearY.getProgress() / 100f, sbStretch.getProgress() / 100f));
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

    private void addShearTextModel(ShearTextModel shearTextModel) {
        for (StickerModel t : lstSticker) {
            if (t.getTextSticker() != null)
                if (t.getTextSticker().getId() == textSticker.getId()) {
                    t.getTextModel().setShearTextModel(shearTextModel);
                    break;
                }
        }
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
                    TextModel textModel = new TextModel(t.getTextModel());

                    stickerView.addSticker(text);
                    setTextSticker(text, textModel);

                    if (t.getTextModel().getShearTextModel() != null) {
                        stickerView.shearSticker(t.getTextModel().getShearTextModel().getShearX(), true);
                        stickerView.shearSticker(t.getTextModel().getShearTextModel().getShearY(), false);
                        stickerView.stretchSticker(t.getTextModel().getShearTextModel().getStretch());
                    }

                    lstSticker.add(new StickerModel(textModel, null, null, null, null,
                            null, backgroundModel, text, null));
                    break;
                }
        }
    }

    //EditTextLayout
    private void setTextSticker(TextSticker sticker, TextModel textModel) {
        sticker.setText(textModel.getContent());

        for (StyleFontModel f : textModel.getFontModel().getLstStyle()) {
            if (f.isSelected()) {
                sticker.setTypeface(Utils.getTypeFace(textModel.getFontModel().getNameFont(), f.getName(), this));
                break;
            }
        }
        if (textModel.getColor() != null) sticker.setTextColor(textModel.getColor());

        sticker.setAlpha(textModel.getOpacity());

        if (textModel.getShadowModel() != null) {
            sticker.setShadow(textModel.getShadowModel().getBlur(), textModel.getShadowModel().getxPos(),
                    textModel.getShadowModel().getyPos(), textModel.getShadowModel().getColorBlur());
        }

        switch (textModel.getTypeAlign()) {
            case 0:
                sticker.setTextAlign(Layout.Alignment.ALIGN_NORMAL);
                break;
            case 1:
                sticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
                break;
            case 2:
                sticker.setTextAlign(Layout.Alignment.ALIGN_OPPOSITE);
                break;
        }
        sticker.resizeText();
    }

    //edit text
    private void editText() {
        Intent intent = new Intent(this, AddTextActivity.class);
        if (stickerView.getCurrentSticker() == null)
            Utils.showToast(this, getResources().getString(R.string.choose_sticker_text));
        else
            for (StickerModel t : lstSticker) {
                if (t.getTextSticker() != null)
                    if (t.getTextSticker().getId() == textSticker.getId()) {
                        intent.putExtra("text", t.getTextModel());
                        launcherEditText.launch(intent, ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left));
                        isAddText = false;
                        break;
                    }
            }
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

        layerAdapter.setCurrent(stickerView.getStickerCount() - 1);
    }

    private void duplicate() {
        if (drawableSticker == null) {
            Utils.showToast(this, getString(R.string.choose_sticker_text));
            return;
        }

        for (StickerModel st : lstSticker) {
            if (st.getDrawableSticker() != null) {
                if (st.getDrawableSticker().getId() == drawableSticker.getId()
                        && !st.getDrawableSticker().isOverlay()
                        && !st.getDrawableSticker().isImage()
                        && !st.getDrawableSticker().isDecor()
                        && !st.getDrawableSticker().isTemplate()) {

                    Bitmap bitmap = Utils.getBitmapFromAsset(this, st.getEmojiModel().getNameFolder(), st.getEmojiModel().getNameEmoji(), true, false);

                    BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);

                    drawable.setAlpha(st.getEmojiModel().getOpacity());
                    DrawableSticker sticker = new DrawableSticker(this, drawable, new ArrayList<>(), getId(), false, false, false, false);

                    stickerView.addSticker(sticker);
                    lstSticker.add(new StickerModel(null, st.getEmojiModel(), null, null, null,
                            null, st.getBackgroundModel(), null, sticker));
                    break;
                }

                if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isOverlay()) {

                    Bitmap bitmap = Utils.getBitmapFromAsset(this, st.getOverlayModel().getNameFolder(), st.getOverlayModel().getNameOverlay(), false, false);
                    BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);

                    DrawableSticker sticker = new DrawableSticker(this, drawable, new ArrayList<>(), getId(), false, true, false, false);
                    sticker.setAlpha(drawableSticker.getAlpha());

                    stickerView.addSticker(sticker);
                    lstSticker.add(new StickerModel(null, null, st.getOverlayModel(), null, null,
                            null, backgroundModel, null, sticker));
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

            if (stickerView.getCurrentSticker() == null)
                Utils.showToast(this, getResources().getString(R.string.choose_sticker_text));
            else
                for (StickerModel st : lstSticker) {
                    if (st.getDrawableSticker() != null)
                        if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isTemplate()) {

                            st.getTemplateModel().setLstPathData(template.getLstPathData());
                            drawableSticker.setPathData(template.getLstPathData());
                            stickerView.invalidate();

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
            public void onUp(View v, int value) {

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
            public void onUp(View v, int value) {

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
            public void onUp(View v, int value) {

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
            public void onUp(View v, int value) {

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
        if (stickerView.getCurrentSticker() == null)
            Utils.showToast(this, getResources().getString(R.string.choose_sticker_text));
        else
            for (StickerModel st : lstSticker) {
                if (st.getDrawableSticker() != null)
                    if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isTemplate()) {

                        DrawableSticker sticker = new DrawableSticker(this, null, st.getTemplateModel().getLstPathData(), getId(), false, false, false, true);

                        sticker.setAlpha(st.getDrawableSticker().getAlpha());

                        if (st.getTemplateModel().getColorModel() != null)
                            sticker.setColor(st.getTemplateModel().getColorModel());

                        stickerView.addSticker(sticker);
                        lstSticker.add(new StickerModel(null, null, null, null, null, st.getTemplateModel(), backgroundModel, null, sticker));
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
                    st.getTemplateModel().setColorModel(color);
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

        if (drawableSticker != null)
            for (StickerModel st : lstSticker) {
                if (st.getDrawableSticker() != null)
                    if (st.getDrawableSticker().getId() == drawableSticker.getId()) {
                        stickerModel = st;
                        stickerView.remove(st.getDrawableSticker());
                        break;
                    }
            }
        if (textSticker != null)
            for (StickerModel st : lstSticker) {
                if (st.getTextSticker() != null)
                    if (st.getTextSticker().getId() == textSticker.getId()) {
                        stickerModel = st;
                        stickerView.remove(st.getTextSticker());
                        break;
                    }
            }

        if (stickerView.getStickerCount() > 0) {
            Sticker sticker = stickerView.getSticker(0);
            if (sticker instanceof DrawableSticker) drawableSticker = (DrawableSticker) sticker;
            if (sticker instanceof TextSticker) textSticker = (TextSticker) sticker;
        } else stickerView.setCurrentSticker(null);

        if (!islLayer) seekAndHideLayout(0);
        else {
            layerAdapter.setData(stickerView.getListLayer());
            layerAdapter.setCurrent(0);
            if (stickerView.getStickerCount() > 0)
                stickerView.setCurrentSticker(stickerView.getListLayer().get(0).getSticker());
            else seekAndHideLayout(0);
        }

        lstSticker.remove(stickerModel);
    }

    //SetUpData
    private void getData() {
        String strUri = DataLocalManager.getOption("bitmap");
        String picAsset = DataLocalManager.getOption("bitmap_myapp");
        TemplateModel template = DataLocalManager.getTemp("temp");

        if (!strUri.equals("") || !picAsset.equals("")) {
            isColor = false;
            try {
                Bitmap bitmap = null;
                if (!strUri.equals(""))
                    bitmap = Utils.modifyOrientation(getBaseContext(), MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(strUri)), Uri.parse(strUri));
                if (!picAsset.equals(""))
                    bitmap = Utils.getBitmapFromAsset(this, "offline_myapp", picAsset, false, false);

                backgroundModel.setBackground(bitmap);
                backgroundModel.setBackgroundRoot(bitmap);

                if (bitmap != null && isReplaceBackground) {
                    setUpDataFilterBackground();
                    backgroundModel.setBackground(CGENativeLibrary.cgeFilterImage_MultipleEffects(backgroundModel.getBackgroundRoot(),
                            FilterBlendImage.EFFECT_CONFIGS[backgroundModel.getPositionFilterBackground()], 0.8f));
                    adjust(backgroundModel.getBackground());

                    if (vCrop.getVisibility() == View.GONE) {
                        seekAndHideLayout(12);
                        vCrop.setData(backgroundModel.getBackgroundRoot());
//                        vCrop.setSize(backgroundModel.getSizeMain());
                    }
                } else if (!isReplaceBackground) {
                    vCrop.setData(bitmap);
                    vCrop.setSize(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (template != null) {
            isColor = false;
            Bitmap bitmap = Utils.getBitmapFromAsset(this, "template/template_background", template.getBackground(), false, false);
            backgroundModel.setBackground(bitmap);
            backgroundModel.setBackgroundRoot(bitmap);

            DrawableSticker drawableSticker = new DrawableSticker(this, null, template.getLstPathData(), getId(), false, false, false, true);

            stickerView.addSticker(drawableSticker);
            lstSticker.add(new StickerModel(null, null, null, null, null, template, backgroundModel, null, drawableSticker));
            isTemplate = true;
            if (bitmap != null)
                vCrop.setData(bitmap);
        } else {
            ColorModel color = DataLocalManager.getColor("color");
            isColor = true;
            seekAndHideLayout(12);
            if (vMain.getVisibility() == View.VISIBLE) vMain.setVisibility(View.GONE);
            if (vCrop.getVisibility() == View.VISIBLE) vCrop.setVisibility(View.GONE);
            if (vColor.getVisibility() == View.GONE) vColor.setVisibility(View.VISIBLE);

            vColor.setData(color);
            vColor.setSize(0);
            vColor.setAlpha(backgroundModel.getOpacity());
        }
    }

    //setUpProject
    private void setUpDataProject() {
        DataLocalManager.setCheck("isProject", false);
        Project project = DataLocalManager.getProject("project");
        if (project == null) return;

        if (!project.getListTextModel().isEmpty()) {

            for (TextModel textModel : project.getListTextModel()) {

            }
        }

    }

    private void adjust(Bitmap bm) {
        if (brightness != 0f)
            backgroundModel.setBackgroundRoot(UtilsAdjust.adjustBrightness(bm, brightness));
        if (contrast != 0f)
            backgroundModel.setBackgroundRoot(UtilsAdjust.adjustBrightness(bm, contrast));
        if (exposure != 0f)
            backgroundModel.setBackgroundRoot(UtilsAdjust.adjustExposure(bm, exposure));
        if (highlight != 0f)
            backgroundModel.setBackgroundRoot(UtilsAdjust.adjustHighLight(bm, highlight));
        if (shadow != 0f)
            backgroundModel.setBackgroundRoot(UtilsAdjust.adjustHighLight(bm, shadow));
        if (black != 0f)
            backgroundModel.setBackgroundRoot(UtilsAdjust.adjustBlacks(bm, black));
        if (white != 0f)
            backgroundModel.setBackgroundRoot(UtilsAdjust.adjustWhites(bm, white));
        if (saturation != 0f)
            backgroundModel.setBackgroundRoot(UtilsAdjust.adjustSaturation(bm, saturation));
        if (hue != 0f)
            backgroundModel.setBackgroundRoot(UtilsAdjust.adjustHue(bm, hue));
        if (warmth != 0f)
            backgroundModel.setBackgroundRoot(UtilsAdjust.adjustWarmth(bm, warmth));
        if (vibrance != 0f)
            backgroundModel.setBackgroundRoot(UtilsAdjust.adjustVibrance(bm, vibrance));
        if (vignette != 0f)
            backgroundModel.setBackgroundRoot(UtilsAdjust.adjustVignette(bm, vignette));

//        vMain.setImageBitmap(bitmap);
    }

    private int getId() {
        if (lstSticker.isEmpty()) return 0;
        else if (lstSticker.get(lstSticker.size() - 1).getTextSticker() != null)
            return lstSticker.get(lstSticker.size() - 1).getTextSticker().getId() + 1;
        else
            return lstSticker.get(lstSticker.size() - 1).getDrawableSticker().getId() + 1;
    }

    private void seekAndHideLayout(int pos) {
        if (stickerView.getStickerCount() == 0)
            ivLayer.setImageResource(R.drawable.ic_layer_uncheck);
        else ivLayer.setImageResource(R.drawable.ic_layer);

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
                    llLayerExport.setVisibility(View.VISIBLE);
                    llReUndo.setVisibility(View.VISIBLE);
                    vOperation.setVisibility(View.VISIBLE);
                }
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                if (vOperation.getVisibility() == View.VISIBLE) {
                    vOperation.startAnimation(animation);
                    llLayerExport.setVisibility(View.GONE);
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

                if (rlEditOpacityText.getVisibility() == View.VISIBLE)
                    setUpLayoutEditOpacityText(1);

                if (llEditShadow.getVisibility() == View.VISIBLE)
                    setUpLayoutEditShadow(1);

                if (llEditTransform.getVisibility() == View.VISIBLE)
                    setUpLayoutEditTransform(1);

                if (rlEditColor.getVisibility() == View.VISIBLE)
                    setUpLayoutEditColor(1);

                if (rlEditFontSize.getVisibility() == View.VISIBLE)
                    setUpLayoutEditFontSize(1);

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

                if (rlEditOpacityEmoji.getVisibility() == View.VISIBLE)
                    setUpLayoutEditOpacityEmoji(1);

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

                if (rlEditBlend.getVisibility() == View.VISIBLE)
                    setUpLayoutEditBlendImage(1);

                if (rlEditOpacityImage.getVisibility() == View.VISIBLE)
                    setUpLayoutEditOpacityImage(1);

                if (llEditShadowImage.getVisibility() == View.VISIBLE)
                    setUpLayoutEditShadowImage(1);

                if (rlEditFilter.getVisibility() == View.VISIBLE)
                    setUpLayoutEditFilterImage(1);

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

                if (rlEditOpacityBackground.getVisibility() == View.VISIBLE)
                    setUpLayoutEditOpacityBackground(1);

                if (rlAdjust.getVisibility() == View.VISIBLE)
                    setUpLayoutEditAdjustBackground(1);

                if (rlEditFilterBackground.getVisibility() == View.VISIBLE)
                    setUpLayoutEditFilterBackground(1);

                if (isColor) {
                    rlFilterBackground.setVisibility(View.GONE);
                    rlAdjustBackground.setVisibility(View.GONE);
                    rlFlipYBackground.setVisibility(View.GONE);
                    rlFlipXBackground.setVisibility(View.GONE);
                } else {
                    rlFilterBackground.setVisibility(View.VISIBLE);
                    rlAdjustBackground.setVisibility(View.VISIBLE);
                    rlFlipYBackground.setVisibility(View.VISIBLE);
                    rlFlipXBackground.setVisibility(View.VISIBLE);
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

                if (rlEditOpacityOverlay.getVisibility() == View.VISIBLE)
                    setUpLayoutEditOpacityOverlay(1);

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

                if (rlEditOpacityDecor.getVisibility() == View.VISIBLE)
                    setUpLayoutEditOpacityDecor(1);

                if (llEditShadowDecor.getVisibility() == View.VISIBLE)
                    setUpLayoutEditShadowDecor(1);

                if (rlEditColorDecor.getVisibility() == View.VISIBLE)
                    setUpLayoutEditColorDecor(1);

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
                }

                llLayerExport.setVisibility(View.GONE);
                llReUndo.setVisibility(View.GONE);

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

                if (vMain.getVisibility() == View.VISIBLE) vMain.setVisibility(View.GONE);

                if (vColor.getVisibility() == View.VISIBLE && !isColor)
                    vColor.setVisibility(View.GONE);

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (vSize.getVisibility() == View.GONE) {
                    vSize.startAnimation(animation);
                    tvTitle.setVisibility(View.VISIBLE);
                    vSize.setVisibility(View.VISIBLE);
                    ivTick.setVisibility(View.VISIBLE);
                }
                if (vCrop.getVisibility() == View.GONE && !isColor)
                    vCrop.setVisibility(View.VISIBLE);
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

                if (rlEditOpacityTemp.getVisibility() == View.VISIBLE)
                    setUpLayoutEditOpacityTemp(1);

                if (llEditShadowTemp.getVisibility() == View.VISIBLE)
                    setUpLayoutEditShadowTemp(1);

                if (rlEditColorTemp.getVisibility() == View.VISIBLE)
                    setUpLayoutColorTextTemp(1);

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlExpandEditTemp.getVisibility() == View.GONE) {
                    rlExpandEditTemp.startAnimation(animation);
                    rlExpandEditTemp.setVisibility(View.VISIBLE);
                    llLayerExport.setVisibility(View.VISIBLE);
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
        backgroundModel.setSizeMain(pos);
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

                if (!isColor) {
                    vCrop.setSize(0);
                    vCrop.setData(backgroundModel.getBackgroundRoot());
                } else vColor.setSize(0);
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

                if (!isColor) {
                    vCrop.setSize(1);
                    vCrop.setData(backgroundModel.getBackgroundRoot());
                } else vColor.setSize(1);
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

                if (!isColor) {
                    vCrop.setSize(2);
                    vCrop.setData(backgroundModel.getBackgroundRoot());
                } else vColor.setSize(2);
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

                if (!isColor) {
                    vCrop.setSize(3);
                    vCrop.setData(backgroundModel.getBackgroundRoot());
                } else vColor.setSize(3);
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


                if (!isColor) {
                    vCrop.setSize(4);
                    vCrop.setData(backgroundModel.getBackgroundRoot());
                } else vColor.setSize(4);
                break;
        }
    }

    private void setUpLayout() {
        ivBack = findViewById(R.id.ivBack);
        ivTick = findViewById(R.id.ivTick);
        stickerView = findViewById(R.id.stickerView);
        vCrop = findViewById(R.id.vCrop);
        vMain = findViewById(R.id.vMain);
        vColor = findViewById(R.id.vColor);
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
        ivExport = findViewById(R.id.ivExport);
        rlAddText = findViewById(R.id.rlAddText);
        rlSticker = findViewById(R.id.rlStick);
        rlImage = findViewById(R.id.rlImage);
        rlBackground = findViewById(R.id.rlBackground);
        rlBlend = findViewById(R.id.rlBlend);
        rlDecor = findViewById(R.id.rlDecor);
        rlCrop = findViewById(R.id.rlCrop);
        llLayerExport = findViewById(R.id.llLayerExport);
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
        rlEditCrop = findViewById(R.id.rlEditCrop);
        rcvEditCrop = findViewById(R.id.rcvEditCrop);
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
        tvCancelCropImage = findViewById(R.id.tvCancelCropImage);
        ivTickCropImage = findViewById(R.id.ivTickCropImage);
        pathCrop = findViewById(R.id.pathCrop);

        lstSticker = new ArrayList<>();
        lstFilterBlend = new ArrayList<>();
        lstColor = new ArrayList<>();
        lstColor.addAll(DataColor.setListColor(this));

        backgroundModel = new BackgroundModel();

        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.parseColor("#575757"), Color.parseColor("#575757")});
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradientDrawable.setCornerRadius(10f);
        ivColor.setBackground(gradientDrawable);
    }

    private void clearData() {
        if (bitmapDrawable != null) bitmapDrawable = null;
        if (bitmapFilterBlend != null) bitmapFilterBlend = null;
        if (lstFilterBlend != null) lstFilterBlend = null;
        lstFilterBlend = new ArrayList<>();
        DataLocalManager.setOption("", "bitmap");
        DataLocalManager.setOption("", "bitmap_myapp");
        DataLocalManager.setTemp(null, "temp");
    }

    @Override
    public void onBackPressed() {
        stickerView.setCurrentSticker(null);
        animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
        if (vSize.getVisibility() == View.VISIBLE && isColor) {
            vSize.startAnimation(animation);
            tvTitle.setVisibility(View.GONE);
            vSize.setVisibility(View.GONE);
            ivTick.setVisibility(View.GONE);
        } else if (vSize.getVisibility() == View.VISIBLE && !isColor) {
            clickTick();
            return;
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
            llLayerExport.setVisibility(View.VISIBLE);
            llReUndo.setVisibility(View.VISIBLE);
            vOperation.setVisibility(View.VISIBLE);
        } else {
            @SuppressLint("InflateParams")
            View v = LayoutInflater.from(this).inflate(R.layout.dialog_exit_edit, null);
            tvCancel = v.findViewById(R.id.tvCancel);
            tvDiscard = v.findViewById(R.id.tvDiscard);
            tvSave = v.findViewById(R.id.tvSave);

            AlertDialog dialog = new AlertDialog.Builder(this, R.style.SheetDialog).create();
            dialog.setView(v);
            dialog.setCancelable(false);
            dialog.show();

            tvDiscard.setOnClickListener(vDiscard -> {
                clearData();
                super.onBackPressed();
                Utils.setAnimExit(this);
                dialog.cancel();
            });
            tvSave.setOnClickListener(vSave -> {
                saveProject();
                clearData();
                super.onBackPressed();
                Utils.setAnimExit(this);
                dialog.cancel();
            });
            tvCancel.setOnClickListener(vCancel -> dialog.cancel());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isReplaceBackground) {
            stickerView.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
            stickerView.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
            vCrop.setAlpha(backgroundModel.getOpacity());
            getData();
        }
    }
}