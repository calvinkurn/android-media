package com.tokopedia.transaction.bcaoneklik.presenter;

import com.tokopedia.transaction.bcaoneklik.listener.CreditCardAuthenticationView;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.authenticator.AuthenticatorLogicModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.authenticator.AuthenticatorPageModel;

import java.util.List;

/**
 * Created by kris on 10/11/17. Tokopedia
 */

public interface CreditCardAuthenticationPresenter {

    void setListener(CreditCardAuthenticationView view);

    void updateWhiteListStatus(AuthenticatorPageModel model);

    List<AuthenticatorLogicModel> initiateLogicModel(AuthenticatorPageModel model);
}
