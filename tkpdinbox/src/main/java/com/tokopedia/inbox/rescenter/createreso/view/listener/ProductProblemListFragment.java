package com.tokopedia.inbox.rescenter.createreso.view.listener;


import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ComplaintResult;
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

        void onProblemResultListUpdated(List<ComplaintResult> problemResults);

        void enableBottomButton();

        void disableBottomButton();

        void saveData(ArrayList<ComplaintResult> problemResults);
    }

    interface Presenter extends CustomerPresenter<View> {

        void loadProblemAndProduct(ProductProblemListViewModel productProblemViewModelList, List<ComplaintResult> complaintResults);

        void addOrRemoveStringProblem(ProductProblemViewModel productProblemViewModel);

        void updateProblemResultData();

        void removeProblemResult(ProductProblemViewModel productProblemViewModel);

        void processResultData(ComplaintResult complaintResult, int resultStepCode);

        void buttonContinueClicked();
    }
}
