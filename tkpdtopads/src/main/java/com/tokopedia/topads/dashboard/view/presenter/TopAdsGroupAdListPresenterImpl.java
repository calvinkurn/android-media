package com.tokopedia.topads.dashboard.view.presenter;

import android.content.Context;

import com.tokopedia.topads.common.view.listener.TopAdsSuggestionViewListener;
import com.tokopedia.topads.dashboard.data.model.request.GetSuggestionBody;
import com.tokopedia.topads.dashboard.data.model.response.GetSuggestionResponse;
import com.tokopedia.topads.dashboard.domain.interactor.ListenerInteractor;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetSuggestionUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGroupAdInteractor;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGroupAdInteractorImpl;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.request.SearchAdRequest;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.seller.base.view.listener.BaseListViewListener;

import java.util.Date;
import java.util.List;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 12/22/16.
 */

public class TopAdsGroupAdListPresenterImpl extends TopAdsAdListPresenterImpl<GroupAd> implements TopAdsGroupAdListPresenter {

    protected final TopAdsGroupAdInteractor groupAdInteractor;
    private TopAdsGetSuggestionUseCase getSuggestionUseCase;
    private String shopID;

    public TopAdsGroupAdListPresenterImpl(Context context, TopAdsSuggestionViewListener baseListViewListener, TopAdsGetSuggestionUseCase getSuggestionUseCase, String shopID) {
        super(context, baseListViewListener);
        this.groupAdInteractor = new TopAdsGroupAdInteractorImpl(context);
        this.getSuggestionUseCase = getSuggestionUseCase;
        this.shopID = shopID;
    }

    @Override
    public void searchAd(Date startDate, Date endDate, String keyword, int status, final int page) {
        SearchAdRequest searchAdRequest = new SearchAdRequest();
        searchAdRequest.setShopId(getShopId());
        searchAdRequest.setStartDate(startDate);
        searchAdRequest.setEndDate(endDate);
        searchAdRequest.setKeyword(keyword);
        searchAdRequest.setStatus(status);
        searchAdRequest.setPage(page);
        groupAdInteractor.searchAd(searchAdRequest, new ListenerInteractor<PageDataResponse<List<GroupAd>>>() {
            @Override
            public void onSuccess(final PageDataResponse<List<GroupAd>> pageDataResponse) {

                GetSuggestionBody getSuggestionBody = new GetSuggestionBody();
                getSuggestionBody.setRounding(true);
                getSuggestionBody.setShopId(Long.valueOf(shopID));
                getSuggestionBody.setSource("top_ads_new_cost_without_group");
                getSuggestionBody.setDataType("detail");
                getSuggestionBody.setSuggestionType("group_bid");
                for (GroupAd groupAd : pageDataResponse.getData()) {
                    getSuggestionBody.addId(groupAd.getId());
                }

                getSuggestionUseCase.execute(TopAdsGetSuggestionUseCase.createRequestParams(getSuggestionBody), new Subscriber<GetSuggestionResponse>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        baseListViewListener.onLoadSearchError(e);
                    }

                    @Override
                    public void onNext(GetSuggestionResponse getSuggestionResponse) {
                        List<GroupAd> data = pageDataResponse.getData();
                        for (int i=0;i< getSuggestionResponse.getData().size();i++) {
                            data.get(i).setDatum(getSuggestionResponse.getData().get(i));
                        }
                        baseListViewListener.onSearchLoaded(data, pageDataResponse.getPage().getTotal());
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {
                baseListViewListener.onLoadSearchError(throwable);
            }
        });
    }


    @Override
    public void unSubscribe() {
        if (groupAdInteractor != null) {
            groupAdInteractor.unSubscribe();
        }
    }
}
