package com.tokopedia.inbox.rescenter.createreso.view.listener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ButtonState;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemViewModel;

import java.util.List;

/**
 * Created by yoasfs on 11/08/17.
 */

public interface CreateResolutionCenter {


    interface View extends CustomerView {

        void updateView(ButtonState buttonState);

        void showCreateResoResponse(boolean isSuccess, String message);

        void transitionToChooseProductAndProblemPage();

        void showSuccessToast();

        void showErrorToast(String error);
    }



    interface Presenter extends CustomerPresenter<View> {

        void chooseProductProblemClicked(ButtonState buttonState);

        void solutionClicked(ButtonState buttonState);

        void uploadProveClicked(ButtonState buttonState);

        void createResoClicked(ButtonState buttonState);

        void loadProductProblem(String orderId);
    }
}
