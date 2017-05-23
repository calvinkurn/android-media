package com.tokopedia.tkpd.tkpdfeed.feedplus.view;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ActivityCardViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.ProductFeedViewModel;

/**
 * @author by nisie on 5/15/17.
 */

public interface FeedPlus{

    public interface View extends CustomerView {
        void onShareButtonClicked( String shareUrl,
                                   String title,
                                   String imgUrl,
                                   String contentMessage);

        void onGoToProductDetail();

        void onGoToFeedDetail(ActivityCardViewModel activityCardViewModel);

        void onGoToShopDetail();

        void onCopyClicked(String s);

        void onGoToBlogWebView(String url);

        void onOpenVideo(String videoUrl, String subtitle);

        void onGoToBuyProduct(ProductFeedViewModel productFeedViewModel);

    }

    public interface Presenter extends CustomerPresenter<View>{

    }
}
