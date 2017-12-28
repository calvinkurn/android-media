package com.tokopedia.inbox.rescenter.createreso.view.listener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ProblemResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.StatusViewModel;

import java.util.List;

/**
 * Created by yoasfs on 14/08/17.
 */

public interface ProductProblemDetailFragment {

    interface View extends CustomerView {
        void populateDataToScreen(ProductProblemViewModel productProblemViewModel);

        void updateArriveStatusButton(boolean isDelivered, boolean canShowInfo);

        void populateReasonSpinner(String[] reasonStringArray, int position);

        void updateComplainReasonView(boolean isSuccess, String message);

        void updateComplainReasonValue(String complainString);

        void updateBottomMainButton(boolean isEnabled);

        void showInfoDialog(ProductProblemViewModel productProblemViewModel);

        void updatePlusMinusButton(int currentValue, int maxValue);

        void saveData(ProblemResult problemResult, int resultStepCode);
    }

    interface Presenter extends CustomerPresenter<View> {
        void populateData(ProductProblemViewModel productProblemViewModel, ProblemResult problemResult);

        void btnArrivedClicked();

        void btnNotArrivedClicked();

        void btnInfoClicked();

        void updateSpinner(boolean isDelivered);

        void updateTroubleValue(String trouble);

        void updateComplainReason(String reason);

        void increaseQty();

        void decreaseQty();

        void btnSaveClicked(boolean isSaveAndChooseOtherButton);

        long getDuration(String deliveryDate);

        String getDeliveryDate(List<StatusViewModel> statusList);

        void onDisableInfoView();
    }
}
