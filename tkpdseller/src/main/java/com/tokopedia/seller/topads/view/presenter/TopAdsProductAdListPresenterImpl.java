package com.tokopedia.seller.topads.view.presenter;

import android.content.Context;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.data.source.local.TopAdsCacheDataSourceImpl;
import com.tokopedia.seller.topads.data.source.local.TopAdsDbDataSourceImpl;
import com.tokopedia.seller.topads.domain.interactor.ListenerInteractor;
import com.tokopedia.seller.topads.domain.interactor.TopAdsProductAdInteractor;
import com.tokopedia.seller.topads.domain.interactor.TopAdsProductAdInteractorImpl;
import com.tokopedia.seller.topads.data.model.data.ProductAd;
import com.tokopedia.seller.topads.data.model.request.SearchAdRequest;
import com.tokopedia.seller.topads.data.model.response.PageDataResponse;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.TopAdsManagementService;
import com.tokopedia.seller.topads.view.listener.TopAdsListPromoViewListener;

import java.util.Date;
import java.util.List;

/**
 * Created by zulfikarrahman on 12/16/16.
 */
public class TopAdsProductAdListPresenterImpl extends TopAdsAdListPresenterImpl<ProductAd> implements TopAdsProductAdListPresenter {

    protected final TopAdsProductAdInteractor productAdInteractor;

    public TopAdsProductAdListPresenterImpl(Context context, TopAdsListPromoViewListener topadsListPromoViewListener) {
        super(context, topadsListPromoViewListener);
        this.productAdInteractor = new TopAdsProductAdInteractorImpl(new TopAdsManagementService(new SessionHandler(context).getAccessToken(context)), new TopAdsDbDataSourceImpl(), new TopAdsCacheDataSourceImpl(context));
    }

    @Override
    public void searchAd(Date startDate, Date endDate, String keyword, int status, long groupId, int page) {
        SearchAdRequest searchAdRequest = new SearchAdRequest();
        searchAdRequest.setShopId(getShopId());
        searchAdRequest.setStartDate(startDate);
        searchAdRequest.setEndDate(endDate);
        searchAdRequest.setKeyword(keyword);
        searchAdRequest.setStatus(status);
        searchAdRequest.setGroup(groupId);
        searchAdRequest.setPage(page);
        productAdInteractor.searchAd(searchAdRequest, new ListenerInteractor<PageDataResponse<List<ProductAd>>>() {
            @Override
            public void onSuccess(PageDataResponse<List<ProductAd>> pageDataResponse) {
                topAdsListPromoViewListener.onSearchAdLoaded(pageDataResponse.getData(), pageDataResponse.getPage().getTotal());
            }

            @Override
            public void onError(Throwable throwable) {
                topAdsListPromoViewListener.onLoadSearchAdError();
            }
        });
    }

    @Override
    public void unSubscribe() {
        if (productAdInteractor != null) {
            productAdInteractor.unSubscribe();
        }
    }
}