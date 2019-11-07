package com.tokopedia.seller.opportunity.activity;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.fragment.OpportunitySortFragment;
import com.tokopedia.seller.opportunity.viewmodel.SortingTypeViewModel;

import java.util.ArrayList;

/**
 * Created by nisie on 3/6/17.
 */

public class OpportunitySortActivity extends BasePresenterActivity {

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

        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            OpportunitySortFragment fragment = OpportunitySortFragment.createInstance(getIntent().getExtras());
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.commit();
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

    public static Intent getCallingIntent(Context context,
                                          ArrayList<SortingTypeViewModel> listSort) {
        Intent intent = new Intent(context, OpportunitySortActivity.class);
        intent.putParcelableArrayListExtra(OpportunitySortFragment.ARGS_LIST_SORT, listSort);
        return intent;
    }
}
