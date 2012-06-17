package com.since1985i.babyroad;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class DiaryView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diaryview);
        ImageView img_edit = (ImageView)findViewById(R.id.img_right_1);
        img_edit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                DiaryEdit.actionEdit(DiaryView.this);
            }
        });

        Button btn_back = (Button)findViewById(R.id.btn_left_1);
        btn_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
