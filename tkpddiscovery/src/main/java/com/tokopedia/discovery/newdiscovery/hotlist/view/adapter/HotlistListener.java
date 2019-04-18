package com.tokopedia.discovery.newdiscovery.hotlist.view.adapter;

import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistProductViewModel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by hangnadi on 10/9/17.
 */

public interface HotlistListener {

    void onHashTagClicked(String name, String url, String departmentID);

    void onWishlistClicked(int position, String productName, String productID, boolean wishlist);

    void onHotlistDescClicked(String messageClickAble);

    void onProductClicked(HotlistProductViewModel productViewModel, int adapterPosition);

    void onBannerAdsClicked(String appLink);

    String getUserId();

    boolean isLoggedIn();

    void onQuickFilterSelected(HashMap<String, String> filter, String eventLabel, String duplicateKey);

    void setQuickFilterList(List<QuickFilterItem> quickFilterItems);
}
