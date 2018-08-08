package com.tokopedia.transaction.bcaoneklik.listener;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.transaction.bcaoneklik.model.PaymentSettingModel;
import com.tokopedia.transaction.bcaoneklik.model.bcaoneclick.PaymentListModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.authenticator.AuthenticatorLogicModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.authenticator.AuthenticatorPageModel;

import java.util.List;

/**
 * Created by kris on 8/2/17. Tokopedia
 */

public interface ListPaymentTypeView {

    int REGISTER_BCA_ONE_CLICK_REQUEST_CODE = 1;
    int EDIT_BCA_ONE_CLICK_REQUEST_CODE = 2;
    int EDIT_AUTHENTICATION_PAGE = 3;
    int CREDIT_CARD_DETAIL_REQUEST_CODE = 4;

    Context getContext();

    void showMainDialog();

    void dismissMainDialog();

    void showProgressDialog();

    void dismissProgressDialog();

    void successDeleteCreditCard(String message);

    void onLoadCreditCardError(String errorMessage);

    void onLoadAllError(String errorMessage);

    void onDeleteCreditCardError(String errorMessage);

    void showError (String errorMessage);

    void onShowDeleteBcaOneClickDialog(String tokenId, String name, String credentialNumber);

    void showDeleteBcaOneClickError();

    void onDeleteCreditCardClicked(String tokenId, String cardId);

    void onBcaOneClickSuccessGetToken(Bundle bundle);

    void onBcaOneClickSuccessGetRegisterToken(Bundle bundle);

    void onFetchDataComplete(PaymentSettingModel model);

    void openAuthenticatorPage(AuthenticatorPageModel data);

    void refreshList();

    void onErrorGetBcaOneClickToken(Throwable e);
}
