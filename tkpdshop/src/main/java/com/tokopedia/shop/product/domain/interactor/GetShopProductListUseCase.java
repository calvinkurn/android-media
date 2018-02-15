package com.tokopedia.shop.product.domain.interactor;

import com.tokopedia.shop.common.constant.ShopParamApiContant;
import com.tokopedia.shop.note.data.source.cloud.model.ShopNoteDetail;
import com.tokopedia.shop.product.domain.repository.ShopProductRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 2/8/18.
 */

public class GetShopProductListUseCase extends UseCase<ShopNoteDetail> {

    private final static String SHOP_CLOSED = "SHOP_CLOSED";

    private final ShopProductRepository shopNoteRepository;

    @Inject
    public GetShopProductListUseCase(ShopProductRepository shopNoteRepository) {
        super();
        this.shopNoteRepository = shopNoteRepository;
    }

    @Override
    public Observable<ShopNoteDetail> createObservable(RequestParams requestParams) {
        HashMap<String, Object> parameters = requestParams.getParameters();
        parameters.remove(SHOP_CLOSED);
        boolean shopClosed = requestParams.getBoolean(SHOP_CLOSED, false);
        return shopNoteRepository.getShopProductList(parameters, shopClosed);
    }

    public static RequestParams createRequestParam(String shopId, String keyword, String etalaseId, int page, int orderBy, boolean shopClosed) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(ShopParamApiContant.SHOP_ID, shopId);
        requestParams.putString(ShopParamApiContant.KEYWORD, keyword);
        requestParams.putString(ShopParamApiContant.ETALASE_ID, etalaseId);
        requestParams.putInt(ShopParamApiContant.PAGE, page);
        requestParams.putInt(ShopParamApiContant.ORDER_BY, orderBy);
        requestParams.putBoolean(SHOP_CLOSED, shopClosed);
        return requestParams;
    }
}
