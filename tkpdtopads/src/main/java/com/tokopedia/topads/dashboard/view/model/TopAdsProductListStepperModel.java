package com.tokopedia.topads.dashboard.view.model;

import android.os.Parcel;

import com.tokopedia.seller.base.view.model.StepperModel;

import java.util.List;

/**
 * Created by zulfikarrahman on 8/15/17.
 */

public class TopAdsProductListStepperModel implements StepperModel {

    List<TopAdsProductViewModel> topAdsProductViewModels;
    private String idToAdd;
    private String source;

    public String getIdToAdd() {
        return idToAdd;
    }

    public List<TopAdsProductViewModel> getTopAdsProductViewModels() {
        return topAdsProductViewModels;
    }

    public void setTopAdsProductViewModels(List<TopAdsProductViewModel> topAdsProductViewModels) {
        this.topAdsProductViewModels = topAdsProductViewModels;
    }

    public TopAdsProductListStepperModel() {
    }

    public void setIdToAdd(String idToAdd) {
        this.idToAdd = idToAdd;
    }



    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.topAdsProductViewModels);
        dest.writeString(this.idToAdd);
        dest.writeString(this.source);
    }

    protected TopAdsProductListStepperModel(Parcel in) {
        this.topAdsProductViewModels = in.createTypedArrayList(TopAdsProductViewModel.CREATOR);
        this.idToAdd = in.readString();
        this.source = in.readString();
    }

    public static final Creator<TopAdsProductListStepperModel> CREATOR = new Creator<TopAdsProductListStepperModel>() {
        @Override
        public TopAdsProductListStepperModel createFromParcel(Parcel source) {
            return new TopAdsProductListStepperModel(source);
        }

        @Override
        public TopAdsProductListStepperModel[] newArray(int size) {
            return new TopAdsProductListStepperModel[size];
        }
    };
}
