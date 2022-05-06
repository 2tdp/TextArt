package com.datnt.textart.fragment.splash;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.datnt.textart.R;
import com.datnt.textart.utils.Utils;

public class ThreeSplashFragment extends Fragment {

    public ThreeSplashFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_three_splash, container, false);
    }
}