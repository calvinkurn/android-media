package com.tokopedia.inbox.contactus;


import com.tokopedia.inbox.contactus.model.ImageUploadResult;

import okhttp3.RequestBody;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by nisie on 8/16/16.
 */
public interface UploadImageContactUsParam {

    @Multipart
    @POST("/upload/attachment")
    Observable<ImageUploadResult> uploadImage(
            @Header("Content-MD5") String contentMD5,
            @Header("Date") String date,
            @Header("Authorization") String authorization,
            @Header("X-Method") String xMethod,
            @Part("user_id") RequestBody userId,
            @Part("device_id") RequestBody deviceId,
            @Part("hash") RequestBody hash,
            @Part("device_time") RequestBody deviceTime,
            @Part("fileToUpload\"; filename=\"image.jpg")  RequestBody imageFile,
            @Part("id") RequestBody imageId,
            @Part("web_service") RequestBody web_service

    );

    @Multipart
    @POST("/upload/attachment/public/image")
    Observable<ImageUploadResult> uploadImagePublic(
            @Header("Content-MD5") String contentMD5,
            @Header("Date") String date,
            @Header("Authorization") String authorization,
            @Header("X-Method") String xMethod,
            @Part("user_id") RequestBody userId,
            @Part("device_id") RequestBody deviceId,
            @Part("hash") RequestBody hash,
            @Part("device_time") RequestBody deviceTime,
            @Part("fileToUpload\"; filename=\"image.jpg")  RequestBody imageFile,
            @Part("id") RequestBody imageId,
            @Part("web_service") RequestBody web_service

    );
}
