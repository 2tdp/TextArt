package com.datnt.textart.fragment.splash;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.datnt.textart.R;
import com.datnt.textart.utils.Utils;

import java.util.Objects;

public class OneSplashFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_splash, container, false);

        init(view);

        return view;
    }

    private void init(View view) {

    }
}