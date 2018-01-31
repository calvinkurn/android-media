package com.tokopedia.network;

/**
 * @author by nisie on 1/30/18.
 */

public class SessionUrl {
    public static String BASE_DOMAIN = "https://ws.tokopedia.com/";
    public static String ACCOUNTS_DOMAIN = "https://accounts.tokopedia.com/";

    public static final String PATH_GET_TOKEN = "token";
    public static final String PATH_GET_INFO = "info";
    public static final String PATH_DISCOVER_LOGIN = "api/discover";
    public static final String DO_REGISTER = "api/register";
    public static final String CREATE_PASSWORD = "api/create-password";
    public static final String RESET_PASSWORD = "api/reset";
    public static final String RESENT_ACTIVATION = "/api/resend";
    public static final String ACTIVATE_UNICODE = "/token";
    public static final String CHANGE_EMAIL = "/api/v1/activation/change-email";
    public static final String PATH_EDIT_PROFILE = "/api/v1/user/profile-edit";


    public class OTP {
        private static final String BASE_OTP = "/otp";
        public static final String REQUEST_OTP = BASE_OTP + "/request";
        public static final String VALIDATE_OTP = BASE_OTP + "/validate";
        public static final String REQUEST_OTP_EMAIL = BASE_OTP + "/email/request";
        public static final String PATH_GET_METHOD_LIST = BASE_OTP + "/ws/mode-list";
    }

    public class Image {
        public static final String VALIDATE_SIZE = "/api/image/validate-size";
        public static final String GET_UPLOAD_HOST = "/api/image/upload-host";
        public static final String SUBMIT_DETAIL = "/api/image/submit-detail";
    }

    public class Ktp {
        public static final String CHECK_STATUS = "/api/ktp/check-status";
    }

    public class MSISDN {
        public static final String VERIFY_PHONE_NUMBER = "/api/msisdn/verify-msisdn";
        public static final String CHANGE_PHONE_NUMBER = "/api/msisdn/change-msisdn";
    }

    public class ChangeMSISDN {
        public static final String GET_WARNING = "/api/v1/change-msisdn/get-warning";
        public static final String SEND_EMAIL = "/api/v1/change-msisdn/update";
        public static final String VALIDATE = "/api/v1/change-msisdn/validate";
        public static final String VALIDATE_EMAIL_CODE = "/api/v1/change-msisdn/validate-code";
    }

    public static class Truecaller {
        public static final String VERIFY_PHONE = "/web-service/v4/truecaller/check";
    }

    public static class User {
        public static final String PATH_MAKE_LOGIN = "v4/session/make_login.pl";
    }
}
