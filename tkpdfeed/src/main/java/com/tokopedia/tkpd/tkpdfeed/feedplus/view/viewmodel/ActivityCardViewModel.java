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


    public ActivityCardViewModel() {
    }

    public ActivityCardViewModel(ArrayList<ProductFeedViewModel> listProduct) {
        this.productCardHeaderViewModel = new ProductCardHeaderViewModel();
        this.shareUrl = "https://tokopedia.com";
        this.actionText = "Mengubah 1 produk";
        this.listProduct = listProduct;
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

}
