package com.tokopedia.seller.myproduct.network.apis;

import com.tokopedia.seller.myproduct.model.UploadProductImageData;

import okhttp3.RequestBody;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by m.normansyah on 04/12/2015.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public interface UploadImageProduct {

    @Multipart
    @POST("")
    Observable<UploadProductImageData> uploadProductV4(
            @Url String url,
            @Header("Content-MD5") String contentMD5,// 1
            @Header("Date") String date,// 2
            @Header("Authorization") String authorization, // 3
            @Header("X-Method") String xMethod,// 4
            @Part("user_id") RequestBody userId,// 5
            @Part("device_id") RequestBody deviceId, // 6
            @Part("hash") RequestBody hash,// 7
            @Part("device_time") RequestBody deviceTime,// 8
            @Part("fileToUpload\"; filename=\"image.jpg") RequestBody imageFile, // "; filename="image.jpg"
            @Part("name") RequestBody name,
            @Part("new_add") RequestBody newAdd,
            @Part("product_id") RequestBody productId,
            @Part("token") RequestBody token,
            @Part("server_id") RequestBody serverId
    );

}
