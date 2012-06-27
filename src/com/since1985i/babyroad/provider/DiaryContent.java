package com.since1985i.babyroad.provider;

import java.util.ArrayList;

import com.since1985i.babyroad.exception.DiaryException;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class DiaryContent {

	public static final String AUTHORITY = "com.since1985i.babyroad.provider.DiaryProvider";
	
	public static final Uri BASE_CONTENT_URI_STR = Uri.parse("content://" + AUTHORITY);
	
	public static final String COLUMN_ID = "_id";
	
	public Uri mContentUri = BASE_CONTENT_URI_STR;
	
	public String mTableName = "DiaryContent";
	
	public long mId = -1;
	
	protected boolean mIsSaved = false;
	
	protected Context mContext = null;
	
	public DiaryContent(Context context, Uri contentUri, String tableName) {
		mContext = context;
		mContentUri = contentUri;
		mTableName = tableName;
	}
	
	public Uri save() throws DiaryException {
		if(mIsSaved){
			throw new DiaryException(DiaryException.CONTENT_HAS_SAVED, "This " + mTableName + " has saved");
		}
		Uri uri = mContext.getContentResolver().insert(getContentUri(), toValues());
		if (uri != null) {
			mId = Long.parseLong(uri.getPathSegments().get(1));
		}
		return uri;
		
	};

	public int update() throws DiaryException {
		if(!mIsSaved){
			throw new DiaryException(DiaryException.CONTENT_HAS_NOT_SAVED, "This " + mTableName + " has not saved");
		}
		return mContext.getContentResolver().update(getContentUri(), toValues(), null, null);
	};
	
	public int update(ContentValues contentValues) throws DiaryException {
		if(!mIsSaved){
			throw new DiaryException(DiaryException.CONTENT_HAS_NOT_SAVED, "This " + mTableName + " has not saved");
		}
		return mContext.getContentResolver().update(getContentUri(), contentValues, null, null);
	};

	public void saveOrUpdate() {
		if(mIsSaved){
			mContext.getContentResolver().update(getContentUri(), toValues(), null, null);
		} else {
			Uri uri = mContext.getContentResolver().insert(getContentUri(), toValues());
			if (uri != null) {
				mId = Long.parseLong(uri.getPathSegments().get(1));
			}
		}
	};

	public int delete(){
		return mContext.getContentResolver().delete(getContentUri(), null, null);
	};
	
	public ContentValues toValues(){
		return null; 
	}
	
	public Uri getContentUri() {
		if (mId != -1) {
		    ContentUris.withAppendedId(mContentUri, mId);
		}
		return mContentUri;
	}

	//Diaries
    public static class Diary extends DiaryContent {
        
        //Diary table name
        public static final String TABLE_NAME = "Diary";
        public static final String URI_PATH = "/Diary";
        //CONTENT_URI
        public static final Uri CONTENT_URI = Uri.parse(BASE_CONTENT_URI_STR + URI_PATH);
        
        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of diaries.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.since1985i.babyroad.diary.diary";

        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single diary.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.since1985i.babyroad.diary.diary";
        
        //Diary column name
        public static final String COLUMN_TITLE = "Title";
        public static final String COLUMN_CONTENT = "Content";
        public static final String COLUMN_TYPE = "Type";
        public static final String COLUMN_FLAG = "Flag";
        public static final String COLUMN_CREATED = "Created";
        public static final String COLUMN_MODIFIED = "Modified";

        //Diary type
        //Normal diary
        public static final int TYPE_NORMAL = 0;
        //Important diary
        public static final int TYPE_IMPORTANT = 1;
        
        public static String[] DIARY_PROJECTION = new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_CONTENT, COLUMN_TYPE, COLUMN_FLAG, COLUMN_CREATED, COLUMN_MODIFIED };
        public static int DIARY_PROJECTION_COLUMN_ID = 0;
        public static int DIARY_PROJECTION_COLUMN_TITLE = 1;
        public static int DIARY_PROJECTION_COLUMN_CONTENT = 2;
        public static int DIARY_PROJECTION_COLUMN_TYPE = 3;
        public static int DIARY_PROJECTION_COLUMN_FLAG = 4;
        public static int DIARY_PROJECTION_COLUMN_CREATED = 5;
        public static int DIARY_PROJECTION_COLUMN_MODIFIED = 6;
        
        public static String SELECTION_ID = COLUMN_ID + "= ?";

        public String mTitle;
        public String mContent;
        public int mType;
        public int mFlag;
        public long mCreated;
        public long mModified;
        
        public Diary(Context context) {
            super(context, CONTENT_URI, TABLE_NAME);
        }

        public ContentValues toValues(){
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_TITLE, mTitle);
            cv.put(COLUMN_CONTENT, mContent);
            cv.put(COLUMN_TYPE, mType);
            cv.put(COLUMN_FLAG, mFlag);
            cv.put(COLUMN_CREATED, mCreated);
            cv.put(COLUMN_MODIFIED, mModified);
            return cv;
        }

        public static Diary restoreDiaryById(Context context, long Id) {
            Diary diary = null;
            Cursor c = null;
            try {
                c = context.getContentResolver().query(CONTENT_URI, 
                        DIARY_PROJECTION, SELECTION_ID, new String[]{Id + ""}, null);
                if(c.moveToNext()) {
                    diary = new Diary(context);
                    diary.mIsSaved = true;
                    diary.mId = c.getLong(DIARY_PROJECTION_COLUMN_ID);
                    diary.mTitle = c.getString(DIARY_PROJECTION_COLUMN_TITLE);
                    diary.mContent = c.getString(DIARY_PROJECTION_COLUMN_CONTENT);
                    diary.mType = c.getInt(DIARY_PROJECTION_COLUMN_TYPE);
                    diary.mFlag = c.getInt(DIARY_PROJECTION_COLUMN_FLAG);
                    diary.mCreated = c.getLong(DIARY_PROJECTION_COLUMN_CREATED);
                    diary.mModified = c.getLong(DIARY_PROJECTION_COLUMN_MODIFIED);
                }
            } finally {
                if(c != null && !c.isClosed()) {
                    c.close();
                }
            }
            return diary;
        }
        
        public static Diary restoreDiaryByCursor(Context context, Cursor c) {
            Diary diary = new Diary(context);
            diary.mIsSaved = true;
            diary.mId = c.getLong(DIARY_PROJECTION_COLUMN_ID);
            diary.mTitle = c.getString(DIARY_PROJECTION_COLUMN_TITLE);
            diary.mContent = c.getString(DIARY_PROJECTION_COLUMN_CONTENT);
            diary.mType = c.getInt(DIARY_PROJECTION_COLUMN_TYPE);
            diary.mFlag = c.getInt(DIARY_PROJECTION_COLUMN_FLAG);
            diary.mCreated = c.getLong(DIARY_PROJECTION_COLUMN_CREATED);
            diary.mModified = c.getLong(DIARY_PROJECTION_COLUMN_MODIFIED);
            return diary;
        }
    }
	
	public static class Body extends DiaryContent {
		
		//Body table name
		public static final String TABLE_NAME = "Body";
		public static final String URI_PATH = "/Body";
		public static final Uri CONTENT_URI = Uri.parse(BASE_CONTENT_URI_STR + URI_PATH);
		
		/**
         * The MIME type of {@link #CONTENT_URI} providing a directory of bodies.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.since1985i.babyroad.diary.body";

        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single body.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.since1985i.babyroad.diary.body";
        
		//Body column name
		public static final String COLUMN_DIARYID = "DiaryId";
		public static final String COLUMN_FLAG = "Flag";
		public static final String COLUMN_BODYCONTENT = "BodyContent";
		public static final String COLUMN_CREATED = "Created";
		public static final String COLUMN_MODIFIED = "Modified";
		
		//Body type
		public static final int BODY_TYPE_DEFAULT = 1;
		//Favorite body
		public static final int BODY_TYPE_FAVORITE = 2;
		
		public static String[] BODY_ID_PROJECTION = new String[]{COLUMN_ID };

		public static String[] BODY_PROJECTION = new String[]{COLUMN_ID, 
			COLUMN_DIARYID, COLUMN_FLAG, COLUMN_BODYCONTENT, COLUMN_CREATED, COLUMN_MODIFIED };
		public static int BODY_PROJECTION_COLUMN_ID = 0;
		public static int BODY_PROJECTION_COLUMN_DIARYID = 1;
		public static int BODY_PROJECTION_COLUMN_FLAG = 2;
		public static int BODY_PROJECTION_COLUMN_BODYCONTENT = 3;
		public static int BODY_PROJECTION_COLUMN_CREATED = 4;
		public static int BODY_PROJECTION_COLUMN_MODIFIED = 5;
		
		public static String SELECTION_ID = COLUMN_ID + "= ?";
		public static String SELECTION_DIARYID = COLUMN_DIARYID + "= ?";
		
		public int mDiaryId;
		public int mFlag;
		public String mBodyContent;
		public long mCreated;
		public long mModified;
		
		public Body(Context context) {
			super(context, CONTENT_URI, TABLE_NAME);
		}

		public ContentValues toValues(){
			ContentValues cv = new ContentValues();
			cv.put(COLUMN_DIARYID, mDiaryId);
			cv.put(COLUMN_FLAG, mFlag);
			cv.put(COLUMN_BODYCONTENT, mBodyContent);
			cv.put(COLUMN_CREATED, mCreated);
			cv.put(COLUMN_MODIFIED, mModified);
			return cv;
		}
		
		public static Body restoreBodyById(Context context, long Id) {
			Body body = null;
			Cursor c = null;
			try {
				c = context.getContentResolver().query(CONTENT_URI, 
						BODY_PROJECTION, SELECTION_ID, new String[]{Id +""}, null);
				if(c.moveToNext()) {
					body = new Body(context);
					body.mIsSaved = true;
					body.mId = c.getLong(BODY_PROJECTION_COLUMN_ID);
					body.mDiaryId = c.getInt(BODY_PROJECTION_COLUMN_DIARYID);
					body.mFlag = c.getInt(BODY_PROJECTION_COLUMN_FLAG);
					body.mBodyContent = c.getString(BODY_PROJECTION_COLUMN_BODYCONTENT);
					body.mCreated = c.getLong(BODY_PROJECTION_COLUMN_CREATED);
					body.mModified = c.getLong(BODY_PROJECTION_COLUMN_MODIFIED);
				}
			} finally {
				if(c != null && !c.isClosed()) {
					c.close();
				}
			}
			return body;
		}

	}

	//Diaries
    public static class Attachment extends DiaryContent {
        
        //Attachment table name
        public static final String TABLE_NAME = "Attachment";
        public static final String URI_PATH = "/Attachment";
        //CONTENT_URI
        public static final Uri CONTENT_URI = Uri.parse(BASE_CONTENT_URI_STR + URI_PATH);
        
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.since1985i.babyroad.diary.attachment";

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.since1985i.babyroad.diary.attachment";
        
        //Attachment column name
        public static final String COLUMN_NAME = "Name";
        public static final String COLUMN_FILEURI = "FileUri";
        public static final String COLUMN_DIARYID = "DiaryId";
        public static final String COLUMN_FLAG = "Flag";
        public static final String COLUMN_CREATED = "Created";
        public static final String COLUMN_MODIFIED = "Modified";

        //Attachment type
        //Normal Attachment
        public static final int TYPE_NORMAL = 0;
        //Important Attachment
        public static final int TYPE_IMPORTANT = 1;
        
        public static String[] ATTACHMENT_PROJECTION = new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_FILEURI, COLUMN_DIARYID, COLUMN_FLAG, COLUMN_CREATED, COLUMN_MODIFIED };
        public static int ATTACHMENT_PROJECTION_COLUMN_ID = 0;
        public static int ATTACHMENT_PROJECTION_COLUMN_NAME = 1;
        public static int ATTACHMENT_PROJECTION_COLUMN_FILEURI = 2;
        public static int ATTACHMENT_PROJECTION_COLUMN_DIARYID = 3;
        public static int ATTACHMENT_PROJECTION_COLUMN_FLAG = 4;
        public static int ATTACHMENT_PROJECTION_COLUMN_CREATED = 5;
        public static int ATTACHMENT_PROJECTION_COLUMN_MODIFIED = 6;
        
        public static String SELECTION_ID = COLUMN_ID + "= ?";

        public String mName;
        public String mFileUri;
        public int mDiaryId;
        public int mFlag;
        public long mCreated;
        public long mModified;
        
        public Attachment(Context context) {
            super(context, CONTENT_URI, TABLE_NAME);
        }

        public ContentValues toValues(){
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_NAME, mName);
            cv.put(COLUMN_FILEURI, mFileUri);
            cv.put(COLUMN_DIARYID, mDiaryId);
            cv.put(COLUMN_FLAG, mFlag);
            cv.put(COLUMN_CREATED, mCreated);
            cv.put(COLUMN_MODIFIED, mModified);
            return cv;
        }

        public static Diary restoreDiaryById(Context context, long Id) {
            Diary diary = null;
            Cursor c = null;
            try {
                c = context.getContentResolver().query(CONTENT_URI, 
                        ATTACHMENT_PROJECTION, SELECTION_ID, new String[]{Id + ""}, null);
                if(c.moveToNext()) {
                    diary = new Diary(context);
                    diary.mIsSaved = true;
                    diary.mId = c.getLong(ATTACHMENT_PROJECTION_COLUMN_ID);
                    diary.mTitle = c.getString(ATTACHMENT_PROJECTION_COLUMN_NAME);
                    diary.mContent = c.getString(ATTACHMENT_PROJECTION_COLUMN_FILEURI);
                    diary.mType = c.getInt(ATTACHMENT_PROJECTION_COLUMN_DIARYID);
                    diary.mFlag = c.getInt(ATTACHMENT_PROJECTION_COLUMN_FLAG);
                    diary.mCreated = c.getLong(ATTACHMENT_PROJECTION_COLUMN_CREATED);
                    diary.mModified = c.getLong(ATTACHMENT_PROJECTION_COLUMN_MODIFIED);
                }
            } finally {
                if(c != null && !c.isClosed()) {
                    c.close();
                }
            }
            return diary;
        }
        
        public static Diary restoreDiaryByCursor(Context context, Cursor c) {
            Diary diary = new Diary(context);
            diary.mIsSaved = true;
            diary.mId = c.getLong(ATTACHMENT_PROJECTION_COLUMN_ID);
            diary.mTitle = c.getString(ATTACHMENT_PROJECTION_COLUMN_NAME);
            diary.mContent = c.getString(ATTACHMENT_PROJECTION_COLUMN_FILEURI);
            diary.mType = c.getInt(ATTACHMENT_PROJECTION_COLUMN_DIARYID);
            diary.mFlag = c.getInt(ATTACHMENT_PROJECTION_COLUMN_FLAG);
            diary.mCreated = c.getLong(ATTACHMENT_PROJECTION_COLUMN_CREATED);
            diary.mModified = c.getLong(ATTACHMENT_PROJECTION_COLUMN_MODIFIED);
            return diary;
        }
    }

    
    //Settings
    public static class Setting extends DiaryContent {
		
		//Setting table name
		public static final String TABLE_NAME = "Setting";
		public static final String URI_PATH = "/Setting";
		//CONTENT_URI
        public static final Uri CONTENT_URI = Uri.parse(BASE_CONTENT_URI_STR + URI_PATH);
		
		/**
         * The MIME type of {@link #CONTENT_URI} providing a directory of settings.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.since1985i.babyroad.diary.setting";

        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single setting.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.since1985i.babyroad.diary.setting";
        
		//Setting column name
		public static final String COLUMN_NAME = "SettingName";
		public static final String COLUMN_VALUE = "SettingValue";
		
		public static String[] SETTING_PROJECTION = new String[]{COLUMN_ID, 
			COLUMN_NAME, COLUMN_VALUE };
		public static int SETTING_PROJECTION_COLUMN_ID = 0;
		public static int SETTING_PROJECTION_COLUMN_NAME = 1;
		public static int SETTING_PROJECTION_COLUMN_VALUE = 2;
		
		public static String SELECTION_ID = COLUMN_ID + "= ?";
		
		public String mSettingName;
		public String mSettingValue;
		
		public Setting(Context context) {
			super(context, CONTENT_URI, TABLE_NAME);
		}

		public ContentValues toValues(){
			ContentValues cv = new ContentValues();
			cv.put(COLUMN_NAME, mSettingName);
			cv.put(COLUMN_VALUE, mSettingValue);
			return cv;
		}
		
		public static Setting restoreSettingById(Context context, long Id) {
			Setting setting = null;
			
			Cursor c = context.getContentResolver().query(
					CONTENT_URI, SETTING_PROJECTION, SELECTION_ID,
					new String[] { Id + "" }, null);
			try {
				if (c.moveToNext()) {
					setting = new Setting(context);
					setting.mIsSaved = true;
					setting.mId = c.getLong(SETTING_PROJECTION_COLUMN_ID);
					setting.mSettingName = c
							.getString(SETTING_PROJECTION_COLUMN_NAME);
					setting.mSettingValue = c
							.getString(SETTING_PROJECTION_COLUMN_VALUE);
				}
			} finally {
				if (c != null) {
					c.close();
				}
			}
			return setting;
		}
		
		public static Setting[] restoreSettings(Context context) {
            ArrayList<Setting> settings = new ArrayList<Setting>();
			
			Cursor c = context.getContentResolver().query(
					CONTENT_URI, SETTING_PROJECTION, null,
					null, null);
			try {
				while (c.moveToNext()) {
					Setting setting = new Setting(context);
					setting.mIsSaved = true;
					setting.mId = c.getLong(SETTING_PROJECTION_COLUMN_ID);
					setting.mSettingName = c
							.getString(SETTING_PROJECTION_COLUMN_NAME);
					setting.mSettingValue = c
							.getString(SETTING_PROJECTION_COLUMN_VALUE);
					settings.add(setting);
				}
			} finally {
				if (c != null) {
					c.close();
				}
			}
			return settings.toArray(new Setting[]{});
		}
		
	}
}
