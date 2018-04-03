package com.tokopedia.tokocash.accountsetting.presentation.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tokocash.accountsetting.presentation.model.OAuthInfo;

/**
 * Created by nabillasabbaha on 2/27/18.
 */

public interface AccountSettingContract {

    interface View extends CustomerView {
        void renderWalletOAuthInfoData(OAuthInfo oAuthInfo);

        void renderErrorGetWalletAccountSettingData(Throwable e);

        void renderSuccessUnlinkAccount();

        void renderSuccessUnlinkToHome();

        void renderErrorUnlinkAccount(Throwable e);

        String getUserEmail();
    }

    interface Presenter extends CustomerPresenter<View> {
        void processGetWalletAccountData();

        void processDeleteConnectedUser(String refreshToken,
                                        String identifier, String identifierType);

        void deleteCacheBalanceAndTokenTokoCash();
    }
}
