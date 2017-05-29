package com.tokopedia.tkpd.tkpdfeed.feedplus.view;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.FeedDetailHeaderViewModel;

import java.util.ArrayList;

/**
 * @author by nisie on 5/18/17.
 */

public interface FeedPlusDetail {

    public interface View extends CustomerView {

        void onWishlistClicked();

        void onGoToShopDetail(Integer shopId);

        void onErrorGetFeedDetail(String errorMessage);

        void onSuccessGetFeedDetail(FeedDetailHeaderViewModel header,
                                    ArrayList<Visitable> listDetail,
                                    boolean hasNextPage);

        void showLoading();

        void showLoadingProgress();
    }

    public interface Presenter extends CustomerPresenter<View> {
    }
}
