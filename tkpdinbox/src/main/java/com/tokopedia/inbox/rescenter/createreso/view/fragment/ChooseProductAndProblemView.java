package com.tokopedia.inbox.rescenter.createreso.view.fragment;

import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemViewModel;

import java.util.List;

/**
 * Created by yoasfs on 14/08/17.
 */

public interface ChooseProductAndProblemView {
    void populateProblemAndProoduct(List<ProductProblemViewModel> productProblemViewModelList);
}
