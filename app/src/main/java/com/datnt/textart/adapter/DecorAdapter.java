package com.datnt.textart.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.datnt.textart.R;
import com.datnt.textart.callback.ICallBackItem;
import com.datnt.textart.model.DecorModel;
import com.datnt.textart.model.EmojiModel;

import java.util.ArrayList;

public class DecorAdapter extends RecyclerView.Adapter<DecorAdapter.DecorHolder> {

    private Context context;
    private ArrayList<DecorModel> lstDecor;
    private ICallBackItem callBack;

    public DecorAdapter(Context context, ICallBackItem callBack) {
        this.context = context;
        this.callBack = callBack;
    }

    public void setData(ArrayList<DecorModel> lstDecor) {
        this.lstDecor = lstDecor;
        notifyChange();
    }

    @NonNull
    @Override
    public DecorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DecorHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emoji, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DecorHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstDecor.isEmpty()) return lstDecor.size();
        return 0;
    }

    class DecorHolder extends RecyclerView.ViewHolder {

        private ImageView ivDecor;

        public DecorHolder(@NonNull View itemView) {
            super(itemView);

            ivDecor = itemView.findViewById(R.id.ivEmoji);
        }

        public void onBind(int position) {
            DecorModel decor = lstDecor.get(position);
            if (decor == null) return;

            Glide.with(context)
                    .load(Uri.parse("file:///android_asset/decor/" + decor.getNameFolder() + "/" + decor.getNameDecor()))
                    .into(ivDecor);

            itemView.setOnClickListener(v -> callBack.callBackItem(decor, position));
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyChange() {
        notifyDataSetChanged();
    }
}
