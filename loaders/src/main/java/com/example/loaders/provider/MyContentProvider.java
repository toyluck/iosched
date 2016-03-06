package com.example.loaders.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;


/**
 * Created by hyc on 16-3-6.
 */
public class MyContentProvider extends ContentProvider {

    private MyDataBase dbHelper;
    public static final int CONTACTS_DIR = 0;
    public static final int CONTACTS_ITEM = 1;
    public static final String AUTHORITY = "com.example.loaders";
    private static UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, MyDataBase.Tables.CONTACTS, CONTACTS_DIR);
        uriMatcher.addURI(AUTHORITY, MyDataBase.Tables.CONTACTS+"/#", CONTACTS_ITEM);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new MyDataBase(getContext());
        return true;
    }



    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor=null;
        switch (uriMatcher.match(uri)) {
            case CONTACTS_DIR:
                cursor=db.query(MyDataBase.Tables.CONTACTS,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case CONTACTS_ITEM:
                String selectionArg = selectionArgs[0] == null ? "" : selectionArgs[0];
                cursor= query(MyContract.Tags.CONTENT_URI, MyContract.SearchTopicsSessions.CONTACTS_PROJECTION,
                        MyContract.SearchTopicsSessions.TOPIC_TAG_SELECTION,
                        new String[] {  selectionArg + "%"},
                       null);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case CONTACTS_DIR:
                return "vnd.android.cursor.dir/vnd."+AUTHORITY+"."+ MyDataBase.Tables.CONTACTS;
            case CONTACTS_ITEM:
                return "vnd.android.cursor.item/vnd."+AUTHORITY+"."+ MyDataBase.Tables.CONTACTS;

        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri uriReturn=null;
        switch (uriMatcher.match(uri))
        {
            case CONTACTS_DIR:
            case CONTACTS_ITEM:
                long newId = db.insertOrThrow(MyDataBase.Tables.CONTACTS, null, values);
                uriReturn=Uri.parse("content://"+AUTHORITY+"/"+ MyDataBase.Tables.CONTACTS+"/"+newId);
        }
        return uriReturn;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Uri uriDeleted=null;
                int delete = 0;
        switch (uriMatcher.match(uri))
        {
            case CONTACTS_DIR:
                delete = db.delete(MyDataBase.Tables.CONTACTS, selection, selectionArgs);
                break;
            case CONTACTS_ITEM:
                String id=uri.getPathSegments().get(1);
                delete = db.delete(MyDataBase.Tables.CONTACTS, " _id = ? ", new String[]{id});

        }
        return delete;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();

        int updateId=0;
        switch (uriMatcher.match(uri))
        {
            case CONTACTS_DIR:
                updateId = db.update(MyDataBase.Tables.CONTACTS, values,selection,selectionArgs);
                break;
            case CONTACTS_ITEM:
                String id=uri.getPathSegments().get(1);
                updateId = db.update(MyDataBase.Tables.CONTACTS,values, " _id = ? ", new String[]{id});

        }
        return updateId;
    }
}
