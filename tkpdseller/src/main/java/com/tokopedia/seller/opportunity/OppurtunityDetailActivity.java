package com.tokopedia.seller.opportunity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.fragment.OppurtunityDetailFragment;
import com.tokopedia.seller.opportunity.listener.OppurtunityDetailView;
import com.tokopedia.seller.opportunity.presenter.OppurtunityDetailImpl;
import com.tokopedia.seller.opportunity.presenter.OppurtunityDetailPresenter;
import com.tokopedia.seller.opportunity.viewmodel.Opportunity;

/**
 * Created by hangnadi on 2/27/17.
 */

public class OppurtunityDetailActivity extends BasePresenterActivity<OppurtunityDetailPresenter>
        implements OppurtunityDetailView {

    private static final String OPPURTUNITY_FRAGMENT_TAG = OppurtunityDetailFragment.class.getSimpleName();

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
        presenter = new OppurtunityDetailImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_oppurtunity_detail;
    }

    @Override
    protected void initView() {
        if (getFragmentManager().findFragmentByTag(OPPURTUNITY_FRAGMENT_TAG) == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, OppurtunityDetailFragment.createInstance(), OPPURTUNITY_FRAGMENT_TAG)
                    .commit();
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

    public static Intent getDetailIntent(Context context, Opportunity opportunity) {
        Intent intent = new Intent(context, OppurtunityDetailActivity.class);
//        intent.putExtra(ARGS_OPPORTUNITY, opportunity);
        return intent;
    }
}
