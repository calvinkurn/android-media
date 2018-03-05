package com.tokopedia.shop.product.util;

import android.app.Activity;
import android.net.Uri;

import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;

import java.util.List;
import java.util.Set;

/**
 * Created by nathan on 3/5/18.
 */

public class ShopProductOfficialStoreUtils {

    private static final String TOKOPEDIA_HOST = "tokopedia";

    private static final String URL_QUERY_SORT = "sort";
    private static final String URL_QUERY_KEYWORD = "keyword";
    private static final String URL_QUERY_PAGE = "page";

    private static final String URL_PATH_ETALASE = "etalase";
    private static final String URL_PATH_PRODUCT = "product";
    private static final String URL_PATH_PAGE = "page";

    public static boolean overrideUrl(Activity activity, String url) {
//        Uri uri = Uri.parse(url);
//        if (uri.getScheme().equals(TOKOPEDIA_HOST)) {
//            List<String> paths = uri.getPathSegments();
//            if (paths.size() > 1) {
//                switch (paths.get(1)) {
//                    case URL_PATH_ETALASE:
//                        String id = uri.getLastPathSegment();
//                        GetShopProductParam getShopProductParam = getShopProductRequestModel(uri);
//                        getShopProductParam.setEtalaseId(id);
//                        mOfficialShopInteractionListener.OnProductListPageRedirected(getShopProductParam);
//                        break;
//                    case URL_PATH_PRODUCT:
//                        String productId = uri.getLastPathSegment();
//                        mOfficialShopInteractionListener.OnProductInfoPageRedirected(productId);
//                        break;
//                    case URL_PATH_PAGE:
//                        String page = uri.getLastPathSegment();
//                        GetShopProductParam getShopProductParam1 = getShopProductRequestModel(uri);
//                        if (page != null) {
//                            getShopProductParam1.setPage(Integer.parseInt(page));
//                        }
//                        mOfficialShopInteractionListener.OnProductListPageRedirected(getShopProductParam1);
//                        break;
//                }
//            } else {
//                GetShopProductParam getShopProductParam = getShopProductRequestModel(uri);
//                mOfficialShopInteractionListener.OnProductListPageRedirected(getShopProductParam);
//            }
//        } else if (url.contains("shop-static")) {
//            webView.loadUrl(url);
//        } else if (uri.getScheme().startsWith("http")) {
//            mOfficialShopInteractionListener.OnWebViewPageRedirected(url);
//        }
        return true;
    }

//    private static ShopProductRequestModel getShopProductRequestModel(Uri uri) {
//        Set<String> parameterNames = uri.getQueryParameterNames();
//        GetShopProductParam getShopProductParam = new GetShopProductParam();
//        for (String parameterName : parameterNames) {
//            switch (parameterName) {
//                case URL_QUERY_SORT:
//                    getShopProductParam.setOrderBy(uri.getQueryParameter(parameterName));
//                    break;
//                case URL_QUERY_KEYWORD:
//                    getShopProductParam.setKeyword(uri.getQueryParameter(parameterName));
//                    break;
//                case URL_QUERY_PAGE:
//                    getShopProductParam.setPage(Integer.parseInt(uri.getQueryParameter(parameterName)));
//                    break;
//            }
//        }
//        return getShopProductParam;
//    }
}
