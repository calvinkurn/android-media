package com.tokopedia.flight.review.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.flight.booking.data.cloud.entity.CartEntity;
import com.tokopedia.flight.review.data.model.AttributesVoucher;

/**
 * Created by zulfikarrahman on 11/10/17.
 */

public interface FlightBookingReviewContract {

    interface View extends CustomerView{

        void onErrorCheckVoucherCode(String e);

        void onSuccessCheckVoucherCode(AttributesVoucher attributesVoucher);

        void onErrorSubmitData(Throwable e);

        void onSuccessSubmitData();

        void hideProgressDialog();

        void showUpdateDataErrorStateLayout(String messageFromException);

        void showExpireTransactionDialog();

        void onGetCartData(CartEntity cartEntity);

        void showProgressDialog();
    }

    interface Presenter extends CustomerPresenter<View>{

        void checkVoucherCode(String cartId, String voucherCode);

        void submitData();

        void processGetCartData();
    }
}
