package com.tokopedia.seller.topads.dashboard.view.model;

import android.os.Parcel;

import com.tokopedia.seller.base.view.model.StepperModel;

/**
 * Created by zulfikarrahman on 8/9/17.
 */

public class TopAdsCreatePromoShopModel implements StepperModel {
    TopAdsDetailShopViewModel topAdsDetailShopViewModel;

    public TopAdsDetailShopViewModel getTopAdsDetailShopViewModel() {
        return topAdsDetailShopViewModel;
    }

    public void setTopAdsDetailShopViewModel(TopAdsDetailShopViewModel topAdsDetailShopViewModel) {
        this.topAdsDetailShopViewModel = topAdsDetailShopViewModel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.topAdsDetailShopViewModel, flags);
    }

    public TopAdsCreatePromoShopModel() {
    }

    protected TopAdsCreatePromoShopModel(Parcel in) {
        this.topAdsDetailShopViewModel = in.readParcelable(TopAdsDetailShopViewModel.class.getClassLoader());
    }

    public static final Creator<TopAdsCreatePromoShopModel> CREATOR = new Creator<TopAdsCreatePromoShopModel>() {
        @Override
        public TopAdsCreatePromoShopModel createFromParcel(Parcel source) {
            return new TopAdsCreatePromoShopModel(source);
        }

        @Override
        public TopAdsCreatePromoShopModel[] newArray(int size) {
            return new TopAdsCreatePromoShopModel[size];
        }
    };
}
