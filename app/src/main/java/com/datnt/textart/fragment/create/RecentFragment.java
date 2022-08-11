package com.datnt.textart.fragment.create;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.datnt.textart.R;
import com.datnt.textart.activity.RequestPermissionActivity;
import com.datnt.textart.activity.edit.EditActivity;
import com.datnt.textart.adapter.BucketAdapter;
import com.datnt.textart.adapter.RecentAdapter;
import com.datnt.textart.callback.ICheckTouch;
import com.datnt.textart.callback.IClickFolder;
import com.datnt.textart.data.DataPic;
import com.datnt.textart.model.picture.BucketPicModel;
import com.datnt.textart.model.picture.PicModel;
import com.datnt.textart.sharepref.DataLocalManager;
import com.datnt.textart.utils.Utils;

import java.util.ArrayList;

public class RecentFragment extends Fragment {

    private ArrayList<BucketPicModel> lstBucket;
    private boolean isBackground;

    private RelativeLayout rlExpand;
    private RecyclerView rcvPicRecent, rcvBucket;
    private RecentAdapter recentAdapter;
    private View vBg;
    private ImageView ivExpand;
    private Animation animation;
    private IClickFolder clickFolder;
    private ICheckTouch clickTouch;

    public static RecentFragment newInstance(boolean isBG) {
        RecentFragment fragment = new RecentFragment();
        Bundle args = new Bundle();
        args.putBoolean("isBG", isBG);
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

        lstBucket = new ArrayList<>(DataLocalManager.getListBucket("bucket"));

        if (getArguments() != null) isBackground = getArguments().getBoolean("isBG");

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
                    animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up);
                    rlExpand.startAnimation(animation);
                    vBg.setVisibility(View.GONE);
                    rlExpand.setVisibility(View.GONE);
                    ivExpand.setImageResource(R.drawable.ic_bottom_pink);
                } else {
                    animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_down);
                    rlExpand.startAnimation(animation);
                    vBg.setVisibility(View.VISIBLE);
                    rlExpand.setVisibility(View.VISIBLE);
                    ivExpand.setImageResource(R.drawable.ic_top_pink);
                }
            } else {
                if (check) {
                    animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up);
                    rlExpand.startAnimation(animation);
                    vBg.setVisibility(View.GONE);
                    rlExpand.setVisibility(View.GONE);
                }
            }
        } else {
            if (check) {
                animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up);
                rlExpand.startAnimation(animation);
                vBg.setVisibility(View.GONE);
                rlExpand.setVisibility(View.GONE);
                ivExpand.setImageResource(R.drawable.ic_bottom_pink);
            }
        }
        if (animation != null)
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    rlExpand.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
    }

    private void setUpBucket() {
        BucketAdapter bucketAdapter = new BucketAdapter(requireContext(), (Object o, int pos) -> {
            BucketPicModel bucket = (BucketPicModel) o;
            recentAdapter.setData(bucket.getLstPic());
            recentAdapter.notifyChange();
            setExpand(ivExpand, "");
            clickFolder.clickFolder(bucket.getBucket());
        });
        if (!lstBucket.isEmpty()) bucketAdapter.setData(lstBucket);

        LinearLayoutManager manager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rcvBucket.setLayoutManager(manager);
        rcvBucket.setAdapter(bucketAdapter);
    }

    private void setUpPic() {
        recentAdapter = new RecentAdapter(requireContext(), (Object o, int pos) -> {
            PicModel pic = (PicModel) o;
            DataLocalManager.setOption(pic.getUri(), "bitmap");
            DataLocalManager.setOption("", "bitmap_myapp");
            if (!isBackground)
                Utils.setIntent(requireActivity(), EditActivity.class.getName());
            else clickTouch.checkTouch(true);
        });
        if (!lstBucket.isEmpty()) recentAdapter.setData(lstBucket.get(0).getLstPic());

        GridLayoutManager manager = new GridLayoutManager(requireContext(), 3);
        rcvPicRecent.setLayoutManager(manager);
        rcvPicRecent.setAdapter(recentAdapter);
    }

    public void changeFolder(IClickFolder clickFolder) {
        this.clickFolder = clickFolder;
    }

    public void finish(ICheckTouch clickTouch) {
        this.clickTouch = clickTouch;
    }
}