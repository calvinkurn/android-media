package com.tokopedia.shop.product.data.source.cloud;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.shop.common.data.source.cloud.api.EtalaseApi;
import com.tokopedia.shop.product.data.source.cloud.model.EtalaseModel;
import com.tokopedia.shop.product.data.source.cloud.model.PagingListOther;
import com.tokopedia.shop.product.domain.model.ShopEtalaseRequestModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by normansyahputa on 2/28/18.
 */

public class ShopEtalaseCloudDataSource {
    private EtalaseApi etalaseApi;

    @Inject
    public ShopEtalaseCloudDataSource(EtalaseApi shopApi) {
        this.etalaseApi = shopApi;
    }

    public Observable<Response<DataResponse<PagingListOther<EtalaseModel>>>> getShopEtalaseList(
            ShopEtalaseRequestModel shopProductRequestModel
    ) {
        return etalaseApi.getShopEtalase(shopProductRequestModel.getHashMap());
    }
}
