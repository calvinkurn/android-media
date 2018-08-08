package com.tokopedia.seller.product.edit.data.source.cloud.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.seller.product.edit.data.source.cloud.model.catalogdata.CatalogDataModel;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Hendry on 4/5/2017.
 */

public interface SearchApi {

    @GET(TkpdBaseURL.Ace.PATH_SEARCH + TkpdBaseURL.Ace.PATH_CATALOG)
    Observable<CatalogDataModel> getCatalog(@QueryMap Map<String, String> params);
}
