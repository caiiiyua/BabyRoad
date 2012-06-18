package com.since1985i.babyroad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;

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

        Gallery gallery = (Gallery) findViewById(R.id.diaryedit_gallery);
        ImageAdapter imageAdapter = new ImageAdapter(this);
        gallery.setAdapter(imageAdapter);
    }
    
    
    class ImageAdapter extends BaseAdapter
    {
        private Context mContext;

        public ImageAdapter(Context context)
        {
            mContext = context;
        }

        public int getCount()
        {
            return 10;
        }

        public Object getItem(int position)
        {
            return position;
        }

        public long getItemId(int position)
        {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(R.drawable.ic_add_photo);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.FILL_PARENT));
            return imageView;
        }
    }

}
