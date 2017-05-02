package com.tokopedia.seller.topads.view.presenter;

import com.tokopedia.seller.topads.view.listener.TopAdsDetailEditView;
import com.tokopedia.seller.topads.view.model.TopAdsDetailProductViewModel;
import com.tokopedia.seller.topads.view.model.TopAdsProductViewModel;

import java.util.ArrayList;

/**
 * Created by Nathan on 5/9/16.
 */
public interface TopAdsDetailNewProductPresenter extends TopAdsDetailEditProductPresenter<TopAdsDetailEditView> {

    void saveAd(TopAdsDetailProductViewModel detailAd, ArrayList<TopAdsProductViewModel> topAdsProductList);
}
