package com.tokopedia.transaction.checkout.domain.datamodel.voucher;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 27/02/18.
 */

public class PromoCodeCartListData implements Parcelable {
    private boolean isError;
    private String errorMessage;
    private DataVoucher dataVoucher;

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public DataVoucher getDataVoucher() {
        return dataVoucher;
    }

    public void setDataVoucher(DataVoucher dataVoucher) {
        this.dataVoucher = dataVoucher;
    }

    public static class DataVoucher implements Parcelable {
        private String code;
        private int promoCodeId;
        private String discountAmount;
        private int cashbackAmount;
        private int saldoAmount;
        private int cashbackTopCashAmount;
        private int cashbackVoucherAmount;
        private int extraAmount;
        private String cashbackVoucherDescription;
        private String gatewayId;
        private String token;
        private String messageSuccess;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getPromoCodeId() {
            return promoCodeId;
        }

        public void setPromoCodeId(int promoCodeId) {
            this.promoCodeId = promoCodeId;
        }

        public String getDiscountAmount() {
            return discountAmount;
        }

        public void setDiscountAmount(String discountAmount) {
            this.discountAmount = discountAmount;
        }

        public int getCashbackAmount() {
            return cashbackAmount;
        }

        public void setCashbackAmount(int cashbackAmount) {
            this.cashbackAmount = cashbackAmount;
        }

        public int getSaldoAmount() {
            return saldoAmount;
        }

        public void setSaldoAmount(int saldoAmount) {
            this.saldoAmount = saldoAmount;
        }

        public int getCashbackTopCashAmount() {
            return cashbackTopCashAmount;
        }

        public void setCashbackTopCashAmount(int cashbackTopCashAmount) {
            this.cashbackTopCashAmount = cashbackTopCashAmount;
        }

        public int getCashbackVoucherAmount() {
            return cashbackVoucherAmount;
        }

        public void setCashbackVoucherAmount(int cashbackVoucherAmount) {
            this.cashbackVoucherAmount = cashbackVoucherAmount;
        }

        public int getExtraAmount() {
            return extraAmount;
        }

        public void setExtraAmount(int extraAmount) {
            this.extraAmount = extraAmount;
        }

        public String getCashbackVoucherDescription() {
            return cashbackVoucherDescription;
        }

        public void setCashbackVoucherDescription(String cashbackVoucherDescription) {
            this.cashbackVoucherDescription = cashbackVoucherDescription;
        }

        public String getGatewayId() {
            return gatewayId;
        }

        public void setGatewayId(String gatewayId) {
            this.gatewayId = gatewayId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getMessageSuccess() {
            return messageSuccess;
        }

        public void setMessageSuccess(String messageSuccess) {
            this.messageSuccess = messageSuccess;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.code);
            dest.writeInt(this.promoCodeId);
            dest.writeString(this.discountAmount);
            dest.writeInt(this.cashbackAmount);
            dest.writeInt(this.saldoAmount);
            dest.writeInt(this.cashbackTopCashAmount);
            dest.writeInt(this.cashbackVoucherAmount);
            dest.writeInt(this.extraAmount);
            dest.writeString(this.cashbackVoucherDescription);
            dest.writeString(this.gatewayId);
            dest.writeString(this.token);
            dest.writeString(this.messageSuccess);
        }

        public DataVoucher() {
        }

        protected DataVoucher(Parcel in) {
            this.code = in.readString();
            this.promoCodeId = in.readInt();
            this.discountAmount = in.readString();
            this.cashbackAmount = in.readInt();
            this.saldoAmount = in.readInt();
            this.cashbackTopCashAmount = in.readInt();
            this.cashbackVoucherAmount = in.readInt();
            this.extraAmount = in.readInt();
            this.cashbackVoucherDescription = in.readString();
            this.gatewayId = in.readString();
            this.token = in.readString();
            this.messageSuccess = in.readString();
        }

        public static final Parcelable.Creator<DataVoucher> CREATOR = new Parcelable.Creator<DataVoucher>() {
            @Override
            public DataVoucher createFromParcel(Parcel source) {
                return new DataVoucher(source);
            }

            @Override
            public DataVoucher[] newArray(int size) {
                return new DataVoucher[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isError ? (byte) 1 : (byte) 0);
        dest.writeString(this.errorMessage);
        dest.writeParcelable(this.dataVoucher, flags);
    }

    public PromoCodeCartListData() {
    }

    protected PromoCodeCartListData(Parcel in) {
        this.isError = in.readByte() != 0;
        this.errorMessage = in.readString();
        this.dataVoucher = in.readParcelable(DataVoucher.class.getClassLoader());
    }

    public static final Parcelable.Creator<PromoCodeCartListData> CREATOR = new Parcelable.Creator<PromoCodeCartListData>() {
        @Override
        public PromoCodeCartListData createFromParcel(Parcel source) {
            return new PromoCodeCartListData(source);
        }

        @Override
        public PromoCodeCartListData[] newArray(int size) {
            return new PromoCodeCartListData[size];
        }
    };
}
