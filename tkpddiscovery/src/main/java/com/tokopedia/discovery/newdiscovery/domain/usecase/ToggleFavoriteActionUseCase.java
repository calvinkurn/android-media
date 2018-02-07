package com.tokopedia.discovery.newdiscovery.domain.usecase;

import android.content.Context;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.shopinfo.facades.authservices.ActionService;
import com.tokopedia.discovery.newdiscovery.data.mapper.ToggleFavoriteActionMapper;
import com.tokopedia.discovery.newdiscovery.domain.model.ActionResultModel;

import rx.Observable;

import static com.tokopedia.core.shopinfo.facades.authservices.ActionApi.PARAM_SHOP_AD_KEY;
import static com.tokopedia.core.shopinfo.facades.authservices.ActionApi.PARAM_SHOP_DOMAIN;
import static com.tokopedia.core.shopinfo.facades.authservices.ActionApi.PARAM_SHOP_ID;
import static com.tokopedia.core.shopinfo.facades.authservices.ActionApi.PARAM_SHOP_SRC;

/**
 * Created by henrypriyono on 10/23/17.
 */

public class ToggleFavoriteActionUseCase extends UseCase<ActionResultModel> {

    private ActionService service;
    private ToggleFavoriteActionMapper mapper;
    private final Context context;

    public ToggleFavoriteActionUseCase(ThreadExecutor threadExecutor,
                                       PostExecutionThread postExecutionThread,
                                       ActionService service,
                                       ToggleFavoriteActionMapper mapper,
                                       Context context) {
        super(threadExecutor, postExecutionThread);
        this.service = service;
        this.mapper = mapper;
        this.context = context;
    }

    public static RequestParams generateParam(String shopId, String shopDomain) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_SHOP_ID, shopId);
        params.putString(PARAM_SHOP_DOMAIN, shopDomain);
        params.putString(PARAM_SHOP_SRC, "");
        params.putString(PARAM_SHOP_AD_KEY, "");

        return params;
    }

    @Override
    public Observable<ActionResultModel> createObservable(RequestParams requestParams) {
        return service.getApi()
                .actionFavoriteShop(AuthUtil.generateParams(context, requestParams.getParamsAllValueInString()))
                .map(mapper);
    }
}
