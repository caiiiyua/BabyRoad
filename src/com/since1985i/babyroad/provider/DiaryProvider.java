package com.since1985i.babyroad.provider;


import java.util.Arrays;

import com.since1985i.babyroad.provider.DiaryContent.Attachment;
import com.since1985i.babyroad.provider.DiaryContent.Body;
import com.since1985i.babyroad.provider.DiaryContent.Diary;
import com.since1985i.babyroad.provider.DiaryContent.Setting;
import com.since1985i.babyroad.util.LogUtil;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class DiaryProvider extends ContentProvider {
    // Used for debugging and logging
    private static final String TAG = "DiaryProvider";

    /**
     * The database that the provider uses as its underlying data store
     */
    private static final String DATABASE_NAME = "Diary.db";

    /**
     * The database version
     */
    private static final int DATABASE_VERSION = 1;

    //Diary uri matches
    private static final int DIARIES = 1;
    private static final int DIARY_ID = 2;
    
    //Diary Body uri matches
    private static final int BODIES = 11;
    private static final int BODY_ID = 12;
    
    //Attachment uri matches
    private static final int ATTACHMENTS = 21;
    private static final int ATTACHMENT_ID = 22;
    
    //Setting uri matches
    private static final int SETTINGS = 31;
    private static final int SETTING_ID = 32;

    /**
     * A UriMatcher instance
     */
    private static final UriMatcher sUriMatcher;

    // Handle to a new DatabaseHelper.
    private DatabaseHelper mOpenHelper;


    /**
     * A block that instantiates and sets static objects
     */
    static {

        /*
         * Creates and initializes the URI matcher
         */
        // Create a new instance
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //Diary match pattern
        sUriMatcher.addURI(DiaryContent.AUTHORITY, DiaryContent.Diary.TABLE_NAME, DIARIES);
        sUriMatcher.addURI(DiaryContent.AUTHORITY, DiaryContent.Diary.TABLE_NAME +"/#", DIARY_ID);
        
        //Body match pattern
        sUriMatcher.addURI(DiaryContent.AUTHORITY, DiaryContent.Body.TABLE_NAME, BODIES);
        sUriMatcher.addURI(DiaryContent.AUTHORITY, DiaryContent.Body.TABLE_NAME +"/#", BODY_ID);

        //Attachment match pattern
        sUriMatcher.addURI(DiaryContent.AUTHORITY, DiaryContent.Attachment.TABLE_NAME, ATTACHMENTS);
        sUriMatcher.addURI(DiaryContent.AUTHORITY, DiaryContent.Attachment.TABLE_NAME +"/#", ATTACHMENTS);

        //Setting match pattern
        sUriMatcher.addURI(DiaryContent.AUTHORITY, DiaryContent.Setting.TABLE_NAME, SETTINGS);
        sUriMatcher.addURI(DiaryContent.AUTHORITY, DiaryContent.Setting.TABLE_NAME +"/#", SETTING_ID);
        
    }

    /**
    *
    * This class helps open, create, and upgrade the database file. Set to package visibility
    * for testing purposes.
    */
   static class DatabaseHelper extends SQLiteOpenHelper {

	   private final Context mContext;
       DatabaseHelper(Context context) {
           // calls the super constructor, requesting the default cursor factory.
           super(context, DATABASE_NAME, null, DATABASE_VERSION);
           mContext = context;
       }

       /**
        *
        * Creates the underlying database with table name and column names taken from the
        * Diary class.
        */
       @Override
       public void onCreate(SQLiteDatabase db) {
           String createDiaryTable = "CREATE TABLE " + Diary.TABLE_NAME + " ("
                   + Diary.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                   + Diary.COLUMN_TITLE + " TEXT,"
                   + Diary.COLUMN_CONTENT + " TEXT,"
                   + Diary.COLUMN_TYPE + " INTEGER,"
                   + Diary.COLUMN_FLAG + " INTEGER,"
                   + Diary.COLUMN_CREATED + " INTEGER,"
                   + Diary.COLUMN_MODIFIED + " INTEGER"
                   + ");"; 

    	   String createBodyTable = "CREATE TABLE " + Body.TABLE_NAME + " ("
                   + Body.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                   + Body.COLUMN_DIARYID + " INTEGER,"
                   + Body.COLUMN_FLAG + " INTEGER,"
                   + Body.COLUMN_BODYCONTENT + " TEXT,"
                   + Body.COLUMN_CREATED + " INTEGER,"
                   + Body.COLUMN_MODIFIED + " INTEGER"
                   + ");";

    	   String createAttachmentTable = "CREATE TABLE " + Attachment.TABLE_NAME + " ("
                   + Attachment.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                   + Attachment.COLUMN_NAME + " TEXT,"
                   + Attachment.COLUMN_FILEURI + " TEXT,"
                   + Attachment.COLUMN_DIARYID + " INTEGER,"
                   + Attachment.COLUMN_FLAG + " INTEGER,"
                   + Attachment.COLUMN_CREATED + " INTEGER,"
                   + Attachment.COLUMN_MODIFIED + " INTEGER"
                   + ");"; 
    	   
    	   String createSettingTable = "CREATE TABLE " + Setting.TABLE_NAME + " ("
                   + Setting.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                   + Setting.COLUMN_NAME + " TEXT,"
                   + Setting.COLUMN_VALUE + " INTEGER"
                   + ");"; 
    	   
    	   LogUtil.log("create database table: " + createDiaryTable);
           db.execSQL(createDiaryTable);
           LogUtil.log("create database table: " + createBodyTable);
           db.execSQL(createBodyTable);
           LogUtil.log("create database table: " + createAttachmentTable);
           db.execSQL(createAttachmentTable);
           LogUtil.log("create database table: " + createSettingTable);
           db.execSQL(createSettingTable);

       }

       /**
        *
        * Demonstrates that the provider must consider what happens when the
        * underlying datastore is changed. In this sample, the database is upgraded the database
        * by destroying the existing data.
        * A real application should upgrade the database in place.
        */
       @Override
       public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

           // Logs that the database is being upgraded
           LogUtil.logW(TAG, "Upgrading database from version " + oldVersion + " to "
                   + newVersion + ", which will destroy all old data");

           // Kills the table and existing data
    	   db.execSQL("DROP TABLE IF EXISTS " + Diary.TABLE_NAME);
           db.execSQL("DROP TABLE IF EXISTS " + Body.TABLE_NAME);
           db.execSQL("DROP TABLE IF EXISTS " + Attachment.TABLE_NAME);
           db.execSQL("DROP TABLE IF EXISTS " + Setting.TABLE_NAME);
           // Recreates the database with a new version
           onCreate(db);
       }
   }

   /**
    *
    * Initializes the provider by creating a new DatabaseHelper. onCreate() is called
    * automatically when Android creates the provider in response to a resolver request from a
    * client.
    */
   @Override
   public boolean onCreate() {

       // Creates a new helper object. Diary that the database itself isn't opened until
       // something tries to access it, and it's only created if it doesn't already exist.
       mOpenHelper = new DatabaseHelper(getContext());

       // Assumes that any failures will be reported by a thrown exception.
       return true;
   }

   @Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
       LogUtil.log("DiaryProvider Query: " + uri);
		String table;
		/**
		 * Choose the table and based on URI pattern-matching.
		 */
		switch (sUriMatcher.match(uri)) {

		case DIARIES:
		case DIARY_ID:
			table = DiaryContent.Diary.TABLE_NAME;
			break;

		case BODIES:
        case BODY_ID:
            table = DiaryContent.Body.TABLE_NAME;
            break;

        case ATTACHMENTS:
        case ATTACHMENT_ID:
            table = DiaryContent.Attachment.TABLE_NAME;
            break;

		case SETTINGS:
		case SETTING_ID:
			table = DiaryContent.Setting.TABLE_NAME;
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		SQLiteDatabase db = mOpenHelper.getReadableDatabase();

		Cursor c = db.query(table, // The table to query
				projection, // The columns to return from the query
				selection, // The columns for the where clause
				selectionArgs, // The values for the where clause
				null, // don't group the rows
				null, // don't filter by row groups
				sortOrder // The sort order
				);

		c.setNotificationUri(getContext().getContentResolver(), uri);
		LogUtil.log("DiaryProvider Query " + table + ", projection=" + (projection == null ? null : Arrays.toString(projection))
				+ ", selection=" + selection
				+ ", selectionArgs=" + (selectionArgs == null ? null : Arrays.toString(selectionArgs))
				+ ", orderBy=" + sortOrder);
		return c;
	}

   /**
    * This is called when a client calls {@link android.content.ContentResolver#getType(Uri)}.
    * Returns the MIME data type of the URI given as a parameter.
    *
    * @param uri The URI whose MIME type is desired.
    * @return The MIME type of the URI.
    * @throws IllegalArgumentException if the incoming URI pattern is invalid.
    */
   @Override
   public String getType(Uri uri) {

       /**
        * Chooses the MIME type based on the incoming URI pattern
        */
       switch (sUriMatcher.match(uri)) {

           case DIARIES:
        	   return DiaryContent.Diary.CONTENT_TYPE;
           case DIARY_ID:
        	   return DiaryContent.Diary.CONTENT_ITEM_TYPE;

           case BODIES:
               return DiaryContent.Body.CONTENT_TYPE;
           case BODY_ID:
               return DiaryContent.Body.CONTENT_ITEM_TYPE;

           case ATTACHMENTS:
               return DiaryContent.Attachment.CONTENT_TYPE;
           case ATTACHMENT_ID:
               return DiaryContent.Attachment.CONTENT_ITEM_TYPE;

           case SETTINGS:
        	   return DiaryContent.Setting.CONTENT_TYPE;
           case SETTING_ID:
        	   return DiaryContent.Setting.CONTENT_ITEM_TYPE;

           default:
               throw new IllegalArgumentException("Unknown URI " + uri);
       }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {

        LogUtil.log("DiaryProvider Insert: " + uri);
    	// A map to hold the new record's values.
        ContentValues values;

        // If the incoming values map is not null, uses it for the new values.
        if (initialValues != null) {
            values = new ContentValues(initialValues);

        } else {
            // Otherwise, create a new value map
            values = new ContentValues();
        }

        // Gets the current system time in milliseconds
        Long now = Long.valueOf(System.currentTimeMillis());
    	
    	String table;
		/**
		 * Choose the table and based on URI pattern-matching.
		 */
		switch (sUriMatcher.match(uri)) {

		case DIARIES:
		case DIARY_ID:
			table = DiaryContent.Diary.TABLE_NAME;
			// If the values map doesn't contain the creation date, sets the value to the current time.
	        if (values.containsKey(DiaryContent.Diary.COLUMN_CREATED) == false) {
	            values.put(DiaryContent.Diary.COLUMN_CREATED, now);
	        }
	        // If the values map doesn't contain the modification date, sets the value to the current
	        // time.
	        if (values.containsKey(DiaryContent.Diary.COLUMN_MODIFIED) == false) {
	            values.put(DiaryContent.Diary.COLUMN_MODIFIED, now);
	        }
			break;

		case BODIES:
        case BODY_ID:
            table = DiaryContent.Body.TABLE_NAME;
            // If the values map doesn't contain the creation date, sets the value to the current time.
            if (values.containsKey(DiaryContent.Body.COLUMN_CREATED) == false) {
                values.put(DiaryContent.Body.COLUMN_CREATED, now);
            }
            // If the values map doesn't contain the modification date, sets the value to the current
            // time.
            if (values.containsKey(DiaryContent.Body.COLUMN_MODIFIED) == false) {
                values.put(DiaryContent.Body.COLUMN_MODIFIED, now);
            }
            break;

        case ATTACHMENTS:
        case ATTACHMENT_ID:
            table = DiaryContent.Attachment.TABLE_NAME;
            // If the values map doesn't contain the creation date, sets the value to the current time.
            if (values.containsKey(DiaryContent.Attachment.COLUMN_CREATED) == false) {
                values.put(DiaryContent.Attachment.COLUMN_CREATED, now);
            }
            // If the values map doesn't contain the modification date, sets the value to the current
            // time.
            if (values.containsKey(DiaryContent.Attachment.COLUMN_MODIFIED) == false) {
                values.put(DiaryContent.Attachment.COLUMN_MODIFIED, now);
            }
            break;
		// If the incoming URI is for Settings, chooses the Setting table
		case SETTINGS:
		case SETTING_ID:
			table = DiaryContent.Setting.TABLE_NAME;
			break;
		default:
			// If the URI doesn't match any of the known patterns, throw an
			// exception.
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

        // Opens the database object in "write" mode.
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        // Performs the insert and returns the ID of the new note.
        long rowId = db.insert(
            table,        // The table to insert into.
            null,  // A hack, SQLite sets this column value to null
                                             // if values is empty.
            values                           // A map of column names, and the values to insert
                                             // into the columns.
        );

        // If the insert succeeded, the row ID exists.
        if (rowId > 0) {
            // Creates a URI with the note ID pattern and the new row ID appended to it.
            Uri noteUri = ContentUris.withAppendedId(uri, rowId);
            // Notifies observers registered against this provider that the data changed.
            getContext().getContentResolver().notifyChange(noteUri, null);
            LogUtil.log("DiaryProvider Insert " + table + " and generated rowid = "+ rowId);
            return noteUri;
        }
        // If the insert didn't succeed, then the rowID is <= 0. Throws an exception.
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {

        LogUtil.log("DiaryProvider Delete: " + uri);
        String table ;
        String finalWhere;

        int count;

        /**
		 * Choose the table and based on URI pattern-matching.
		 */
		switch (sUriMatcher.match(uri)) {

		case DIARIES:
			table = DiaryContent.Diary.TABLE_NAME;
			finalWhere = where;
			break;
		case DIARY_ID:
			table = DiaryContent.Diary.TABLE_NAME;
			finalWhere =
					DiaryContent.Diary.COLUMN_ID + " = " + uri.getPathSegments().get(1);
			if (where != null) {
				finalWhere = finalWhere + " AND " + where;
			}
			break;

		case BODIES:
            table = DiaryContent.Body.TABLE_NAME;
            finalWhere = where;
            break;
        case BODY_ID:
            table = DiaryContent.Body.TABLE_NAME;
            finalWhere =
                    DiaryContent.Body.COLUMN_ID + " = " + uri.getPathSegments().get(1);
            if (where != null) {
                finalWhere = finalWhere + " AND " + where;
            }
            break;

        case ATTACHMENTS:
            table = DiaryContent.Attachment.TABLE_NAME;
            finalWhere = where;
            break;
        case ATTACHMENT_ID:
            table = DiaryContent.Attachment.TABLE_NAME;
            finalWhere =
                    DiaryContent.Attachment.COLUMN_ID + " = " + uri.getPathSegments().get(1);
            if (where != null) {
                finalWhere = finalWhere + " AND " + where;
            }
            break;
		// If the incoming URI is for Settings, chooses the Setting table
		case SETTINGS:
			table = DiaryContent.Setting.TABLE_NAME;
			finalWhere = where;
			break;
		case SETTING_ID:
			table = DiaryContent.Setting.TABLE_NAME;
			finalWhere =
					DiaryContent.Setting.COLUMN_ID + " = " + uri.getPathSegments().get(1);
			if (where != null) {
				finalWhere = finalWhere + " AND " + where;
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
        
        // Opens the database object in "write" mode.
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        count = db.delete(
        		table,  // The database table name.
                finalWhere,                // The final WHERE clause
                whereArgs                  // The incoming where clause values.
            );
        getContext().getContentResolver().notifyChange(uri, null);
        LogUtil.log("DiaryProvider Delete " + table + ", count=" + count + ", finalWhere=" + finalWhere
				+ ", whereArgs=" + (whereArgs == null ? null : Arrays.toString(whereArgs)));
        return count;
    }


    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {

        LogUtil.log("DiaryProvider Update: " + uri);
    	String table;
        int count;
        String finalWhere;

        /**
		 * Choose the table and based on URI pattern-matching.
		 */
		switch (sUriMatcher.match(uri)) {

		case DIARIES:
			table = DiaryContent.Diary.TABLE_NAME;
			finalWhere = where;
			break;
		case DIARY_ID:
			table = DiaryContent.Diary.TABLE_NAME;
			finalWhere =
					DiaryContent.Diary.COLUMN_ID + " = " + uri.getPathSegments().get(1);
			if (where != null) {
				finalWhere = finalWhere + " AND " + where;
			}
			break;

		case BODIES:
            table = DiaryContent.Body.TABLE_NAME;
            finalWhere = where;
            break;
        case BODY_ID:
            table = DiaryContent.Body.TABLE_NAME;
            finalWhere =
                    DiaryContent.Body.COLUMN_ID + " = " + uri.getPathSegments().get(1);
            if (where != null) {
                finalWhere = finalWhere + " AND " + where;
            }
            break;

        case ATTACHMENTS:
            table = DiaryContent.Attachment.TABLE_NAME;
            finalWhere = where;
            break;
        case ATTACHMENT_ID:
            table = DiaryContent.Attachment.TABLE_NAME;
            finalWhere =
                    DiaryContent.Attachment.COLUMN_ID + " = " + uri.getPathSegments().get(1);
            if (where != null) {
                finalWhere = finalWhere + " AND " + where;
            }
            break;
		// If the incoming URI is for Settings, chooses the Setting table
		case SETTINGS:
			table = DiaryContent.Setting.TABLE_NAME;
			finalWhere = where;
			break;
		case SETTING_ID:
			table = DiaryContent.Setting.TABLE_NAME;
			finalWhere =
					DiaryContent.Setting.COLUMN_ID + " = " + uri.getPathSegments().get(1);
			if (where != null) {
				finalWhere = finalWhere + " AND " + where;
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
        // Opens the database object in "write" mode.
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        
        count = db.update(
                table,                    // The database table name.
                values,                   // A map of column names and new values to use.
                finalWhere,               // The final WHERE clause to use
                                          // placeholders for whereArgs
                whereArgs                 // The where clause column values to select on, or
                                          // null if the values are in the where argument.
            );
        getContext().getContentResolver().notifyChange(uri, null);
        LogUtil.log("DiaryProvider Update " + table + ", count=" + count + ", finalWhere=" + finalWhere
				+ ", whereArgs=" + (whereArgs == null ? null : Arrays.toString(whereArgs)));
        return count;
    }
}
