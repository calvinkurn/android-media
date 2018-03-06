package com.tokopedia.shop.product.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.tokopedia.shop.ShopModuleRouter;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;
import com.tokopedia.shop.product.view.activity.ShopProductListActivity;
import com.tokopedia.shop.product.view.fragment.ShopProductListFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by nathan on 3/5/18.
 */

public class ShopProductOfficialStoreUtils {

    public static final String TOKOPEDIA_HOST = "tokopedia";

    private static final String URL_QUERY_SORT = "sort";
    private static final String URL_QUERY_KEYWORD = "keyword";
    private static final String URL_QUERY_PAGE = "page";

    private static final String URL_PATH_ETALASE = "etalase";
    private static final String URL_PATH_PRODUCT = "product";
    private static final String URL_PATH_PAGE = "page";
    private static final String URL_RECHARGE_HOST = "pulsa.tokopedia.com";
    public static final String HTTP = "http";

    public static boolean overrideUrl(Activity activity, String url, String shopId) {
        Uri uri = Uri.parse(url);
        if (uri.getScheme().equals(TOKOPEDIA_HOST)) {
            List<String> paths = uri.getPathSegments();
            if (paths.size() > 1) {
                switch (paths.get(1)) {
                    case URL_PATH_ETALASE:
                        String id = uri.getLastPathSegment();
                        HashMap<String, String> params = getShopProductRequestModel(uri);
                        activity.startActivity(ShopProductListActivity.createIntent(activity, shopId, params.get(URL_QUERY_KEYWORD), id,
                                params.get(URL_QUERY_SORT), params.get(URL_QUERY_PAGE)));
                        break;
                    case URL_PATH_PRODUCT:
                        if(activity.getApplication() instanceof ShopModuleRouter){
                            String productId = uri.getLastPathSegment();
                            ((ShopModuleRouter)activity.getApplication()).goToProductDetailById(activity, productId);
                        }
                        break;
                    case URL_PATH_PAGE:
                        String page = uri.getLastPathSegment();
                        params = getShopProductRequestModel(uri);

                        activity.startActivity(ShopProductListActivity.createIntent(activity, shopId, params.get(URL_QUERY_KEYWORD), null,
                                params.get(URL_QUERY_SORT), page));
                        break;
                }
            } else {
                HashMap<String, String> params = getShopProductRequestModel(uri);
                activity.startActivity(ShopProductListActivity.createIntent(activity, shopId, params.get(URL_QUERY_KEYWORD), null,
                        params.get(URL_QUERY_SORT), params.get(URL_QUERY_PAGE)));
            }
        } else if (uri.getScheme().startsWith(HTTP)) {
            if(activity.getApplication() instanceof ShopModuleRouter) {
                ((ShopModuleRouter)activity.getApplication()).goToWebview(url);
            }
        }
        return true;
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
}
