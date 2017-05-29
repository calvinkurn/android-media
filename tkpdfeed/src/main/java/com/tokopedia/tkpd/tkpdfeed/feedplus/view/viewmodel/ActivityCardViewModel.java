package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.FeedPlusTypeFactory;

import java.util.ArrayList;

/**
 * @author by nisie on 5/15/17.
 */

public class ActivityCardViewModel extends ProductCardViewModel {

    private ProductCardHeaderViewModel productCardHeaderViewModel;
    private String shareUrl;
    private String actionText;
    private String feedId;
    private String shareLinkDescription;

    public ActivityCardViewModel(ProductCardHeaderViewModel productCardHeaderViewModel,
                                 ArrayList<ProductFeedViewModel> listProduct,
                                 String shareUrl,
                                 String shareLinkDescription,
                                 String actionText,
                                 String feedId) {
        this.listProduct = listProduct;
        this.productCardHeaderViewModel = productCardHeaderViewModel;
        this.shareUrl = shareUrl;
        this.shareLinkDescription = shareLinkDescription;
        this.actionText = actionText;
        this.feedId = feedId;
    }

    @Override
    public int type(FeedPlusTypeFactory typeFactory) {
        return typeFactory.type(this);
    }


    public ProductCardHeaderViewModel getHeader() {
        return productCardHeaderViewModel;
    }

    public void setHeader(ProductCardHeaderViewModel productCardHeaderViewModel) {
        this.productCardHeaderViewModel = productCardHeaderViewModel;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getActionText() {
        return actionText;
    }

    public void setActionText(String actionText) {
        this.actionText = actionText;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public ProductCardHeaderViewModel getProductCardHeaderViewModel() {
        return productCardHeaderViewModel;
    }

    public void setProductCardHeaderViewModel(ProductCardHeaderViewModel productCardHeaderViewModel) {
        this.productCardHeaderViewModel = productCardHeaderViewModel;
    }

    public String getShareLinkDescription() {
        return shareLinkDescription;
    }

    public void setShareLinkDescription(String shareLinkDescription) {
        this.shareLinkDescription = shareLinkDescription;
    }
}
