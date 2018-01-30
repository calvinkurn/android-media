package com.tokopedia.discovery.newdiscovery.search.fragment.product.helper;

import android.text.TextUtils;

import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;

import java.util.HashMap;

/**
 * Created by henrypriyono on 11/10/17.
 */

public class NetworkParamHelper {
    public static HashMap<String, String> getParamMap(String paramString) {
        HashMap<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(paramString)) {
            String[] params = paramString.split("&");
            for (String param : params) {
                String[] val = param.split("=");
                if (val.length > 0 && val.length <= 2) {
                    String name = val[0];
                    String value = val[1];
                    map.put(name, value);
                }
            }
        }
        return map;
    }

    public static String getQueryValue(String paramString) {
        if (!TextUtils.isEmpty(paramString)) {
            String[] params = paramString.split("&");
            for (String param : params) {
                String name = param.split("=")[0];
                if (BrowseApi.Q.equals(name)) {
                    return param.split("=")[1];
                }
            }
        }
        return "";
    }
}
