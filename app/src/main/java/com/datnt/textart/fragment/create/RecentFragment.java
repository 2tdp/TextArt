package com.datnt.textart.fragment.create;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.datnt.textart.R;
import com.datnt.textart.adapter.BucketAdapter;
import com.datnt.textart.adapter.RecentAdapter;
import com.datnt.textart.data.DataPic;
import com.datnt.textart.model.BucketPicModel;
import com.datnt.textart.model.PicModel;

import java.util.ArrayList;

public class RecentFragment extends Fragment {

    private RelativeLayout rlExpand;
    private RecyclerView rcvPic, rcvBucket;
    private RecentAdapter recentAdapter;
    private BucketAdapter bucketAdapter;
    private View vExpand, vBg;
    private ImageView ivExpand;
    private ViewPager2 viewPager;

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
        rcvPic = view.findViewById(R.id.rcvPic);
        rcvBucket = view.findViewById(R.id.rcvBucketPic);
        rlExpand = view.findViewById(R.id.rlExpand);
        vExpand = view.findViewById(R.id.viewExpand);
        vBg = view.findViewById(R.id.viewBg);

        rlExpand.getLayoutParams().height = (int) getResources().getDisplayMetrics().heightPixels * 60 / 100;

        setUpLayout();
        evenClick();
    }

    private void evenClick() {
        vBg.setOnClickListener(v -> setExpand(ivExpand));

//        rcvPic.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//            @Override
//            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
//                int touch = 0;
//                switch (e.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        viewPager.setUserInputEnabled(false);
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        viewPager.setUserInputEnabled(true);
//                        break;
//                }
//                return false;
//            }
//
//            @Override
//            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
//
//            }
//
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//            }
//        });
    }

    private void setUpLayout() {
        setUpPic();
        setUpBucket();
    }

    public void setExpand(ImageView ivExpand) {
        this.ivExpand = ivExpand;
        checkExpand(ivExpand, rlExpand.getVisibility() == View.VISIBLE);
    }

    private void checkExpand(ImageView ivExpand, boolean check) {
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
    }

    private void setUpBucket() {
        ArrayList<BucketPicModel> lstBucket = new ArrayList<>(DataPic.getBucketPictureList(requireContext()));

        bucketAdapter = new BucketAdapter(requireContext(), (Object o, int pos) -> {
            BucketPicModel bucket = (BucketPicModel) o;
            recentAdapter.setData(bucket.getLstPic());
            recentAdapter.notifyChange();
            setExpand(ivExpand);
        });
        if (!lstBucket.isEmpty()) bucketAdapter.setData(lstBucket);

        LinearLayoutManager manager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rcvBucket.setLayoutManager(manager);
        rcvBucket.setAdapter(bucketAdapter);
    }

    private void setUpPic() {
        ArrayList<PicModel> lstPic = new ArrayList<>(DataPic.getAllPictureList(requireContext()));

        recentAdapter = new RecentAdapter(requireContext(), (Object o, int pos) -> {
        });
        if (!lstPic.isEmpty()) recentAdapter.setData(lstPic);

        GridLayoutManager manager = new GridLayoutManager(requireContext(), 3);
        rcvPic.setLayoutManager(manager);
        rcvPic.setAdapter(recentAdapter);
    }

    public ViewPager2 getViewPager() {
        return viewPager;
    }

    public void setViewPager(ViewPager2 viewPager) {
        this.viewPager = viewPager;
    }
}