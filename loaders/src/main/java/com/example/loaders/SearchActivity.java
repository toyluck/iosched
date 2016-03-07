package com.example.loaders;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

import com.example.loaders.provider.MyContentProvider;
import com.example.loaders.provider.MyContract;
import com.example.loaders.provider.MyDataBase;
import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by hyc on 16-3-6.
 */
public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    @InjectView(R.id.scrim)
    View scrim;
    @InjectView(R.id.search_view)
    SearchView searchView;
    @InjectView(R.id.toolbar_searchactionbar)
    Toolbar toolbarSearchactionbar;
    @InjectView(R.id.search_results)
    ListView searchResults;
    @InjectView(R.id.search_panel)
    CardView searchPanel;
    private SimpleCursorAdapter cursorAdapter;
    private String KEY;
    private MyCursorAdapter myCursorAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.inject(this);

        setupSearchView();
        initListView();
        doInsertAnim();


        overridePendingTransition(0, 0);
    }

    private void initListView() {
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.item_search_result, null,
                new String[]{"contact_name","contact_number"}, new int[]{R.id.search_result}, 0);
//        searchResults.setAdapter(cursorAdapter);
        myCursorAdapter = new MyCursorAdapter(this,null);
        searchResults.setAdapter(myCursorAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void dismiss(View view) {
        ActivityCompat.finishAfterTransition(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void doInsertAnim() {
        scrim.animate().alpha(1).setDuration(500L).setInterpolator(AnimationUtils.
                loadInterpolator(this, android.R.interpolator.fast_out_slow_in)).start();

        searchPanel.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                searchPanel.getViewTreeObserver().removeOnPreDrawListener(this);
                ViewGroup searchPanelParent = (ViewGroup) searchPanel.getParent();
                int revealRadius = searchPanelParent.getHeight();
                Animator animator = ViewAnimationUtils.createCircularReveal(searchPanel, searchPanel.getRight(), searchPanel.getTop(), 0, revealRadius);
                animator.setDuration(500L).setInterpolator(AnimationUtils.loadInterpolator(searchPanel.getContext(), android.R.interpolator.fast_out_slow_in));
                animator.start();
                return false;
            }
        });
    }

    private void setupSearchView() {
        Drawable up = DrawableCompat.wrap(ContextCompat.getDrawable(this, R.drawable.ic_up));
        DrawableCompat.setTint(up, getResources().getColor(R.color.app_body_text_2));

        toolbarSearchactionbar.setNavigationIcon(up);
        toolbarSearchactionbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(null);
            }
        });

        RxSearchView.queryTextChanges(searchView).debounce(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<CharSequence>() {
            @Override
            public void call(CharSequence charSequence) {
                searchFor(String.valueOf(charSequence));
            }
        });
       /* searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
//                searchFor(query);
                return false;
            }

        });*/

    }

    private void searchFor(String query) {
        Bundle args = new Bundle();
        KEY = "query";
        args.putString(KEY,query);
        getLoaderManager().restartLoader(0,args,this );
    }

    // These are the Contacts rows that we will retrieve.
    static final String[] CONTACTS_SUMMARY_PROJECTION = new String[]{
            Contacts._ID,
            Contacts.DISPLAY_NAME,
            Contacts.CONTACT_STATUS,
            Contacts.CONTACT_PRESENCE,
            Contacts.PHOTO_ID,
            Contacts.LOOKUP_KEY,
            Contacts.HAS_PHONE_NUMBER,
    };

    static final String[] CONTACTS_PROJECTION=new String[]{
            BaseColumns._ID,
            "contact_name",
            "contact_number",
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri;
        Uri uri = Uri.parse("content://" + MyContentProvider.AUTHORITY + "/" + MyDataBase.Tables.CONTACTS);

        String query = (String) args.get(KEY);
        if (!TextUtils.isEmpty(query)){
            baseUri=Uri.withAppendedPath(MyContract.Tags.CONTENT_FILTER_URI,Uri.encode(query));

        }else {
            baseUri=Contacts.CONTENT_URI;
        }

        String select = "((" + Contacts.DISPLAY_NAME + " NOTNULL) AND ("
                + Contacts.HAS_PHONE_NUMBER + "=1) AND ("
                + Contacts.DISPLAY_NAME + " != '' ))";
        String sel="SELECT * FROM contacts where contact_name like  \"你%\";";
//        return new CursorLoader(this,uri,CONTACTS_PROJECTION, MyContract.SearchTopicsSessions.TOPIC_TAG_SELECTION,new String[]{query}, "contact_name" + " COLLATE LOCALIZED ASC");

        return new CursorLoader(this,uri,CONTACTS_PROJECTION, null,new String[]{query+"%"},null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        myCursorAdapter.swapCursor(data);
    }



    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
