package com.tokopedia.session.register;

/**
 * Created by nisie on 1/27/17.
 */
public interface RegisterConstant {

    int MAX_PHONE_NUMBER = 13;
    int MIN_PHONE_NUMBER = 6;
    int PASSWORD_MINIMUM_LENGTH = 6;
    int IS_EMAIL_ACTIVE = 1;

    String NAME = "NAME";
    String PASSWORD = "PASSWORD";
    String EMAIL = "EMAIL";
    String IS_AUTO_VERIFY = "ISAUTOVERIFY";

    int GO_TO_REGISTER = 0;
    int GO_TO_ACTIVATION_PAGE = 1;
    int GO_TO_LOGIN = 2;
    int GO_TO_RESET_PASSWORD = 3;

    String PARAM_BIRTHDAY = "birth_day";
    String PARAM_BIRTHMONTH = "birth_month";
    String PARAM_BIRTHYEAR = "birth_year";
    String PARAM_CONFIRM_PASSWORD = "confirm_password";
    String PARAM_EMAIL = "email";
    String PARAM_FACEBOOK_USERID = "fb_user_id";
    String PARAM_FULLNAME = "full_name";
    String PARAM_GENDER = "gender";
    String PARAM_PASSWORD = "password";
    String PARAM_PHONE = "phone";
    String PARAM_IS_AUTO_VERIFY = "is_auto_verify";
}
