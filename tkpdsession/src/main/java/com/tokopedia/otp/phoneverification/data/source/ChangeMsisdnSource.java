package com.tokopedia.otp.phoneverification.data.source;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.otp.phoneverification.data.model.ChangePhoneNumberViewModel;
import com.tokopedia.otp.phoneverification.domain.mapper.ChangePhoneNumberMapper;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by nisie on 5/10/17.
 */

public class ChangeMsisdnSource {
    private final AccountsService accountsService;
    private final ChangePhoneNumberMapper changePhoneNumberMapper;
    private final SessionHandler sessionHandler;

    public ChangeMsisdnSource(AccountsService accountsService,
                              ChangePhoneNumberMapper changePhoneNumberMapper,
                              SessionHandler sessionHandler) {
        this.accountsService = accountsService;
        this.changePhoneNumberMapper = changePhoneNumberMapper;
        this.sessionHandler = sessionHandler;
    }


    public Observable<ChangePhoneNumberViewModel> changePhoneNumber(TKPDMapParam<String, Object> parameters) {
        return accountsService.getApi()
                .changePhoneNumber(AuthUtil.generateParamsNetwork2(MainApplication.getAppContext(), parameters))
                .map(changePhoneNumberMapper)
                .doOnNext(savePhoneNumber());
    }

    private Action1<ChangePhoneNumberViewModel> savePhoneNumber() {
        return new Action1<ChangePhoneNumberViewModel>() {
            @Override
            public void call(ChangePhoneNumberViewModel changePhoneNumberViewModel) {
                if (changePhoneNumberViewModel.isSuccess())
                    sessionHandler.setPhoneNumber(changePhoneNumberViewModel.getPhoneNumber());
            }
        };
    }
}
