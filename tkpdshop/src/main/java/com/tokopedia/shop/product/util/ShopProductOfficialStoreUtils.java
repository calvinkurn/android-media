package com.tokopedia.shop.product.util;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.utils.network.URLGenerator;
import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.product.view.activity.ShopProductListActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by nathan on 3/5/18.
 */

public class ShopProductOfficialStoreUtils {

    private static final String SEAMLESS = "seamless";
    public static final String TOKOPEDIA_HOST = "tokopedia";

    private static final String URL_QUERY_SORT = "sort";
    private static final String URL_QUERY_KEYWORD = "keyword";
    private static final String URL_QUERY_PAGE = "page";

    private static final String URL_PATH_ETALASE = "etalase";
    private static final String URL_PATH_PRODUCT = "product";
    private static final String URL_PATH_PAGE = "page";
    public static final String HTTP = "http";

    public static boolean overrideUrl(Activity activity, String url, String shopId) {
        Uri uri = Uri.parse(url);
        if (uri.getScheme().equals(TOKOPEDIA_HOST)) {
            processUriTokopedia(activity, shopId, uri);
        } else if (uri.getScheme().startsWith(HTTP)) {
            if (activity.getApplication() instanceof ShopModuleRouter) {
                ((ShopModuleRouter) activity.getApplication()).goToWebview(url);
            }
        }
        return true;
    }

    private static void processUriTokopedia(Activity activity, String shopId, Uri uri) {
        String keyword = "";
        String page = "";
        List<String> paths = uri.getPathSegments();
        if (paths.size() > 1) {
            switch (paths.get(1)) {
                case URL_PATH_ETALASE:
                    String id = uri.getLastPathSegment();
                    HashMap<String, String> params = getShopProductRequestModel(uri);
                    if (!TextUtils.isEmpty(params.get(URL_QUERY_KEYWORD))) {
                        keyword = params.get(URL_QUERY_KEYWORD);
                    }
                    // Pointing specific page on apps will be break the page
                    page = params.get(URL_QUERY_PAGE);
                    activity.startActivity(ShopProductListActivity.createIntent(activity, shopId, keyword, id, params.get(URL_QUERY_SORT)));
                    break;
                case URL_PATH_PRODUCT:
                    if (activity.getApplication() instanceof ShopModuleRouter) {
                        String productId = uri.getLastPathSegment();
                        ((ShopModuleRouter) activity.getApplication()).goToProductDetailById(activity, productId);
                    }
                    break;
                case URL_PATH_PAGE:
                    params = getShopProductRequestModel(uri);
                    if (!TextUtils.isEmpty(params.get(URL_QUERY_KEYWORD))) {
                        keyword = params.get(URL_QUERY_KEYWORD);
                    }
                    // Pointing specific page on apps will be break the page
                    page = uri.getLastPathSegment();
                    activity.startActivity(ShopProductListActivity.createIntent(activity, shopId, keyword, null, params.get(URL_QUERY_SORT)));
                    break;
            }
        } else {
            HashMap<String, String> params = getShopProductRequestModel(uri);
            // Pointing specific page on apps will be break the page
            if (!TextUtils.isEmpty(params.get(URL_QUERY_KEYWORD))) {
                keyword = params.get(URL_QUERY_KEYWORD);
            }
            page = params.get(URL_QUERY_PAGE);
            activity.startActivity(ShopProductListActivity.createIntent(activity, shopId, keyword, null, params.get(URL_QUERY_SORT)));
        }
    }

    private static HashMap<String, String> getShopProductRequestModel(Uri uri) {
        HashMap<String, String> params = new HashMap<>();
        Set<String> parameterNames = uri.getQueryParameterNames();
        for (String parameterName : parameterNames) {
            switch (parameterName) {
                case URL_QUERY_SORT:
                    params.put(URL_QUERY_SORT, uri.getQueryParameter(parameterName));
                    break;
                case URL_QUERY_KEYWORD:
                    params.put(URL_QUERY_KEYWORD, uri.getQueryParameter(parameterName));
                    break;
                case URL_QUERY_PAGE:
                    params.put(URL_PATH_PAGE, uri.getQueryParameter(parameterName));
                    break;
            }
        }
        return params;
    }

    public static String getLogInUrl(String url, String fcmTokenId, String uid) {
        if (!url.contains(SEAMLESS)) {
            return URLGenerator.generateURLSessionLogin(encodeUrl(url), fcmTokenId, uid);
        }
        return url;
    }

    private static String encodeUrl(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return url;
    }
}
