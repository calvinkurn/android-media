package com.tokopedia.discovery.newdiscovery.helper;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
            paramList.add(entry.getKey() + "=" + entry.getValue());
        }
        String result = TextUtils.join("&", paramList);
        try {
            return URLEncoder.encode(result,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            return result;
        }
    }
}
