package com.tokopedia.discovery.search.domain;

import android.content.Context;

import com.tokopedia.core.base.domain.DefaultParams;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;

/**
 * @author erry on 23/02/17.
 */

public class SearchParam implements DefaultParams {

    public static final String KEY_DEVICE = "device";
    public static final String KEY_SOURCE = "source";
    public static final String KEY_QUERY = "q";
    public static final String KEY_UNIQUE_ID = "unique_id";
    public static final String KEY_COUNT = "count";
    public static final String DEFAULT_DEVICE = "android";
    public static final String DEFAULT_SOURCE = "searchbar";
    public static final String DEFAULT_COUNT = "5";

    private final TKPDMapParam<String, String> param;

    public SearchParam(Context context) {
        param = new TKPDMapParam<>();
        param.put(KEY_DEVICE, DEFAULT_DEVICE);
        param.put(KEY_SOURCE, DEFAULT_SOURCE);
        param.put(KEY_COUNT, DEFAULT_COUNT);
        String unique_id = AuthUtil.md5(GCMHandler.getRegistrationId(context));
        if (SessionHandler.isV4Login(context)) {
            unique_id = AuthUtil.md5(SessionHandler.getLoginID(context));
        }
        param.put(KEY_UNIQUE_ID, unique_id);
    }

    public SearchParam(TKPDMapParam<String, String> param) {
        this.param = param;
    }

    public TKPDMapParam<String, String> getParam() {
        return param;
    }
}