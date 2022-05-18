package com.datnt.textart.fragment.create;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.datnt.textart.R;
import com.datnt.textart.activity.edit.EditActivity;
import com.datnt.textart.adapter.BucketAdapter;
import com.datnt.textart.adapter.RecentAdapter;
import com.datnt.textart.data.DataPic;
import com.datnt.textart.model.BucketPicModel;
import com.datnt.textart.model.PicModel;
import com.datnt.textart.sharepref.DataLocalManager;

import java.util.ArrayList;

public class RecentFragment extends Fragment {

    private RelativeLayout rlExpand;
    private RecyclerView rcvPicRecent, rcvBucket;
    private RecentAdapter recentAdapter;
    private BucketAdapter bucketAdapter;
    private View vBg;
    private ImageView ivExpand;

    public static RecentFragment newInstance() {
        RecentFragment fragment = new RecentFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent, container, false);

        init(view);

        return view;
    }

    private void init(View view) {
        rcvPicRecent = view.findViewById(R.id.rcvPicRecent);
        rcvBucket = view.findViewById(R.id.rcvBucketPic);
        rlExpand = view.findViewById(R.id.rlExpand);
        vBg = view.findViewById(R.id.viewBg);

        rlExpand.getLayoutParams().height = getResources().getDisplayMetrics().heightPixels * 60 / 100;

        setUpLayout();
        evenClick();
    }

    private void evenClick() {
        vBg.setOnClickListener(v -> setExpand(ivExpand, ""));
    }

    private void setUpLayout() {
        setUpPic();
        setUpBucket();
    }

    public void setExpand(ImageView ivExpand, String click) {
        this.ivExpand = ivExpand;
        checkExpand(ivExpand, rlExpand.getVisibility() == View.VISIBLE, click);
    }

    private void checkExpand(ImageView ivExpand, boolean check, String click) {
        if (click.equals("")) {
            if (ivExpand != null) {
                if (check) {
                    vBg.setVisibility(View.GONE);
                    rlExpand.setVisibility(View.GONE);
                    ivExpand.setImageResource(R.drawable.ic_bottom_pink);
                    rlExpand.setAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up));
                } else {
                    vBg.setVisibility(View.VISIBLE);
                    rlExpand.setVisibility(View.VISIBLE);
                    ivExpand.setImageResource(R.drawable.ic_top_pink);
                    rlExpand.setAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_down));
                }
            } else {
                if (check) {
                    vBg.setVisibility(View.GONE);
                    rlExpand.setVisibility(View.GONE);
                    rlExpand.setAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up));
                }
            }
        } else {
            if (check) {
                vBg.setVisibility(View.GONE);
                rlExpand.setVisibility(View.GONE);
                ivExpand.setImageResource(R.drawable.ic_bottom_pink);
                rlExpand.setAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up));
            }
        }
    }

    private void setUpBucket() {
        ArrayList<BucketPicModel> lstBucket = new ArrayList<>(DataPic.getBucketPictureList(requireContext()));

        bucketAdapter = new BucketAdapter(requireContext(), (Object o, int pos) -> {
            BucketPicModel bucket = (BucketPicModel) o;
            recentAdapter.setData(bucket.getLstPic());
            recentAdapter.notifyChange();
            setExpand(ivExpand, "");
        });
        if (!lstBucket.isEmpty()) bucketAdapter.setData(lstBucket);

        LinearLayoutManager manager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rcvBucket.setLayoutManager(manager);
        rcvBucket.setAdapter(bucketAdapter);
    }

    private void setUpPic() {
        ArrayList<PicModel> lstPic = new ArrayList<>(DataPic.getAllPictureList(requireContext()));

        recentAdapter = new RecentAdapter(requireContext(), (Object o, int pos) -> {
            PicModel pic = (PicModel) o;
            Intent intent = new Intent(requireActivity(), EditActivity.class);
            intent.putExtra("bitmap", pic.getUri());
            startActivity(intent);
            DataLocalManager.setOption("bitmap", "bitmap");
        });
        if (!lstPic.isEmpty()) recentAdapter.setData(lstPic);

        GridLayoutManager manager = new GridLayoutManager(requireContext(), 3);
        rcvPicRecent.setLayoutManager(manager);
        rcvPicRecent.setAdapter(recentAdapter);
    }
}