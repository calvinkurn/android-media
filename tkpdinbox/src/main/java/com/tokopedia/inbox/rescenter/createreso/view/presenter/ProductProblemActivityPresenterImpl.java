package com.tokopedia.inbox.rescenter.createreso.view.presenter;

import android.content.Context;

import com.tokopedia.inbox.rescenter.createreso.view.activity.ProductProblemActivity;
import com.tokopedia.inbox.rescenter.createreso.view.activity.ProductProblemView;
import com.tokopedia.inbox.rescenter.createreso.view.fragment.ChooseProductAndProblemFragment;
import com.tokopedia.inbox.rescenter.createreso.view.listener.ProductProblemActivityPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemListViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemViewModel;

/**
 * Created by yoasfs on 15/08/17.
 */

public class ProductProblemActivityPresenterImpl implements ProductProblemActivityPresenter {
    private Context context;
    private ProductProblemView mainView;

    public ProductProblemActivityPresenterImpl(Context context, ProductProblemView productProblemView) {
        this.context = context;
        this.mainView = productProblemView;
    }

    @Override
    public void initFragment(ProductProblemListViewModel productProblemListViewModel) {
        mainView.inflateFragment(ChooseProductAndProblemFragment.newInstance(productProblemListViewModel),
                ChooseProductAndProblemFragment.class.getSimpleName());
    }
}
