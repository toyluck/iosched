package com.atrue.hyc.searchview.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.atrue.hyc.searchview.provider.ScheduleConract.ContactColmns;

/**
 * Created by HYC on 2016/2/24.
 */
public class MyDataBase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Hyc.db";
    public static final int GOOGLER_24 = 1;
    public static final int GOOGLER_25 = 2;
    public static final int GOOGLER_26 = 3;
    public static final int GOOGLER_VERSION = GOOGLER_24;
    private final Context _context;

    interface Tables {
        String CONTACTS = "contacts";
    }


    public MyDataBase(Context context) {
        super(context, DATABASE_NAME, null, GOOGLER_VERSION);
        _context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.CONTACTS + " (" +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ContactColmns.CONTACT_NAME +"TEXT NOT NULL,"+
                ContactColmns.CONTACT_NUMBER+ "TEXT NOT NULL,"+
                "UNIQUE ("+ContactColmns.CONTACT_NUMBER+") ON CONFLICT REPLACE"
                +" )"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static void deleteDatabase(Context context){
        context.deleteDatabase(DATABASE_NAME);
    }
}
