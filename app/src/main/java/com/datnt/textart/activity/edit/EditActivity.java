package com.datnt.textart.activity.edit;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

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
import com.datnt.textart.activity.project.CreateProjectActivity;
import com.datnt.textart.adapter.ColorAdapter;
import com.datnt.textart.adapter.FilterBlendImageAdapter;
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
import com.datnt.textart.data.DataEmoji;
import com.datnt.textart.data.DataPic;
import com.datnt.textart.data.FilterBlendImage;
import com.datnt.textart.fragment.emoji.EmojiBearFragment;
import com.datnt.textart.fragment.emoji.EmojiFaceFragment;
import com.datnt.textart.model.ColorModel;
import com.datnt.textart.model.EmojiModel;
import com.datnt.textart.model.FilterBlendModel;
import com.datnt.textart.model.StickerModel;
import com.datnt.textart.model.StyleFontModel;
import com.datnt.textart.model.TextModel;
import com.datnt.textart.sharepref.DataLocalManager;
import com.datnt.textart.utils.Utils;
import com.flask.colorpicker.ColorPickerView;

import org.wysaid.nativePort.CGENativeLibrary;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class EditActivity extends AppCompatActivity {

    private ImageView ivOriginal, iv1_1, iv9_16, iv4_5, iv16_9, ivBack, ivTick, ivUndo, ivRedo,
            ivLayer, ivImport, ivLook, ivLock, ivColor, ivColorBlur, ivColorBlurImage, ivVignette, ivVibrance,
            ivWarmth, ivHue, ivSaturation, ivWhites, ivBlacks, ivShadows, ivHighLight, ivExposure,
            ivContrast, ivBrightness;
    private TextView tvOriginal, tv1_1, tv9_16, tv4_5, tv16_9, tvTitle, tvFontSize, tvCancelEdittext,
            tvTitleEditText, tvShearX, tvShearY, tvStretch, tvXPos, tvYPos, tvBlur, tvCancelEditEmoji,
            tvTitleEmoji, tvCancelEditImage, tvTitleEditImage, tvXPosImage, tvYPosImage, tvBlurImage,
            tvCancelEditBackground, tvTitleEditBackground, tvAdjustBackground, tvVignette, tvVibrance,
            tvWarmth, tvHue, tvSaturation, tvWhites, tvBlacks, tvShadows, tvHighLight, tvExposure,
            tvContrast, tvBrightness;
    private SeekBar sbFontSize;
    private CustomSeekbarTwoWay sbStretch, sbShearX, sbShearY, sbXPos, sbYPos, sbBlur, sbXPosImage,
            sbYPosImage, sbBlurImage, sbAdjust;
    private CustomSeekbarRunText sbOpacityText, sbOpacityEmoji, sbOpacityImage;
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
            rlShadows, rlHighLight, rlExposure, rlContrast, rlBrightness, rlAdjust, rlEditFilterBackground;
    private LinearLayout llLayoutImport, llReUndo, llEditTransform, llEditShadow, llEditEmoji, llEditShadowImage;
    private HorizontalScrollView vSize, vOperation, vEditText, vEditImage, vEditBackground;
    private CustomView vMain;
    private StickerView stickerView;
    private RecyclerView rcvEditColor, rcvTitleEmoji, rcvEditFilter, rcvEditBlend, rcvEditFilterBackground;
    private ViewPager2 vpEmoji;
    private ViewPagerAddFragmentsAdapter addFragmentsAdapter;

    private EmojiFaceFragment faceFragment;
    private EmojiBearFragment bearFragment;
    private TitleEmojiAdapter emojiTitleAdapter;
    private FilterBlendImageAdapter filterBlendImageAdapter;
    private Bitmap bitmap, bitmapDrawable, bitmapFilterBlend;
    private ArrayList<StickerModel> lstSticker;
    private ArrayList<FilterBlendModel> lstFilterBlend;
    private TextSticker textSticker;
    private DrawableSticker drawableSticker;
    private GradientDrawable gradientDrawable;
    private boolean check, isFirstEmoji, replaceEmoji, isReplaceImage, isFilter, isBackground;
    private int sizeMain, positionFilter = 0, positionBlend = 0, positionFilterBackground = 0;
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
                if (sticker instanceof DrawableSticker) {
                    drawableSticker = (DrawableSticker) sticker;
                    textSticker = null;
                    if (!drawableSticker.isImage()) seekAndHideLayout(5);
                    else image();
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
                    if (!drawableSticker.isImage()) seekAndHideLayout(5);
                    else {
                        for (StickerModel st : lstSticker) {
                            if (st.getDrawableSticker() != null)
                                if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isImage()) {
                                    clearData();
                                    bitmapFilterBlend = st.getBitmapRoot();
                                    positionFilter = st.getPositionFilter();
                                    positionBlend = st.getPositionBlend();
                                }
                        }
                        image();
                    }
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
                    if (!drawableSticker.isImage()) seekAndHideLayout(5);
                    else {
                        for (StickerModel st : lstSticker) {
                            if (st.getDrawableSticker() != null)
                                if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isImage()) {
                                    clearData();
                                    bitmapFilterBlend = st.getBitmapRoot();
                                    positionFilter = st.getPositionFilter();
                                    positionBlend = st.getPositionBlend();
                                }
                        }
                        image();
                    }
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
        ivTick.setOnClickListener(v -> seekAndHideLayout(0));

        ivOriginal.setOnClickListener(v -> checkSize(0));
        iv1_1.setOnClickListener(v -> checkSize(1));
        iv9_16.setOnClickListener(v -> checkSize(2));
        iv4_5.setOnClickListener(v -> checkSize(3));
        iv16_9.setOnClickListener(v -> checkSize(4));

        ivLayer.setOnClickListener(v -> seekAndHideLayout(2));
        rlLayer.setOnClickListener(v -> seekAndHideLayout(0));
        rlEditText.setOnClickListener(v -> seekAndHideLayout(0));
        rlEditEmoji.setOnClickListener(v -> seekAndHideLayout(0));
        rlPickEmoji.setOnClickListener(v -> {
            if (replaceEmoji) addEmoji(1);
            else addEmoji(0);
        });
        rlEditImage.setOnClickListener(v -> seekAndHideLayout(0));
        rlEditBackground.setOnClickListener(v -> seekAndHideLayout(0));

        //text
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
            emoji();
            replaceEmoji = false;
        });
        rlDelEmoji.setOnClickListener(v -> delStick());
        rlReplaceEmoji.setOnClickListener(v -> replaceEmoji());
        rlOpacityEmoji.setOnClickListener(v -> opacityEmoji());
        rlFlipY.setOnClickListener(v -> flipYEmoji());
        rlFlipX.setOnClickListener(v -> flipXEmoji());

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

        rlAddText.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddTextActivity.class);
            launcherEditText.launch(intent, ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left));
            check = true;
        });
    }

    Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what) {
                case 0:
                    Bitmap bitmap = null;
                    Drawable drawable = new BitmapDrawable(getResources(), bitmapFilterBlend);
                    DrawableSticker sticker = new DrawableSticker(drawable, getId(), true);
                    for (StickerModel st : lstSticker) {
                        if (st.getDrawableSticker() != null)
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
                        lstSticker.add(new StickerModel(null, bitmapFilterBlend, bitmapFilterBlend, null, sticker, 0, 0));
                    } else {
                        if (bitmap != null) {
                            sticker.setDrawable(new BitmapDrawable(getResources(), bitmap));
                            lstSticker.add(new StickerModel(null, bitmapFilterBlend, bitmap, null, sticker, positionFilter, positionBlend));
                        } else {
                            sticker.setDrawable(new BitmapDrawable(getResources(), bitmapFilterBlend));
                            lstSticker.add(new StickerModel(null, bitmapFilterBlend, bitmapFilterBlend, null, sticker, 0, 0));
                        }
                        stickerView.replace(sticker, true);
                    }
                    image();
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
                        if (bitmap != null)
                            bitmapFilterBlend = Bitmap.createBitmap(modifyOrientation(
                                    Bitmap.createScaledBitmap(bitmap, 400, 400 * bitmap.getHeight() / bitmap.getWidth(), false), uri));
                        else Utils.showToast(getBaseContext(), getString(R.string.cant_get_image));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    handler.sendEmptyMessage(0);
                }).start();
            }
        }
    });

    //background
    private void background() {
        seekAndHideLayout(7);
        if (bitmap != null) setUpDataFilterBackground();
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
            DrawableSticker sticker = new DrawableSticker(new BitmapDrawable(getResources(), bitmap), getId(), true);
            Bitmap bm = null;

            for (StickerModel st : lstSticker) {
                if (st.getDrawableSticker() != null)
                    if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isImage()) {
                        sticker.setAlpha(drawableSticker.getAlpha());
                        bm = st.getBitmapRoot();
                    }
            }
            stickerView.replace(sticker, true);
            drawableSticker = sticker;
            lstSticker.add(new StickerModel(null, bm, bitmap, null, sticker, 0, pos));

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
            if (st.getDrawableSticker() != null)
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
            DrawableSticker sticker = new DrawableSticker(new BitmapDrawable(getResources(), bitmap), getId(), true);
            Bitmap bm = null;

            for (StickerModel st : lstSticker) {
                if (st.getDrawableSticker() != null)
                    if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isImage()) {
                        sticker.setAlpha(drawableSticker.getAlpha());
                        bm = st.getBitmapRoot();
                    }
            }
            stickerView.replace(sticker, true);
            drawableSticker = sticker;
            lstSticker.add(new StickerModel(null, bm, bitmap, null, sticker, pos, 0));

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

    private void duplicateImage() {
        if (drawableSticker == null) {
            Utils.showToast(this, getString(R.string.choose_sticker_text));
            return;
        }

        for (StickerModel st : lstSticker) {
            if (st.getDrawableSticker() != null)
                if (st.getDrawableSticker().getId() == drawableSticker.getId() && st.getDrawableSticker().isImage()) {
                    DrawableSticker sticker = new DrawableSticker(new BitmapDrawable(getResources(), st.getBitmap()), getId(), true);
                    sticker.setAlpha(st.getDrawableSticker().getAlpha());
                    stickerView.addSticker(sticker);
                    lstSticker.add(new StickerModel(null, st.getBitmapRoot(), st.getBitmap(), null, sticker, st.getPositionFilter(), st.getPositionBlend()));
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

    private void flipXEmoji() {
        for (StickerModel st : lstSticker) {
            if (st.getDrawableSticker() != null)
                if (st.getDrawableSticker().getId() == drawableSticker.getId()) {
                    Bitmap bitmap = createFlippedBitmap(st.getBitmap(), true, false);
                    stickerView.replace(new DrawableSticker(new BitmapDrawable(getResources(), bitmap), st.getDrawableSticker().getId(), false));
                    st.setBitmap(bitmap);
                    break;
                }
        }
    }

    private void flipYEmoji() {
        for (StickerModel st : lstSticker) {
            if (st.getDrawableSticker() != null)
                if (st.getDrawableSticker().getId() == drawableSticker.getId()) {
                    Bitmap bitmap = createFlippedBitmap(st.getBitmap(), false, true);
                    stickerView.replace(new DrawableSticker(new BitmapDrawable(getResources(), bitmap), st.getDrawableSticker().getId(), false));
                    st.setBitmap(bitmap);
                    break;
                }
        }
    }

    public static Bitmap createFlippedBitmap(Bitmap source, boolean xFlip, boolean yFlip) {
        Matrix matrix = new Matrix();
        matrix.postScale(xFlip ? -1 : 1, yFlip ? -1 : 1, source.getWidth() / 2f, source.getHeight() / 2f);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private void opacityEmoji() {
        setUpLayoutEditOpacityEmoji(0);
        tvCancelEditEmoji.setOnClickListener(v -> setUpLayoutEditOpacityEmoji(1));

        for (StickerModel st : lstSticker) {
            if (st.getDrawableSticker() != null)
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

    private void replaceEmoji() {
        seekAndHideLayout(4);
        replaceEmoji = true;
    }

    private void addEmoji(int pos) {
        boolean existEmoji = false;
        switch (pos) {
            case 0:
                if (!bearFragment.getEmoji().isEmpty()) {
                    for (EmojiModel emoji : bearFragment.getEmoji()) {
                        addNewEmoji(getId(), emoji, 0);
                    }
                    existEmoji = true;
                }

                if (!faceFragment.getEmoji().isEmpty()) {
                    for (EmojiModel emoji : faceFragment.getEmoji()) {
                        addNewEmoji(getId(), emoji, 0);
                    }
                    existEmoji = true;
                }
                break;
            case 1:
                if (bearFragment.replaceEmoji() != null) {
                    addNewEmoji(drawableSticker.getId(), bearFragment.replaceEmoji(), pos);
                    existEmoji = true;
                }

                if (faceFragment.replaceEmoji() != null) {
                    addNewEmoji(drawableSticker.getId(), faceFragment.replaceEmoji(), pos);
                    existEmoji = true;
                }
                break;
        }
        if (existEmoji) seekAndHideLayout(5);
        else seekAndHideLayout(0);
        bitmapDrawable = null;
    }

    private void addNewEmoji(int id, EmojiModel emoji, int pos) {
        bitmapDrawable = Utils.getBitmapFromAsset(this, emoji.getNameFolder(), emoji.getNameEmoji());
        Drawable drawable = new BitmapDrawable(getResources(), bitmapDrawable);
        DrawableSticker sticker = new DrawableSticker(drawable, id, false);

        for (StickerModel st : lstSticker) {
            if (st.getDrawableSticker() != null)
                if (st.getDrawableSticker().getId() == drawableSticker.getId() && !st.getDrawableSticker().isImage()) {
                    sticker.setAlpha(drawableSticker.getAlpha());
                }
        }

        switch (pos) {
            case 0:
                stickerView.addSticker(sticker);
            case 1:
                stickerView.replace(sticker, true);
                break;
        }
        lstSticker.add(new StickerModel(null, null, bitmapDrawable, null, sticker, -1, -1));
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
        bearFragment = EmojiBearFragment.newInstance("bear");
        addFragmentsAdapter.addFrag(bearFragment);
        faceFragment = EmojiFaceFragment.newInstance("face");
        addFragmentsAdapter.addFrag(faceFragment);
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

    //transfrom
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

        ArrayList<ColorModel> lstColor = new ArrayList<>(setListColor());

        ColorAdapter colorAdapter = new ColorAdapter(this, R.layout.item_color_edit, (o, pos) -> {
            ColorModel color = (ColorModel) o;
            if (pos == 0) pickColor();
            else {
                if (color.getColorStart() == color.getColorEnd()) {

                    addColorTextModel(color);

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

            addColorTextModel(color);

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

            addColorTextModel(color);

            textSticker.setTextColor(color);
            stickerView.replace(textSticker, true);

            GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{colorPicker.getSelectedColor(), colorPicker.getSelectedColor()});
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
            gradientDrawable.setCornerRadius(10f);
            ivColor.setBackground(gradientDrawable);
            dialog.cancel();
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
        if (textSticker != null) sticker.setAlpha(textSticker.getAlpha());
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
        lstSticker.add(new StickerModel(textModel, null, null, sticker, null, -1, -1));
        Log.d("2tdp", "lstSticker: " + lstSticker.size());
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

    //del
    private void delStick() {
        if (textSticker == null && drawableSticker == null) {
            Utils.showToast(this, getResources().getString(R.string.choose_sticker_text));
            return;
        }
        if (textSticker != null) {
            StickerModel stickerModel = null;
            for (StickerModel st : lstSticker) {
                if (st.getTextSticker() != null)
                    if (st.getTextSticker().getId() == textSticker.getId()) stickerModel = st;
            }
            lstSticker.remove(stickerModel);
            stickerView.remove(textSticker);
            textSticker = null;
            seekAndHideLayout(0);
            Log.d("2tdp", "lstSticker: " + lstSticker.size());
            return;
        }
        StickerModel stickerModel = null;
        for (StickerModel st : lstSticker) {
            if (st.getDrawableSticker().getId() == drawableSticker.getId()) stickerModel = st;
        }
        lstSticker.remove(stickerModel);
        stickerView.remove(drawableSticker);
        seekAndHideLayout(0);
        Log.d("2tdp", "lstSticker: " + lstSticker.size());
    }

    private void getData() {
        String strUri = DataLocalManager.getOption("bitmap");
        if (!isBackground) vMain.setSize(0);
        else vMain.setSize(sizeMain);
        if (!strUri.equals("")) {
            try {
                bitmap = modifyOrientation(MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(strUri)), Uri.parse(strUri));
                if (bitmap != null) vMain.setData(bitmap, null);

            } catch (IOException e) {
                e.printStackTrace();
            }
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

    public Bitmap modifyOrientation(Bitmap bitmap, Uri uri) throws IOException {
        InputStream is = getContentResolver().openInputStream(uri);
        ExifInterface ei = new ExifInterface(is);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        Log.e("2tdpppp", "modifyOrientation:  " + orientation);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateBitmap(bitmap, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateBitmap(bitmap, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateBitmap(bitmap, 270);
//            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
//                ivFilter.setRotation(180);
//                break;
//            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
//                return flip(bitmap, false, true);
            default:
                return bitmap;
        }
    }

    private Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
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

                if (rlExpandLayer.getVisibility() == View.VISIBLE) {
                    rlExpandLayer.startAnimation(animation);
                    rlExpandLayer.setVisibility(View.GONE);
                }

                if (rlExpandEditText.getVisibility() == View.VISIBLE) {
                    rlExpandEditText.startAnimation(animation);
                    rlExpandEditText.setVisibility(View.GONE);
                }

                if (rlExpandEditEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEditEmoji.startAnimation(animation);
                    rlExpandEditEmoji.setVisibility(View.GONE);
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

                if (rlExpandEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEmoji.startAnimation(animation);
                    rlExpandEmoji.setVisibility(View.GONE);
                }

                if (rlExpandEditText.getVisibility() == View.VISIBLE) {
                    rlExpandEditText.startAnimation(animation);
                    rlExpandEditText.setVisibility(View.GONE);
                }

                if (rlExpandEditImage.getVisibility() == View.VISIBLE) {
                    rlExpandEditImage.startAnimation(animation);
                    rlExpandEditImage.setVisibility(View.GONE);
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

                if (rlExpandEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEmoji.startAnimation(animation);
                    rlExpandEmoji.setVisibility(View.GONE);
                }

                if (rlExpandEditText.getVisibility() == View.VISIBLE) {
                    rlExpandEditText.startAnimation(animation);
                    rlExpandEditText.setVisibility(View.GONE);
                }

                if (rlExpandEditEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEditEmoji.startAnimation(animation);
                    rlExpandEditEmoji.setVisibility(View.GONE);
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

                if (rlExpandEditText.getVisibility() == View.VISIBLE) {
                    rlExpandEditText.startAnimation(animation);
                    rlExpandEditText.setVisibility(View.GONE);
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlExpandEditBackground.getVisibility() == View.GONE) {
                    rlExpandEditBackground.startAnimation(animation);
                    rlExpandEditBackground.setVisibility(View.VISIBLE);
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

        lstSticker = new ArrayList<>();
        lstFilterBlend = new ArrayList<>();

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
}