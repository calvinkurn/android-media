package com.tokopedia.inbox.rescenter.createreso.view.activity;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.view.listener.ProductProblemListActivityPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.presenter.ProductProblemListActivityPresenterImpl;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ProblemResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemListViewModel;

import java.util.ArrayList;

/**
 * Created by yoasfs on 14/08/17.
 */

public class ProductProblemListActivity extends BasePresenterActivity<ProductProblemListActivityPresenter> implements ProductProblemListActivityView, HasComponent {

    private static final String KEY_PARAM_PASS_DATA = "pass_data";
    public static final String PROBLEM_RESULT_LIST_DATA = "problem_result_list_data";


    ProductProblemListViewModel productProblemListViewModel;
    ArrayList<ProblemResult> problemResultList = new ArrayList<>();

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        productProblemListViewModel = (ProductProblemListViewModel) extras.get(KEY_PARAM_PASS_DATA);
        problemResultList = extras.getParcelableArrayList(PROBLEM_RESULT_LIST_DATA);
    }

    @Override
    protected void initialPresenter() {
        presenter = new ProductProblemListActivityPresenterImpl(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_product_problem;
    }

    @Override
    protected void initView() {
        presenter.initFragment(productProblemListViewModel, problemResultList);

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
