package com.datnt.textart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.datnt.textart.R;
import com.datnt.textart.callback.ICallBackItem;

public class MyAppAdapter extends RecyclerView.Adapter<MyAppAdapter.MyAppHolder> {

    private Context context;
    private ICallBackItem callBack;

    public MyAppAdapter(Context context, ICallBackItem callBack) {
        this.context = context;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public MyAppHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyAppHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyAppHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MyAppHolder extends RecyclerView.ViewHolder {
        public MyAppHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void onBind(int position) {

        }
    }
}
