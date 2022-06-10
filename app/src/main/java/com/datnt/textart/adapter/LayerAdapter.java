package com.datnt.textart.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.datnt.textart.R;
import com.datnt.textart.callback.ICallBackItem;
import com.datnt.textart.customview.stickerview.DrawableSticker;
import com.datnt.textart.customview.stickerview.Sticker;
import com.datnt.textart.customview.stickerview.TextSticker;
import com.datnt.textart.model.LayerModel;
import com.datnt.textart.utils.Utils;
import com.datnt.textart.utils.UtilsAdjust;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class LayerAdapter extends RecyclerView.Adapter<LayerAdapter.LayerHolder> {

    private Context context;
    private ArrayList<LayerModel> lstLayer;
    private final ICallBackItem callBack;

    private Bitmap bitmap = null;
    private final Paint paintText;
    private final Rect rectText;

    public LayerAdapter(Context context, ICallBackItem callBack) {
        this.context = context;
        this.callBack = callBack;
        lstLayer = new ArrayList<>();

        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setStrokeJoin(Paint.Join.ROUND);
        paintText.setStrokeCap(Paint.Cap.ROUND);
        rectText = new Rect();
    }

    public void setData(ArrayList<LayerModel> lstLayer) {
        this.lstLayer = new ArrayList<>(lstLayer);
        changeNotify();
    }

    @NonNull
    @Override
    public LayerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LayerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layer, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LayerHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstLayer.isEmpty()) return lstLayer.size();
        return 0;
    }

    class LayerHolder extends RecyclerView.ViewHolder {

        private RoundedImageView ivLayer;
        private ImageView ivLook, ivLock;

        public LayerHolder(@NonNull View itemView) {
            super(itemView);

            ivLayer = itemView.findViewById(R.id.ivLayer);
            ivLock = itemView.findViewById(R.id.ivLock);
            ivLook = itemView.findViewById(R.id.ivLook);
        }

        public void onBind(int position) {
            LayerModel layer = lstLayer.get(position);
            if (layer == null) return;

            if (layer.isSelected())
                itemView.setBackgroundResource(R.drawable.border_item_layer_selected);
            else itemView.setBackgroundResource(R.drawable.border_item_layer_unselected);


            if (layer.getSticker().isLock())
                ivLock.setVisibility(View.VISIBLE);
            else ivLock.setVisibility(View.GONE);

            if (layer.getSticker().isLook())
                ivLook.setVisibility(View.GONE);
            else ivLook.setVisibility(View.VISIBLE);

            if (layer.getSticker() instanceof DrawableSticker) {
                DrawableSticker drawableSticker = (DrawableSticker) layer.getSticker();
                ivLayer.setImageBitmap(((BitmapDrawable) drawableSticker.getDrawable()).getBitmap());
            } else if (layer.getSticker() instanceof TextSticker) {

                TextSticker textSticker = (TextSticker) layer.getSticker();

                if (bitmap == null)
                    bitmap = Utils.loadBitmapFromView(ivLayer);

                String str = textSticker.getText();

                paintText.setTextSize(15);

                if (textSticker.getShader() != null) paintText.setShader(textSticker.getShader());
                else
                    paintText.setColor(Color.parseColor(UtilsAdjust.toRGBString(textSticker.getColor())));

                if (str != null)
                    paintText.getTextBounds(str, 0, str.length(), rectText);

                Canvas canvas = new Canvas(bitmap);
                canvas.drawText(str, bitmap.getWidth() / 2f - rectText.width() / 2f, bitmap.getHeight() / 2f - rectText.height(), paintText);

                ivLayer.setImageBitmap(bitmap);
            }

            itemView.setOnClickListener(v -> {
                callBack.callBackItem(layer, position);
            });
        }
    }

    public void setCurrent(int pos) {
        for (int i = 0; i < lstLayer.size(); i++) lstLayer.get(i).setSelected(i == pos);
        changeNotify();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void changeNotify() {
        notifyDataSetChanged();
    }
}
