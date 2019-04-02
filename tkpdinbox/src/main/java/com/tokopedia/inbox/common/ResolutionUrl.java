package com.tokopedia.inbox.common;

/**
 * Created by meta on 21/02/19.
 */
public class ResolutionUrl {

    public static String HOSTNAME =  "https://m.tokopedia.com/";
    private static final String MOBILE =  "/mobile";

    public static final String RESO_CREATE = HOSTNAME + "resolution-center/create/%s" + MOBILE;
    private static final String RESO_INBOX =  HOSTNAME + "resolution-center/inbox/";
    public static final String RESO_DETAIL =  HOSTNAME + "resolution-center/detail/%s" + MOBILE;
    public static final String RESO_DETAIL_NEW =  HOSTNAME + "resolution/detail/%s" + MOBILE;

    public static final String RESO_INBOX_SELLER =  RESO_INBOX + "seller" + MOBILE;
    public static final String RESO_INBOX_BUYER =  RESO_INBOX + "buyer" + MOBILE;

    public static final String RESO_APPLINK =  "tokopedia://webview?titlebar=true&url=";
}
