package com.tokopedia.transaction.checkout.view.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 20/02/18.
 */

public class DeleteCartData implements Parcelable {
    private boolean success;
    private String message;

    private DeleteCartData(Builder builder) {
        success = builder.success;
        message = builder.message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }


    public static final class Builder {
        private boolean success;
        private String message;

        public Builder() {
        }

        public Builder success(boolean val) {
            success = val;
            return this;
        }

        public Builder message(String val) {
            message = val;
            return this;
        }

        public DeleteCartData build() {
            return new DeleteCartData(this);
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.success ? (byte) 1 : (byte) 0);
        dest.writeString(this.message);
    }

    protected DeleteCartData(Parcel in) {
        this.success = in.readByte() != 0;
        this.message = in.readString();
    }

    public static final Parcelable.Creator<DeleteCartData> CREATOR = new Parcelable.Creator<DeleteCartData>() {
        @Override
        public DeleteCartData createFromParcel(Parcel source) {
            return new DeleteCartData(source);
        }

        @Override
        public DeleteCartData[] newArray(int size) {
            return new DeleteCartData[size];
        }
    };
}
