package com.tokopedia.session.register;

/**
 * Created by nisie on 1/27/17.
 */
public interface RegisterConstant {

    int PASSWORD_MINIMUM_LENGTH = 6;

    String NAME = "NAME";
    String PASSWORD = "PASSWORD";
    String EMAIL = "EMAIL";
    String IS_AUTO_VERIFY = "ISAUTOVERIFY";

    int GO_TO_REGISTER = 1;
    int GO_TO_LOGIN = 2;

    String PARAM_BIRTHDAY = "birth_day";// 1
    String PARAM_BIRTHMONTH = "birth_month";// 2
    String PARAM_BIRTHYEAR = "birth_year";// 3
    String PARAM_CONFIRM_PASSWORD = "confirm_password";// 4
    String PARAM_EMAIL = "email";// 5
    String PARAM_FACEBOOK_USERID = "fb_user_id";// 6
    String PARAM_FULLNAME = "full_name";// 7
    String PARAM_GENDER = "gender";// 8
    String PARAM_PASSWORD = "password";// 9
    String PARAM_PHONE = "phone";// 10
    String PARAM_IS_AUTO_VERIFY = "is_auto_verify";// 11
}
