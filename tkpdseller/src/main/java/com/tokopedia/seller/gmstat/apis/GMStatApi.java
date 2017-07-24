package com.tokopedia.seller.gmstat.apis;

import com.tokopedia.seller.gmstat.models.GetBuyerDataOld;
import com.tokopedia.seller.gmstat.models.GetKeywordOld;
import com.tokopedia.seller.gmstat.models.GetPopularProductOld;
import com.tokopedia.seller.gmstat.models.GetProductGraphOld;
import com.tokopedia.seller.gmstat.models.GetShopCategoryOld;
import com.tokopedia.seller.gmstat.models.GetTransactionGraphOld;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by normansyahputa on 11/2/16.
 */

public interface GMStatApi {

    @GET("/v1/shop_stats?action=get_product_graph")
    Observable<Response<GetProductGraphOld>> getProductGraph(@QueryMap Map<String, String> params);

    @GET("/v1/shop_stats?action=get_transaction_graph")
    Observable<Response<GetTransactionGraphOld>> getTransactionGraph(@QueryMap Map<String, String> params);

    @GET("/v1/shop_stats?action=get_popular_product")
    Observable<Response<GetPopularProductOld>> getPopulatProduct(@QueryMap Map<String, String> params);

    @GET("/v1/shop_stats?action=get_buyer_graph")
    Observable<Response<GetBuyerDataOld>> getBuyerData(@QueryMap Map<String, String> params);

    @GET("/v1/shop_stats?action=get_shop_category")
    Observable<Response<GetShopCategoryOld>> getShopCategory(@QueryMap Map<String, String> params);

    @GET("/v1/shop_stats?action=get_keyword")
    Observable<Response<GetKeywordOld>> getKeyword(@QueryMap Map<String, String> params);
}
