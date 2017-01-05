package com.tokopedia.seller.topads.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.datasource.TopAdsCacheDataSourceImpl;
import com.tokopedia.seller.topads.datasource.TopAdsDbDataSourceImpl;
import com.tokopedia.seller.topads.interactor.ListenerInteractor;
import com.tokopedia.seller.topads.interactor.TopAdsProductAdInteractor;
import com.tokopedia.seller.topads.interactor.TopAdsProductAdInteractorImpl;
import com.tokopedia.seller.topads.model.data.GroupAd;
import com.tokopedia.seller.topads.model.data.ProductAd;
import com.tokopedia.seller.topads.model.data.ProductAdAction;
import com.tokopedia.seller.topads.model.data.ProductAdBulkAction;
import com.tokopedia.seller.topads.model.request.DataRequest;
import com.tokopedia.seller.topads.model.request.SearchAdRequest;
import com.tokopedia.seller.topads.model.response.PageDataResponse;
import com.tokopedia.seller.topads.network.apiservice.TopAdsManagementService;
import com.tokopedia.seller.topads.view.listener.TopAdsListPromoViewListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zulfikarrahman on 12/16/16.
 */
public class TopAdsProductAdListPresenterImpl extends TopAdsAdListPresenterImpl<ProductAd> implements TopAdsProductAdListPresenter {

    protected final TopAdsProductAdInteractor productAdInteractor;

    public TopAdsProductAdListPresenterImpl(Context context, TopAdsListPromoViewListener topadsListPromoViewListener) {
        super(context, topadsListPromoViewListener);
        this.productAdInteractor = new TopAdsProductAdInteractorImpl(new TopAdsManagementService(), new TopAdsDbDataSourceImpl(), new TopAdsCacheDataSourceImpl(context));
    }

    @Override
    public void searchAd(Date startDate, Date endDate, String keyword, int status, int group, int page) {
        SearchAdRequest searchAdRequest = new SearchAdRequest();
        searchAdRequest.setShopId(getShopId());
        searchAdRequest.setStartDate(startDate);
        searchAdRequest.setEndDate(endDate);
        searchAdRequest.setKeyword(keyword);
        searchAdRequest.setStatus(status);
        searchAdRequest.setGroup(group);
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
    public void turnOffAdList(List<ProductAd> adList) {
        DataRequest<ProductAdBulkAction> dataRequest = generateActionRequest(adList, TopAdsNetworkConstant.ACTION_BULK_OFF_AD);
        productAdInteractor.bulkAction(dataRequest, new ListenerInteractor<ProductAdBulkAction>() {
            @Override
            public void onSuccess(ProductAdBulkAction dataResponseActionAds) {
                topAdsListPromoViewListener.onTurnOffAdSuccess();
            }

            @Override
            public void onError(Throwable throwable) {
                topAdsListPromoViewListener.onTurnOffAdFailed();
            }
        });
    }

    @Override
    public void turnOnAddList(List<ProductAd> adList) {
        DataRequest<ProductAdBulkAction> dataRequest = generateActionRequest(adList, TopAdsNetworkConstant.ACTION_BULK_ON_AD);
        productAdInteractor.bulkAction(dataRequest, new ListenerInteractor<ProductAdBulkAction>() {
            @Override
            public void onSuccess(ProductAdBulkAction dataResponseActionAds) {
                topAdsListPromoViewListener.onTurnOnAdSuccess();
            }

            @Override
            public void onError(Throwable throwable) {
                topAdsListPromoViewListener.onTurnOnAdFailed();
            }
        });
    }

    @NonNull
    private DataRequest<ProductAdBulkAction> generateActionRequest(List<ProductAd> ads, String action) {
        DataRequest<ProductAdBulkAction> dataRequest = new DataRequest<>();
        ProductAdBulkAction dataRequestSingleAd = new ProductAdBulkAction();
        dataRequestSingleAd.setAction(action);
        dataRequestSingleAd.setShopId(SessionHandler.getShopID(context));
        List<ProductAdAction> dataRequestSingleAdses = new ArrayList<>();
        for (ProductAd ad : ads) {
            ProductAdAction data = new ProductAdAction();
            data.setId(ad.getId());
            dataRequestSingleAdses.add(data);
        }
        dataRequestSingleAd.setAds(dataRequestSingleAdses);
        dataRequest.setData(dataRequestSingleAd);
        return dataRequest;
    }

    @Override
    public void onDestroy() {
        if (productAdInteractor != null) {
            productAdInteractor.unSubscribe();
        }
    }
}
