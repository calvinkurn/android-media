package com.tokopedia.transaction.cart.model.paramcheckout;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author anggaprasetiyo on 11/4/16.
 */

public class CheckoutData implements Parcelable {

    private String lpFlag = "1";
    private String depositAmount;
    private String dropShipString;
    private String step;
    private String gateway;
    private String token;
    private String partialString;
    private String usedDeposit;
    private List<CheckoutDropShipperData> dropShipperDataList;
    private String voucherCode;

    private CheckoutData(Builder builder) {
        setLpFlag(builder.lpFlag);
        setDepositAmount(builder.depositAmount);
        setDropShipString(builder.dropShipString);
        setStep(builder.step);
        setGateway(builder.gateway);
        setToken(builder.token);
        setPartialString(builder.partialString);
        setUsedDeposit(builder.usedDeposit);
        setDropShipperDataList(builder.dropShipperDataList);
        setVoucherCode(builder.voucherCode);
    }

    public List<CheckoutDropShipperData> getDropShipperDataList() {
        return dropShipperDataList;
    }

    public CheckoutData() {
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public void setDropShipperDataList(List<CheckoutDropShipperData> dropShipperDataList) {
        this.dropShipperDataList = dropShipperDataList;
    }

    public String getLpFlag() {
        return lpFlag;
    }

    public void setLpFlag(String lpFlag) {
        this.lpFlag = lpFlag;
    }

    public String getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(String depositAmount) {
        this.depositAmount = depositAmount;
    }

    public String getDropShipString() {
        return dropShipString;
    }

    public void setDropShipString(String dropShipString) {
        this.dropShipString = dropShipString;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPartialString() {
        return partialString;
    }

    public void setPartialString(String partialString) {
        this.partialString = partialString;
    }

    public String getUsedDeposit() {
        return usedDeposit;
    }

    public void setUsedDeposit(String usedDeposit) {
        this.usedDeposit = usedDeposit;
    }


    public static final class Builder {
        private String lpFlag = "1";
        private String depositAmount;
        private String dropShipString;
        private String step;
        private String gateway;
        private String token;
        private String partialString;
        private String usedDeposit;
        private List<CheckoutDropShipperData> dropShipperDataList;
        private String voucherCode;

        public Builder() {
        }

        public Builder lpFlag(String val) {
            lpFlag = val;
            return this;
        }

        public Builder depositAmount(String val) {
            depositAmount = val;
            return this;
        }

        public Builder dropShipString(String val) {
            dropShipString = val;
            return this;
        }

        public Builder step(String val) {
            step = val;
            return this;
        }

        public Builder gateway(String val) {
            gateway = val;
            return this;
        }

        public Builder token(String val) {
            token = val;
            return this;
        }

        public Builder partialString(String val) {
            partialString = val;
            return this;
        }

        public Builder usedDeposit(String val) {
            usedDeposit = val;
            return this;
        }

        public Builder dropShipperDataList(List<CheckoutDropShipperData> val) {
            dropShipperDataList = val;
            return this;
        }

        public Builder voucherCode(String val) {
            voucherCode = val;
            return this;
        }

        public CheckoutData build() {
            return new CheckoutData(this);
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.lpFlag);
        dest.writeString(this.depositAmount);
        dest.writeString(this.dropShipString);
        dest.writeString(this.step);
        dest.writeString(this.gateway);
        dest.writeString(this.token);
        dest.writeString(this.partialString);
        dest.writeString(this.usedDeposit);
        dest.writeTypedList(this.dropShipperDataList);
        dest.writeString(this.voucherCode);
    }

    protected CheckoutData(Parcel in) {
        this.lpFlag = in.readString();
        this.depositAmount = in.readString();
        this.dropShipString = in.readString();
        this.step = in.readString();
        this.gateway = in.readString();
        this.token = in.readString();
        this.partialString = in.readString();
        this.usedDeposit = in.readString();
        this.dropShipperDataList = in.createTypedArrayList(CheckoutDropShipperData.CREATOR);
        this.voucherCode = in.readString();
    }

    public static final Parcelable.Creator<CheckoutData> CREATOR
            = new Parcelable.Creator<CheckoutData>() {
        @Override
        public CheckoutData createFromParcel(Parcel source) {
            return new CheckoutData(source);
        }

        @Override
        public CheckoutData[] newArray(int size) {
            return new CheckoutData[size];
        }
    };
}
