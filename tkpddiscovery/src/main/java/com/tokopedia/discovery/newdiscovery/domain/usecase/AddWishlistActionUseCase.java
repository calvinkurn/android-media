package com.tokopedia.discovery.newdiscovery.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.mojito.MojitoNoRetryAuthService;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoAuthApi;
import com.tokopedia.discovery.newdiscovery.data.mapper.AddWishlistActionMapper;
import com.tokopedia.discovery.newdiscovery.domain.model.ActionResultModel;

import rx.Observable;

/**
 * Created by henrypriyono on 10/19/17.
 */

public class AddWishlistActionUseCase extends UseCase<ActionResultModel> {
    public static final String PARAM_USER_ID = "PARAM_USER_ID";
    public static final String PARAM_PRODUCT_ID = "PARAM_PRODUCT_ID";

    private MojitoAuthApi service;
    private AddWishlistActionMapper mapper;

    public AddWishlistActionUseCase(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    MojitoAuthApi service,
                                    AddWishlistActionMapper mapper) {
        super(threadExecutor, postExecutionThread);
        this.service = service;
        this.mapper = mapper;
    }

    public static RequestParams generateParam(String productId, String userId) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_USER_ID, userId);
        params.putString(PARAM_PRODUCT_ID, productId);
        return params;
    }

    @Override
    public Observable<ActionResultModel> createObservable(RequestParams requestParams) {
        return service
                .addWishlist(
                        requestParams.getString(PARAM_PRODUCT_ID, ""),
                        requestParams.getString(PARAM_USER_ID, ""))
                .map(mapper);
    }
}
