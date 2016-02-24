package com.atrue.hyc.searchview.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.atrue.hyc.searchview.util.SelectionBuilder;
import com.atrue.hyc.searchview.provider.ScheduleConract.Contacts;
import com.atrue.hyc.searchview.provider.ScheduleConract.Sessions;

import java.io.FileDescriptor;
import java.io.PrintWriter;

/**
 * Created by HYC on 2016/2/24.
 */
public class ScheduleProvider extends ContentProvider {

    private MyDataBase _OpenHelper;
    private ScheduleProviderUriMatcher _uriMatcher;

    @Override
    public boolean onCreate() {
        _OpenHelper = new MyDataBase(getContext());
        _uriMatcher = new ScheduleProviderUriMatcher();
        return true;
    }
    private void deleteDatabase(){
        _OpenHelper.close();
        Context context = getContext();
        MyDataBase.deleteDatabase(context);
        _OpenHelper=new MyDataBase(getContext());
    }
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        SQLiteDatabase db = _OpenHelper.getReadableDatabase();
        String tagsFilter = uri.getQueryParameter(ScheduleConract.Sessions.QUERY_PARAMETER_TAG_FILTER);
        String categories = uri.getQueryParameter(ScheduleConract.Sessions.QUERY_PARAMETER_CATEGORIER);

        ScheduleUriEnum matchingUriEnum = _uriMatcher.matchUri(uri);

        switch (matchingUriEnum){

            default:{
        final SelectionBuilder builder= buildExpandedSelection(uri,matchingUriEnum._code);
            }
        }
        return null;
    }

    private SelectionBuilder buildExpandedSelection(Uri uri, int code) {
        SelectionBuilder builder = new SelectionBuilder();
        ScheduleUriEnum matchingUriEnum = _uriMatcher.matchUri(uri);
        if (matchingUriEnum==null){
            throw new UnsupportedOperationException("Unknown uri :"+uri);
        }

        switch (matchingUriEnum){
            case CONTACT:return builder.table(MyDataBase.Tables.CONTACTS);
            case CONTACT_ID:
                String contactId = Contacts.getContactId(uri);
                return builder.table(MyDataBase.Tables.CONTACTS).where(Contacts.CONTACT_ID+"=?",contactId);
            default:
                throw new UnsupportedOperationException("Unknown uri : "+uri);
        }
    }


    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    @Override
    public void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(fd, writer, args);
    }


}
