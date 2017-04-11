package com.tokopedia.seller.product.data.source.cloud.api;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.product.data.source.cloud.model.AddProductPictureServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.AddProductSubmitServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.addproductvalidation.AddProductValidationServiceModel;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author sebastianuskh on 4/11/17.
 */

public interface UploadProductApi {

    @FormUrlEncoded
    @POST
    Observable<Response<AddProductValidationServiceModel>> addProductValidation(@FieldMap TKPDMapParam<String, String> params);

    @FormUrlEncoded
    @POST
    Observable<Response<AddProductPictureServiceModel>> addProductPicture(@FieldMap TKPDMapParam<String, String> params);

    @FormUrlEncoded
    @POST
    Observable<Response<AddProductSubmitServiceModel>> addProductSubmit(@FieldMap TKPDMapParam<String, String> stringStringTKPDMapParam);
}
