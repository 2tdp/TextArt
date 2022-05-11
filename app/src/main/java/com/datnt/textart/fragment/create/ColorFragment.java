package com.datnt.textart.fragment.create;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.datnt.textart.R;
import com.datnt.textart.adapter.ColorAdapter;
import com.datnt.textart.callback.ICallBackItem;
import com.datnt.textart.model.ColorModel;

import java.util.ArrayList;

public class ColorFragment extends Fragment {

    private RecyclerView rcvColor;
    private ColorAdapter colorAdapter;

    public static ColorFragment newInstance() {
        ColorFragment fragment = new ColorFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_color, container, false);

        init(view);

        return view;
    }

    private void init(View view) {
        rcvColor = view.findViewById(R.id.rcvColor);

        setUpLayout();
        evenClick();
    }

    private void evenClick() {

    }

    private void setUpLayout() {
        ArrayList<ColorModel> lstColor = new ArrayList<>(setListColor());

        colorAdapter = new ColorAdapter(requireContext(), (o, pos) -> {
        });
        colorAdapter.setData(lstColor);

        GridLayoutManager manager = new GridLayoutManager(requireContext(), 4);
        rcvColor.setLayoutManager(manager);
        rcvColor.setAdapter(colorAdapter);
    }

    private ArrayList<ColorModel> setListColor() {
        ArrayList<ColorModel> lstColor = new ArrayList<>();

        int[] arrColor = getResources().getIntArray(R.array.lstColor);

        int direction = 0;
        for (int i = arrColor.length - 1; i >= 0; i--) {
            if (i < 70) lstColor.add(new ColorModel(1, arrColor[i], arrColor[i], true));
            else {
                lstColor.add(new ColorModel(direction, arrColor[i], arrColor[i - 1], true));
                i--;
                if (direction < 4) direction++;
                else direction = 0;
            }
        }

        lstColor.add(0, new ColorModel(1, 0, 0, false));

        return lstColor;
    }

}