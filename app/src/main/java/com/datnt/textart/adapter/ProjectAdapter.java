package com.datnt.textart.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.datnt.textart.R;
import com.datnt.textart.callback.ICallBackItem;
import com.datnt.textart.model.Project;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectHolder> {

    private Context context;
    private ArrayList<Project> lstProject;
    private ICallBackItem callBack;

    public ProjectAdapter(Context context, ICallBackItem callBack) {
        this.context = context;
        this.callBack = callBack;
        lstProject = new ArrayList<>();
    }

    public void setData(ArrayList<Project> lstProject) {
        this.lstProject = lstProject;
        changeNotify();
    }

    @NonNull
    @Override
    public ProjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProjectHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_template, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstProject.isEmpty()) return lstProject.size();
        return 0;
    }

    class ProjectHolder extends RecyclerView.ViewHolder {

        private RoundedImageView ivProject;

        public ProjectHolder(@NonNull View itemView) {
            super(itemView);

            ivProject = itemView.findViewById(R.id.ivTemp);
        }

        public void onBind(int position) {
            Project project = lstProject.get(position);
            if (project == null) return;

            ivProject.setImageBitmap(project.getThumb());

            itemView.setOnClickListener(v -> callBack.callBackItem(project, position));
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void changeNotify() {
        notifyDataSetChanged();
    }
}
