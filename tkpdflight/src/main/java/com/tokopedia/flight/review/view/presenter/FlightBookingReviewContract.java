package com.tokopedia.flight.review.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by zulfikarrahman on 11/10/17.
 */

public interface FlightBookingReviewContract {

    interface View extends CustomerView{

        void onErrorCheckVoucherCode(Throwable e);

        void onSuccessCheckVoucherCode();

        void onErrorSubmitData(Throwable e);

        void onSuccessSubmitData();
    }

    interface Presenter extends CustomerPresenter<View>{

        void checkVoucherCode(String voucherCode);

        void submitData();
    }
}
