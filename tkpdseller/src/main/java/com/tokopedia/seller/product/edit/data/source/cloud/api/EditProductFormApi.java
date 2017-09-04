package com.tokopedia.seller.product.edit.data.source.cloud.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.product.edit.data.source.cloud.model.editproductform.EditProductFormServiceModel;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author sebastianuskh on 4/21/17.
 */

public interface EditProductFormApi {
    @GET(TkpdBaseURL.Product.V4_PRODUCT + TkpdBaseURL.Product.PATH_GET_EDIT_PRODUCT_FORM)
    Observable<Response<EditProductFormServiceModel>> fetchEditProductForm(@QueryMap TKPDMapParam<String, String> param);
}
