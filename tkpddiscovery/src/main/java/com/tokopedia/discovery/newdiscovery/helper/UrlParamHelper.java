package com.tokopedia.discovery.newdiscovery.helper;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UrlParamHelper {
    public static String generateUrlParamString(Map<String, String> paramMap) {
        if (paramMap == null) {
            return "";
        }
        List<String> paramList = new ArrayList<>();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            paramList.add(entry.getKey() + "=" + entry.getValue().replace(" ", "+"));
        }
        return TextUtils.join("&", paramList);
    }
}
