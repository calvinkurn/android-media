package com.tokopedia.inbox.common;

/**
 * Created by meta on 21/02/19.
 */
public class ResolutionUrl {

    private static final String HOSTNAME =  "https://m.tokopedia.com/";

    public static final String RESO_CREATE = HOSTNAME + "resolution-center/create/";
    private static final String RESO_INBOX =  HOSTNAME + "resolution-center/inbox/";
    public static final String RESO_DETAIL =  HOSTNAME + "resolution-center/detail/";

    public static final String RESO_INBOX_SELLER =  RESO_INBOX + "seller";
    public static final String RESO_INBOX_BUYER =  RESO_INBOX + "buyer";
}
