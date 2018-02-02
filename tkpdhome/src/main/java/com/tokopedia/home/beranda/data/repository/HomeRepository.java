package com.tokopedia.home.beranda.data.repository;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.entity.home.Ticker;
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

public interface HomeRepository {

    Observable<HomeCategoryResponseModel> getHomeCategorys();

    Observable<HomeCategoryResponseModel> getHomeCategorysCache();

    Observable<TopPicksResponseModel> getTopPicks(RequestParams requestParams);

    Observable<TopPicksResponseModel> getTopPicksCache();

    Observable<HomeBannerResponseModel> getBanners(RequestParams requestParams);

    Observable<HomeBannerResponseModel> getBannersCache();

    Observable<BrandsOfficialStoreResponseModel> getBrandsOfficialStore();

    Observable<BrandsOfficialStoreResponseModel> getBrandsOfficialStoreCache();

    Observable<Ticker> getTickers();

    Observable<Ticker> getTickersCache();

    Observable<List<Visitable>> getAllHomeData();


}
