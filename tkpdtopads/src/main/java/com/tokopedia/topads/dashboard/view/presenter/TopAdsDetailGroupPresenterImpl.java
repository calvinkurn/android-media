package com.tokopedia.topads.dashboard.view.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.request.GetSuggestionBody;
import com.tokopedia.topads.dashboard.data.model.request.SearchAdRequest;
import com.tokopedia.topads.dashboard.data.model.response.GetSuggestionResponse;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.dashboard.domain.interactor.ListenerInteractor;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetDetailGroupUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetSuggestionUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGroupAdInteractor;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailGroupDomainModel;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 8/14/17.
 */

public class TopAdsDetailGroupPresenterImpl extends TopAdsDetailPresenterImpl<GroupAd> implements TopAdsDetailPresenter {

    protected final TopAdsGroupAdInteractor groupAdInteractor;
    private TopAdsGetDetailGroupUseCase topAdsGetDetailGroupUseCase;
    private TopAdsGetSuggestionUseCase getSuggestionUseCase;

    public TopAdsDetailGroupPresenterImpl(Context context, TopAdsDetailListener<GroupAd> topAdsDetailListener, TopAdsGroupAdInteractor groupAdInteractor,
                                          @Nullable TopAdsGetDetailGroupUseCase topAdsGetDetailGroupUseCase, @Nullable TopAdsGetSuggestionUseCase getSuggestionUseCase) {
        super(context, topAdsDetailListener);
        this.groupAdInteractor = groupAdInteractor;
        this.topAdsGetDetailGroupUseCase = topAdsGetDetailGroupUseCase;
        this.getSuggestionUseCase = getSuggestionUseCase;
    }

    @Override
    public void unSubscribe() {
        super.unsubscribe();
        if (groupAdInteractor != null) {
            groupAdInteractor.unSubscribe();
        }
    }

    @Override
    public void refreshAd(Date startDate, Date endDate, final String id) {
        SearchAdRequest searchAdRequest = new SearchAdRequest();
        searchAdRequest.setShopId(getShopId());
        searchAdRequest.setStartDate(startDate);
        searchAdRequest.setEndDate(endDate);
        searchAdRequest.setGroupId(id);
        groupAdInteractor.searchAd(searchAdRequest, new ListenerInteractor<PageDataResponse<List<GroupAd>>>() {
            @Override
            public void onSuccess(PageDataResponse<List<GroupAd>> pageDataResponse) {
                if (topAdsGetDetailGroupUseCase != null)
                    getDetailGroup(id, pageDataResponse.getData().get(0));
                else
                    topAdsDetailListener.onAdLoaded(pageDataResponse.getData().get(0));
            }

            @Override
            public void onError(Throwable throwable) {
                if(topAdsDetailListener != null)
                    topAdsDetailListener.onLoadAdError();
            }
        });
    }

    public void getDetailGroup(final String adId, final GroupAd groupAd){
        topAdsGetDetailGroupUseCase.execute(TopAdsGetDetailGroupUseCase.createRequestParams(adId), new Subscriber<TopAdsDetailGroupDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(topAdsDetailListener != null)
                    topAdsDetailListener.onLoadAdError();
            }

            @Override
            public void onNext(TopAdsDetailGroupDomainModel topAdsDetailGroupDomainModel) {
                if (getSuggestionUseCase != null) {
                    // add keyword total
                    groupAd.setKeywordTotal(topAdsDetailGroupDomainModel.getKeywordTotal());
                    getSuggestion(adId, groupAd);
                } else {
                    topAdsDetailListener.onAdLoaded(groupAd);
                }
            }
        });
    }

    public void getSuggestion(String groupId, final GroupAd groupAd){
        GetSuggestionBody getSuggestionBody = new GetSuggestionBody();
        getSuggestionBody.setRounding(true);
        getSuggestionBody.setSource(TopAdsNetworkConstant.SOURCE_GROUP_AD_LIST);
        getSuggestionBody.setDataType(TopAdsNetworkConstant.SUGGESTION_DATA_TYPE_DETAIL);
        getSuggestionBody.setSuggestionType(TopAdsNetworkConstant.SUGGESTION_TYPE_GROUP_ID);
        List<Long> group = new ArrayList<>();
        group.add(Long.valueOf(groupId));
        getSuggestionBody.setIds(group);

        getSuggestionUseCase.execute(TopAdsGetSuggestionUseCase.createRequestParams(getSuggestionBody), new Subscriber<GetSuggestionResponse>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {
                if(topAdsDetailListener != null)
                    topAdsDetailListener.onLoadAdError();
            }

            @Override
            public void onNext(GetSuggestionResponse getSuggestionResponse) {
                // add keyword total
                groupAd.setDatum(getSuggestionResponse.getData().get(0));
                topAdsDetailListener.onAdLoaded(groupAd);
            }
        });
    }
}
