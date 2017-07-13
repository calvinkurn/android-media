package com.tokopedia.transaction.cart.model.paramcheckout;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author anggaprasetiyo on 11/4/16.
 */

public class CheckoutData implements Parcelable {
    private static final String DEFAULT_PAYMENT_GATEWAY_ID = "99";

    private String lpFlag = "1";
    private String depositAmount;
    private String dropShipString;
    private String step;
    private String gateway;
    private String token;
    private String partialString;
    private String usedDeposit;
    private boolean errorPayment;
    private String errorPaymentMessage;
    private List<CheckoutDropShipperData> dropShipperDataList;
    private String voucherCode;
    private String donationValue;
    private List<String> keroKeyParams;
    private List<String> keroValueParams;

    private CheckoutData(Builder builder) {
        setLpFlag(builder.lpFlag);
        setDepositAmount(builder.depositAmount);
        setDropShipString(builder.dropShipString);
        setStep(builder.step);
        setGateway(builder.gateway);
        setToken(builder.token);
        setPartialString(builder.partialString);
        setUsedDeposit(builder.usedDeposit);
        setErrorPayment(builder.errorPayment);
        setErrorPaymentMessage(builder.errorPaymentMessage);
        setDropShipperDataList(builder.dropShipperDataList);
        setVoucherCode(builder.voucherCode);
        setDonationValue(builder.donationValue);
        setKeroKeyParams(builder.keroKeyParams);
        setKeroValueParams(builder.keroValueParams);
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
        return gateway == null || gateway.isEmpty() ? DEFAULT_PAYMENT_GATEWAY_ID : gateway;
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
        return usedDeposit == null || usedDeposit.isEmpty() ? "0" : usedDeposit;
    }

    public void setUsedDeposit(String usedDeposit) {
        this.usedDeposit = usedDeposit;
    }

    public boolean isErrorPayment() {
        return errorPayment;
    }

    public void setErrorPayment(boolean errorPayment) {
        this.errorPayment = errorPayment;
    }

    public String getErrorPaymentMessage() {
        return errorPaymentMessage;
    }

    public void setErrorPaymentMessage(String errorPaymentMessage) {
        this.errorPaymentMessage = errorPaymentMessage;
    }

    public String getDonationValue() {
        return donationValue;
    }

    public void setDonationValue(String donationValue) {
        this.donationValue = donationValue;
    }

    public List<String> getKeroKeyParams() {
        return keroKeyParams;
    }

    public void setKeroKeyParams(List<String> keroKeyParams) {
        this.keroKeyParams = keroKeyParams;
    }

    public List<String> getKeroValueParams() {
        return keroValueParams;
    }

    public void setKeroValueParams(List<String> keroValueParams) {
        this.keroValueParams = keroValueParams;
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
        private boolean errorPayment;
        private String errorPaymentMessage;
        private List<CheckoutDropShipperData> dropShipperDataList;
        private String voucherCode;
        private String donationValue;
        private List<String> keroKeyParams;
        private List<String> keroValueParams;

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
            usedDeposit = val.isEmpty() ? "0" : val;
            return this;
        }

        public Builder errorPayment(boolean val) {
            errorPayment = val;
            return this;
        }

        public Builder errorPaymentMessage(String val) {
            errorPaymentMessage = val;
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

        public Builder donationValue(String val) {
            donationValue = val;
            return this;
        }

        public Builder keroKeyParams(List<String> keroKeyParams) {
            this.keroKeyParams = keroKeyParams;
            return this;
        }

        public Builder keroValueParams(List<String> keroValueParams) {
            this.keroValueParams = keroValueParams;
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
        dest.writeByte(this.errorPayment ? (byte) 1 : (byte) 0);
        dest.writeString(this.errorPaymentMessage);
        dest.writeTypedList(this.dropShipperDataList);
        dest.writeString(this.voucherCode);
        dest.writeString(this.donationValue);
        dest.writeStringList(keroKeyParams);
        dest.writeStringList(keroValueParams);
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
        this.errorPayment = in.readByte() != 0;
        this.errorPaymentMessage = in.readString();
        this.dropShipperDataList = in.createTypedArrayList(CheckoutDropShipperData.CREATOR);
        this.voucherCode = in.readString();
        this.donationValue = in.readString();
        this.keroKeyParams = in.createStringArrayList();
        this.keroValueParams = in.createStringArrayList();
    }

    public static final Creator<CheckoutData> CREATOR = new Creator<CheckoutData>() {
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
