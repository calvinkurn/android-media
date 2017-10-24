package com.tokopedia.otp.securityquestion.data.source.changephonenumberrequest;

import android.content.Context;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.otp.securityquestion.data.model.changephonenumberrequest.ValidateImageModel;
import com.tokopedia.otp.securityquestion.domain.mapper.changephonenumberrequest.ValidateImageMapper;

import rx.Observable;

/**
 * Created by nisie on 3/9/17.
 */

public class CloudValidateImageSource {
    private Context context;
    private final AccountsService accountsService;
    private ValidateImageMapper validateImageMapper;

    public CloudValidateImageSource(Context context,
                                    AccountsService accountsService,
                                    ValidateImageMapper validateImageMapper) {
        this.context = context;
        this.accountsService = accountsService;
        this.validateImageMapper = validateImageMapper;
    }

    public Observable<ValidateImageModel> validateImage(TKPDMapParam<String, Object> params) {
        return accountsService.getApi().validateImage(params)
                .map(validateImageMapper);
    }
}
