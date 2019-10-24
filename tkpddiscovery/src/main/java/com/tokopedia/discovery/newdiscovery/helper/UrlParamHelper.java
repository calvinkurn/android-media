package com.tokopedia.discovery.newdiscovery.helper;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UrlParamHelper {

    public static String generateUrlParamString(Map<String, String> paramMap) {
        if (mapIsEmpty(paramMap)) {
            return "";
        }

        List<String> paramList = createParameterListFromMap(paramMap);

        return joinWithDelimiter("&", paramList);
    }

    private static boolean mapIsEmpty(Map<String, String> paramMap) {
        return paramMap == null || paramMap.size() <= 0;
    }

    private static List<String> createParameterListFromMap(Map<String, String> paramMap) {
        List<String> paramList = new ArrayList<>();

        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            if (mapEntryHasNulls(entry)) continue;

            addParameterEntryToList(paramList, entry);
        }

        return paramList;
    }

    private static boolean mapEntryHasNulls(Map.Entry<String, String> entry) {
        return entry.getKey() == null || entry.getValue() == null;
    }

    private static void addParameterEntryToList(List<String> paramList, Map.Entry<String, String> entry) {
        try {
            paramList.add(entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static String joinWithDelimiter(@NotNull CharSequence delimiter, @NotNull Iterable tokens) {
        final Iterator<?> it = tokens.iterator();

        if (!it.hasNext()) {
            return "";
        }

        final StringBuilder sb = new StringBuilder();
        sb.append(it.next());

        while (it.hasNext()) {
            sb.append(delimiter);
            sb.append(it.next());
        }

        return sb.toString();
    }
}
