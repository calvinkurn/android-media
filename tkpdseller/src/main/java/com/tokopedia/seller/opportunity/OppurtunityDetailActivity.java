package com.tokopedia.seller.opportunity;

import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.seller.R;

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
}
