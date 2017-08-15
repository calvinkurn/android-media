package com.tokopedia.inbox.rescenter.createreso.view.activity;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.view.listener.ProductProblemActivityPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.presenter.ProductProblemActivityPresenterImpl;
import com.tokopedia.inbox.rescenter.createreso.view.presenter.ProductProblemFragmentPresenter;

/**
 * Created by yoasfs on 14/08/17.
 */

public class ProductProblemActivity extends BasePresenterActivity<ProductProblemActivityPresenter> implements ProductProblemView, HasComponent {

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
        presenter = new ProductProblemActivityPresenterImpl(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_product_problem;
    }

    @Override
    protected void initView() {
        presenter.initFragment();

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
    public void inflateFragment(Fragment fragment, String TAG) {
        if (getFragmentManager().findFragmentByTag(TAG) == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment, TAG)
                    .commit();
        }
    }

    @Override
    public Object getComponent() {
        return getApplicationComponent();
    }
}
