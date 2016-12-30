package com.tokopedia.seller.topads.view.fragment;

import com.tokopedia.seller.topads.presenter.TopAdsDetailProductPresenter;
import com.tokopedia.seller.topads.presenter.TopAdsDetailProductPresenterImpl;
import com.tokopedia.seller.topads.view.listener.TopAdsDetailViewListener;

/**
 * Created by zulfikarrahman on 12/29/16.
 */

public class TopAdsDetailProductFragment extends TopAdsDetailFragment<TopAdsDetailProductPresenter> {
    @Override
    protected void initialPresenter() {
        presenter = new TopAdsDetailProductPresenterImpl(this);
    }

}
