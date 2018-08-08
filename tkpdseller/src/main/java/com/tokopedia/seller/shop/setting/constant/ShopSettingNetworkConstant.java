package com.tokopedia.seller.shop.setting.constant;

/**
 * Created by zulfikarrahman on 3/22/17.
 */

public class ShopSettingNetworkConstant {

    //generate host
    public static final String SERVER_LANGUAGE = "new_add";
    public static final String GOLANG_VALUE = "2";

    //upload image
    public static final String UPLOAD_SHOP_IMAGE = "/web-service/v4/action/upload-image/";
    public static final String HTTPS = "https://";
    public static final String UPLOAD_SHOP_IMAGE_PATH = UPLOAD_SHOP_IMAGE + "upload_shop_image.pl";
    public static final String RESOLUTION_DEFAULT_VALUE = "300";
    public static final String LOGO_FILENAME_IMAGE_JPG = "logo\"; filename=\"image.jpg";
    public static final String RESOLUTION = "resolution";
    public static final String SERVER_ID = "server_id";

    public static String getUploadImageUrl(String uploadHostUrl) {
        return HTTPS + uploadHostUrl + UPLOAD_SHOP_IMAGE;
    }
}
