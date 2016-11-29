package com.tokopedia.session.session.model;

/**
 * Created by m.normansyah on 05/11/2015.
 * Modified by m.normansyah on 17-11-2015, this is just interface
 */
public class LoginModel {

    public static final int NEED_TO_CREATE_PASSWORD_AFTER_LOGIN = 1;
    public static final int NEED_TO_ACTIVATE_EMAIL_AFTER_LOGIN = 2;

    public static final int SECURTY_1_NO_HAPE_VALUE = 1;
    public static final int SECURTY_1_REKENING_VALUE = 2;
    public static final int SECURTY_1_DEFAULT_VALUE = 0;
    public static final int SECURTY_2_VALUE = 2;
    public static final int SECURTY_2_DEFAULT_VALUE = 0;

    public static final int FORBIDEN_TO_LOGIN = 0;
    public static final int NOT_LOGIN_YET = 0;

    // loginType constant
    public static final String EmailType =  "E-mail";
    public static final String GoogleType = "Google+";
    public static final String FacebookType = "Facebook";
    public static final String WebViewType = "WebView";
    public static final String secType ="enterSecurityType";

}
