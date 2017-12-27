package com.tokopedia.topads.dashboard.view.fragment;

import com.tokopedia.core.analytics.UnifyTracking;

/**
 * Created by zulfikarrahman on 8/29/17.
 */

public class TopAdsNewProductListExistingGroupEditFragment extends TopAdsNewProductListExistingGroupFragment {
    @Override
    public void goToGroupDetail(String groupId) {
        // do nothing
    }

    @Override
    protected void goToNextPage() {
        super.goToNextPage();
        UnifyTracking.eventTopadsEditGroupPromoAddProduct();
    }
}
