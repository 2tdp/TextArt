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
import com.datnt.textart.model.FilterModel;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class FilterImageAdapter extends RecyclerView.Adapter<FilterImageAdapter.FilterImageHolder> {

    private Context context;
    private ArrayList<FilterModel> lstFilter;
    private ICallBackItem callBack;
    private int oldPosition = -1;

    public FilterImageAdapter(Context context, ICallBackItem callBack) {
        this.context = context;
        this.callBack = callBack;
        lstFilter = new ArrayList<>();
    }

    public void setData(ArrayList<FilterModel> lstFilter) {
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
            FilterModel filterModel = lstFilter.get(position);
            if (filterModel == null) return;

            if (filterModel.isCheck() && position == oldPosition)
                ivPic.setBorderColor(context.getResources().getColor(R.color.pink));
            else
                ivPic.setBorderColor(context.getResources().getColor(R.color.white));

            if (position == 0) ivPic.setImageResource(R.drawable.ic_none);
            else ivPic.setImageBitmap(filterModel.getBitmap());

            itemView.setOnClickListener(v -> {
                callBack.callBackItem(filterModel, position);
                oldPosition = position;
            });
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void changeNotify() {
        notifyDataSetChanged();
    }
}
