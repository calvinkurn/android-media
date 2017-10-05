package com.tokopedia.discovery.interfaces;

import com.tokopedia.core.discovery.model.ObjContainer;
import com.tokopedia.core.util.Pair;


/**
 * Created by noiz354 on 3/23/16.
 */
public interface DiscoveryListener {
    int BROWSE_PRODUCT = -100;
    int BROWSE_CATALOG = -101;
    int BROWSE_SHOP = -102;
    int HOTLIST_BANNER = -107;
    int DYNAMIC_ATTRIBUTE = -108;
    int TOPADS = -111;
    int SEARCH_SUGGESTION = -112;
    int DELETE_SUGGESTION = -113;
    int OS_BANNER = -116;
    int CATEGORY_NAVIGATION_ROOT = -117;

    int SHOW_SEARCH = -113;
    int SHOW_CATEGORY = -114;
    int SHOW_DFAULT = SHOW_SEARCH;

    String ERRORCONTAINER = "ERROR_CONTAINER";
    String BROWSEPRODUCT = "BROWSE_PRODUCT";
    String BROWSESHOP = "BROWSE_SHOPS";
    String BROWSECATALOG = "BROWSE_CATALOGS";
    String HOTLISTBANNER = "HOTLISTBANNER";
    String DYNAMICATTRIBUTE = "DYNAMICATTRIBUTE";
    String OSBANNER = "OSBANNER";

    void onComplete(int type, Pair<String, ? extends ObjContainer> data);
    void onFailed(int type, Pair<String, ? extends ObjContainer> data);
    void onSuccess(int type, Pair<String, ? extends ObjContainer> data);
}
