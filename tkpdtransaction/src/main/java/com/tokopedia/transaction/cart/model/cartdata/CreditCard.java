
package com.tokopedia.transaction.cart.model.cartdata;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreditCard implements Parcelable {

    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("charge_idr")
    @Expose
    private String chargeIdr;
    @SerializedName("charge_25")
    @Expose
    private String charge25;
    @SerializedName("charge")
    @Expose
    private String charge;
    @SerializedName("total_idr")
    @Expose
    private String totalIdr;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getChargeIdr() {
        return chargeIdr;
    }

    public void setChargeIdr(String chargeIdr) {
        this.chargeIdr = chargeIdr;
    }

    public String getCharge25() {
        return charge25;
    }

    public void setCharge25(String charge25) {
        this.charge25 = charge25;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getTotalIdr() {
        return totalIdr;
    }

    public void setTotalIdr(String totalIdr) {
        this.totalIdr = totalIdr;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.total);
        dest.writeString(this.chargeIdr);
        dest.writeString(this.charge25);
        dest.writeString(this.charge);
        dest.writeString(this.totalIdr);
    }

    public CreditCard() {
    }

    protected CreditCard(Parcel in) {
        this.total = in.readString();
        this.chargeIdr = in.readString();
        this.charge25 = in.readString();
        this.charge = in.readString();
        this.totalIdr = in.readString();
    }

    public static final Creator<CreditCard> CREATOR = new Creator<CreditCard>() {
        @Override
        public CreditCard createFromParcel(Parcel source) {
            return new CreditCard(source);
        }

        @Override
        public CreditCard[] newArray(int size) {
            return new CreditCard[size];
        }
    };
}
