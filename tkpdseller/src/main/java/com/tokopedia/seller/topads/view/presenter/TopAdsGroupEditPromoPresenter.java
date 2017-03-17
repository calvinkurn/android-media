package com.tokopedia.seller.topads.view.presenter;

import com.tokopedia.seller.topads.view.listener.TopAdsGroupEditPromoView;

/**
 * Created by zulfikarrahman on 3/1/17.
 */

public interface TopAdsGroupEditPromoPresenter extends TopAdsManageGroupPromoPresenter<TopAdsGroupEditPromoView> {
    void moveOutProductGroup(String shopId, String adId);

    void moveToNewProductGroup(String adid, String groupName, String shopID);

    void moveToExistProductGroup(String adid, String groupId, String shopID);
}
