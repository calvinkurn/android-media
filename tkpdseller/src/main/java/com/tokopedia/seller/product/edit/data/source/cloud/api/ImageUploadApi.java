package com.tokopedia.seller.product.edit.data.source.cloud.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.product.common.constant.ProductNetworkConstant;
import com.tokopedia.seller.product.edit.data.source.cloud.model.UploadImageModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.addproductpicture.AddProductPictureServiceModel;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import rx.Observable;

/**
 * Created by zulfikarrahman on 3/22/17.
 */

public interface ImageUploadApi {
    @Multipart
    @POST(ProductNetworkConstant.UPLOAD_IMAGE_PRODUCT_PATH)
    Observable<Response<UploadImageModel>> uploadImage(@PartMap Map<String, RequestBody> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Upload.PATH_UPLOAD_IMAGE_HELPER + TkpdBaseURL.Upload.PATH_ADD_PRODUCT_PICTURE)
    Observable<Response<AddProductPictureServiceModel>> addProductPicture(@FieldMap TKPDMapParam<String, String> params);
}
