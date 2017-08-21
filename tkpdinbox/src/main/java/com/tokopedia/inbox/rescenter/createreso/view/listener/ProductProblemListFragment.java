package com.tokopedia.inbox.rescenter.createreso.view.listener;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemViewModel;

import java.util.List;

/**
 * Created by yoasfs on 14/08/17.
 */

public interface ProductProblemListFragment {

    interface View extends CustomerView {

        void populateProblemAndProduct(List<ProductProblemViewModel> productProblemViewModelList);
    }

    interface Presenter extends CustomerPresenter<View> {

        void loadProblemAndProduct(List<ProductProblemViewModel> productProblemViewModelList);
    }
}
