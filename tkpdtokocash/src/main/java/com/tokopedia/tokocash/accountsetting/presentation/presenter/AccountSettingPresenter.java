package com.tokopedia.tokocash.accountsetting.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.tokocash.CacheUtil;
import com.tokopedia.tokocash.WalletUserSession;
import com.tokopedia.tokocash.accountsetting.domain.GetOAuthInfoTokoCashUseCase;
import com.tokopedia.tokocash.accountsetting.domain.PostUnlinkTokoCashUseCase;
import com.tokopedia.tokocash.accountsetting.presentation.contract.AccountSettingContract;
import com.tokopedia.tokocash.accountsetting.presentation.model.OAuthInfo;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 2/27/18.
 */

public class AccountSettingPresenter extends BaseDaggerPresenter<AccountSettingContract.View>
        implements AccountSettingContract.Presenter {

    private GetOAuthInfoTokoCashUseCase getOAuthInfoTokoCashUseCase;
    private PostUnlinkTokoCashUseCase postUnlinkTokoCashUseCase;
    private CacheManager cacheManager;
    private WalletUserSession walletUserSession;

    @Inject
    public AccountSettingPresenter(GetOAuthInfoTokoCashUseCase getOAuthInfoTokoCashUseCase,
                                   PostUnlinkTokoCashUseCase postUnlinkTokoCashUseCase,
                                   CacheManager cacheManager, WalletUserSession walletUserSession) {
        this.getOAuthInfoTokoCashUseCase = getOAuthInfoTokoCashUseCase;
        this.postUnlinkTokoCashUseCase = postUnlinkTokoCashUseCase;
        this.cacheManager = cacheManager;
        this.walletUserSession = walletUserSession;
    }

    @Override
    public void processGetWalletAccountData() {
        getOAuthInfoTokoCashUseCase.execute(RequestParams.EMPTY, new Subscriber<OAuthInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().renderErrorGetWalletAccountSettingData(e);
            }

            @Override
            public void onNext(OAuthInfo oAuthInfo) {
                if (oAuthInfo != null)
                    getView().renderWalletOAuthInfoData(oAuthInfo);
            }
        });
    }

    @Override
    public void processDeleteConnectedUser(String refreshToken, final String identifier, String identifierType) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PostUnlinkTokoCashUseCase.REVOKE_TOKEN, refreshToken);
        requestParams.putString(PostUnlinkTokoCashUseCase.IDENTIFIER, identifier);
        requestParams.putString(PostUnlinkTokoCashUseCase.IDENTIFIER_TYPE, identifierType);
        postUnlinkTokoCashUseCase.execute(requestParams, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().renderErrorUnlinkAccount(e);
            }

            @Override
            public void onNext(Boolean isSuccess) {
                if (isSuccess) {
                    if (getView().getUserEmail().equals(identifier))
                        getView().renderSuccessUnlinkToHome();
                    else
                        getView().renderSuccessUnlinkAccount();
                }
            }
        });
    }

    @Override
    public void deleteCacheBalanceAndTokenTokoCash() {
        cacheManager.delete(CacheUtil.KEY_TOKOCASH_BALANCE_CACHE);
        walletUserSession.setTokenWallet("");
    }
}
