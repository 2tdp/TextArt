package com.datnt.textart.fragment.create;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.datnt.textart.R;

public class MyAppFragment extends Fragment {

    public MyAppFragment() {
        // Required empty public constructor
    }

    public static MyAppFragment newInstance() {
        MyAppFragment fragment = new MyAppFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_app, container, false);
    }
}