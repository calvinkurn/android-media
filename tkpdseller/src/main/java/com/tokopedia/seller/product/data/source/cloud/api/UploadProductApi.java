package com.tokopedia.seller.product.data.source.cloud.api;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.product.data.source.cloud.model.AddProductPictureServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.AddProductSubmitServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.AddProductValidationServiceModel;

import retrofit2.Response;
import rx.Observable;

/**
 * @author sebastianuskh on 4/11/17.
 */

public interface UploadProductApi {

    Observable<Response<AddProductValidationServiceModel>> addProductValidation(TKPDMapParam<String, String> params);

    Observable<Response<AddProductPictureServiceModel>> addProductPicture(TKPDMapParam<String, String> params);

    Observable<Response<AddProductSubmitServiceModel>> addProductSubmit(TKPDMapParam<String, String> stringStringTKPDMapParam);
}
