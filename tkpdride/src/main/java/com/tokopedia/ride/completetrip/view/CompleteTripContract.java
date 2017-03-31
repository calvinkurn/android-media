package com.tokopedia.ride.completetrip.view;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.ride.completetrip.domain.model.Receipt;

/**
 * Created by alvarisi on 3/31/17.
 */

public interface CompleteTripContract {
    interface View extends CustomerView {
        void showGetReceiptLoading();

        void hideGetReceiptLoading();

        RequestParams getReceiptParam();

        void renderReceipt(Receipt receipt);

        void showMessage(String message);
    }

    interface Presenter extends CustomerPresenter<View> {
        void actionGetReceipt();
    }
}
