package com.tokopedia.topads.dashboard.view.model;

import android.os.Parcel;

/**
 * Created by zulfikarrahman on 8/9/17.
 */

public class TopAdsCreatePromoWithoutGroupModel extends TopAdsProductListStepperModel {
    TopAdsDetailProductViewModel detailProductViewModel;

    public TopAdsDetailProductViewModel getDetailProductViewModel() {
        return detailProductViewModel;
    }

    public TopAdsCreatePromoWithoutGroupModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.detailProductViewModel, flags);
    }

    protected TopAdsCreatePromoWithoutGroupModel(Parcel in) {
        super(in);
        this.detailProductViewModel = in.readParcelable(TopAdsDetailProductViewModel.class.getClassLoader());
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

    public void setDetailGroupCostViewModel(TopAdsDetailGroupViewModel detailProductCostViewModel) {
        if(detailProductViewModel == null) {
            this.detailProductViewModel = detailProductCostViewModel;
        }else if(detailProductCostViewModel != null){
            detailProductViewModel.setPriceBid(detailProductCostViewModel.getPriceBid());
            detailProductViewModel.setPriceDaily(detailProductCostViewModel.getPriceDaily());
            detailProductViewModel.setBudget(detailProductCostViewModel.isBudget());
            detailProductViewModel.setSuggestionBidValue(detailProductCostViewModel.getSuggestionBidValue());
            detailProductViewModel.setSuggestionBidButton(detailProductCostViewModel.getSuggestionBidButton());
        }
    }
}
