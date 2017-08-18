package com.tokopedia.transaction.bcaoneklik.presenter;

import com.tokopedia.transaction.bcaoneklik.model.BcaOneClickData;
import com.tokopedia.transaction.bcaoneklik.model.PaymentListModel;

import rx.Subscriber;

/**
 * Created by kris on 8/2/17. Tokopedia
 */

public interface ListPaymentTypePresenter {

    void onRegisterOneClickBcaChosen(Subscriber<BcaOneClickData> subscriber);

    void onGetPaymentList(Subscriber<PaymentListModel> subscriber);

    void onDeletePaymentList(Subscriber<PaymentListModel> subscriber, String tokenId);

    void onDestroyed();
}
