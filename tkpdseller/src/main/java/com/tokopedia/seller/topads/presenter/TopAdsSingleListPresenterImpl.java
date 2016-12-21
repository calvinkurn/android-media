package com.tokopedia.seller.topads.presenter;

import android.content.Context;

import com.tokopedia.seller.topads.view.fragment.TopAdsSingleListFragment;
import com.tokopedia.seller.topads.view.listener.TopAdsListPromoViewListener;

/**
 * Created by zulfikarrahman on 12/16/16.
 */
public class TopAdsSingleListPresenterImpl extends TopAdsListPresenterImpl implements TopAdsSingleListPresenter {
    public TopAdsSingleListPresenterImpl(Context context, TopAdsListPromoViewListener topadsListPromoViewListener) {
        super(context, topadsListPromoViewListener);
    }

}
