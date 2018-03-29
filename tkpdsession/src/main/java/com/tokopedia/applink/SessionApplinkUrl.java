package com.tokopedia.applink;

import com.tokopedia.abstraction.constant.TkpdAppLink;

/**
 * @author by nisie on 1/30/18.
 */

public class SessionApplinkUrl extends TkpdAppLink {
    public static final String REGISTER = "tokopedia://registration";
    public static final String LOGIN = "tokopedia://login";
    public static final String PROFILE = "tokopedia://people/{user_id}";

}
