package com.tokopedia.seller.shop.open.view;

import android.os.Parcel;

import com.tokopedia.seller.base.view.model.StepperModel;

/**
 * Created by normansyahputa on 12/19/17.
 */

public class ShopOpenStepperModel implements StepperModel{

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public ShopOpenStepperModel() {
    }

    protected ShopOpenStepperModel(Parcel in) {
    }

    public static final Creator<ShopOpenStepperModel> CREATOR = new Creator<ShopOpenStepperModel>() {
        @Override
        public ShopOpenStepperModel createFromParcel(Parcel source) {
            return new ShopOpenStepperModel(source);
        }

        @Override
        public ShopOpenStepperModel[] newArray(int size) {
            return new ShopOpenStepperModel[size];
        }
    };
}
