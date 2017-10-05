package com.tokopedia.gm.cashback.domain;

import android.text.TextUtils;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.common.cashback.DataCashbackModel;
import com.tokopedia.seller.product.edit.domain.ShopInfoRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 10/4/17.
 */

public class GetCashbackUseCase extends UseCase<List<DataCashbackModel>> {
    public static final String PRODUCT_IDS = "product_ids";
    private final GMCashbackRepository cashbackRepository;
    private final ShopInfoRepository shopInfoRepository;

    @Inject
    public GetCashbackUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                              GMCashbackRepository cashbackRepository, ShopInfoRepository shopInfoRepository) {
        super(threadExecutor, postExecutionThread);
        this.cashbackRepository = cashbackRepository;
        this.shopInfoRepository = shopInfoRepository;
    }

    @Override
    public Observable<List<DataCashbackModel>> createObservable(RequestParams requestParams) {
        return cashbackRepository.getCashbackList(requestParams.getString(PRODUCT_IDS, ""), shopInfoRepository.getShopId());
    }

    public static RequestParams createRequestParams(List<String> productIds){
        RequestParams requestParams = RequestParams.create();
        String productIdsJoin = TextUtils.join(",", productIds);
        requestParams.putString(PRODUCT_IDS, productIdsJoin);
        return requestParams;
    }
}
