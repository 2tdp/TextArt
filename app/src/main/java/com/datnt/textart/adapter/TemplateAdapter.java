package com.datnt.textart.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.datnt.textart.R;
import com.datnt.textart.callback.ICallBackItem;
import com.datnt.textart.customview.CustomViewPathData;
import com.datnt.textart.model.TemplateModel;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.TemplateHolder> {

    private Context context;
    private int layoutResource;
    private ArrayList<TemplateModel> lstTemp;
    private ICallBackItem callBack;

    public TemplateAdapter(Context context, int layoutResource, ICallBackItem callBack) {
        this.context = context;
        this.layoutResource = layoutResource;
        this.callBack = callBack;
        lstTemp = new ArrayList<>();
    }

    public void setData(ArrayList<TemplateModel> lstTemp) {
        this.lstTemp = lstTemp;
        changeNotify();
    }

    @NonNull
    @Override
    public TemplateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TemplateHolder(LayoutInflater.from(parent.getContext()).inflate(layoutResource, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TemplateHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstTemp.isEmpty()) return lstTemp.size();
        return 0;
    }

    class TemplateHolder extends RecyclerView.ViewHolder {

        RoundedImageView ivTemp;
        CustomViewPathData ivTempText;

        public TemplateHolder(@NonNull View itemView) {
            super(itemView);
            ivTemp = itemView.findViewById(R.id.ivTemp);
            ivTempText = itemView.findViewById(R.id.ivTempText);
        }

        public void onBind(int position) {
            TemplateModel template = lstTemp.get(position);
            if (template == null) return;

            if (layoutResource == R.layout.item_template)
                Glide.with(context)
                        .load(Uri.parse("file:///android_asset/template/template/" + template.getName()))
                        .into(ivTemp);
            else {
                itemView.setBackgroundResource(R.drawable.border_item_layer_unselected);
                ivTempText.setDataPath(template.getLstPathData(), false, true);
            }

            itemView.setOnClickListener(v -> callBack.callBackItem(template, position));
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void changeNotify() {
        notifyDataSetChanged();
    }
}
