package com.tokopedia.inbox.rescenter.createreso.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.base.BaseDaggerFragment;
import com.tokopedia.inbox.rescenter.createreso.view.adapter.ProductProblemAdapter;
import com.tokopedia.inbox.rescenter.createreso.view.di.DaggerCreateResoComponent;
import com.tokopedia.inbox.rescenter.createreso.view.listener.ProductProblem;
import com.tokopedia.inbox.rescenter.createreso.view.listener.ProductProblemItemListener;
import com.tokopedia.inbox.rescenter.createreso.view.presenter.ProductProblemFragmentPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemListViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemViewModel;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by yoasfs on 14/08/17.
 */

public class ChooseProductAndProblemFragment extends BaseDaggerFragment implements ProductProblem.View, ProductProblemItemListener {

    public static final String PRODUCT_PROBLEM_DATA = "product_problem_data";

    RecyclerView rvProductProblem;
    ProductProblemAdapter adapter;
    ProductProblemListViewModel productProblemListViewModel;

    public static ChooseProductAndProblemFragment newInstance(ProductProblemListViewModel productProblemListViewModel) {
        ChooseProductAndProblemFragment fragment = new ChooseProductAndProblemFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PRODUCT_PROBLEM_DATA, productProblemListViewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Inject
    ProductProblemFragmentPresenter presenter;

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);
        DaggerCreateResoComponent daggerCreateResoComponent =
                (DaggerCreateResoComponent) DaggerCreateResoComponent.builder()
                        .appComponent(appComponent)
                        .build();

        daggerCreateResoComponent.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter.attachView(this);
        return super.onCreateView(inflater, container, savedInstanceState);
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
        productProblemListViewModel = arguments.getParcelable(PRODUCT_PROBLEM_DATA);

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_product_problem;
    }

    @Override
    protected void initView(View view) {
        rvProductProblem = (RecyclerView) view.findViewById(R.id.rv_product_problem);
        rvProductProblem.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ProductProblemAdapter(context, this);
        rvProductProblem.setAdapter(adapter);
        adapter.updateAdapter(productProblemListViewModel.getProductProblemViewModels());
//        presenter.populateProductProblem();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public void populateProblemAndProduct(List<ProductProblemViewModel> productProblemViewModelList) {
        adapter.updateAdapter(productProblemViewModelList);
    }

    @Override
    public void onItemClicked(ProductProblemViewModel productProblemViewModel) {
        Toast.makeText(context, productProblemViewModel.getOrder().getProduct().getName(), Toast.LENGTH_SHORT).show();
    }
}
