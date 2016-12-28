package com.tokopedia.seller.topads.presenter;

import android.content.Context;

import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.interactor.DashboardTopadsInteractor;
import com.tokopedia.seller.topads.interactor.DashboardTopadsInteractorImpl;
import com.tokopedia.seller.topads.view.listener.TopAdsListPromoViewListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/24/16.
 */
public abstract class TopAdsListPresenterImpl<T> implements TopAdsListPresenter<T> {

    protected final TopAdsListPromoViewListener topAdsListPromoViewListener;
    protected final Context context;
    protected final DashboardTopadsInteractor dashboardTopadsInteractor;
    protected PagingHandler pagingHandler;

    public TopAdsListPresenterImpl(Context context,TopAdsListPromoViewListener topAdsListPromoViewListener) {
        this.topAdsListPromoViewListener = topAdsListPromoViewListener;
        this.context = context;
        this.dashboardTopadsInteractor = new DashboardTopadsInteractorImpl(context);
        pagingHandler = new PagingHandler();
    }

    protected String getShopId() {
        SessionHandler session = new SessionHandler(context);
        return session.getShopID();
    }

    @Override
    public void loadMore(Date startDate, Date endDate, int lastItemPosition, int visibleItem) {
        if(hasNextPage() && isLastItemPosition(lastItemPosition, visibleItem)){
            pagingHandler.nextPage();
            getListTopAdsFromNet(startDate, endDate);
        }
    }

    private boolean hasNextPage() {
        return false;
    }

    private boolean isLastItemPosition(int lastItemPosition, int visibleItem) {
        return lastItemPosition == visibleItem;
    }
}