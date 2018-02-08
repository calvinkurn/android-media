package com.tokopedia.seller.product.etalase.view.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.seller.R;

/**
 * @author sebastianuskh on 4/5/17.
 */

public class MyEtalaseItemViewModel implements EtalaseViewModel, Parcelable {
    public static final int LAYOUT = R.layout.item_product_etalase_picker;
    private Integer etalaseId;
    private String etalaseName;

    public void setEtalaseId(Integer etalaseId) {
        this.etalaseId = etalaseId;
    }

    public void setEtalaseName(String etalaseName) {
        this.etalaseName = etalaseName;
    }

    public Integer getEtalaseId() {
        return etalaseId;
    }

    public String getEtalaseName() {
        return etalaseName;
    }

    @Override
    public int getType() {
        return LAYOUT;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.etalaseId);
        dest.writeString(this.etalaseName);
    }

    public MyEtalaseItemViewModel(Integer etalaseId, String etalaseName) {
        this.etalaseId = etalaseId;
        this.etalaseName = etalaseName;
    }

    public MyEtalaseItemViewModel() {
    }

    protected MyEtalaseItemViewModel(Parcel in) {
        this.etalaseId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.etalaseName = in.readString();
    }

    public static final Parcelable.Creator<MyEtalaseItemViewModel> CREATOR = new Parcelable.Creator<MyEtalaseItemViewModel>() {
        @Override
        public MyEtalaseItemViewModel createFromParcel(Parcel source) {
            return new MyEtalaseItemViewModel(source);
        }

        @Override
        public MyEtalaseItemViewModel[] newArray(int size) {
            return new MyEtalaseItemViewModel[size];
        }
    };
}
