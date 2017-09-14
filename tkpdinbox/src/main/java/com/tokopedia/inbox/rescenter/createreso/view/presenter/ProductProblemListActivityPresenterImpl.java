package com.tokopedia.inbox.rescenter.createreso.view.presenter;

import android.content.Context;

import com.tokopedia.inbox.rescenter.createreso.view.activity.ProductProblemListActivityView;
import com.tokopedia.inbox.rescenter.createreso.view.fragment.ChooseProductAndProblemFragment;
import com.tokopedia.inbox.rescenter.createreso.view.listener.ProductProblemListActivityPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ProblemResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemListViewModel;

import java.util.ArrayList;

/**
 * Created by yoasfs on 15/08/17.
 */

public class ProductProblemListActivityPresenterImpl
        implements ProductProblemListActivityPresenter {
    private Context context;
    private ProductProblemListActivityView mainView;
    private ChooseProductAndProblemFragment chooseProductAndProblemFragment;

    public ProductProblemListActivityPresenterImpl(Context context,
                                                   ProductProblemListActivityView productProblemListActivityView) {
        this.context = context;
        this.mainView = productProblemListActivityView;
    }

    @Override
    public void initFragment(ProductProblemListViewModel productProblemListViewModel,
                             ArrayList<ProblemResult> problemResultList) {
        chooseProductAndProblemFragment = ChooseProductAndProblemFragment.newInstance(productProblemListViewModel, problemResultList);
        mainView.inflateFragment(chooseProductAndProblemFragment,
                ChooseProductAndProblemFragment.class.getSimpleName());
    }
}
