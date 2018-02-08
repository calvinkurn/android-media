package com.tokopedia.seller.product.common.constant;

/**
 * Created by Nathaniel on 11/15/2016.
 */

public class ProductNetworkConstant {

    public static final String PARAM_SC = "sc";
    public static final String PARAM_Q = "q";
    public static final String PARAM_DEVICE= "device";
    public static final String PARAM_START= "start";
    public static final String PARAM_ROWS= "rows";

    public static final String VALUE_SOURCE_ANDROID = "android";

    //generate host
    public static final String SERVER_LANGUAGE = "new_add";
    public static final String GOLANG_VALUE = "2";

    //upload image
    public static final String UPLOAD_IMAGE = "/web-service/v4/action/upload-image/";
    public static final String HTTPS = "https://";
    public static final String UPLOAD_SHOP_IMAGE_PATH = "upload_shop_image.pl";
    public static final String UPLOAD_IMAGE_PRODUCT_PATH = "upload_product_image.pl";
    public static final String RESOLUTION_DEFAULT_VALUE = "300";
    public static final String LOGO_FILENAME_IMAGE_JPG = "fileToUpload\"; filename=\"image.jpg";
    public static final String RESOLUTION = "resolution";
    public static final String SERVER_ID = "server_id";
    public static final String PRODUCT_ID = "product_id";

    //manage product
    public static final String PRODUCT_PRICE = "product_price";
    public static final String PRODUCT_PRICE_CURRENCY = "product_price_currency";
    public static final String SHOP_ID = "shop_id";

    public static String getUploadImageUrl(String uploadHostUrl){
        return HTTPS + uploadHostUrl + UPLOAD_IMAGE;
    }
}