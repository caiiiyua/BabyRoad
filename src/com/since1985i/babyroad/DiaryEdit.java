package com.since1985i.babyroad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DiaryEdit extends Activity {

    public static int MODE_COMPOSE = 1;
    public static int MODE_EDIT = 2;

    public static String EXTRA_MODE = "Extra_Mode";

    public static void actionCompose(Context activity){
        Intent it = new Intent();
        it.setClass(activity, DiaryEdit.class);
        activity.startActivity(it);
    }

    public static void actionEdit(Context activity){
        Intent it = new Intent();
        it.setClass(activity, DiaryEdit.class);
        it.putExtra(EXTRA_MODE, MODE_EDIT);
        activity.startActivity(it);
    }

    private int mMode = MODE_COMPOSE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diaryedit);

        mMode = getIntent().getIntExtra(EXTRA_MODE, MODE_COMPOSE);

        Button btn_back = (Button)findViewById(R.id.btn_left_1);
        btn_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
