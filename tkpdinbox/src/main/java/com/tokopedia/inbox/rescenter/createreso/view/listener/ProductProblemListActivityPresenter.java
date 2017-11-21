package com.tokopedia.inbox.rescenter.createreso.view.listener;


import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ProblemResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemListViewModel;

import java.util.ArrayList;

/**
 * Created by yoasfs on 15/08/17.
 */

public interface ProductProblemListActivityPresenter {
    void initFragment(ProductProblemListViewModel productProblemListViewModel, ArrayList<ProblemResult> problemResultList);
}
