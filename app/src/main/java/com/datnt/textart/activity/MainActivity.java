package com.datnt.textart.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.datnt.textart.R;
import com.datnt.textart.activity.project.CreateProjectActivity;
import com.datnt.textart.activity.project.ProjectsActivity;
import com.datnt.textart.activity.template.TemplateActivity;
import com.datnt.textart.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private ImageView ivVip;
    private TextView tvCrePro, tvTemp, tvPro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        setUpLayout();
        setUpFragmentManager();
        evenClick();
    }

    private void evenClick() {
        tvCrePro.setOnClickListener(v -> {
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_click_create, null);
            ImageView ivUseTemp = view.findViewById(R.id.ivUseTemp);
            ImageView ivCreate = view.findViewById(R.id.ivCreate);
            TextView tvCancel = view.findViewById(R.id.tvCancel);

            AlertDialog dialog = new AlertDialog.Builder(this, R.style.SheetDialog).create();
            dialog.setView(view);
            dialog.setCancelable(false);
            dialog.show();

            tvCancel.setOnClickListener(v1 -> dialog.cancel());
            ivUseTemp.setOnClickListener(v2 -> {
                Utils.setIntent(this, TemplateActivity.class.getName());
            });
            ivCreate.setOnClickListener(v3 -> {
                Utils.setIntent(this, CreateProjectActivity.class.getName());
            });
        });

        tvPro.setOnClickListener(v -> {
            Utils.setIntent(this, ProjectsActivity.class.getName());
        });

        tvTemp.setOnClickListener(v -> {
            Utils.setIntent(this, TemplateActivity.class.getName());
        });
    }

    private void setUpFragmentManager() {
    }

    private void setUpLayout() {
        ivVip = findViewById(R.id.ivVip);
        tvCrePro = findViewById(R.id.tvCreate);
        tvTemp = findViewById(R.id.tvTemp);
        tvPro = findViewById(R.id.tvPro);
    }
}