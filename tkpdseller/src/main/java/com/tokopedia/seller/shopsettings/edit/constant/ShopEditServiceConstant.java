package com.tokopedia.seller.shopsettings.edit.constant;

/**
 * Created by Toped18 on 5/24/2016.
 */
public interface ShopEditServiceConstant {
    // common
    String TYPE = "type";
    String RETRY_FLAG = "RETRY_FLAG";
    String RECEIVER = "receiver";
    int NUMBER_OF_TRY = 1;
    int TIMEOUT_TIME = 10000;

    int STATUS_RUNNING = 0;
    int STATUS_FINISHED = 1;
    int STATUS_ERROR = 2;

    String NETWORK_ERROR_FLAG ="NETWORK_ERROR_FLAG";
    String MESSAGE_ERROR_FLAG ="MESSAGE_ERROR_FLAG";
    String noNetworkConnection = "Tidak ada koneksi";
    int INVALID_NETWORK_ERROR_FLAG = -1;
    String INVALID_MESSAGE_ERROR = "default";

    // type
    int INVALID_TYPE = -1;
    int CREATE_SHOP = 0;
    int GET_SHOP_DATA = 2;
    int POST_EDIT_DATA = 3;
    int UPDATE_SHOP_IMAGE = 4;
    int UPDATE_SHOP_SCHEDULE = 5;
    int CREATE_SHOP_WITHOUT_IMAGE = 7;



    String OPEN_SHOP_VALIDATION_PARAM = "OPEN_SHOP_VALIDATION_PARAM";
    String OPEN_SHOP_SUBMIT_DATA = "OPEN_SHOP_SUBMIT_DATA";
    String OPEN_SHOP_PICTURE_DATA = "OPEN_SHOP_PICTURE_DATA";
    String OPEN_SHOP_VALIDATION_DATA = "OPEN_SHOP_VALIDATION_DATA";



    // generate host tkpdurl
    // TODO move to TkpdUrl.java
    String GENERATE_HOST_V4 = "https://m.tokopedia.com/v4/action/generate-host/generate_host.pl";

    // param for generate host
    String SERVER_LANGUAGE = "new_add";
    String GOLANG_VALUE = "2";

    String TAG = "STUART";
    String UPLOAD_SHOP_IMAGE = "/web-service/v4/action/upload-image/upload_shop_image.pl/";
    String HTTPS = "https://";
    String GENERATED_RETROFIR_INSTANCE_API = "GENERATED_RETROFIR_INSTANCE_API";
    String GENERATE_HOST_MODEL = "GENERATE_HOST_MODEL";


    String MODEL_GET_SHOP_DATA = "MODEL_GET_SHOP_DATA";
    String MODEL_EDIT_SHOP_DATA = "MODEL_EDIT_SHOP_DATA";
    String MODEL_RESPONSE_EDIT_SHOP = "MODEL_RESPONSE_EDIT_SHOP";
    String MODEL_RESPONSE_UPDATE_SHOP_CLOSE = "MODEL_RESPONSE_UPDATE_SHOP_CLOSE";
    String MODEL_CHECK_DOMAIN = "MODEL_CHECK_DOMAIN";
    String MODEL_SCHEDULE_SHOP = "MODEL_SCHEDULE_SHOP";
    String UPDATE_SHOP_IMAGE_MODEL = "UPDATE_SHOP_IMAGE_MODEL";

    String JSON_SHOP_DATA_CACHE = "JSON_SHOP_DATA_CACHE";
    String PIC_SRC = "pic_src";
    String INPUT_IMAGE = "INPUT_IMAGE";
    String UPLOAD_SHOP_LOGO_DATA = "UPLOAD_SHOP_LOGO_DATA";

    String SERVER_ID = "server_id";
    String SHOP_LOGO = "shop_logo";
    String USER_ID = "user_id";

    String DOMAIN_NAME_CHECK = "shop_domain";




}
