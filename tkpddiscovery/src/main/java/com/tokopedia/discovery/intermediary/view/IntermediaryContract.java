package com.tokopedia.discovery.intermediary.view;

import android.os.Bundle;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.network.entity.intermediary.CategoryHadesModel;
import com.tokopedia.discovery.intermediary.domain.model.BannerModel;
import com.tokopedia.discovery.intermediary.domain.model.BrandModel;
import com.tokopedia.discovery.intermediary.domain.model.ChildCategoryModel;
import com.tokopedia.discovery.intermediary.domain.model.CuratedSectionModel;
import com.tokopedia.discovery.intermediary.domain.model.HeaderModel;
import com.tokopedia.discovery.intermediary.domain.model.HotListModel;
import com.tokopedia.discovery.intermediary.domain.model.VideoModel;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.wishlist.common.listener.WishListActionListener;

import java.util.List;
import java.util.Map;

/**
 * Created by alifa on 3/24/17.
 */

public interface IntermediaryContract {

    interface View extends CustomerView {

        void renderHeader(HeaderModel headerModel);

        void renderTopAds();

        void renderCategoryChildren(List<ChildCategoryModel> childCategoryModelList);

        void renderCuratedProducts(List<CuratedSectionModel> curatedSectionModelList);

        void renderHotList(List<HotListModel> hotListModelList);

        void renderBanner(List<BannerModel> bannerModelList);

        void renderVideo(VideoModel videoModel);

        void renderBrands(List<BrandModel> brandModels);

        void showLoading();

        void hideLoading();

        void emptyState();

        void skipIntermediaryPage(CategoryHadesModel domainModel);

        void skipIntermediaryPage();

        void backToTop();

        void updateDepartementId(String id);

        void trackEventEnhance(Map<String, Object> dataLayer);

        String getTrackerAttribution();

        boolean isUserHasLogin();

        void launchLoginActivity(Bundle extras);

        String getUserId();

        void stopFirebaseTrace();

    }

    interface Presenter extends CustomerPresenter<View> {

        void getIntermediaryCategory(String categoryId);

        void addFavoriteShop(String categoryId);

        void addWishLish(int position, Data data);

        void setWishlishListener(WishListActionListener wishListActionListener);
    }
}
