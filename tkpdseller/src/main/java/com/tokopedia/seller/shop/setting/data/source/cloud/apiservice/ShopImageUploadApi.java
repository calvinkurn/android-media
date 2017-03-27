package com.tokopedia.seller.shop.setting.data.source.cloud.apiservice;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.seller.shop.setting.constant.ShopSettingNetworkConstant;
import com.tokopedia.seller.shop.setting.data.model.GenerateHostModel;
import com.tokopedia.seller.shop.setting.data.model.UploadShopImageModel;

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

public interface ShopImageUploadApi {
    @Multipart
    @POST(ShopSettingNetworkConstant.UPLOAD_SHOP_IMAGE_PATH)
    Observable<Response<UploadShopImageModel>> uploadImage(@PartMap Map<String, RequestBody> params);
}
