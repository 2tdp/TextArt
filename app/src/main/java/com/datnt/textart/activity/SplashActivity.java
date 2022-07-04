package com.datnt.textart.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.datnt.textart.R;
import com.datnt.textart.adapter.ViewPagerAddFragmentsAdapter;
import com.datnt.textart.fragment.splash.OneSplashFragment;
import com.datnt.textart.fragment.splash.ThreeSplashFragment;
import com.datnt.textart.fragment.splash.TwoSplashFragment;
import com.datnt.textart.sharepref.DataLocalManager;
import com.datnt.textart.utils.Utils;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private DotsIndicator indicator;
    private TextView tvSplash;

    private final ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            if (position == 2) tvSplash.setText(Utils.underLine(getString(R.string.start)));
            else tvSplash.setText(Utils.underLine(getString(R.string.next)));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarTransparent(this);
        setContentView(R.layout.splash_activity);

        init();

        FirstInstall();
    }

    private void init() {
        viewPager2 = findViewById(R.id.vpSplash);
        indicator = findViewById(R.id.dots_indicator);
        tvSplash = findViewById(R.id.tvSplash);

        setUpViewPager();

        tvSplash.setOnClickListener(view -> {
            if (viewPager2.getCurrentItem() == 2) {
                Utils.setIntent(this, RequestPermissionActivity.class.getName());
                finish();
            } else {
                viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1, true);
            }
        });
    }

    private void setUpViewPager() {
        ViewPagerAddFragmentsAdapter viewPagerAdapter = new ViewPagerAddFragmentsAdapter(getSupportFragmentManager(), getLifecycle());

        OneSplashFragment oneSplashFragment = new OneSplashFragment();
        TwoSplashFragment twoSplashFragment = new TwoSplashFragment();
        ThreeSplashFragment threeSplashFragment = new ThreeSplashFragment();

        viewPagerAdapter.addFrag(oneSplashFragment);
        viewPagerAdapter.addFrag(twoSplashFragment);
        viewPagerAdapter.addFrag(threeSplashFragment);

        viewPager2.unregisterOnPageChangeCallback(onPageChangeCallback);
        viewPager2.registerOnPageChangeCallback(onPageChangeCallback);

        viewPager2.setAdapter(viewPagerAdapter);
        indicator.setViewPager2(viewPager2);
    }

    private void FirstInstall() {
        if (!DataLocalManager.getFirstInstall("first")) {
            DataLocalManager.setFirstInstall("first", true);
        } else {
            Utils.setIntent(this, RequestPermissionActivity.class.getName());
            finish();
        }
    }
}
