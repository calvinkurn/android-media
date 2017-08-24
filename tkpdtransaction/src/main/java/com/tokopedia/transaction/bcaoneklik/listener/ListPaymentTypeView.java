package com.tokopedia.transaction.bcaoneklik.listener;

import android.content.Context;

import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardModel;

/**
 * Created by kris on 8/2/17. Tokopedia
 */

public interface ListPaymentTypeView {

    Context getContext();

    void showMainDialog();

    void receivedCreditCardList(CreditCardModel creditCardModel);

    void successDeleteCreditCard();

    void onLoadCreditCardError(String errorMessage);

    void onDeleteCreditCardError(String errorMessage);
}
