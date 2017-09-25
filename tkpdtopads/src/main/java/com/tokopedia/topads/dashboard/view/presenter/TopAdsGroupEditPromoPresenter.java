package com.tokopedia.topads.dashboard.view.presenter;

import com.tokopedia.topads.dashboard.view.listener.TopAdsGroupEditPromoView;

/**
 * Created by zulfikarrahman on 3/1/17.
 */

public interface TopAdsGroupEditPromoPresenter extends TopAdsManageGroupPromoPresenter<TopAdsGroupEditPromoView> {
    void moveOutProductGroup(String shopId, String adId);

    void moveToNewProductGroup(String adid, String groupName, String shopID);

    void moveToExistProductGroup(String adid, String groupId, String shopID);
}
