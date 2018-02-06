package com.tokopedia.seller.product.edit.data.source.cloud.api;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.product.edit.constant.ProductUrl;
import com.tokopedia.seller.product.edit.data.source.cloud.model.addproductsubmit.AddProductSubmitServiceModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.shop.open.data.model.response.DataResponse;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by zulfikarrahman on 2/5/18.
 */

public interface ProductApi {

    @POST(ProductUrl.URL_ADD_PRODUCT)
    Observable<Response<DataResponse<AddProductSubmitServiceModel>>> addProductSubmit(@Body ProductViewModel productViewModel);

    @PATCH(ProductUrl.URL_EDIT_PRODUCT)
    Observable<Response<DataResponse<AddProductSubmitServiceModel>>> editProductSubmit(@Body ProductViewModel productViewModel, @Path(ProductUrl.PRODUCT_ID) String productId);
}
