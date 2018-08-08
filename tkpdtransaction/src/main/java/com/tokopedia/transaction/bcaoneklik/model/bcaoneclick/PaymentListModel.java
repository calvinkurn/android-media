package com.tokopedia.transaction.bcaoneklik.model.bcaoneclick;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kris on 8/2/17. Tokopedia
 */

public class PaymentListModel implements Parcelable {

    @SerializedName("bca_oneklik_data")
    private List<BcaOneClickUserModel> bcaOneClickUserModels;

    public PaymentListModel() {
    }

    protected PaymentListModel(Parcel in) {
        bcaOneClickUserModels = in.createTypedArrayList(BcaOneClickUserModel.CREATOR);
    }

    public static final Creator<PaymentListModel> CREATOR = new Creator<PaymentListModel>() {
        @Override
        public PaymentListModel createFromParcel(Parcel in) {
            return new PaymentListModel(in);
        }

        @Override
        public PaymentListModel[] newArray(int size) {
            return new PaymentListModel[size];
        }
    };

    public List<BcaOneClickUserModel> getBcaOneClickUserModels() {
        return bcaOneClickUserModels;
    }

    public void setBcaOneClickUserModels(List<BcaOneClickUserModel> bcaOneClickUserModels) {
        this.bcaOneClickUserModels = bcaOneClickUserModels;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(bcaOneClickUserModels);
    }
}
