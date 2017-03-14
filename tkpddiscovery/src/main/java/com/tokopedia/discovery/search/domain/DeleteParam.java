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

public class DeleteParam implements DefaultParams {


    public static final String UNIQUE_ID = "unique_id";
    public static final String KEY_Q = "q";
    public static final String KEY_DELETE_ALL = "clear_all";

    public static final String DEFAULT_DELETE_ALL = "false";

    private final TKPDMapParam<String, String> param;

    public DeleteParam(Context context) {
        param = new TKPDMapParam<>();
        param.put(KEY_DELETE_ALL, DEFAULT_DELETE_ALL);
        String unique_id = AuthUtil.md5(GCMHandler.getRegistrationId(context));
        if (SessionHandler.isV4Login(context)) {
            unique_id = AuthUtil.md5(SessionHandler.getLoginID(context));
        }
        param.put(UNIQUE_ID, unique_id);
    }

    public DeleteParam(TKPDMapParam<String, String> param) {
        this.param = param;
    }

    public TKPDMapParam<String, String> getParam() {
        return param;
    }
}