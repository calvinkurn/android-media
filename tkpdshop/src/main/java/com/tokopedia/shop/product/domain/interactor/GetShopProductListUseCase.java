package com.tokopedia.shop.product.domain.interactor;

import android.view.View;

import com.tokopedia.interfaces.merchant.shop.info.ShopInfo;
import com.tokopedia.shop.common.constant.ShopStatusDef;
import com.tokopedia.shop.common.constant.ShopUrl;
import com.tokopedia.shop.info.domain.repository.ShopInfoRepository;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductList;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;
import com.tokopedia.shop.product.domain.repository.ShopProductRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 2/8/18.
 */

public class GetShopProductListUseCase extends UseCase<ShopProductList> {

    private final static String SHOP_REQUEST = "SHOP_REQUEST";
    public static final String BASE_URL = "BASE_URL";

    private ShopInfoRepository shopInfoRepository;
    private final ShopProductRepository shopNoteRepository;

    @Inject
    public GetShopProductListUseCase(ShopInfoRepository shopInfoRepository, ShopProductRepository shopNoteRepository) {
        super();
        this.shopInfoRepository = shopInfoRepository;
        this.shopNoteRepository = shopNoteRepository;
    }

    @Override
    public Observable<ShopProductList> createObservable(RequestParams requestParams) {
        final ShopProductRequestModel shopProductRequestModel = (ShopProductRequestModel) requestParams.getObject(SHOP_REQUEST);
        return shopInfoRepository.getShopInfo(shopProductRequestModel.getShopId()).flatMap(new Func1<ShopInfo, Observable<ShopProductList>>() {
            @Override
            public Observable<ShopProductList> call(ShopInfo shopInfo) {
                String baseUrl = ShopUrl.BASE_ACE_URL+"/";
                switch ((int) shopInfo.getInfo().getShopStatus()){
                    case ShopStatusDef.CLOSED:
                        baseUrl = ShopUrl.BASE_URL+"/";
                        break;
                    case ShopStatusDef.DELETED:
                    case ShopStatusDef.MODERATED:
                    case ShopStatusDef.NOT_ACTIVE:
                    case ShopStatusDef.MODERATED_PERMANENTLY:
                        break;
                }
                return shopNoteRepository.getShopProductList(baseUrl, shopProductRequestModel);
            }
        });
    }

    public static RequestParams createRequestParam(ShopProductRequestModel shopProductRequestModel) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(SHOP_REQUEST, shopProductRequestModel);
        return requestParams;
    }
}
