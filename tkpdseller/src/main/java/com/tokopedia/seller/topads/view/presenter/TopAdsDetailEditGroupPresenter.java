package com.tokopedia.seller.topads.view.presenter;

import com.tokopedia.seller.topads.view.listener.TopAdsDetailEditView;
import com.tokopedia.seller.topads.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.seller.topads.view.model.TopAdsProductViewModel;

import java.util.List;

/**
 * Created by Nathan on 5/9/16.
 */
public interface TopAdsDetailEditGroupPresenter<T extends TopAdsDetailEditView> extends TopAdsDetailEditPresenter<T> {

    void saveAd(TopAdsDetailGroupViewModel topAdsDetailGroupViewModel, List<TopAdsProductViewModel> topAdsProductViewModelList);

}
