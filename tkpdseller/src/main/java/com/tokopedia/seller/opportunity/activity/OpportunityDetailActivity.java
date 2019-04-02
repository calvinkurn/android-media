package com.tokopedia.seller.opportunity.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.fragment.OpportunityDetailFragment;
import com.tokopedia.seller.opportunity.listener.OpportunityDetailView;
import com.tokopedia.seller.opportunity.presenter.OpportunityDetailImpl;
import com.tokopedia.seller.opportunity.presenter.OpportunityDetailPresenter;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.OpportunityItemViewModel;

/**
 * Created by hangnadi on 2/27/17.
 */

public class OpportunityDetailActivity extends BasePresenterActivity<OpportunityDetailPresenter>
        implements OpportunityDetailView {

    private static final String OPPORTUNITY_FRAGMENT_TAG = OpportunityDetailFragment.class.getSimpleName();
    public static final String OPPORTUNITY_EXTRA_PARAM = "extra_param";

    public static Intent createIntent(Context context, OpportunityItemViewModel opportunityItemViewModel) {
        Intent intent = new Intent(context, OpportunityDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(OPPORTUNITY_EXTRA_PARAM, opportunityItemViewModel);
        intent.putExtras(bundle);
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
        presenter = new OpportunityDetailImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_opportunity_detail;
    }

    @Override
    protected void initView() {

        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null)
            bundle.putAll(getIntent().getExtras());

        if (getFragmentManager().findFragmentByTag(OPPORTUNITY_FRAGMENT_TAG) == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, OpportunityDetailFragment.createInstance(bundle), OPPORTUNITY_FRAGMENT_TAG)
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

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_OPPORTUNITY_DETAIL;
    }
}
