package com.tokopedia.inbox.rescenter.createreso.view.presenter;

import android.content.Context;

import com.tokopedia.inbox.rescenter.createreso.view.activity.ProductProblemDetailActivityView;
import com.tokopedia.inbox.rescenter.createreso.view.fragment.ChooseProductAndProblemFragment;
import com.tokopedia.inbox.rescenter.createreso.view.fragment.ProductProblemDetailFragment;
import com.tokopedia.inbox.rescenter.createreso.view.listener.ProductProblemDetailActivityPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ProblemResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemViewModel;

/**
 * Created by yoasfs on 15/08/17.
 */

public class ProductProblemDetailActivityPresenterImpl
        implements ProductProblemDetailActivityPresenter {
    private Context context;
    private ProductProblemDetailActivityView mainView;

    public ProductProblemDetailActivityPresenterImpl(Context context, ProductProblemDetailActivityView productProblemListActivityView) {
        this.context = context;
        this.mainView = productProblemListActivityView;
    }

    @Override
    public void initFragment(ProductProblemViewModel productProblemViewModel, ProblemResult problemResult) {
        mainView.inflateFragment(ProductProblemDetailFragment.newInstance(productProblemViewModel, problemResult),
                ChooseProductAndProblemFragment.class.getSimpleName());
    }
}
