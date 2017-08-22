package com.tokopedia.inbox.rescenter.createreso.view.listener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.ProductProblemResponseDomain;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ProblemResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ButtonState;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemListViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoasfs on 11/08/17.
 */

public interface CreateResolutionCenter {


    interface View extends CustomerView {

        void updateView(ResultViewModel resultViewModel);

        void showCreateResoResponse(boolean isSuccess, String message);

        void transitionToChooseProductAndProblemPage(ProductProblemListViewModel productProblemListViewModel);

        void showSuccessToast();

        void showErrorToast(String error);
    }



    interface Presenter extends CustomerPresenter<View> {

        void chooseProductProblemClicked();

        void solutionClicked();

        void uploadProveClicked();

        void createResoClicked();

        void loadProductProblem(String orderId);

        void addResultFromStep1(ArrayList<ProblemResult> problemResultList);
    }
}
