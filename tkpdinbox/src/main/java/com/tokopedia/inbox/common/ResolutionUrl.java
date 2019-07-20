package com.tokopedia.inbox.common;

import com.tokopedia.url.TokopediaUrl;

/**
 * Created by meta on 21/02/19.
 */
public class ResolutionUrl {

    public static String HOSTNAME = TokopediaUrl.Companion.getInstance().getMOBILEWEB();
    private static final String MOBILE =  "/mobile";

    public static final String RESO_CREATE = "resolution-center/create/%s" + MOBILE;
    private static final String RESO_INBOX =  "resolution-center/inbox/";
    public static final String RESO_DETAIL =  "resolution-center/detail/%s" + MOBILE;
    public static final String RESO_DETAIL_NEW =  "resolution/detail/%s" + MOBILE;

    public static final String RESO_INBOX_SELLER =  RESO_INBOX + "seller" + MOBILE;
    public static final String RESO_INBOX_BUYER =  RESO_INBOX + "buyer" + MOBILE;

    public static final String RESO_APPLINK =  "tokopedia://webview?titlebar=true&url=";
}
