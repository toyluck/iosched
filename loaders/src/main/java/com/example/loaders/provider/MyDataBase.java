package com.example.loaders.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by hyc on 16-3-6.
 */
public class MyDataBase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="hyc.db";
    public static final int LEVEL_1=1;
    public static final int DB_LEVEL=LEVEL_1;
    private final Context context;

  public   interface Tables{
        String CONTACTS="contacts";
    }

    public MyDataBase(Context context) {
        super(context, DATABASE_NAME, null, DB_LEVEL);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="CREATE TABLE "+Tables.CONTACTS+"(" +
                BaseColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT  ," +
                "contact_name "+ " TEXT  ," +
                "contact_number "+" TEXT  " +
                " )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
