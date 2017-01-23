package com.tokopedia.seller.topads.presenter;

import android.content.Context;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.view.listener.TopAdsDetailViewListener;

/**
 * Created by zulfikarrahman on 12/29/16.
 */
public abstract class TopAdsDetailPresenterImpl implements TopAdsDetailPresenter {

    protected final TopAdsDetailViewListener topAdsDetailViewListener;

    private Context context;

    public TopAdsDetailPresenterImpl(Context context, TopAdsDetailViewListener topAdsDetailViewListener) {
        this.context = context;
        this.topAdsDetailViewListener = topAdsDetailViewListener;
    }

    protected String getShopId() {
        SessionHandler session = new SessionHandler(context);
        return session.getShopID();
    }
}
