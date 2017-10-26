package com.tokopedia.topads.dashboard.view.presenter;

import android.content.Context;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.product.edit.domain.ShopInfoRepository;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
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

    public TopAdsGroupAdListPresenterImpl(Context context, BaseListViewListener baseListViewListener, TopAdsGetSuggestionUseCase getSuggestionUseCase) {
        super(context, baseListViewListener);
        this.groupAdInteractor = new TopAdsGroupAdInteractorImpl(context);
        this.getSuggestionUseCase = getSuggestionUseCase;
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
                getSuggestionBody.setSource(TopAdsNetworkConstant.SOURCE_GROUP_AD_LIST);
                getSuggestionBody.setDataType(TopAdsNetworkConstant.SUGGESTION_DATA_TYPE_DETAIL);
                getSuggestionBody.setSuggestionType(TopAdsNetworkConstant.SUGGESTION_TYPE_GROUP_ID);
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
