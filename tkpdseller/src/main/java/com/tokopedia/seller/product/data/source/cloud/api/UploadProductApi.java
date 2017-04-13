package com.tokopedia.seller.product.data.source.cloud.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.product.data.source.cloud.model.addproductpicture.AddProductPictureServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.addproductsubmit.AddProductSubmitServiceModel;
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
    @POST(TkpdBaseURL.Product.V4_ACTION_PRODUCT + TkpdBaseURL.Product.PATH_ADD_VALIDATION)
    Observable<Response<AddProductValidationServiceModel>> addProductValidation(@FieldMap TKPDMapParam<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Upload.PATH_UPLOAD_IMAGE_HELPER + TkpdBaseURL.Upload.PATH_ADD_PRODUCT_PICTURE)
    Observable<Response<AddProductPictureServiceModel>> addProductPicture(@FieldMap TKPDMapParam<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.V4_ACTION_PRODUCT + TkpdBaseURL.Product.PATH_ADD_PRODUCT_SUBMIT)
    Observable<Response<AddProductSubmitServiceModel>> addProductSubmit(@FieldMap TKPDMapParam<String, String> stringStringTKPDMapParam);
}
