package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.FeedPlusTypeFactory;

import java.util.ArrayList;

/**
 * @author by nisie on 5/15/17.
 */

public class ActivityCardViewModel extends ProductCardViewModel implements Parcelable {

    private ProductCardHeaderViewModel productCardHeaderViewModel;
    private String shareUrl;

    public ActivityCardViewModel() {
    }

    public ActivityCardViewModel(ArrayList<ProductFeedViewModel> listProduct) {
        this.productCardHeaderViewModel = new ProductCardHeaderViewModel();
        this.shareUrl = "https://tokopedia.com";
        this.listProduct = listProduct;
    }

    protected ActivityCardViewModel(Parcel in) {
        productCardHeaderViewModel = in.readParcelable(ProductCardHeaderViewModel.class.getClassLoader());
        shareUrl = in.readString();
    }

    public static final Creator<ActivityCardViewModel> CREATOR = new Creator<ActivityCardViewModel>() {
        @Override
        public ActivityCardViewModel createFromParcel(Parcel in) {
            return new ActivityCardViewModel(in);
        }

        @Override
        public ActivityCardViewModel[] newArray(int size) {
            return new ActivityCardViewModel[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(productCardHeaderViewModel, flags);
        dest.writeString(shareUrl);
    }
}
