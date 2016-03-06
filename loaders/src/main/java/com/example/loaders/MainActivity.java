package com.example.loaders;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.loaders.provider.MyContentProvider;
import com.example.loaders.provider.MyDataBase;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @InjectView(R.id.editText)
    EditText editText;
    private Uri insertedUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

    }
    int count=998;
    @OnClick({R.id.textView, R.id.query, R.id.insert, R.id.update, R.id.delete})
    public void onClicked(View view) {
        count--;
        ContentResolver contentResolver = getContentResolver();
        switch (view.getId()) {
            case R.id.textView:
                break;
            case R.id.query:
            {
                Uri uri = Uri.parse("content://" + MyContentProvider.AUTHORITY + "/" + MyDataBase.Tables.CONTACTS);
                Cursor query = contentResolver.query(uri, null, null, null, null);
                if (query!=null){
                    while (query.moveToNext()){
                    String name=query.getString(query.getColumnIndex("contact_name"));
                    String number=query.getString(query.getColumnIndex("contact_number"));
                        String s = editText.getText().toString();
                        s+="\n"+name +"--"+number;
                        editText.setText(s);
                    }
                    query.close();
                }
            }
                break;
            case R.id.insert: {
                Uri uri = Uri.parse("content://" + MyContentProvider.AUTHORITY + "/" + MyDataBase.Tables.CONTACTS);
                ContentValues values = new ContentValues();
                values.put("contact_name", "你猜"+count);
                values.put("contact_number", "n_123456=="+count);
                insertedUri = contentResolver.insert(uri, values);
            }
            break;
            case R.id.update: {
                Uri uri = Uri.parse("content://" + MyContentProvider.AUTHORITY + "/" + MyDataBase.Tables.CONTACTS + "/" + insertedUri.getPathSegments().get(1));
                ContentValues values = new ContentValues();
                values.put("contact_name", "shi偷");
                values.put("contact_number", "7894562");
                contentResolver.update(uri, values, null, null);
            }
            break;
            case R.id.delete: {
                Uri uri = Uri.parse("content://" + MyContentProvider.AUTHORITY + "/" + MyDataBase.Tables.CONTACTS + "/" + insertedUri.getPathSegments().get(1));
                contentResolver.delete(uri, null, null);
            }
            break;


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_search) {
            startActivity(new Intent(this, SearchActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
