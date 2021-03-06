package com.atrue.hyc.searchview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
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

import com.atrue.hyc.searchview.provider.ScheduleConract;

/**
 * Created by HYC on 2016/2/20.
 */
public class SeachActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final int TOKEN = 0x4;
    private static CardView cardView;
    private SearchView searchView;
    private String mQuery;
    private ListView mSearchResults;
    private String TAG = SeachActivity.class.getSimpleName();
    private ListView _SearchResult;
    private SimpleCursorAdapter mCursorAdapter;

    public static void startSeachActivity(Context context) {
        Intent intent = new Intent(context, SeachActivity.class);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        _SearchResult = (ListView) findViewById(R.id.search_results);

        mCursorAdapter = new SimpleCursorAdapter(this, R.layout.item_search_result, null,
                new String[]{Phone.NUMBER},
                new int[]{R.id.search_result}, 0);
        _SearchResult.setAdapter(mCursorAdapter);

    }

    // These are the Contacts rows that we will retrieve.
    static final String[] CONTACTS_SUMMARY_PROJECTION = new String[]{
            Contacts._ID,
            Contacts.DISPLAY_NAME,
            Contacts.CONTACT_STATUS,
            Contacts.CONTACT_PRESENCE,
            Contacts.PHOTO_ID,
            Contacts.LOOKUP_KEY,
    };
    static final String[] CONTACTS_COMMON = new String[]{
            Phone.DISPLAY_NAME,
            Phone.NUMBER
};

@Override
protected void onStart(){
        super.onStart();
        cardView=(CardView)findViewById(R.id.search_panel);

        initSearchView();
//        为searchView 设置返回键

        Toolbar toolbar=getActionBarToolBar();
//        在xml中直接设置无用.直接设置返回键图片又太大
//        toolbar.setNavigationIcon(R.drawable.ic_up);

        Drawable up=DrawableCompat.wrap(ContextCompat.getDrawable(this,R.drawable.ic_up));
        DrawableCompat.setTint(up,getResources().getColor(R.color.app_body_text_2));
        toolbar.setNavigationIcon(up);

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
@Override
public void onClick(View view){
        navigateUpOrBack(SeachActivity.this,null);
        }
        });

        mSearchResults=(ListView)findViewById(R.id.search_results);


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        doInsertAnimation();

        String query=getIntent().getStringExtra(SearchManager.QUERY);
        query=query==null?"":query;
        mQuery=query;

        if(searchView!=null){
        searchView.setQuery(query,false);
        }

        overridePendingTransition(0,0);
        }

/**
 * This utility method handles Up navigation intents by searching for a parent activity and
 * navigating there if defined. When using this for an activity make sure to define both the
 * native parentActivity as well as the AppCompat one when supporting API levels less than 16.
 * when the activity has a single parent activity. If the activity doesn't have a single parent
 * activity then don't define one and this method will use back button functionality. If "Up"
 * functionality is still desired for activities without parents then use
 * {@code syntheticParentActivity} to define one dynamically.
 * <p/>
 * Note: Up navigation intents are represented by a back arrow in the top left of the Toolbar
 * in Material Design guidelines.
 *
 * @param currentActivity         Activity in use when navigate Up action occurred.
 * @param syntheticParentActivity Parent activity to use when one is not already configured.
 */
public static void navigateUpOrBack(Activity currentActivity,
        Class<?extends Activity>syntheticParentActivity){
        // Retrieve parent activity from AndroidManifest.
        Intent intent=NavUtils.getParentActivityIntent(currentActivity);

        // Synthesize the parent activity when a natural one doesn't exist.
        if(intent==null&&syntheticParentActivity!=null){
        try{
        intent=NavUtils.getParentActivityIntent(currentActivity,syntheticParentActivity);
        }catch(PackageManager.NameNotFoundException e){
        e.printStackTrace();
        }
        }

        if(intent==null){
        // No parent defined in manifest. This indicates the activity may be used by
        // in multiple flows throughout the app and doesn't have a strict parent. In
        // this case the navigation up button should act in the same manner as the
        // back button. This will result in users being forwarded back to other
        // applications if currentActivity was invoked from another application.
        currentActivity.onBackPressed();
        }else{
        if(NavUtils.shouldUpRecreateTask(currentActivity,intent)){
        // Need to synthesize a backstack since currentActivity was probably invoked by a
        // different app. The preserves the "Up" functionality within the app according to
        // the activity hierarchy defined in AndroidManifest.xml via parentActivity
        // attributes.
        TaskStackBuilder builder=TaskStackBuilder.create(currentActivity);
        builder.addNextIntentWithParentStack(intent);
        builder.startActivities();
        }else{
        // Navigate normally to the manifest defined "Up" activity.
        NavUtils.navigateUpTo(currentActivity,intent);
        }
        }
        }

private Toolbar getActionBarToolBar(){

        return(Toolbar)findViewById(R.id.toolbar_actionbar);
        }

private void initSearchView(){
        searchView=(SearchView)findViewById(R.id.search_view);
//        设置SearchView
        SearchManager searchManager=(SearchManager)getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


        searchView.setIconified(false);


        searchView.setQueryHint("搜索!");

        searchView.setOnCloseListener(new SearchView.OnCloseListener(){
@Override
public boolean onClose(){
        dismiss(null);
        return false;
        }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
@Override
public boolean onQueryTextSubmit(String query){
        searchView.clearFocus();
        return true;
        }

@Override
public boolean onQueryTextChange(String newText){
        SearchFor(newText);
        return true;
        }
        });
        }
private static final String ARG_QUERY="query";

private void SearchFor(String query){
        // ANALYTICS EVENT: Start a search on the Search activity
        // Contains: Nothing (Event params are constant:  Search query not included)
        Bundle args=new Bundle(1);
        if(query==null){
        query="";
        }
        args.putString(ARG_QUERY,query);
        if(TextUtils.equals(query,mQuery)){
        getLoaderManager().initLoader(TOKEN,null,this);
        }else{
        getLoaderManager().restartLoader(TOKEN,null,this);
        }
        mQuery=query;

        }

@Override
protected void onPause(){
        super.onPause();
        if(isFinishing()){
        overridePendingTransition(0,0);
        }
        }

@Override
protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        if(intent.hasExtra(SearchManager.QUERY)){
        System.out.println("Something is deaded");
        }
        }


@Override
public void onBackPressed(){
        dismiss(null);
        }


public void dismiss(View view){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
        doExitAnim();
        }else{
        ActivityCompat.finishAfterTransition(this);
        }
        }

private void doExitAnim(){

final View searchPanel=findViewById(R.id.search_panel);

//找到最远点
        int revealRadius=(int)Math.sqrt((Math.pow(searchPanel.getWidth(),2)+Math.pow(searchPanel.getHeight(),2)));

        Animator shrink=ViewAnimationUtils.createCircularReveal(searchPanel,searchPanel.getRight(),searchPanel.getTop(),revealRadius,0f);

        shrink.setDuration(250L);
        shrink.setInterpolator(AnimationUtils.loadInterpolator(SeachActivity.this,android.R.interpolator.fast_out_slow_in));
        shrink.addListener(new AnimatorListenerAdapter(){
@Override
public void onAnimationEnd(Animator animation){
        searchPanel.setVisibility(View.INVISIBLE);
        ActivityCompat.finishAfterTransition(SeachActivity.this);
        }
        });
        shrink.start();

        View scrim=findViewById(R.id.scrim);
        scrim.animate().alpha(0f).setDuration(200L).setInterpolator(
        AnimationUtils.loadInterpolator(SeachActivity.this,
        android.R.interpolator.fast_out_slow_in))
        .start();


        }

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
private void doInsertAnimation(){

        View scrim=findViewById(R.id.scrim);
        scrim.animate()
        .alpha(1f)
        .setDuration(500L)
        .setInterpolator(
        AnimationUtils.loadInterpolator(this,android.R.interpolator.fast_out_slow_in))
        .start();
        cardView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener(){
@Override
public boolean onPreDraw(){
        cardView.getViewTreeObserver().removeOnPreDrawListener(this);

        int revealRadius=((ViewGroup)cardView.getParent()).getHeight();

        Animator animator=ViewAnimationUtils.createCircularReveal(cardView,cardView.getRight(),cardView.getTop(),0,revealRadius);
        animator.setDuration(250L);
        animator.setInterpolator(AnimationUtils.loadInterpolator(SeachActivity.this,android.R.interpolator.fast_out_slow_in));
        animator.start();

        return false;
        }
        });

        }


@Override
public Loader<Cursor>onCreateLoader(int id,Bundle args){

        Uri baseUri=ScheduleConract.Contacts.CONTENT_URI;
        String select="(("+Contacts.DISPLAY_NAME+" NOTNULL) AND ("
        +Contacts.HAS_PHONE_NUMBER+"=1) AND ("
        +Contacts.DISPLAY_NAME+" != '' ))";
        return new CursorLoader(this,baseUri,CONTACTS_COMMON,null,null,null);
        }

@Override
public void onLoadFinished(Loader<Cursor>loader,Cursor data){
        mCursorAdapter.swapCursor(data);
        }

@Override
public void onLoaderReset(Loader<Cursor>loader){
        }
        }
