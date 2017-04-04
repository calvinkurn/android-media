package com.tokopedia.session.changephonenumber.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.changephonenumber.data.SubmitImageModel;
import com.tokopedia.session.changephonenumber.data.mapper.SubmitImageMapper;

import rx.Observable;

/**
 * Created by nisie on 3/13/17.
 */

public class CloudSubmitImageDataSource {
    private Context context;
    private final AccountsService accountsService;
    private SubmitImageMapper submitImageMapper;

    public CloudSubmitImageDataSource(Context context,
                                      AccountsService accountsService,
                                      SubmitImageMapper submitImageMapper) {
        this.context = context;
        this.accountsService = accountsService;
        this.submitImageMapper = submitImageMapper;
    }

    public Observable<SubmitImageModel> submitImage(TKPDMapParam<String, Object> params) {
        return accountsService.getApi().submitImage(params)
                .map(submitImageMapper);
    }
}
