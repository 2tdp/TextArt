package com.datnt.textart.fragment.create;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.datnt.textart.R;
import com.datnt.textart.adapter.MyAppAdapter;
import com.datnt.textart.adapter.RecentAdapter;
import com.datnt.textart.data.DataPic;
import com.datnt.textart.model.PicModel;

import java.util.ArrayList;

public class MyAppFragment extends Fragment {

    private RecyclerView rcvPicMyApp;
    private MyAppAdapter myAppAdapter;

    public static MyAppFragment newInstance() {
        MyAppFragment fragment = new MyAppFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_app, container, false);

        init(view);

        return view;
    }

    private void init(View view) {
        rcvPicMyApp = view.findViewById(R.id.rcvPicMyApp);

        setUpLayout();
        evenClick();
    }

    private void evenClick() {

    }

    private void setUpLayout() {
        ArrayList<PicModel> lstPic = new ArrayList<>();

        myAppAdapter = new MyAppAdapter(requireContext(), (Object o, int pos) -> {
        });

        GridLayoutManager manager = new GridLayoutManager(requireContext(), 3);
        rcvPicMyApp.setLayoutManager(manager);
        rcvPicMyApp.setAdapter(myAppAdapter);
    }
}