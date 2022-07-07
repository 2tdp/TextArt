package com.datnt.textart.activity.template;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import com.datnt.textart.R;
import com.datnt.textart.activity.base.BaseActivity;
import com.datnt.textart.activity.edit.EditActivity;
import com.datnt.textart.adapter.TemplateAdapter;
import com.datnt.textart.data.DataTemplate;
import com.datnt.textart.model.TemplateModel;
import com.datnt.textart.sharepref.DataLocalManager;
import com.datnt.textart.utils.Utils;

public class TemplateActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);

        init();
    }

    private void init() {
        RecyclerView rcvTemplate = findViewById(R.id.rcvTemplate);
        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> onBackPressed());

        TemplateAdapter templateAdapter = new TemplateAdapter(this, R.layout.item_template, (o, pos) -> {
            TemplateModel template = (TemplateModel) o;
            DataLocalManager.setTemp(template, "temp");
            DataLocalManager.setOption("", "bitmap");
            DataLocalManager.setOption("", "bitmap_myapp");
            Utils.setIntent(this, EditActivity.class.getName());
        });

        templateAdapter.setData(DataTemplate.getTemplate(this, ""));

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        rcvTemplate.setLayoutManager(manager);
        rcvTemplate.setAdapter(templateAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.setAnimExit(this);
    }
}