package com.datnt.textart.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.datnt.textart.R;
import com.datnt.textart.callback.ICallBackItem;
import com.datnt.textart.model.StyleFontModel;

import java.util.ArrayList;

public class StyleFontAdapter extends RecyclerView.Adapter<StyleFontAdapter.StyleFontHolder> {

    private final Context context;
    private ArrayList<StyleFontModel> lstStyleFont;
    private final ICallBackItem callBack;

    public StyleFontAdapter(Context context, ICallBackItem callBack) {
        this.context = context;
        this.callBack = callBack;
    }

    public void setData(ArrayList<StyleFontModel> lstStyleFont) {
        this.lstStyleFont = lstStyleFont;
        notifyChange();
    }

    @NonNull
    @Override
    public StyleFontHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StyleFontHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_font, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StyleFontHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstStyleFont.isEmpty()) return lstStyleFont.size();
        return 0;
    }

    class StyleFontHolder extends RecyclerView.ViewHolder {

        private final TextView tvStyleFont;

        public StyleFontHolder(@NonNull View itemView) {
            super(itemView);

            tvStyleFont = itemView.findViewById(R.id.tvFont);
            itemView.findViewById(R.id.ivFavorite).setVisibility(View.INVISIBLE);
        }

        public void onBind(int position) {
            StyleFontModel styleFont = lstStyleFont.get(position);
            if (styleFont == null) return;

            tvStyleFont.setText(styleFont.getName());
            tvStyleFont.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/"
                    + styleFont.getFont().toLowerCase() + "/"
                    + styleFont.getFont().toLowerCase() + "_" + styleFont.getName().toLowerCase().trim().replaceAll(" ", "_") + ".ttf"));
            if (styleFont.isSelected())
                tvStyleFont.setTextColor(context.getResources().getColor(R.color.pink));
            else tvStyleFont.setTextColor(context.getResources().getColor(R.color.black));

            itemView.setOnClickListener(v -> {
                callBack.callBackItem(styleFont, position);
                setCurrent(position);
            });

        }
    }

    public void setCurrent(int pos) {
        for (int i = 0; i < lstStyleFont.size(); i++) {
            if (i != pos) {
                if (lstStyleFont.get(i).isSelected()) {
                    lstStyleFont.get(i).setSelected(false);
                    notifyItemChanged(i);
                }
            } else {
                lstStyleFont.get(i).setSelected(true);
                notifyItemChanged(i);
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyChange() {
        notifyDataSetChanged();
    }
}
