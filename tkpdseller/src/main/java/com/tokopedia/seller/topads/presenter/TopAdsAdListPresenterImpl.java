package com.tokopedia.seller.topads.presenter;

import android.content.Context;

import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.model.data.Ad;
import com.tokopedia.seller.topads.view.listener.TopAdsListPromoViewListener;

/**
 * Created by zulfikarrahman on 11/24/16.
 */
public abstract class TopAdsAdListPresenterImpl<T extends Ad> implements TopAdsAdListPresenter<T> {

    protected final TopAdsListPromoViewListener topAdsListPromoViewListener;
    protected final Context context;
    protected PagingHandler pagingHandler;

    public TopAdsAdListPresenterImpl(Context context, TopAdsListPromoViewListener topAdsListPromoViewListener) {
        this.topAdsListPromoViewListener = topAdsListPromoViewListener;
        this.context = context;
        pagingHandler = new PagingHandler();
    }

    protected String getShopId() {
        SessionHandler session = new SessionHandler(context);
        return session.getShopID();
    }
}