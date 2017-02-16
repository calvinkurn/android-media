package com.tokopedia.seller.myproduct.network.apis;

import com.tokopedia.seller.myproduct.model.AddProductPictureModel;
import com.tokopedia.seller.myproduct.model.UploadProductImageData;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
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

    String DUPLICATE = "duplicate";
    String PRODUCT_PHOTO = "product_photo";
    String PRODUCT_PHOTO_DEFAULT = "product_photo_default";
    String PRODUCT_PHOTO_DESC = "product_photo_desc";
    String SERVER_ID = "server_id";

    @Multipart
    @POST("")
    Call<UploadProductImageData> uploadProduct(
            @Url String url,
            @Part("image\"; filename=\"image.jpg ") RequestBody imageFile, // "; filename="image.jpg"
            @Part("server_id") RequestBody serverId,
            @Part("user_id") RequestBody user
    );

    @Multipart
    @POST("")
    Observable<String> uploadToLocal(
            @Url String url,
            @Part("fileToUpload\"; filename=\"image.jpg") RequestBody test
    );

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

    @Multipart
    @POST("")
    Observable<UploadProductImageData> addProductPicture(
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

    @FormUrlEncoded
    @POST("")
    Observable<AddProductPictureModel> addProductPictureAfter(
            @Url String url,
            @Header("Content-MD5") String contentMD5,// 1
            @Header("Date") String date,// 2
            @Header("Authorization") String authorization, // 3
            @Header("X-Method") String xMethod,// 4
            @Field("user_id") String userId,// 5
            @Field("device_id") String deviceId, // 6
            @Field("hash") String hash,// 7
            @Field("device_time") String deviceTime,// 8
            @Field(DUPLICATE) String duplicate,
            @Field(PRODUCT_PHOTO) String productPhoto,
            @Field(PRODUCT_PHOTO_DEFAULT) String productPhotoDefault,
            @Field(PRODUCT_PHOTO_DESC) String productPhotoDesc,
            @Field(SERVER_ID) String serverId
    );

    @FormUrlEncoded
    @POST("add_product_picture.pl")
    Observable<AddProductPictureModel> addProductPictureAfter(
            @FieldMap Map<String, String> params
    );
}
