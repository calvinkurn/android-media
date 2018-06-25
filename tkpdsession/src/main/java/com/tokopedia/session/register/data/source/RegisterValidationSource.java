package com.tokopedia.session.register.data.source;

import android.util.Log;

import com.tokopedia.network.service.AccountsService;
import com.tokopedia.session.register.data.mapper.RegisterValidationMapper;
import com.tokopedia.session.register.view.viewmodel.RegisterValidationViewModel;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by alvinatin on 12/06/18.
 */

public class RegisterValidationSource {

    private final AccountsService accountsService;
    private RegisterValidationMapper registerValidationMapper;

    @Inject
    public RegisterValidationSource(AccountsService accountsService, RegisterValidationMapper
            registerValidationMapper) {
        this.accountsService = accountsService;
        this.registerValidationMapper = registerValidationMapper;
    }

    public Observable<RegisterValidationViewModel> validateRegister(Map<String, Object> param) {
        try {
            return accountsService.getApi().validateRegister(param).map(registerValidationMapper);
        } catch (Exception e){
            Log.e("error", e.toString());
            return null;
        }
//        return accountsService.getApi().validateRegister(param).map(registerValidationMapper);
    }
}
