package com.tokopedia.gm.common.domain.interactor;

import com.tokopedia.gm.common.data.source.cloud.model.GMFeaturedProduct;
import com.tokopedia.gm.common.domain.repository.GMCommonRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by User on 9/8/2017.
 */

public class GetShopInfoUseCase extends UseCase<List<GMFeaturedProduct>> {

    private static final String SHOP_ID = "SHOP_ID";

    private GMCommonRepository gmCommonRepository;

    @Inject
    public GetShopInfoUseCase(GMCommonRepository gmCommonRepository) {
        this.gmCommonRepository = gmCommonRepository;
    }

    @Override
    public Observable<List<GMFeaturedProduct>> createObservable(RequestParams requestParams) {
        String shopId = requestParams.getString(SHOP_ID, null);
        return gmCommonRepository.getFeaturedProductList(shopId);
    }

    public static RequestParams createRequestParam(String shopId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(SHOP_ID, shopId);
        return requestParams;
    }
}
