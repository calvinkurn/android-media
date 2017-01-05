package com.tokopedia.seller.topads.presenter;

import com.tokopedia.seller.topads.view.listener.TopAdsDetailViewListener;

/**
 * Created by zulfikarrahman on 12/29/16.
 */
public abstract class TopAdsDetailPresenterImpl implements TopAdsDetailPresenter {

    protected final TopAdsDetailViewListener topAdsDetailViewListener;

    public TopAdsDetailPresenterImpl(TopAdsDetailViewListener topAdsDetailViewListener){
        this.topAdsDetailViewListener = topAdsDetailViewListener;
    }
}
