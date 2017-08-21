package com.tokopedia.inbox.rescenter.createreso.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.listener.ProductProblemListFragment;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemViewModel;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by yoasfs on 14/08/17.
 */

public class ProductProblemFragmentPresenter extends BaseDaggerPresenter<ProductProblemListFragment.View> implements ProductProblemListFragment.Presenter {
    private ProductProblemListFragment.View mainView;

    @Inject
    public ProductProblemFragmentPresenter() {
    }

    @Override
    public void attachView(ProductProblemListFragment.View view) {
        this.mainView = view;
        super.attachView(view);
    }

    @Override
    public void loadProblemAndProduct(List<ProductProblemViewModel> productProblemViewModelList) {
        mainView.populateProblemAndProduct(productProblemViewModelList);
    }
}
