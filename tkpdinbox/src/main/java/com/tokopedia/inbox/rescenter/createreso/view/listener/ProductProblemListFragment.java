package com.tokopedia.inbox.rescenter.createreso.view.listener;


import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ProblemResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemListViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoasfs on 14/08/17.
 */

public interface ProductProblemListFragment {

    interface View extends CustomerView {

        void populateProblemAndProduct(ProductProblemListViewModel productProblemViewModelList);

        void onProblemResultListUpdated(List<ProblemResult> problemResults);

        void enableBottomButton();

        void disableBottomButton();

        void saveData(ArrayList<ProblemResult> problemResults);
    }

    interface Presenter extends CustomerPresenter<View> {

        void loadProblemAndProduct(ProductProblemListViewModel productProblemViewModelList, List<ProblemResult> problemResultList);

        void addOrRemoveStringProblem(ProductProblemViewModel productProblemViewModel);

        void updateProblemResultData();

        void removeProblemResult(ProductProblemViewModel productProblemViewModel);

        void processResultData(ProblemResult problemResult, int resultStepCode);

        void buttonContinueClicked();
    }
}
