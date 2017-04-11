package com.tokopedia.topads.sdk.domain;

import com.tokopedia.topads.sdk.base.TKPDMapParam;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class TopAdsParams {

    public static final String KEY_EP = "ep";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_DEVICE = "device";
    public static final String KEY_ITEM = "item";
    public static final String KEY_SRC = "src";
    public static final String KEY_PAGE = "page";
    public static final String KEY_DEPARTEMENT_ID = "dep_id";
    public static final String KEY_HOTLIST_ID = "h";
    public static final String KEY_QUERY = "q";
    public static final String KEY_PMIN = "pmin";
    public static final String KEY_PMAX = "pmax";
    public static final String KEY_FSHOP = "fshop";
    public static final String KEY_FLOC = "floc";
    public static final String KEY_FCITY = "fcity";
    public static final String KEY_WHOLESALE = "wholesale";
    public static final String KEY_SHIPPING = "shipping";
    public static final String KEY_PREORDER = "preorder";
    public static final String KEY_CONDITION = "condition";
    public static final String KEY_FRERETURNS = "freereturns";
    public static final String KEY_CASHBACK = "cashback";
    public static final String KEY_VARIANT = "variant";

    public static final String DEFAULT_KEY_ITEM = "2";
    public static final String DEFAULT_KEY_EP = "none";
    public static final String DEFAULT_KEY_DEVICE = "android";



    private final TKPDMapParam<String, String> param;


    public TopAdsParams() {
        param = new TKPDMapParam<>();
        param.put(KEY_ITEM, DEFAULT_KEY_ITEM);
        param.put(KEY_EP, DEFAULT_KEY_EP);
        param.put(KEY_DEVICE, DEFAULT_KEY_DEVICE);
    }

    public TKPDMapParam<String, String> getParam() {
        return param;
    }
}
