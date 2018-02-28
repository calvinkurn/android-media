package com.tokopedia.transaction.checkout.view.data.voucher;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 27/02/18.
 */

public class PromoCodeCartShipmentData implements Parcelable {
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
        private String voucherAmountIdr;
        private int voucherNoOtherPromotion;
        private int voucherAmount;
        private int voucherStatus;

        public String getVoucherAmountIdr() {
            return voucherAmountIdr;
        }

        public void setVoucherAmountIdr(String voucherAmountIdr) {
            this.voucherAmountIdr = voucherAmountIdr;
        }

        public int getVoucherNoOtherPromotion() {
            return voucherNoOtherPromotion;
        }

        public void setVoucherNoOtherPromotion(int voucherNoOtherPromotion) {
            this.voucherNoOtherPromotion = voucherNoOtherPromotion;
        }

        public int getVoucherAmount() {
            return voucherAmount;
        }

        public void setVoucherAmount(int voucherAmount) {
            this.voucherAmount = voucherAmount;
        }

        public int getVoucherStatus() {
            return voucherStatus;
        }

        public void setVoucherStatus(int voucherStatus) {
            this.voucherStatus = voucherStatus;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.voucherAmountIdr);
            dest.writeInt(this.voucherNoOtherPromotion);
            dest.writeInt(this.voucherAmount);
            dest.writeInt(this.voucherStatus);
        }

        public DataVoucher() {
        }

        protected DataVoucher(Parcel in) {
            this.voucherAmountIdr = in.readString();
            this.voucherNoOtherPromotion = in.readInt();
            this.voucherAmount = in.readInt();
            this.voucherStatus = in.readInt();
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

    public PromoCodeCartShipmentData() {
    }

    protected PromoCodeCartShipmentData(Parcel in) {
        this.isError = in.readByte() != 0;
        this.errorMessage = in.readString();
        this.dataVoucher = in.readParcelable(PromoCodeCartListData.DataVoucher.class.getClassLoader());
    }

    public static final Parcelable.Creator<PromoCodeCartShipmentData> CREATOR = new Parcelable.Creator<PromoCodeCartShipmentData>() {
        @Override
        public PromoCodeCartShipmentData createFromParcel(Parcel source) {
            return new PromoCodeCartShipmentData(source);
        }

        @Override
        public PromoCodeCartShipmentData[] newArray(int size) {
            return new PromoCodeCartShipmentData[size];
        }
    };
}
