package com.atrue.hyc.searchview.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.atrue.hyc.searchview.appwidget.ScheduleWidgeProvider;
import com.atrue.hyc.searchview.util.SelectionBuilder;
import com.atrue.hyc.searchview.provider.ScheduleConract.Contacts;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Set;

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

    //删除现有的  并新建一个
    private void deleteDatabase() {
        _OpenHelper.close();
        Context context = getContext();
        MyDataBase.deleteDatabase(context);
        _OpenHelper = new MyDataBase(getContext());
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = _OpenHelper.getWritableDatabase();
        String tagsFilter = uri.getQueryParameter(ScheduleConract.Sessions.QUERY_PARAMETER_TAG_FILTER);
        String categories = uri.getQueryParameter(ScheduleConract.Sessions.QUERY_PARAMETER_CATEGORIER);

        ScheduleUriEnum matchingUriEnum = _uriMatcher.matchUri(uri);

        switch (matchingUriEnum) {

            default: {
                final SelectionBuilder builder = buildExpandedSelection(uri, matchingUriEnum._code);
                boolean distinct = ScheduleConractHelper.isQueryDistinct(uri);
                Cursor cursor = builder.where(selection, selectionArgs).query(db, distinct, projection, sortOrder, null);

                Context context = getContext();
                if (null != context) {
                    cursor.setNotificationUri(context.getContentResolver(), uri);
                }

                return cursor;
            }
            case CONTACT://
                break;
            case CONTACT_ID:
                break;
        }
        return null;
    }

    /**
     * 通过{@code Uri}
     *
     * @param uri
     * @param match
     * @return
     */
    private SelectionBuilder buildExpandedSelection(Uri uri, int match) {
        SelectionBuilder builder = new SelectionBuilder();
        ScheduleUriEnum matchingUriEnum = _uriMatcher.matchCode(match);
        if (matchingUriEnum == null) {
            throw new UnsupportedOperationException("Unknown uri :" + uri);
        }

        switch (matchingUriEnum) {
            case CONTACT:
                return builder.table(MyDataBase.Tables.CONTACTS);
            case CONTACT_ID:
                String contactId = Contacts.getContactId(uri);
                return builder.table(MyDataBase.Tables.CONTACTS).where(Contacts.CONTACT_ID + "=?", contactId);
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
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
        SQLiteDatabase db = _OpenHelper.getWritableDatabase();
        ScheduleUriEnum matchUriEnum = _uriMatcher.matchUri(uri);
        if (matchUriEnum._table!=null){
            db.insertOrThrow(matchUriEnum._path,null,contentValues);
            notifyChange(uri);
        }

        switch (matchUriEnum){
            case CONTACT:return Contacts.buildContactUri(contentValues.getAsString(Contacts._ID));
            case CONTACT_ID:return Contacts.buildContactUri(contentValues.getAsString(Contacts._ID));
        }
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

    /**
     * Notifies the system that the given {@code uri} data has changed.
     * <p/>
     * We only notify changes if the uri wasn't called by the sync adapter, to avoid issuing a large
     * amount of notifications while doing a sync. The
     * {@linkcom.google.samples.apps.iosched.sync.ConferenceDataHandler} notifies all top level
     * conference paths once the conference data sync is done, and the
     * {@linkcom.google.samples.apps.iosched.sync.userdata.AbstractUserDataSyncHelper} notifies all
     * user data related paths once the user data sync is done.
     */
    private void notifyChange(Uri uri) {
        if (!ScheduleConractHelper.isUriCalledFromSyncAdapter(uri)) {
            Context context = getContext();
            context.getContentResolver().notifyChange(uri, null);
            // Widgets can't register content observers so we refresh widgets separately.
            context.sendBroadcast(ScheduleWidgeProvider.getRefreshBroadcastIntent(context, false));
        }
    }
}
