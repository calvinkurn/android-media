package com.tokopedia.inbox.rescenter.createreso.view.listener;

import android.content.Context;
import androidx.core.app.TaskStackBuilder;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.ProductProblemResponseDomain;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ComplaintResult;
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

        void transitionToChooseProductAndProblemPage(ProductProblemListViewModel productProblemListViewModel, ArrayList<ComplaintResult> complaintResults);

        void transitionToSolutionPage(ResultViewModel resultViewModel);

        void transitionToUploadProvePage(ResultViewModel resultViewModel);

        void showSuccessToast();

        void showErrorToast(String error);

        void showLoading(boolean isCreateReso);

        void successLoadProductProblemData(ProductProblemResponseDomain responseDomain);

        void errorLoadProductProblemData(String error);

        void showCreateComplainDialog(ResultViewModel resultViewModel);

        void successCreateResoWithoutAttachment(String resolutionId, String cacheKey, String message, String shopName);

        void errorCreateResoWithoutAttachment(String error);

        void successCreateResoWithAttachment(String resolutionId, String message, String shopName);

        void errorCreateResoWithAttachment(String error);
    }

    interface Presenter extends CustomerPresenter<View> {

        void chooseProductProblemClicked();

        void getRestoreData(ResultViewModel resultViewModel);

        void updateProductProblemResponseDomain(ProductProblemResponseDomain productProblemResponseDomain);

        void solutionClicked();

        void uploadProveClicked();

        void createResoClicked();

        void callCreateResolutionAPI();

        void callCreateResolutionAPIWithAttachment();

        void loadProductProblem(String orderId);

        void loadProductProblem(String orderId, String resolutionId);

        void addResultFromStep1(ArrayList<ComplaintResult> complaintResults);

        void addResultFromStep2(ResultViewModel resultViewModel);

        void addResultFromStep3(ResultViewModel resultViewModel);

        TaskStackBuilder getInboxAndDetailResoStackBuilder(Context context, String resolutionId, String shopName);
    }
}
