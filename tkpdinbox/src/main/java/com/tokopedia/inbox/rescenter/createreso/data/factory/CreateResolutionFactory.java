package com.tokopedia.inbox.rescenter.createreso.data.factory;

import android.content.Context;

import com.tokopedia.core.network.apiservices.rescenter.ResCenterActService;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;

/**
 * Created by yoasfs on 16/08/17.
 */

public class CreateResolutionFactory {
    private Context context;
    private ResolutionApi resolutionApi;
    private ResCenterActService resCenterActService;

    public CreateResolutionFactory(Context context,
                                   ResolutionApi resolutionApi,
                                   ResCenterActService resCenterActService
                                   ) {
        this.context = context;
        this.resolutionApi = resolutionApi;
        this.resCenterActService = resCenterActService;
    }

}