package com.tokopedia.inbox.rescenter.utils;


import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.inbox.rescenter.detail.model.actionresponsedata.UploadResCenterImageData;
import com.tokopedia.inbox.rescenter.shipping.model.NewUploadResCenterImageData;

import okhttp3.RequestBody;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by hangnadi on 3/1/16.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public interface UploadImageResCenter {

    @Multipart
    @POST(TkpdBaseURL.Upload.PATH_CONTACT_IMAGE)
    Observable<UploadResCenterImageData> uploadImage(
            @Header("Content-MD5") String contentMD5,// 1
            @Header("Date") String date,// 2
            @Header("Authorization") String authorization, // 3
            @Header("X-Method") String xMethod,// 4
            @Part("user_id") RequestBody userId,// 5
            @Part("device_id") RequestBody deviceId, // 6
            @Part("hash") RequestBody hash,// 7
            @Part("device_time") RequestBody deviceTime,// 8
            @Part("fileToUpload\"; filename=\"image.jpg") RequestBody imageFile, // "; filename="image.jpg"
            @Part("server_id") RequestBody serverId
    );

    @Multipart
    @POST(TkpdBaseURL.Upload.PATH_CREATE_RESOLUTION_PICTURE_FULL)
    Observable<UploadResCenterImageData> createResolutionPicture(
            @Header("Content-MD5") String contentMD5,// 1
            @Header("Date") String date,// 2
            @Header("Authorization") String authorization, // 3
            @Header("X-Method") String xMethod,// 4
            @Part("user_id") RequestBody userId,// 5
            @Part("attachment_string") RequestBody attachmentString, // 6
            @Part("file_path") RequestBody filePath,// 7
            @Part("server_id") RequestBody serverID,// 8
            @Part("web_service") RequestBody webService
    );

    @Multipart
    @POST("/upload/attachment")
    Observable<NewUploadResCenterImageData> uploadImageNew(
            @Header("Content-MD5") String contentMD5,
            @Header("Date") String date,
            @Header("Authorization") String authorization,
            @Header("X-Method") String xMethod,
            @Part("user_id") RequestBody userId,
            @Part("device_id") RequestBody deviceId,
            @Part("hash") RequestBody hash,
            @Part("device_time") RequestBody deviceTime,
            @Part("fileToUpload\"; filename=\"image.jpg") RequestBody imageFile,
            @Part("id") RequestBody imageId,
            @Part("token") RequestBody token,
            @Part("web_service") RequestBody web_service

    );
}
