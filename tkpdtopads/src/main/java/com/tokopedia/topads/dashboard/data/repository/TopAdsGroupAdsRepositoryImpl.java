package com.tokopedia.topads.dashboard.data.repository;

import com.tokopedia.topads.dashboard.data.factory.TopAdsGroupAdFactory;
import com.tokopedia.topads.dashboard.data.model.request.CreateGroupRequest;
import com.tokopedia.topads.dashboard.data.model.request.GetSuggestionBody;
import com.tokopedia.topads.dashboard.data.model.response.DataResponseCreateGroup;
import com.tokopedia.topads.dashboard.data.model.response.GetSuggestionResponse;
import com.tokopedia.topads.dashboard.data.source.cloud.TopAdsGroupAdsDataSource;
import com.tokopedia.topads.dashboard.domain.TopAdsGroupAdsRepository;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailGroupDomainModel;

import java.util.List;

import rx.Observable;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;

/**
 * Created by zulfikarrahman on 2/20/17.
 */

public class TopAdsGroupAdsRepositoryImpl implements TopAdsGroupAdsRepository {

    private final TopAdsGroupAdFactory topAdsGroupAdFactory;

    public TopAdsGroupAdsRepositoryImpl(TopAdsGroupAdFactory topAdsGroupAdFactory) {
        this.topAdsGroupAdFactory = topAdsGroupAdFactory;
    }

    @Override
    public Observable<List<GroupAd>> searchGroupAds(String keyword) {
        TopAdsGroupAdsDataSource topAdsGroupAdsDataSource =
                topAdsGroupAdFactory.createGroupAdsDataSource();
        return topAdsGroupAdsDataSource.searchGroupAds(keyword);
    }

    @Override
    public Observable<DataResponseCreateGroup> createGroup(CreateGroupRequest createGroupRequest) {
        TopAdsGroupAdsDataSource topAdsGroupAdsDataSource =
                topAdsGroupAdFactory.createGroupAdsDataSource();
        return topAdsGroupAdsDataSource.createGroup(createGroupRequest);
    }

    @Override
    public Observable<TopAdsDetailGroupDomainModel> getDetailGroup(String groupId) {
        TopAdsGroupAdsDataSource topAdsGroupAdsDataSource =
                topAdsGroupAdFactory.createGroupAdsDataSource();
        return topAdsGroupAdsDataSource.getDetailGroup(groupId);
    }

    @Override
    public Observable<TopAdsDetailGroupDomainModel> saveDetailGroup(TopAdsDetailGroupDomainModel topAdsDetailGroupDomainModel) {
        TopAdsGroupAdsDataSource topAdsGroupAdsDataSource = topAdsGroupAdFactory.createGroupAdsDataSource();
        return topAdsGroupAdsDataSource.saveDetailGroup(topAdsDetailGroupDomainModel);
    }

    @Override
    public Observable<GetSuggestionResponse> getSuggestion(GetSuggestionBody getSuggestionBody, String shopId) {
        TopAdsGroupAdsDataSource topAdsGroupAdsDataSource = topAdsGroupAdFactory.createGroupAdsDataSource();
        return topAdsGroupAdsDataSource.getSuggestion(getSuggestionBody, shopId);
    }
}
