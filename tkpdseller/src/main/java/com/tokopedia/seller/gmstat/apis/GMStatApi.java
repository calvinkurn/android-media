package com.tokopedia.seller.gmstat.apis;

import com.tokopedia.seller.gmstat.models.GetBuyerData;
import com.tokopedia.seller.gmstat.models.GetKeyword;
import com.tokopedia.seller.gmstat.models.GetPopularProduct;
import com.tokopedia.seller.gmstat.models.GetProductGraph;
import com.tokopedia.seller.gmstat.models.GetShopCategory;
import com.tokopedia.seller.gmstat.models.GetTransactionGraph;

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
    Observable<Response<GetProductGraph>> getProductGraph(@QueryMap Map<String, String> params);

    @GET("/v1/shop_stats?action=get_transaction_graph")
    Observable<Response<GetTransactionGraph>> getTransactionGraph(@QueryMap Map<String, String> params);

    @GET("/v1/shop_stats?action=get_popular_product")
    Observable<Response<GetPopularProduct>> getPopulatProduct(@QueryMap Map<String, String> params);

    @GET("/v1/shop_stats?action=get_buyer_graph")
    Observable<Response<GetBuyerData>> getBuyerData(@QueryMap Map<String, String> params);

    @GET("/v1/shop_stats?action=get_shop_category")
    Observable<Response<GetShopCategory>> getShopCategory(@QueryMap Map<String, String> params);

    @GET("/v1/shop_stats?action=get_keyword")
    Observable<Response<GetKeyword>> getKeyword(@QueryMap Map<String, String> params);
}
