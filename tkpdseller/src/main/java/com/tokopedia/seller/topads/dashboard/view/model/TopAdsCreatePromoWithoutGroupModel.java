package com.tokopedia.seller.topads.dashboard.view.model;

import android.os.Parcel;

import com.tokopedia.seller.base.view.model.StepperModel;

import java.util.List;

/**
 * Created by zulfikarrahman on 8/9/17.
 */

public class TopAdsCreatePromoWithoutGroupModel implements StepperModel {
    TopAdsDetailProductViewModel detailProductViewModel;
    List<TopAdsProductViewModel> productViewModels;

    public TopAdsDetailProductViewModel getDetailProductViewModel() {
        return detailProductViewModel;
    }

    public void setDetailProductViewModel(TopAdsDetailProductViewModel detailProductViewModel) {
        this.detailProductViewModel = detailProductViewModel;
    }

    public List<TopAdsProductViewModel> getProductViewModels() {
        return productViewModels;
    }

    public void setProductViewModels(List<TopAdsProductViewModel> productViewModels) {
        this.productViewModels = productViewModels;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.detailProductViewModel, flags);
        dest.writeTypedList(this.productViewModels);
    }

    public TopAdsCreatePromoWithoutGroupModel() {
    }

    protected TopAdsCreatePromoWithoutGroupModel(Parcel in) {
        this.detailProductViewModel = in.readParcelable(TopAdsDetailProductViewModel.class.getClassLoader());
        this.productViewModels = in.createTypedArrayList(TopAdsProductViewModel.CREATOR);
    }

    public static final Creator<TopAdsCreatePromoWithoutGroupModel> CREATOR = new Creator<TopAdsCreatePromoWithoutGroupModel>() {
        @Override
        public TopAdsCreatePromoWithoutGroupModel createFromParcel(Parcel source) {
            return new TopAdsCreatePromoWithoutGroupModel(source);
        }

        @Override
        public TopAdsCreatePromoWithoutGroupModel[] newArray(int size) {
            return new TopAdsCreatePromoWithoutGroupModel[size];
        }
    };
}
