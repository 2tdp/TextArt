package com.datnt.textart.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.datnt.textart.R;
import com.datnt.textart.callback.ICallBackItem;
import com.datnt.textart.customview.CustomColor;
import com.datnt.textart.model.ColorModel;

import java.util.ArrayList;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorHolder> {

    private Context context;
    private ArrayList<ColorModel> lstColor;
    private ICallBackItem callBack;

    public ColorAdapter(Context context, ICallBackItem callBack) {
        this.context = context;
        this.callBack = callBack;
    }

    public void setData(ArrayList<ColorModel> lstColor) {
        this.lstColor = new ArrayList<>(lstColor);
        notifyChange();
    }

    @NonNull
    @Override
    public ColorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ColorHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_color, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ColorHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstColor.isEmpty()) return lstColor.size();
        return 0;
    }

    class ColorHolder extends RecyclerView.ViewHolder {

        CustomColor customColor;
        ImageView ivPick;

        public ColorHolder(@NonNull View itemView) {
            super(itemView);

            customColor = itemView.findViewById(R.id.customColor);
            ivPick = itemView.findViewById(R.id.ivPick);
        }

        public void onBind(int position) {
            ColorModel color = lstColor.get(position);
            if (color == null) return;

            itemView.setOnClickListener(v -> callBack.callBackItem(color, position));

            if (!color.isColor()) {
                customColor.setVisibility(View.GONE);
                ivPick.setVisibility(View.VISIBLE);
            } else {
                ivPick.setVisibility(View.GONE);
                customColor.setVisibility(View.VISIBLE);
                customColor.setColor(color);
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyChange() {
        notifyDataSetChanged();
    }
}
