package com.since1985i.babyroad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class EntryForTest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry);
        //home page entry
        Button btn_homepage = (Button)findViewById(R.id.btn_homepage);
        btn_homepage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent();
                it.setClass(EntryForTest.this, BabyroadPagesBase.class);
                startActivity(it);
            }
        });
        //diary view entry
        Button btn_diaryview = (Button)findViewById(R.id.btn_diaryview);
        btn_diaryview.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent();
                it.setClass(EntryForTest.this, DiaryView.class);
                startActivity(it);
            }
        });
        //diary edit entry
        Button btn_diaryedit = (Button)findViewById(R.id.btn_diaryedit);
        btn_diaryedit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                DiaryEdit.actionCompose(EntryForTest.this);
            }
        });
    }
}
