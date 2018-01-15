package com.tokopedia.discovery.newdiscovery.hotlist.view.adapter;

import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistProductViewModel;

/**
 * Created by hangnadi on 10/9/17.
 */

public interface ItemClickListener {

    void onHashTagClicked(String name, String url, String departmentID);

    void onWishlistClicked(String productID, boolean wishlist);

    void onHotlistDescClicked(String messageClickAble);

    void onProductClicked(HotlistProductViewModel productViewModel, int adapterPosition);

    void onBannerAdsClicked(String appLink);
}
