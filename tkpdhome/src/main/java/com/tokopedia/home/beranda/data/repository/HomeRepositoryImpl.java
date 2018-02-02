package com.tokopedia.home.beranda.data.repository;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.entity.home.Ticker;
import com.tokopedia.home.beranda.data.source.BrandsOfficialStoreDataSource;
import com.tokopedia.home.beranda.data.source.HomeBannerDataSource;
import com.tokopedia.home.beranda.data.source.HomeCategoryDataSource;
import com.tokopedia.home.beranda.data.source.HomeDataSource;
import com.tokopedia.home.beranda.data.source.TickerDataSource;
import com.tokopedia.home.beranda.data.source.TopPicksDataSource;
import com.tokopedia.home.beranda.data.source.pojo.HomeData;
import com.tokopedia.home.beranda.domain.model.banner.HomeBannerResponseModel;
import com.tokopedia.home.beranda.domain.model.brands.BrandsOfficialStoreResponseModel;
import com.tokopedia.home.beranda.domain.model.category.HomeCategoryResponseModel;
import com.tokopedia.home.beranda.domain.model.toppicks.TopPicksResponseModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public class HomeRepositoryImpl implements HomeRepository {

    private final HomeCategoryDataSource categoryDataSource;
    private final HomeBannerDataSource homeBannerDataSource;
    private final BrandsOfficialStoreDataSource brandsOfficialStoreDataSource;
    private final TopPicksDataSource topPicksDataSource;
    private final TickerDataSource tickerDataSource;
    private final HomeDataSource homeDataSource;


    public HomeRepositoryImpl(HomeCategoryDataSource categoryDataSource, HomeBannerDataSource homeBannerDataSource,
                              BrandsOfficialStoreDataSource brandsOfficialStoreDataSource,
                              TopPicksDataSource topPicksDataSource,
                              TickerDataSource tickerDataSource,
                              HomeDataSource homeDataSource) {
        this.categoryDataSource = categoryDataSource;
        this.homeBannerDataSource = homeBannerDataSource;
        this.brandsOfficialStoreDataSource = brandsOfficialStoreDataSource;
        this.topPicksDataSource = topPicksDataSource;
        this.tickerDataSource = tickerDataSource;
        this.homeDataSource = homeDataSource;
    }

    @Override
    public Observable<HomeCategoryResponseModel> getHomeCategorys() {
        return categoryDataSource.getHomeCategory();
    }

    @Override
    public Observable<TopPicksResponseModel> getTopPicks(RequestParams requestParams) {
        return topPicksDataSource.getTopPicks(requestParams);
    }

    @Override
    public Observable<HomeBannerResponseModel> getBanners(RequestParams requestParams) {
        return homeBannerDataSource.getHomeBanner(requestParams);
    }

    @Override
    public Observable<BrandsOfficialStoreResponseModel> getBrandsOfficialStore() {
        return brandsOfficialStoreDataSource.getBrandsOfficialStore();
    }

    @Override
    public Observable<Ticker> getTickers() {
        return tickerDataSource.getTicker();
    }

    @Override
    public Observable<HomeCategoryResponseModel> getHomeCategorysCache() {
        return categoryDataSource.getCache();
    }

    @Override
    public Observable<TopPicksResponseModel> getTopPicksCache() {
        return topPicksDataSource.getCache();
    }

    @Override
    public Observable<HomeBannerResponseModel> getBannersCache() {
        return homeBannerDataSource.getCache();
    }

    @Override
    public Observable<BrandsOfficialStoreResponseModel> getBrandsOfficialStoreCache() {
        return brandsOfficialStoreDataSource.getCache();
    }

    @Override
    public Observable<Ticker> getTickersCache() {
        return tickerDataSource.getCache();
    }

    @Override
    public Observable<List<Visitable>> getAllHomeData() {
        return homeDataSource.getHomeData();
    }

}
