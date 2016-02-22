package com.atrue.hyc.searchview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by HYC on 2016/2/20.
 */
public class SeachActivity extends AppCompatActivity {


    private static CardView cardView;
    private SearchView searchView;
    private String mQuery;
    private ListView mSearchResults;
    private String TAG=SeachActivity.class.getSimpleName();

    public static void startSeachActivity(Context context) {
        Intent intent = new Intent(context, SeachActivity.class);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        cardView = (CardView) findViewById(R.id.search_panel);

        initSearchView();
//        为searchView 设置返回键

        Toolbar toolbar=getActionBarToolBar();
//        在xml中直接设置无用.直接设置返回键图片又太大
//        toolbar.setNavigationIcon(R.drawable.ic_up);

        Drawable up=DrawableCompat.wrap(ContextCompat.getDrawable(this,R.drawable.ic_up));
        DrawableCompat.setTint(up,getResources().getColor(R.color.app_body_text_2));
        toolbar.setNavigationIcon(up);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activityIntent = NavUtils.getParentActivityIntent(SeachActivity.this);
                String parentActivityName = NavUtils.getParentActivityName(SeachActivity.this);
                Log.d(TAG, "onClick: "+parentActivityName);
            }
        });

        mSearchResults = (ListView) findViewById(R.id.search_results);




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            doInsertAnimation();

        String query = getIntent().getStringExtra(SearchManager.QUERY);
        query = query == null ? "" : query;
        mQuery = query;

        if (searchView != null) {
            searchView.setQuery(query, false);
        }

        overridePendingTransition(0, 0);

    }

    private Toolbar getActionBarToolBar() {


        return (Toolbar) findViewById(R.id.toolbar_actionbar);
    }

    private void initSearchView() {
        searchView = (SearchView) findViewById(R.id.search_view);
//        设置SearchView
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


        searchView.setIconified(false);


        searchView.setQueryHint("搜索!");

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                    dismiss(null);
                return false;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(0, 0);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(SearchManager.QUERY)){
            System.out.println("Something is deaded");
        }
    }


    @Override
    public void onBackPressed() {
        dismiss(null);
    }


    public void dismiss(View view) {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            doExitAnim();
        }else {
            ActivityCompat.finishAfterTransition(this);
        }
    }

    private void doExitAnim() {

        final View searchPanel = findViewById(R.id.search_panel);

//找到最远点
        int revealRadius = (int) Math.sqrt((Math.pow(searchPanel.getWidth(), 2) + Math.pow(searchPanel.getHeight(), 2)));

        Animator shrink = ViewAnimationUtils.createCircularReveal(searchPanel, searchPanel.getRight(), searchPanel.getTop(), revealRadius, 0f);

        shrink.setDuration(250L);
        shrink.setInterpolator(AnimationUtils.loadInterpolator(SeachActivity.this,android.R.interpolator.fast_out_slow_in));
        shrink.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
            searchPanel.setVisibility(View.INVISIBLE);
                ActivityCompat.finishAfterTransition(SeachActivity.this);
            }
        });
        shrink.start();

        View scrim=findViewById(R.id.scrim);
        scrim.animate().alpha(0f).setDuration(200L) .setInterpolator(
                AnimationUtils.loadInterpolator(SeachActivity.this,
                        android.R.interpolator.fast_out_slow_in))
                .start();


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void doInsertAnimation() {

        View scrim = findViewById(R.id.scrim);
        scrim.animate()
                .alpha(1f)
                .setDuration(500L)
                .setInterpolator(
                        AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in))
                .start();
        cardView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                cardView.getViewTreeObserver().removeOnPreDrawListener(this);

                int revealRadius = ((ViewGroup) cardView.getParent()).getHeight();

                Animator animator = ViewAnimationUtils.createCircularReveal(cardView, cardView.getRight(), cardView.getTop(), 0, revealRadius);
                animator.setDuration(250L);
                animator.setInterpolator(AnimationUtils.loadInterpolator(SeachActivity.this, android.R.interpolator.fast_out_slow_in));
                animator.start();

                return false;
            }
        });

    }
}
