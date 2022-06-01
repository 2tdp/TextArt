package com.datnt.textart.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.datnt.textart.R;
import com.datnt.textart.callback.ICallBackItem;
import com.datnt.textart.model.FilterBlendModel;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class FilterBlendImageAdapter extends RecyclerView.Adapter<FilterBlendImageAdapter.FilterImageHolder> {

    private Context context;
    private ArrayList<FilterBlendModel> lstFilter;
    private ICallBackItem callBack;
    private int oldPosition = -1;

    public FilterBlendImageAdapter(Context context, ICallBackItem callBack) {
        this.context = context;
        this.callBack = callBack;
        lstFilter = new ArrayList<>();
    }

    public void setData(ArrayList<FilterBlendModel> lstFilter) {
        this.lstFilter = new ArrayList<>(lstFilter);
        changeNotify();
    }

    @NonNull
    @Override
    public FilterImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FilterImageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_filter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FilterImageHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstFilter.isEmpty()) return lstFilter.size();
        return 0;
    }

    class FilterImageHolder extends RecyclerView.ViewHolder {

        private final RoundedImageView ivPic;

        public FilterImageHolder(@NonNull View itemView) {
            super(itemView);
            ivPic = itemView.findViewById(R.id.ivPic);
        }

        public void onBind(int position) {
            FilterBlendModel filterBlendModel = lstFilter.get(position);
            if (filterBlendModel == null) return;

//            if (position == oldPosition)
//                ivPic.setBorderColor(context.getResources().getColor(R.color.pink));
//            else if (filterModel.isCheck())
//                ivPic.setBorderColor(context.getResources().getColor(R.color.pink));
//            else ivPic.setBorderColor(context.getResources().getColor(R.color.white));


            if (filterBlendModel.isCheck())
                ivPic.setBorderColor(context.getResources().getColor(R.color.pink));
            else
                ivPic.setBorderColor(context.getResources().getColor(R.color.white));

            if (position == 0) ivPic.setImageResource(R.drawable.ic_none);
            else ivPic.setImageBitmap(filterBlendModel.getBitmap());

            itemView.setOnClickListener(v -> {
                callBack.callBackItem(filterBlendModel, position);
                oldPosition = position;
            });
        }
    }

    public void setCurrent(int pos) {
        for (int i = 0; i < lstFilter.size(); i++) {
            lstFilter.get(i).setCheck(i == pos);
//            notifyItemChanged(i);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void changeNotify() {
        notifyDataSetChanged();
    }
}
