package com.tokopedia.inbox.rescenter.createreso.view.activity;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.view.listener.ProductProblemDetailActivityPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.presenter.ProductProblemDetailActivityPresenterImpl;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ProblemResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemViewModel;

/**
 * Created by yoasfs on 14/08/17.
 */

public class ProductProblemDetailActivity
        extends BasePresenterActivity<ProductProblemDetailActivityPresenter>
        implements ProductProblemDetailActivityView, HasComponent {

    public static final String PRODUCT_PROBLEM_DATA = "product_problem_data";
    public static final String PROBLEM_RESULT_DATA = "problem_result_data";

    ProblemResult problemResult;
    ProductProblemViewModel productProblemViewModel;

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        productProblemViewModel = (ProductProblemViewModel) extras.get(PRODUCT_PROBLEM_DATA);
        problemResult = (ProblemResult) extras.get(PROBLEM_RESULT_DATA);
    }

    @Override
    protected void initialPresenter() {
        presenter = new ProductProblemDetailActivityPresenterImpl(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_product_problem;
    }

    @Override
    protected void initView() {
        setTitle(productProblemViewModel.getOrder().getProduct().getName());
        toolbar.setTitle(productProblemViewModel.getOrder().getProduct().getName());
        presenter.initFragment(productProblemViewModel, problemResult);

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
