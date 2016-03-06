package com.example.loaders;


import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by hyc on 16-3-6.
 */
public class MyCursorAdapter extends CursorAdapter {
    public MyCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    public MyCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public MyCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.item_my_result,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView view1 = (TextView) view.findViewById(R.id.text);
        TextView view2 = (TextView) view.findViewById(R.id.text2);
        view1.setText(cursor.getString(cursor.getColumnIndex("contact_number")));
        view2.setText(cursor.getString(cursor.getColumnIndex("contact_name")));

    }
}
