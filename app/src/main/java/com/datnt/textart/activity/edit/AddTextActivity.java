package com.datnt.textart.activity.edit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datnt.textart.R;
import com.datnt.textart.utils.Utils;

public class AddTextActivity extends AppCompatActivity {

    private ImageView ivBack, ivTick, ivLeft, ivCenter, ivRight;
    private TextView tvQuotes, tvFonts, tvStyle, tvFavorite, tvYourFont, tvNewFonts, tvItalic, tvRegular, tvSemiBold, tvBold, tvBoldItalic;
    private EditText etText;
    private RecyclerView rcvQuotes, rcvQuotesTitle, rcvFonts;
    private RelativeLayout rlFonts, rlQuotes, rlText;
    private LinearLayout llStyleFont;
    private Animation animation;

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

        rlText.setOnClickListener(v -> Utils.hideKeyboard(this, etText));

        ivLeft.setOnClickListener(v -> changeStateAlignText(0));
        ivCenter.setOnClickListener(v -> changeStateAlignText(1));
        ivRight.setOnClickListener(v -> changeStateAlignText(2));

        tvQuotes.setOnClickListener(v -> changeStateText(0));
        tvFonts.setOnClickListener(v -> changeStateText(1));
        tvStyle.setOnClickListener(v -> changeStateText(2));
    }

    private void setUpQuotes() {

    }

    private void setUpFonts() {

    }

    private void setUpStyle() {

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
                etText.setGravity(Gravity.START);
                ivLeft.setImageResource(R.drawable.ic_text_align_left_select);
                ivCenter.setImageResource(R.drawable.ic_text_align_center);
                ivRight.setImageResource(R.drawable.ic_text_align_right);
                break;
            case 1:
                etText.setGravity(Gravity.CENTER);
                ivLeft.setImageResource(R.drawable.ic_text_align_left);
                ivCenter.setImageResource(R.drawable.ic_text_align_center_select);
                ivRight.setImageResource(R.drawable.ic_text_align_right);
                break;
            case 2:
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
        tvItalic = findViewById(R.id.tvItalic);
        tvRegular = findViewById(R.id.tvRegular);
        tvSemiBold = findViewById(R.id.tvSemiBold);
        tvBold = findViewById(R.id.tvBold);
        tvBoldItalic = findViewById(R.id.tvBoldItalic);
        rcvFonts = findViewById(R.id.rcvFonts);
        rcvQuotesTitle = findViewById(R.id.rcvQuotesTitle);
        rcvQuotes = findViewById(R.id.rcvQuotes);
        rlFonts = findViewById(R.id.rlFonts);
        rlQuotes = findViewById(R.id.rlQuotes);
        rlText = findViewById(R.id.rlText);
        llStyleFont = findViewById(R.id.llStyleFont);

        changeStateText(0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.setAnimExit(this);
    }
}