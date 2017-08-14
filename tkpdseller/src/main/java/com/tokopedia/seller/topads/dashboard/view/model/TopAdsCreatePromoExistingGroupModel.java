package com.tokopedia.seller.topads.dashboard.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.seller.base.view.model.StepperModel;

import java.util.List;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsCreatePromoExistingGroupModel implements StepperModel {
    String groupId;
    List<TopAdsProductViewModel> topAdsProductViewModels;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<TopAdsProductViewModel> getTopAdsProductViewModels() {
        return topAdsProductViewModels;
    }

    public void setTopAdsProductViewModels(List<TopAdsProductViewModel> topAdsProductViewModels) {
        this.topAdsProductViewModels = topAdsProductViewModels;
    }

    public static Creator<TopAdsCreatePromoExistingGroupModel> getCREATOR() {
        return CREATOR;
    }

    public TopAdsCreatePromoExistingGroupModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.groupId);
        dest.writeTypedList(this.topAdsProductViewModels);
    }

    protected TopAdsCreatePromoExistingGroupModel(Parcel in) {
        this.groupId = in.readString();
        this.topAdsProductViewModels = in.createTypedArrayList(TopAdsProductViewModel.CREATOR);
    }

    public static final Creator<TopAdsCreatePromoExistingGroupModel> CREATOR = new Creator<TopAdsCreatePromoExistingGroupModel>() {
        @Override
        public TopAdsCreatePromoExistingGroupModel createFromParcel(Parcel source) {
            return new TopAdsCreatePromoExistingGroupModel(source);
        }

        @Override
        public TopAdsCreatePromoExistingGroupModel[] newArray(int size) {
            return new TopAdsCreatePromoExistingGroupModel[size];
        }
    };
}
