package com.tokopedia.inbox.rescenter.createreso.view.listener;


import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ProblemResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemViewModel;

/**
 * Created by yoasfs on 15/08/17.
 */

public interface ProductProblemDetailActivityPresenter {
    void initFragment(ProductProblemViewModel productProblemViewModel, ProblemResult problemResult);
}
