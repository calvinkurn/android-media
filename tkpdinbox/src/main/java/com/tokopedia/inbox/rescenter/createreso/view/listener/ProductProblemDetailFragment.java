package com.tokopedia.inbox.rescenter.createreso.view.listener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ProblemResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.StatusTroubleViewModel;

import java.util.List;

/**
 * Created by yoasfs on 14/08/17.
 */

public interface ProductProblemDetailFragment {

    interface View extends CustomerView {
        void populateDataToScreen(ProductProblemViewModel productProblemViewModel);

        void updateArriveStatusButton(boolean isArrived, boolean canShowInfo);

        void populateReasonSpinner(String[] reasonStringArray);

        void updateComplainReasonView(boolean isSuccess, String message);

        void updateComplainReasonValue(String complainString);

        void updateBottomMainButton(boolean isEnabled);

        void updatePlusMinusButton(int currentValue, int maxValue);

        void saveData(ProblemResult problemResult, int resultStepCode);
    }

    interface Presenter extends CustomerPresenter<View> {
        void populateData(ProductProblemViewModel productProblemViewModel, ProblemResult problemResult);

        void btnArrivedClicked();

        void btnNotArrivedClicked();

        void updateSpinner(boolean isDelivered);

        void updateTroubleValue(String trouble);

        void updateComplainReason(String reason);

        void increaseQty();

        void decreaseQty();

        void btnSaveClicked(boolean isSaveAndChooseOtherButton);
    }
}
