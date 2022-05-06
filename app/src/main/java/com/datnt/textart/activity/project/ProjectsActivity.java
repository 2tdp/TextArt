package com.datnt.textart.activity.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.datnt.textart.R;
import com.datnt.textart.activity.Settings;
import com.datnt.textart.utils.Utils;

public class ProjectsActivity extends AppCompatActivity {

    private ImageView ivBack, ivSettings;

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
        ivSettings.setOnClickListener(v -> {
            Utils.setIntent(this, Settings.class.getName());
        });
    }

    private void setUpLayout() {
        ivBack = findViewById(R.id.ivBack);
        ivSettings = findViewById(R.id.ivSettings);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.setAnimExit(this);
    }
}