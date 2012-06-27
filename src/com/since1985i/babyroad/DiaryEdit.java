package com.since1985i.babyroad;

import java.util.ArrayList;

import com.since1985i.babyroad.provider.DiaryContent.Diary;
import com.since1985i.babyroad.util.LogUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;

public class DiaryEdit extends Activity {

    public static int MODE_COMPOSE = 1;
    public static int MODE_EDIT = 2;
    
    public static int REQUEST_CODE_IMAGE_CAPTURE = 1;

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
    private Gallery mGallery;
    private ImageAdapter mImageAdapter;
    private EditText mDiaryBody;
    
    private Diary mDiary;
    private boolean mShouldSave = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diaryedit);
        mShouldSave = true;
        mMode = getIntent().getIntExtra(EXTRA_MODE, MODE_COMPOSE);

        Button btn_back = (Button)findViewById(R.id.btn_left_1);
        btn_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mDiaryBody = (EditText) findViewById(R.id.diaryedit_text);
        mGallery = (Gallery) findViewById(R.id.diaryedit_gallery);
        mImageAdapter = new ImageAdapter(this);
        mGallery.setAdapter(mImageAdapter);
        mGallery.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Gallery gallery = (Gallery)parent;
                if (position + 1 == gallery.getAdapter().getCount()) {
                    startCaptureImage();
                }
            }
            
        });

    }

    private void startCaptureImage() {
        Intent it = new Intent();
        it.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(it, REQUEST_CODE_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bitmap image = (Bitmap)data.getExtras().get("data");
                mImageAdapter.addImage(image);
                mGallery.setAdapter(mImageAdapter);
            }
        }
    }
    
    @Override
    protected void onStop() {
        LogUtil.log("-----onStop");
        if (mShouldSave) {
            saveDiary();
        }
        super.onStop();
    }

    private void saveDiary() {
        LogUtil.log("-----saveDiary");
        if (mDiary == null) {
            mDiary = new Diary(this);
        }
        mDiary.mContent = mDiaryBody.getText().toString();
        mDiary.mTitle = "";
        mDiary.saveOrUpdate();
    }

    class ImageAdapter extends BaseAdapter
    {
        private ArrayList<Bitmap> imagelist = new ArrayList<Bitmap>();

        public void addImage(Bitmap image) {
            if (imagelist.size() == 0) {
                imagelist.add(image);
            } else {
                imagelist.add(imagelist.size() - 1, image);
            }
        }
        private Context mContext;

        public ImageAdapter(Context context)
        {
            mContext = context;
            Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.ic_add_photo);
            addImage(image);
        }

        public int getCount()
        {
            return imagelist.size();
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
//            imageView.setImageResource(R.drawable.ic_add_photo);
            imageView.setImageBitmap(imagelist.get(position));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.FILL_PARENT));
            return imageView;
        }
    }

}
