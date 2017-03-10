package com.tokopedia.session.changephonenumber.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.changephonenumber.data.GeneratePostKeyModel;
import com.tokopedia.session.changephonenumber.data.mapper.GeneratePostKeyMapper;

import rx.Observable;

/**
 * Created by nisie on 3/9/17.
 */

public class CloudGeneratePostKeySource {
    private Context context;
    private final AccountsService accountsService;
    private GeneratePostKeyMapper generatePostKeyMapper;

    public CloudGeneratePostKeySource(Context context,
                                    AccountsService accountsService,
                                    GeneratePostKeyMapper generatePostKeyMapper) {
        this.context = context;
        this.accountsService = accountsService;
        this.generatePostKeyMapper = generatePostKeyMapper;
    }

    public Observable<GeneratePostKeyModel> generatePostKey(TKPDMapParam<String, Object> params) {
        return accountsService.getApi().generatePostKey(params)
                .map(generatePostKeyMapper);
    }
}
