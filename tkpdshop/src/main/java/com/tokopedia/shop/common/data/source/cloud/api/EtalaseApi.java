package com.tokopedia.shop.common.data.source.cloud.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.shop.common.constant.ShopParamApiConstant;
import com.tokopedia.shop.common.constant.ShopUrl;
import com.tokopedia.shop.note.data.source.cloud.model.ShopNoteDetail;
import com.tokopedia.shop.note.data.source.cloud.model.ShopNoteList;
import com.tokopedia.shop.product.data.source.cloud.model.DynamicFilterModel;
import com.tokopedia.shop.product.data.source.cloud.model.EtalaseModel;
import com.tokopedia.shop.product.data.source.cloud.model.PagingListOther;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Hendry on 4/20/2017.
 */

public interface EtalaseApi {

    @GET(ShopUrl.SHOP_ETALASE)
    Observable<Response<DataResponse<PagingListOther<EtalaseModel>>>> getShopEtalase(@QueryMap Map<String, String> params);
}
