package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.feeddetail;

import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.feeddetail.FeedPlusDetailTypeFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.feeddetail.FeedDetailViewModel;

/**
 * @author by nisie on 7/10/17.
 */

public class SingleFeedDetailViewModel extends FeedDetailViewModel{


    @Override
    public int type(FeedPlusDetailTypeFactory typeFactory) {
        return typeFactory.type(this);
    }


    public SingleFeedDetailViewModel(Integer productId, String name, String price,
                                     String imageSource, String url, String cashback,
                                     boolean isWholesale, boolean isPreorder, boolean isFreeReturn,
                                     boolean isWishlist, Double rating) {
        super(productId, name, price, imageSource, url, cashback,
                isWholesale, isPreorder, isFreeReturn, isWishlist, rating);
    }

}
