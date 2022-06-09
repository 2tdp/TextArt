package com.datnt.textart.fragment.decor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.datnt.textart.R;
import com.datnt.textart.adapter.DecorAdapter;
import com.datnt.textart.callback.ICallBackItem;
import com.datnt.textart.data.DataDecor;
import com.datnt.textart.model.DecorModel;
import com.datnt.textart.utils.Utils;

import java.util.ArrayList;

public class DecorDrawFragment extends Fragment {

    private String nameDecor;
    private RecyclerView rcvDecor;

    private ICallBackItem callBack;

    public static DecorDrawFragment newInstance(String nameDecor) {
        DecorDrawFragment fragment = new DecorDrawFragment();
        Bundle args = new Bundle();
        args.putString("draw", nameDecor);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            nameDecor = getArguments().getString("draw");
        }
    }

    private void setUpData(String nameDecor) {
        DecorAdapter decorAdapter = new DecorAdapter(requireContext(), (o, pos) -> {
            callBack.callBackItem(o, pos);
            Utils.showToast(requireContext(), getString(R.string.success));
        });

        decorAdapter.setData(DataDecor.getTitleDecor(requireContext(), nameDecor));
        GridLayoutManager manager = new GridLayoutManager(requireContext(), 3);
        rcvDecor.setLayoutManager(manager);
        rcvDecor.setAdapter(decorAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_decor_draw, container, false);
        init(view);
        setUpData(nameDecor);
        return view;
    }

    private void init(View view) {
        rcvDecor = view.findViewById(R.id.rcvDecor);
    }

    public void getDecor(ICallBackItem callBack) {
        this.callBack = callBack;
    }
}