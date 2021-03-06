package com.datnt.textart.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.datnt.textart.R;
import com.datnt.textart.adapter.RecentAdapter;
import com.datnt.textart.callback.ICallBackItem;
import com.datnt.textart.data.DataPic;
import com.datnt.textart.sharepref.DataLocalManager;
import com.datnt.textart.utils.Utils;

public class ImageFragment extends Fragment {

    private ImageView ivBack;
    private RecyclerView rcvPickImage;

    private RecentAdapter recentAdapter;
    private ICallBackItem callBack;

    public ImageFragment(ICallBackItem callBack) {
        this.callBack = callBack;
    }

    public static ImageFragment newInstance(ICallBackItem callBack) {
        return new ImageFragment(callBack);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image, container, false);

        init(v);
        evenClick();

        return v;
    }

    private void evenClick() {
        ivBack.setOnClickListener(v -> Utils.clearBackStack(requireActivity().getSupportFragmentManager()));

        recentAdapter = new RecentAdapter(requireContext(), (o, pos) -> {
            callBack.callBackItem(o, pos);
            Utils.clearBackStack(requireActivity().getSupportFragmentManager());
        });

        recentAdapter.setData(DataPic.getAllPictureList(requireContext()));
        GridLayoutManager manager = new GridLayoutManager(requireContext(), 3);

        rcvPickImage.setLayoutManager(manager);
        rcvPickImage.setAdapter(recentAdapter);
    }

    private void init(View view) {
        ivBack = view.findViewById(R.id.ivBack);
        rcvPickImage = view.findViewById(R.id.rcvPickImage);
    }
}