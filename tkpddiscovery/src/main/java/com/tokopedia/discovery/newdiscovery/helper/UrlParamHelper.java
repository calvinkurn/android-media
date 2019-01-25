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
            try {
                paramList.add(entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return TextUtils.join("&", paramList);
    }
}
