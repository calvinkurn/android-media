package com.tokopedia.transaction.bcaoneklik.presenter;

import android.content.Context;

import com.tokopedia.transaction.bcaoneklik.listener.ListPaymentTypeView;
import com.tokopedia.transaction.bcaoneklik.model.bcaoneclick.BcaOneClickData;
import com.tokopedia.transaction.bcaoneklik.model.bcaoneclick.PaymentListModel;

import rx.Subscriber;

/**
 * Created by kris on 8/2/17. Tokopedia
 */

public interface ListPaymentTypePresenter {

    void onRequestBcaOneClickAccessToken(
            String tokenId, String credentialType, String credentialNumber,
            int mode
    );

    void onRequestBcaOneClickRegisterToken();

    void checkCreditCardWhiteList();

    void onGetBcaOneClickList(Subscriber<PaymentListModel> subscriber);

    void onGetCreditCardList(Context context);

    void onGetAllPaymentList(Context context);

    void onDeleteBcaOneClick(String tokenId);

    void setViewListener(ListPaymentTypeView view);

    void onCreditCardDeleted(Context context, String tokenId);

    String getUserLoginAccountName();

    void onDestroyed();
}
