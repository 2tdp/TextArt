package com.datnt.textart.fragment.vip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.datnt.textart.R;
import com.datnt.textart.utils.Utils;

public class VipTwoFragment extends Fragment {

    private ImageView ivBack;

    public static VipTwoFragment newInstance() {
        return new VipTwoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vip_two, container, false);

        init(view);

        evenClick();
        return view;
    }

    private void evenClick() {
        ivBack.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());
    }

    private void init(View view) {
        ivBack = view.findViewById(R.id.ivBack);
    }
}