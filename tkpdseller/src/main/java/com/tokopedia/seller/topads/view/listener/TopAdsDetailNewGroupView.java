package com.tokopedia.seller.topads.view.listener;

import com.tokopedia.seller.topads.view.model.TopAdsProductViewModel;

/**
 * Created by hendry on 3/3/17.
 */
public interface TopAdsDetailNewGroupView extends TopAdsDetailEditView {

    void goToGroupDetail(String groupId);

    void onSuccessLoadProduct(TopAdsProductViewModel model);
}
