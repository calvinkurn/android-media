package com.tokopedia.tokocash.accountsetting.domain;

import com.tokopedia.tokocash.accountsetting.data.AccountSettingRepository;
import com.tokopedia.tokocash.accountsetting.presentation.model.OAuthInfo;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by nabillasabbaha on 2/27/18.
 */

public class GetOAuthInfoTokoCashUseCase extends UseCase<OAuthInfo> {

    private AccountSettingRepository accountSettingRepository;

    public GetOAuthInfoTokoCashUseCase(AccountSettingRepository accountSettingRepository) {
        this.accountSettingRepository = accountSettingRepository;
    }

    @Override
    public Observable<OAuthInfo> createObservable(RequestParams requestParams) {
        return accountSettingRepository.getOAuthInfo(requestParams.getParamsAllValueInString());
    }
}
