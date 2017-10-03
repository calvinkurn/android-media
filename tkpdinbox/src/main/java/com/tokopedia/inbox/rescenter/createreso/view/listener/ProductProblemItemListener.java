package com.tokopedia.inbox.rescenter.createreso.view.listener;

import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemViewModel;

/**
 * Created by yoasfs on 15/08/17.
 */

public interface ProductProblemItemListener {
    void onItemClicked(ProductProblemViewModel productProblemViewModel);

    void onRemoveProductProblem(ProductProblemViewModel productProblemViewModel);

    void onStringProblemClicked(ProductProblemViewModel productProblemViewModel);
}
