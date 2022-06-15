package com.datnt.textart.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.graphics.PathParser;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.datnt.textart.R;
import com.datnt.textart.callback.ICallBackItem;
import com.datnt.textart.customview.CustomViewPathData;
import com.datnt.textart.customview.stickerview.DrawableSticker;
import com.datnt.textart.data.DataDecor;
import com.datnt.textart.model.DecorModel;
import com.datnt.textart.model.EmojiModel;
import com.datnt.textart.utils.Utils;
import com.datnt.textart.utils.UtilsAdjust;

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
        return new DecorHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_draw_path, parent, false));
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

        private CustomViewPathData ivPath;

        public DecorHolder(@NonNull View itemView) {
            super(itemView);

            ivPath = itemView.findViewById(R.id.ivPath);
        }

        public void onBind(int position) {
            DecorModel decor = lstDecor.get(position);
            if (decor == null) return;

            ivPath.setDataPath(decor.getLstPathData(), true, false);

            itemView.setOnClickListener(v -> callBack.callBackItem(decor, position));
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyChange() {
        notifyDataSetChanged();
    }
}
