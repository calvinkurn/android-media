package com.tokopedia.seller.topads.view.presenter;

import com.tokopedia.seller.topads.view.listener.TopAdsDetailNewGroupView;
import com.tokopedia.seller.topads.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.seller.topads.view.model.TopAdsProductViewModel;

import java.util.List;

/**
 * Created by Nathan on 5/9/16.
 */
public interface TopAdsDetailNewGroupPresenter<T extends TopAdsDetailNewGroupView> extends TopAdsDetailEditGroupPresenter<T> {
    void saveAdNew(String groupName,
                   TopAdsDetailGroupViewModel topAdsDetailProductViewModel,
                   List<TopAdsProductViewModel> topAdsProductViewModelList);

    void saveAdExisting(int groupId,
                        List<TopAdsProductViewModel> topAdsProductViewModelList);
}
