package com.tokopedia.seller.topads.dashboard.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.seller.base.view.model.StepperModel;

import java.util.List;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsCreatePromoNewGroupModel implements StepperModel {
    String groupName;
    TopAdsDetailGroupViewModel detailAd;
    List<TopAdsProductViewModel> topAdsProductViewModels;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public TopAdsDetailGroupViewModel getDetailAd() {
        return detailAd;
    }

    public void setDetailAd(TopAdsDetailGroupViewModel detailAd) {
        this.detailAd = detailAd;
    }

    public List<TopAdsProductViewModel> getTopAdsProductViewModels() {
        return topAdsProductViewModels;
    }

    public void setTopAdsProductViewModels(List<TopAdsProductViewModel> topAdsProductViewModels) {
        this.topAdsProductViewModels = topAdsProductViewModels;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.groupName);
        dest.writeParcelable(this.detailAd, flags);
        dest.writeTypedList(this.topAdsProductViewModels);
    }

    public TopAdsCreatePromoNewGroupModel() {
    }

    protected TopAdsCreatePromoNewGroupModel(Parcel in) {
        this.groupName = in.readString();
        this.detailAd = in.readParcelable(TopAdsDetailGroupViewModel.class.getClassLoader());
        this.topAdsProductViewModels = in.createTypedArrayList(TopAdsProductViewModel.CREATOR);
    }

    public static final Parcelable.Creator<TopAdsCreatePromoNewGroupModel> CREATOR = new Parcelable.Creator<TopAdsCreatePromoNewGroupModel>() {
        @Override
        public TopAdsCreatePromoNewGroupModel createFromParcel(Parcel source) {
            return new TopAdsCreatePromoNewGroupModel(source);
        }

        @Override
        public TopAdsCreatePromoNewGroupModel[] newArray(int size) {
            return new TopAdsCreatePromoNewGroupModel[size];
        }
    };
}
