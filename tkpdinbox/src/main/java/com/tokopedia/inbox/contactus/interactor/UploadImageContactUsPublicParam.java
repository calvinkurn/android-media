package com.tokopedia.inbox.contactus.interactor;

import com.tokopedia.core.inboxreputation.model.actresult.ImageUploadResult;

import okhttp3.RequestBody;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by nisie on 1/4/17.
 */
public interface UploadImageContactUsPublicParam {

    @Multipart
    @POST("/upload/attachment/public/image")
    Observable<ImageUploadResult> uploadImage(
            @Part("user_id") RequestBody userId,
            @Part("device_id") RequestBody deviceId,
            @Part("hash") RequestBody hash,
            @Part("device_time") RequestBody deviceTime,
            @Part("fileToUpload\"; filename=\"image.jpg")  RequestBody imageFile,
            @Part("id") RequestBody imageId,
            @Part("web_service") RequestBody web_service
    );
}
