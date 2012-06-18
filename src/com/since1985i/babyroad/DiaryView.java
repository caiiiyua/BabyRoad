package com.since1985i.babyroad;

import com.since1985i.babyroad.DiaryEdit.ImageAdapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
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

        Gallery gallery = (Gallery) findViewById(R.id.diaryview_gallery);
        ImageAdapter imageAdapter = new ImageAdapter(this);
        gallery.setAdapter(imageAdapter);
        gallery.setSelection(1);
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
            imageView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            return imageView;
        }
    }
}
