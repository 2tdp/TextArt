package com.datnt.textart.activity.edit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datnt.textart.R;
import com.datnt.textart.activity.base.BaseActivity;
import com.datnt.textart.adapter.FontsAdapter;
import com.datnt.textart.adapter.StyleFontAdapter;
import com.datnt.textart.data.DataFont;
import com.datnt.textart.model.textsticker.FontModel;
import com.datnt.textart.model.textsticker.StyleFontModel;
import com.datnt.textart.model.textsticker.TextModel;
import com.datnt.textart.utils.Utils;

import java.util.ArrayList;

public class AddTextActivity extends BaseActivity {

    private ImageView ivBack, ivTick, ivLeft, ivCenter, ivRight;
    private TextView tvQuotes, tvFonts, tvStyle, tvFavorite, tvYourFont, tvNewFonts, tvClear;
    private EditText etText;
    private RecyclerView rcvQuotes, rcvQuotesTitle, rcvFonts, rcvStyleFont;
    private RelativeLayout rlFonts, rlQuotes, rlText;
    private LinearLayout llStyleFont;
    private Animation animation;
    private FontModel font;
    private TextModel textModel;
    private StyleFontAdapter styleFontAdapter;
    private ArrayList<FontModel> lstFont;
    private int positionStyleFont, positionFont, posGravity;
    private boolean check, isEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_text);

        init();
    }

    private void init() {
        setUpLayout();
        evenClick();
    }

    private void evenClick() {
        ivBack.setOnClickListener(v -> onBackPressed());
        tvClear.setOnClickListener(v -> etText.setText(""));

        rlText.setOnClickListener(v -> Utils.hideKeyboard(this, etText));

        ivLeft.setOnClickListener(v -> changeStateAlignText(0));
        ivCenter.setOnClickListener(v -> changeStateAlignText(1));
        ivRight.setOnClickListener(v -> changeStateAlignText(2));

        tvQuotes.setOnClickListener(v -> changeStateText(0));
        tvFonts.setOnClickListener(v -> changeStateText(1));
        tvStyle.setOnClickListener(v -> changeStateText(2));

        ivTick.setOnClickListener(v -> clickTick());
    }

    private void clickTick() {
        String text = etText.getText().toString();
        if (text.equals("")) {
            Utils.showToast(this, getResources().getString(R.string.pls_enter_text));
        } else {
            if (!isEditText)
                textModel = new TextModel(text, null, font, posGravity, null, null, null, 255);
            else {
                textModel.setContent(text);
                textModel.setFontModel(font);
                textModel.setTypeAlign(posGravity);
            }
            Intent returnIntent = new Intent();
            returnIntent.putExtra("text", textModel);
            setResult(Activity.RESULT_OK, returnIntent);
            Utils.hideKeyboard(this, ivTick);
            onBackPressed();
        }
    }

    private void setUpQuotes() {

    }

    private void setUpFonts() {
        FontsAdapter fontsAdapter = new FontsAdapter(this, (o, pos) -> {
            font = (FontModel) o;
            etText.setTypeface(Utils.getTypeFace(font.getNameFont(), font.getLstStyle().get(0).getName(), this));
            font.getLstStyle().get(0).setSelected(true);
            if (styleFontAdapter != null) styleFontAdapter.setCurrent(0);
            positionStyleFont = 0;
            check = true;
        });
        fontsAdapter.setData(lstFont);
        if (!check) getCurrentStyleFont(lstFont);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvFonts.setLayoutManager(manager);
        rcvFonts.setAdapter(fontsAdapter);
        rcvFonts.scrollToPosition(positionFont);
    }

    private void setUpStyle() {
        styleFontAdapter = new StyleFontAdapter(this, (o, pos) -> {
            StyleFontModel styleFont = (StyleFontModel) o;
            etText.setTypeface(Utils.getTypeFace(styleFont.getFont(), styleFont.getName(), this));
            positionStyleFont = pos + 2;
            check = true;
        });
        if (font != null) styleFontAdapter.setData(font.getLstStyle());
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvStyleFont.setLayoutManager(manager);
        rcvStyleFont.setAdapter(styleFontAdapter);
        rcvStyleFont.scrollToPosition(positionStyleFont);
    }

    private void getCurrentStyleFont(ArrayList<FontModel> lstFont) {
        for (int j = 0; j < lstFont.size(); j++) {
            FontModel f = lstFont.get(j);
            if (f.getNameFont().equals("poppins")) {
                font = f;
                font.setSelected(true);
                positionFont = j + 2;
                for (int i = 0; i < font.getLstStyle().size(); i++) {
                    StyleFontModel style = font.getLstStyle().get(i);
                    if (style.getName().trim().equals("Regular")) {
                        style.setSelected(true);
                        positionStyleFont = i + 2;
                    }
                }
            }
        }
    }

    private void changeLayout(int pos) {
        Utils.hideKeyboard(this, etText);
        switch (pos) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlQuotes.getVisibility() == View.GONE) {
                    rlQuotes.startAnimation(animation);
                    rlQuotes.setVisibility(View.VISIBLE);
                    setUpQuotes();
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
                if (rlFonts.getVisibility() == View.VISIBLE) {
                    rlFonts.startAnimation(animation);
                    rlFonts.setVisibility(View.GONE);
                }

                if (llStyleFont.getVisibility() == View.VISIBLE) {
                    llStyleFont.startAnimation(animation);
                    llStyleFont.setVisibility(View.GONE);
                }
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlFonts.getVisibility() == View.GONE) {
                    rlFonts.startAnimation(animation);
                    rlFonts.setVisibility(View.VISIBLE);
                    setUpFonts();
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
                if (rlQuotes.getVisibility() == View.VISIBLE) {
                    rlQuotes.startAnimation(animation);
                    rlQuotes.setVisibility(View.GONE);
                }

                if (llStyleFont.getVisibility() == View.VISIBLE) {
                    llStyleFont.startAnimation(animation);
                    llStyleFont.setVisibility(View.GONE);
                }
                break;
            case 2:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (llStyleFont.getVisibility() == View.GONE) {
                    llStyleFont.startAnimation(animation);
                    llStyleFont.setVisibility(View.VISIBLE);
                    setUpStyle();
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
                if (rlQuotes.getVisibility() == View.VISIBLE) {
                    rlQuotes.startAnimation(animation);
                    rlQuotes.setVisibility(View.GONE);
                }

                if (rlFonts.getVisibility() == View.VISIBLE) {
                    rlFonts.startAnimation(animation);
                    rlFonts.setVisibility(View.GONE);
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
                    rlFonts.clearAnimation();
                    rlQuotes.clearAnimation();
                    llStyleFont.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
    }

    private void changeStateText(int pos) {
        switch (pos) {
            case 0:
                changeLayout(0);
                tvQuotes.setTextColor(getResources().getColor(R.color.white));
                tvQuotes.setBackgroundResource(R.drawable.boder_text_check);
                tvFonts.setBackgroundResource(R.drawable.boder_text);
                tvFonts.setTextColor(getResources().getColor(R.color.black));
                tvStyle.setBackgroundResource(R.drawable.boder_text);
                tvStyle.setTextColor(getResources().getColor(R.color.black));
                break;
            case 1:
                changeLayout(1);
                tvQuotes.setTextColor(getResources().getColor(R.color.black));
                tvQuotes.setBackgroundResource(R.drawable.boder_text);
                tvFonts.setBackgroundResource(R.drawable.boder_text_check);
                tvFonts.setTextColor(getResources().getColor(R.color.white));
                tvStyle.setBackgroundResource(R.drawable.boder_text);
                tvStyle.setTextColor(getResources().getColor(R.color.black));
                break;
            case 2:
                changeLayout(2);
                tvQuotes.setTextColor(getResources().getColor(R.color.black));
                tvQuotes.setBackgroundResource(R.drawable.boder_text);
                tvFonts.setBackgroundResource(R.drawable.boder_text);
                tvFonts.setTextColor(getResources().getColor(R.color.black));
                tvStyle.setBackgroundResource(R.drawable.boder_text_check);
                tvStyle.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }

    private void changeStateAlignText(int pos) {
        switch (pos) {
            case 0:
                posGravity = 0;
                etText.setGravity(Gravity.START);
                ivLeft.setImageResource(R.drawable.ic_text_align_left_select);
                ivCenter.setImageResource(R.drawable.ic_text_align_center);
                ivRight.setImageResource(R.drawable.ic_text_align_right);
                break;
            case 1:
                posGravity = 1;
                etText.setGravity(Gravity.CENTER);
                ivLeft.setImageResource(R.drawable.ic_text_align_left);
                ivCenter.setImageResource(R.drawable.ic_text_align_center_select);
                ivRight.setImageResource(R.drawable.ic_text_align_right);
                break;
            case 2:
                posGravity = 2;
                etText.setGravity(Gravity.END);
                ivLeft.setImageResource(R.drawable.ic_text_align_left);
                ivCenter.setImageResource(R.drawable.ic_text_align_center);
                ivRight.setImageResource(R.drawable.ic_text_align_right_select);
                break;
        }
    }

    private void setUpLayout() {
        ivBack = findViewById(R.id.ivBack);
        ivTick = findViewById(R.id.ivTick);
        etText = findViewById(R.id.etText);
        ivLeft = findViewById(R.id.ivLeft);
        ivCenter = findViewById(R.id.ivCenter);
        ivRight = findViewById(R.id.ivRight);
        tvQuotes = findViewById(R.id.tvQuotes);
        tvFonts = findViewById(R.id.tvFonts);
        tvStyle = findViewById(R.id.tvStyle);
        tvFavorite = findViewById(R.id.tvFavorite);
        tvYourFont = findViewById(R.id.tvYourFont);
        tvNewFonts = findViewById(R.id.tvNewFonts);
        rcvFonts = findViewById(R.id.rcvFonts);
        rcvQuotesTitle = findViewById(R.id.rcvQuotesTitle);
        rcvQuotes = findViewById(R.id.rcvQuotes);
        rcvStyleFont = findViewById(R.id.rcvStyleFont);
        rlFonts = findViewById(R.id.rlFonts);
        rlQuotes = findViewById(R.id.rlQuotes);
        rlText = findViewById(R.id.rlText);
        llStyleFont = findViewById(R.id.llStyleFont);
        tvClear = findViewById(R.id.tvClear);

        textModel = (TextModel) getIntent().getSerializableExtra("text");
        if (textModel != null) {
            setUpText();
            isEditText = true;
        } else {
            tvQuotes.setVisibility(View.VISIBLE);
            tvClear.setVisibility(View.GONE);
            lstFont = DataFont.getDataFont(this);
            font = new FontModel("poppins", DataFont.getDataStyleFont(this, "poppins"), true, false, false, false);
            for (StyleFontModel f : font.getLstStyle()) {
                if (f.getName().equals("Regular")) f.setSelected(true);
            }
            changeStateText(0);
            changeStateAlignText(0);
        }
    }

    private void setUpText() {
        tvQuotes.setVisibility(View.GONE);
        tvClear.setVisibility(View.VISIBLE);
        etText.setText(textModel.getContent());

        switch (textModel.getTypeAlign()) {
            case 0:
                changeStateAlignText(0);
                etText.setGravity(Gravity.START);
                break;
            case 1:
                changeStateAlignText(1);
                etText.setGravity(Gravity.CENTER);
                break;
            case 2:
                changeStateAlignText(2);
                etText.setGravity(Gravity.END);
                break;
        }

        for (int i = 0; i < textModel.getFontModel().getLstStyle().size(); i++) {
            StyleFontModel f = textModel.getFontModel().getLstStyle().get(i);
            if (f.isSelected()) {
                positionStyleFont = i + 2;
                etText.setTypeface(Utils.getTypeFace(textModel.getFontModel().getNameFont(), f.getName(), this));
                Log.d("2tdp", "font: " + textModel.getFontModel().getNameFont() + "......" + f.getName());
                break;
            }
        }
        lstFont = DataFont.getDataFont(this);
        for (int i = 0; i < lstFont.size(); i++) {
            FontModel f = lstFont.get(i);
            if (f.getNameFont().equals(textModel.getFontModel().getNameFont())) {
                positionFont = i + 2;
                f.setSelected(true);
            }
        }
        font = textModel.getFontModel();
        check = true;
        setUpFonts();
        setUpStyle();
        changeStateText(0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.setAnimExit(this);
        finish();
    }
}