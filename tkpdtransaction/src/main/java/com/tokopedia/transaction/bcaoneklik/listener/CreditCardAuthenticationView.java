package com.tokopedia.transaction.bcaoneklik.listener;

import android.content.Context;

/**
 * Created by kris on 10/11/17. Tokopedia
 */

public interface CreditCardAuthenticationView {

    String CREDIT_CARD_STATUS_KEY = "CREDIT_CARD_STATUS_KEY";

    Context getContext();

    void finishUpdateStatus(String message);

    void showErrorMessage(String errorMessage);

}
