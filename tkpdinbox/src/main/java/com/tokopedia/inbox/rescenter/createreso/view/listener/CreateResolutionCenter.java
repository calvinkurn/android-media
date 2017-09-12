package com.tokopedia.inbox.rescenter.createreso.view.listener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateResoStep1Domain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateResoStep2Domain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.ProductProblemResponseDomain;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ProblemResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemListViewModel;

import java.util.ArrayList;

/**
 * Created by yoasfs on 11/08/17.
 */

public interface CreateResolutionCenter {


    interface View extends CustomerView {

        void updateView(ResultViewModel resultViewModel);

        void showCreateResoResponse(boolean isSuccess, String message);

        void transitionToChooseProductAndProblemPage(ProductProblemListViewModel productProblemListViewModel, ArrayList<ProblemResult> problemResults);

        void transitionToSolutionPage(ResultViewModel resultViewModel);

        void transitionToUploadProvePage(ResultViewModel resultViewModel);

        void showSuccessToast();

        void showErrorToast(String error);

        void showLoading();

        void successLoadProductProblemData(ProductProblemResponseDomain responseDomain);

        void errorLoadProductProblemData(String error);

        void showCreateComplainDialog(ResultViewModel resultViewModel);

        void successCreateResoStep1(String resolutionId, String cacheKey, String message);

        void errorCreateResoStep1(String error);

        void successCreateResoStep2(String resolutionId, String message);

        void errorCreateResoStep2(String error);

        void successCreateResoWithAttachment(String resolutionId, String message);

        void errorCreateResoWithAttachment(String error);

    }

    interface Presenter extends CustomerPresenter<View> {

        void chooseProductProblemClicked();

        void updateProductProblemResponseDomain(ProductProblemResponseDomain productProblemResponseDomain);

        void solutionClicked();

        void uploadProveClicked();

        void createResoClicked();

        void callCreateResolutionAPI();

        void callCreateResolutionAPIWithAttachment();

        void loadProductProblem(String orderId);

        void addResultFromStep1(ArrayList<ProblemResult> problemResultList);

        void addResultFromStep2(ResultViewModel resultViewModel);

        void addResultFromStep3(ResultViewModel resultViewModel);
    }
}
