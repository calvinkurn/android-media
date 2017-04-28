package com.tokopedia.discovery.intermediary.view;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.discovery.intermediary.domain.model.ChildCategoryModel;
import com.tokopedia.discovery.intermediary.domain.model.CuratedSectionModel;
import com.tokopedia.discovery.intermediary.domain.model.HeaderModel;
import com.tokopedia.discovery.intermediary.domain.model.HotListModel;

import java.util.List;

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

        void showLoading();

        void hideLoading();

        void emptyState();

        void skipIntermediaryPage();

        void backToTop();

    }

    interface Presenter extends CustomerPresenter<View> {

        void getIntermediaryCategory(String categoryId);

        void addFavoriteShop(String categoryId);
    }
}
