package com.tokopedia.discovery.util;

import androidx.annotation.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by noiz354 on 6/28/16.
 */
public class PagingHandlerUtil {
    public static Map<String, String> splitQuery(URL url) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String query = url.getQuery();
        if(query != null) {
            String[] pairs = query.split("&");
            if (pairs != null) {
                for (String pair : pairs) {
                    int idx = pair.indexOf("=");
                    query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
                }
            }
        }
        return query_pairs;
    }

    @Nullable
    public static String getCertainKeyword(String url, String keyword) throws UnsupportedEncodingException, MalformedURLException {
        if(url != null && !url.isEmpty() && !url.equals("0")){
            Map<String, String> splitQuery = splitQuery(new URL(url));
            if(splitQuery != null && !splitQuery.isEmpty() && splitQuery.get(keyword) != null)
                return splitQuery.get(keyword);
        }
        return null;
    }
}
