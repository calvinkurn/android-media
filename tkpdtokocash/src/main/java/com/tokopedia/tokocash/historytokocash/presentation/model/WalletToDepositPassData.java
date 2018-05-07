package com.tokopedia.tokocash.historytokocash.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 8/21/17.
 */

public class WalletToDepositPassData implements Parcelable {

    private String title;
    private String method;
    private String url;
    private ParamsActionHistory params;
    private String name;
    private String amountFormatted;

    public String getTitle() {
        return title;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public ParamsActionHistory getParams() {
        return params;
    }

    public String getName() {
        return name;
    }

    public String getAmountFormatted() {
        return amountFormatted;
    }

    private WalletToDepositPassData(Builder builder) {
        title = builder.title;
        method = builder.method;
        url = builder.url;
        params = builder.params;
        name = builder.name;
        amountFormatted = builder.amountFormatted;
    }

    public static final class Builder {
        private String title;
        private String method;
        private String url;
        private ParamsActionHistory params;
        private String name;
        private String amountFormatted;

        public Builder() {
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder method(String val) {
            method = val;
            return this;
        }

        public Builder url(String val) {
            url = val;
            return this;
        }

        public Builder params(ParamsActionHistory val) {
            params = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder amountFormatted(String val) {
            amountFormatted = val;
            return this;
        }

        public WalletToDepositPassData build() {
            return new WalletToDepositPassData(this);
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.method);
        dest.writeString(this.url);
        dest.writeParcelable(this.params, flags);
        dest.writeString(this.name);
        dest.writeString(this.amountFormatted);
    }

    protected WalletToDepositPassData(Parcel in) {
        this.title = in.readString();
        this.method = in.readString();
        this.url = in.readString();
        this.params = in.readParcelable(ParamsActionHistory.class.getClassLoader());
        this.name = in.readString();
        this.amountFormatted = in.readString();
    }

    public static final Creator<WalletToDepositPassData> CREATOR = new Creator<WalletToDepositPassData>() {
        @Override
        public WalletToDepositPassData createFromParcel(Parcel source) {
            return new WalletToDepositPassData(source);
        }

        @Override
        public WalletToDepositPassData[] newArray(int size) {
            return new WalletToDepositPassData[size];
        }
    };

}