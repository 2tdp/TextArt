package com.datnt.textart.activity.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.datnt.textart.R;
import com.datnt.textart.activity.Settings;
import com.datnt.textart.adapter.ProjectAdapter;
import com.datnt.textart.customview.CropRatioView;
import com.datnt.textart.model.Project;
import com.datnt.textart.sharepref.DataLocalManager;
import com.datnt.textart.utils.Utils;
import com.edmodo.cropper.CropImageView;

import java.util.ArrayList;

public class ProjectsActivity extends AppCompatActivity {

    private ImageView ivBack, ivSettings;
    private RecyclerView rcvProject;
    private ProjectAdapter projectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarTransparent(this);
        setContentView(R.layout.activity_projects);

        init();
    }

    private void init() {
        setUpLayout();
        evenClick();
    }

    private void evenClick() {
        ivBack.setOnClickListener(v -> onBackPressed());
        ivSettings.setOnClickListener(v -> Utils.setIntent(this, Settings.class.getName()));
    }

    private void setUpLayout() {
        ivBack = findViewById(R.id.ivBack);
        ivSettings = findViewById(R.id.ivSettings);
        rcvProject = findViewById(R.id.rcvProject);

        setData();
    }

    private void setData() {
        ArrayList<Project> lstProject = DataLocalManager.getListProject("lstProject");

        projectAdapter = new ProjectAdapter(this, (o, pos) -> {
            DataLocalManager.setCheck("isProject", true);
            DataLocalManager.setProject((Project) o, "project");
        });
        if (!lstProject.isEmpty()) projectAdapter.setData(lstProject);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        rcvProject.setLayoutManager(manager);
        rcvProject.setAdapter(projectAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.setAnimExit(this);
    }
}