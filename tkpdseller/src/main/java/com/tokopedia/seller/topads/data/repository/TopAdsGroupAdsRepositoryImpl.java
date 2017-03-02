package com.tokopedia.seller.topads.data.repository;

import com.tokopedia.seller.topads.data.factory.TopAdsGroupAdFactory;
import com.tokopedia.seller.topads.data.model.request.CreateGroupRequest;
import com.tokopedia.seller.topads.data.model.response.DataResponseCreateGroup;
import com.tokopedia.seller.topads.data.source.cloud.TopAdsGroupAdsDataSource;
import com.tokopedia.seller.topads.domain.TopAdsGroupAdsRepository;
import com.tokopedia.seller.topads.domain.model.data.GroupAd;
import com.tokopedia.seller.topads.domain.model.response.DataResponse;

import java.util.List;

import rx.Observable;

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
}
