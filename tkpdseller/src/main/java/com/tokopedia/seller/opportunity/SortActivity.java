package com.tokopedia.seller.opportunity;

import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.fragment.SortFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsFilterContentFragment;

/**
 * Created by nisie on 3/6/17.
 */

public class SortActivity extends BasePresenterActivity implements TopAdsFilterContentFragment.Callback{

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
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        SortFragment fragment = SortFragment.createInstance(getIntent().getExtras());
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
        fragment.setCallback(this);
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
    public void onStatusChanged(boolean active) {

    }
}
