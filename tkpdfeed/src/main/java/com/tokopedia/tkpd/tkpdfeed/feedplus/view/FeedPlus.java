package com.tokopedia.tkpd.tkpdfeed.feedplus.view;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ActivityCardViewModel;

/**
 * @author by nisie on 5/15/17.
 */

public interface FeedPlus{

    public interface View extends CustomerView {
        void onShareButtonClicked();

        void onGoToProductDetail();

        void onGoToFeedDetail(ActivityCardViewModel activityCardViewModel);

        void onGoToShopDetail();

        void onCopyClicked(String s);

        void onGoToBlogWebView(String url);

        void onOpenVideo(String videoUrl);
    }

    public interface Presenter extends CustomerPresenter<View>{

    }
}
