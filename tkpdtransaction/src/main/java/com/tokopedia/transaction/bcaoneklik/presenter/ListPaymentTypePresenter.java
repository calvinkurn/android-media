package com.tokopedia.transaction.bcaoneklik.presenter;

import android.content.Context;

import com.tokopedia.transaction.bcaoneklik.listener.ListPaymentTypeView;
import com.tokopedia.transaction.bcaoneklik.model.BcaOneClickData;
import com.tokopedia.transaction.bcaoneklik.model.PaymentListModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardSuccessDeleteModel;

import rx.Subscriber;

/**
 * Created by kris on 8/2/17. Tokopedia
 */

public interface ListPaymentTypePresenter {

    void onRegisterOneClickBcaChosen(Subscriber<BcaOneClickData> subscriber);

    void onGetPaymentList(Subscriber<PaymentListModel> subscriber);

    void onGetCreditCardList(Context context);

    void onDeletePaymentList(Subscriber<PaymentListModel> subscriber, String tokenId);

    void setViewListener(ListPaymentTypeView view);

    void onCreditCardDeleted(Context context, String tokenId);

    void onDestroyed();
}
