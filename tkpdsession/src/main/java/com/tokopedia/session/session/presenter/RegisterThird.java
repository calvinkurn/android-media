package com.tokopedia.session.session.presenter;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.session.base.BaseImpl;

import java.util.HashMap;

/**
 * Created by noiz354 on 1/27/16.
 */
public abstract class RegisterThird extends BaseImpl<RegisterThirdView> {

    public static final
        String REGISTER_TYPE = "REGISTER_TYPE",
        FULLNAME = "FULLNAME",
        GENDER = "GENDER",
        EMAIL = "EMAIL",
        BIRTHDAY = "BIRTHDAY",
        PASSWORD = "PASSWORD",
        CONFIRM_PASSWORD = "CONFIRM_PASSWORD",
        IS_CHECKED = "IS_CHECKED",
        PHONENUMBER= "PHONENUMBER",
        DATA = "DATA",
        VALIDATE_EMAIL = "VALIDATE_EMAIL",
        DATE_YEAR = "DATE_YEAR",
        DATE_MONTH = "DATE_MONTH",
        DATE_DAY = "DATE_DAY",
        ALLOWED_FIELDS = "ALLOWED_FIELDS"
    ;

    String LOGIN_UUID_KEY = "LOGIN_UUID";
    String UUID_KEY = "uuid";
    String DEFAULT_UUID_VALUE = "";

    public RegisterThird(RegisterThirdView view) {
        super(view);
    }

    public abstract void setData(HashMap<String, Object> data);

    public abstract void register(Context context, Bundle bundle);

    public abstract Object getData(String type);
    public abstract void calculateDateTime();

    public abstract void makeLogin(Context context, Bundle data);
}
