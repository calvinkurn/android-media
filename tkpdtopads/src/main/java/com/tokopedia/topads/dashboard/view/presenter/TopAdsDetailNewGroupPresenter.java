package com.tokopedia.topads.dashboard.view.presenter;

import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailNewGroupView;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;

import java.util.List;

/**
 * Created by Nathan on 5/9/16.
 */
public interface TopAdsDetailNewGroupPresenter<T extends TopAdsDetailNewGroupView> extends TopAdsDetailEditGroupPresenter<T> {
    void saveAdNew(String groupName,
                   TopAdsDetailGroupViewModel topAdsDetailProductViewModel,
                   List<TopAdsProductViewModel> topAdsProductViewModelList);

    void saveAdExisting(String groupId,
                        List<TopAdsProductViewModel> topAdsProductViewModelList);
}
