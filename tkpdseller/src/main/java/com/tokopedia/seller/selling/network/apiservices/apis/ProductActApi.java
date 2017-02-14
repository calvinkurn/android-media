package com.tokopedia.seller.selling.network.apiservices.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.seller.myproduct.model.AddProductWithoutImageModel;
import com.tokopedia.seller.myproduct.model.EditProductPictureModel;
import com.tokopedia.seller.myproduct.model.ProductSubmitModel;
import com.tokopedia.seller.myproduct.model.ProductValidationModel;
import com.tokopedia.seller.selling.orderReject.model.ResponseEditDescription;
import com.tokopedia.seller.selling.orderReject.model.ResponseEditPrice;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by ricoharisin on 11/10/16.
 */

public interface ProductActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_EDIT_DESCRIPTION)
    Observable<ResponseEditDescription> editDescription(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_EDIT_WEIGHT_PRICE)
    Observable<ResponseEditPrice> editWeightPrice(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_ADD_PRODUCT_SUBMIT)
    Observable<ProductSubmitModel> addSubmit(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_ADD_VALIDATION)
    Observable<ProductValidationModel> addValidation(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_ADD_VALIDATION)
    Observable<AddProductWithoutImageModel> addValidationWithoutImage(
            @FieldMap Map<String, String> params
    );

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_DELETE_PRODUCT)
    Observable<Response<TkpdResponse>> delete(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_DELETE_PICTURE)
    Observable<Response<TkpdResponse>> deletePicture(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_EDIT_PICTURE)
    Observable<EditProductPictureModel> editPicture(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_EDIT_PRODUCT)
    Observable<Response<TkpdResponse>> edit(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_EDIT_CATEGORY)
    Observable<Response<TkpdResponse>> editCategory(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_EDIT_INSURANCE)
    Observable<Response<TkpdResponse>> editInsurance(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_EDIT_PRICE)
    Observable<Response<TkpdResponse>> editPrice(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_EDIT_ETALASE)
    Observable<Response<TkpdResponse>> editEtalase(@FieldMap Map<String, String> params);


}
