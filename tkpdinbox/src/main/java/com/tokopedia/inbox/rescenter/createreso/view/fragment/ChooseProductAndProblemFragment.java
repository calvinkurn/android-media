package com.tokopedia.inbox.rescenter.createreso.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.base.BaseDaggerFragment;
import com.tokopedia.inbox.rescenter.createreso.view.presenter.ProductProblemPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemViewModel;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by yoasfs on 14/08/17.
 */

public class ChooseProductAndProblemFragment extends BaseDaggerFragment implements ChooseProductAndProblemView {
    RecyclerView rvProductProblem;

    public static ChooseProductAndProblemFragment newInstance() {
        ChooseProductAndProblemFragment fragment = new ChooseProductAndProblemFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Inject
    ProductProblemPresenter presenter;

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_product_problem;
    }

    @Override
    protected void initView(View view) {
        rvProductProblem = (RecyclerView) view.findViewById(R.id.rv_product_problem);

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public void populateProblemAndProoduct(List<ProductProblemViewModel> productProblemViewModelList) {

    }
}
