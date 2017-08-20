package com.tokopedia.inbox.rescenter.createreso.view.activity;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.view.listener.ProductProblemActivityPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.presenter.ProductProblemActivityPresenterImpl;
import com.tokopedia.inbox.rescenter.createreso.view.presenter.ProductProblemFragmentPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemListViewModel;

/**
 * Created by yoasfs on 14/08/17.
 */

public class ProductProblemActivity extends BasePresenterActivity<ProductProblemActivityPresenter> implements ProductProblemView, HasComponent {

    private static final String KEY_PARAM_PASS_DATA = "pass_data";
    ProductProblemListViewModel productProblemListViewModel;

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        productProblemListViewModel = (ProductProblemListViewModel) extras.get(KEY_PARAM_PASS_DATA);
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
        presenter.initFragment(productProblemListViewModel);

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
