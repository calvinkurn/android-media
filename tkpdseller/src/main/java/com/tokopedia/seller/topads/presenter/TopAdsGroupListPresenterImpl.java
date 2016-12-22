package com.tokopedia.seller.topads.presenter;

import android.content.Context;

import com.tokopedia.seller.topads.model.data.GroupAd;
import com.tokopedia.seller.topads.view.listener.TopAdsListPromoViewListener;

import java.util.List;

/**
 * Created by zulfikarrahman on 12/22/16.
 */

public class TopAdsGroupListPresenterImpl extends TopAdsListPresenterImpl<GroupAd> implements TopAdsGroupListPresenter {
    public TopAdsGroupListPresenterImpl(Context context, TopAdsListPromoViewListener topAdsListPromoViewListener) {
        super(context, topAdsListPromoViewListener);
    }

    @Override
    public void getListTopAdsFromNet() {

    }

    @Override
    public void actionDeleteAds(List<GroupAd> ads) {

    }

    @Override
    public void actionOffAds(List<GroupAd> ads) {

    }

    @Override
    public void actionOnAds(List<GroupAd> ads) {

    }
}
