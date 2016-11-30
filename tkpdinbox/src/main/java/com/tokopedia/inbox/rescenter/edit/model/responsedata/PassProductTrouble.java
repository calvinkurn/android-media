package com.tokopedia.inbox.rescenter.edit.model.responsedata;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.inbox.rescenter.edit.model.passdata.EditResCenterFormData;
import com.tokopedia.inbox.rescenter.edit.model.passdata.ProductData;

/**
 * Created on 8/1/16.
 */
public class PassProductTrouble implements Parcelable {

    private ProductData productData;
    private EditResCenterFormData.TroubleData troubleData;
    private Integer inputQuantity;
    private String inputDescription;

    public ProductData getProductData() {
        return productData;
    }

    public void setProductData(ProductData productData) {
        this.productData = productData;
    }

    public EditResCenterFormData.TroubleData getTroubleData() {
        return troubleData;
    }

    public void setTroubleData(EditResCenterFormData.TroubleData troubleData) {
        this.troubleData = troubleData;
    }

    public Integer getInputQuantity() {
        return inputQuantity;
    }

    public void setInputQuantity(Integer inputQuantity) {
        this.inputQuantity = inputQuantity;
    }

    public String getInputDescription() {
        return inputDescription;
    }

    public void setInputDescription(String inputDescription) {
        this.inputDescription = inputDescription;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.productData, flags);
        dest.writeParcelable(this.troubleData, flags);
        dest.writeValue(this.inputQuantity);
        dest.writeString(this.inputDescription);
    }

    public PassProductTrouble() {
    }

    protected PassProductTrouble(Parcel in) {
        this.productData = in.readParcelable(ProductData.class.getClassLoader());
        this.troubleData = in.readParcelable(EditResCenterFormData.TroubleData.class.getClassLoader());
        this.inputQuantity = (Integer) in.readValue(Integer.class.getClassLoader());
        this.inputDescription = in.readString();
    }

    public static final Creator<PassProductTrouble> CREATOR = new Creator<PassProductTrouble>() {
        @Override
        public PassProductTrouble createFromParcel(Parcel source) {
            return new PassProductTrouble(source);
        }

        @Override
        public PassProductTrouble[] newArray(int size) {
            return new PassProductTrouble[size];
        }
    };
}
