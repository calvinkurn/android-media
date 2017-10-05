package com.tokopedia.tkpd.tkpdfeed.feedplus.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.fragment.FeedPlusDetailFragment;

/**
 * @author by nisie on 5/18/17.
 */

public class FeedPlusDetailActivity extends BasePresenterActivity implements HasComponent {

    public static final String EXTRA_DETAIL_ID = "EXTRA_DETAIL_ID";

    public static Intent getIntent(FragmentActivity activity, String detailId) {
        Intent intent = new Intent(activity, FeedPlusDetailActivity.class);
        intent.putExtra(EXTRA_DETAIL_ID, detailId);
        return intent;
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
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
    protected void initView() {
        setupToolbar();

        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null)
            bundle.putAll(getIntent().getExtras());

        FeedPlusDetailFragment fragment = FeedPlusDetailFragment.createInstance(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (getSupportFragmentManager().findFragmentByTag(
                FeedPlusDetailFragment.class.getSimpleName()) == null) {
            fragmentTransaction.replace(R.id.container,
                    fragment,
                    fragment.getClass().getSimpleName());
        } else
            fragmentTransaction.replace(R.id.container,
                    getSupportFragmentManager().findFragmentByTag(
                            FeedPlusDetailFragment.class.getSimpleName()));
        fragmentTransaction.commit();

    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    public void setupToolbar() {
        super.setupToolbar();

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.white)));
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.grey_700));
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

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

}