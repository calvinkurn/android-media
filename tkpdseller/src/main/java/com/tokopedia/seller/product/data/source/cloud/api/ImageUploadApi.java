package com.tokopedia.seller.product.data.source.cloud.api;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.seller.product.constant.ProductNetworkConstant;
import com.tokopedia.seller.product.data.source.cloud.model.UploadImageModel;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Response;
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
}
