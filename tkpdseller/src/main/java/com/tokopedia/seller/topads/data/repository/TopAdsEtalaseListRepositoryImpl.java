package com.tokopedia.seller.topads.data.repository;

import com.tokopedia.seller.topads.data.factory.TopAdsEtalaseFactory;
import com.tokopedia.seller.topads.data.source.TopAdsEtalaseDataSource;
import com.tokopedia.seller.topads.domain.TopAdsEtalaseListRepository;
import com.tokopedia.seller.topads.data.model.data.Etalase;

import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 2/20/17.
 */

public class TopAdsEtalaseListRepositoryImpl implements TopAdsEtalaseListRepository {

    private final TopAdsEtalaseFactory topAdsEtalaseFactory;

    public TopAdsEtalaseListRepositoryImpl(TopAdsEtalaseFactory topAdsEtalaseFactory) {
        this.topAdsEtalaseFactory = topAdsEtalaseFactory;
    }

    @Override
    public Observable<List<Etalase>> getEtalaseList(String shopId) {
        TopAdsEtalaseDataSource topAdsEtalaseDataSource =
                topAdsEtalaseFactory.createEtalaseDataSource();
        return topAdsEtalaseDataSource.getEtalaseList(shopId);
    }

}
