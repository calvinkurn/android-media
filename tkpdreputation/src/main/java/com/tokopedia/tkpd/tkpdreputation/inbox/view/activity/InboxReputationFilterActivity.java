package com.tokopedia.tkpd.tkpdreputation.inbox.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.InboxReputationFilterFragment;

/**
 * @author by nisie on 8/21/17.
 */

public class InboxReputationFilterActivity extends BasePresenterActivity {

    public static final String CACHE_INBOX_REPUTATION_FILTER = "CACHE_INBOX_REPUTATION_FILTER";

    public static Intent createIntent(Context context, String timeFilter) {
        Intent intent = new Intent(context, InboxReputationFilterActivity.class);
        intent.putExtra(InboxReputationFilterFragment.SELECTED_TIME_FILTER, timeFilter);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_close, menu);
        return true;
    }

    @Override


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_close) {
            finish();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }


    @Override
    protected void initView() {
        String timeFilter = getIntent().getStringExtra(InboxReputationFilterFragment
                .SELECTED_TIME_FILTER);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        Fragment fragment = getSupportFragmentManager().findFragmentByTag
                (InboxReputationFilterFragment
                        .class.getSimpleName());
        if (fragment == null) {
            fragment = InboxReputationFilterFragment.createInstance(timeFilter);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,
                fragment,
                fragment.getClass().getSimpleName());
        fragmentTransaction.commit();

    }

    @Override
    protected void setViewListener() {
    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View view = getWindow().getDecorView();
            int flags = view.getSystemUiVisibility();

            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.white)));
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.grey_700));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.grey_300));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(10);
        }

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Drawable upArrow = ContextCompat.getDrawable(this, android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        if (upArrow != null) {
            upArrow.setColorFilter(ContextCompat.getColor(this, R.color.grey_700), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }
    }
}
