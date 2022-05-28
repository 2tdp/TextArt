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
import com.datnt.textart.model.EmojiModel;

import java.util.ArrayList;

public class TitleEmojiAdapter extends RecyclerView.Adapter<TitleEmojiAdapter.TitleEmojiHolder> {

    private Context context;
    private ArrayList<EmojiModel> lstEmoji;
    private ICallBackItem callBack;

    public TitleEmojiAdapter(Context context, ICallBackItem callBack) {
        this.context = context;
        this.callBack = callBack;
    }

    public void setData(ArrayList<EmojiModel> lstEmoji) {
        this.lstEmoji = lstEmoji;
        changeNotify();
    }

    @NonNull
    @Override
    public TitleEmojiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TitleEmojiHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title_emoji, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TitleEmojiHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstEmoji.isEmpty()) return lstEmoji.size();
        return 0;
    }

    class TitleEmojiHolder extends RecyclerView.ViewHolder {

        private ImageView ivTitleEmoji;

        public TitleEmojiHolder(@NonNull View itemView) {
            super(itemView);

            ivTitleEmoji = itemView.findViewById(R.id.ivTitleEmoji);
        }

        public void onBind(int position) {
            EmojiModel emoji = lstEmoji.get(position);
            if (emoji == null) return;

            if (emoji.isSelected())
                ivTitleEmoji.setBackgroundResource(R.drawable.border_title_emoji);
            else ivTitleEmoji.setBackgroundResource(R.color.white);

            Glide.with(context)
                    .load(Uri.parse("file:///android_asset/emoji/" + emoji.getNameFolder() + "/" + emoji.getNameEmoji()))
                    .into(ivTitleEmoji);

            itemView.setOnClickListener(v -> callBack.callBackItem(emoji, position));
        }
    }

    public void setCurrent(int pos) {
        for (int i = 0; i < lstEmoji.size(); i++) {
            lstEmoji.get(i).setSelected(i == pos);
            notifyItemChanged(i);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void changeNotify() {
        notifyDataSetChanged();
    }
}
