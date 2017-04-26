package com.tokopedia.seller.product.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.product.domain.ShopInfoRepository;
import com.tokopedia.seller.product.domain.model.AddProductShopInfoDomainModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Hendry on 4/20/2017.
 */

public class AddProductShopInfoUseCase extends UseCase<AddProductShopInfoDomainModel> {

    public static final String SHOP_ID = "shop_id";
    public static final String SHOP_DOMAIN = "shop_domain";

    protected ShopInfoRepository shopInfoRepository;

    @Inject
    public AddProductShopInfoUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ShopInfoRepository shopInfoRepository) {
        super(threadExecutor, postExecutionThread);
        this.shopInfoRepository = shopInfoRepository;
    }

    @Override
    public Observable<AddProductShopInfoDomainModel> createObservable(RequestParams requestParams) {
        String shopId = requestParams.getString(SHOP_ID, "");
        String shopDomain = requestParams.getString(SHOP_DOMAIN, "");
        return getShopInfo(shopId, shopDomain);
    }

    protected Observable<AddProductShopInfoDomainModel> getShopInfo(String shopId, String shopDomain) {
        return shopInfoRepository.getAddProductShopInfo(shopId, shopDomain);
    }

    public static RequestParams createRequestParamByShopId(String shopId) {
        RequestParams params = RequestParams.create();
        params.putString(SHOP_ID, shopId);
        return params;
    }

    public static RequestParams createRequestParamByShopDomain(String shopDomain) {
        RequestParams params = RequestParams.create();
        params.putString(SHOP_DOMAIN, shopDomain);
        return params;
    }

}