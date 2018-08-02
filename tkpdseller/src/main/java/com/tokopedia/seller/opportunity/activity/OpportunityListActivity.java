package com.tokopedia.seller.opportunity.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.fragment.OpportunityListFragment;

/**
 * @author by alvinatin on 31/07/18.
 */

public class OpportunityListActivity extends BasePresenterActivity {

//    @DeepLink(ApplinkConst.SELLER_OPPORTUNITY)
    public static Intent getCallingIntentSellerOpportunity(Context context, Bundle extras) {
        return OpportunityListActivity.getCallingIntent(context, "");
    }

    @Override
    protected void setupURIPass(Uri data) { }

    @Override
    protected void setupBundlePass(Bundle extras) { }

    @Override
    protected void initialPresenter() { }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected void initView() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            String query = "";
            if (getIntent() != null && getIntent().getExtras() != null) {
                query = getIntent().getExtras().getString(OpportunityListFragment.EXTRA_QUERY);
            }
            Fragment fragment =
                    OpportunityListFragment.newInstance(query);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void setViewListener() { }

    @Override
    protected void initVar() { }

    @Override
    protected void setActionVar() { }

    public static Intent getCallingIntent(Context context, String query) {
        Intent intent = new Intent(context, OpportunityListActivity.class);
        intent.putExtra(OpportunityListFragment.EXTRA_QUERY, query);
        return intent;
    }
}
