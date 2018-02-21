package com.tokopedia.transaction.checkout.view.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 21/02/18.
 */

public class DeleteUpdateCartData implements Parcelable {
    private boolean success;
    private String message;

    private DeleteUpdateCartData(Builder builder) {
        setSuccess(builder.success);
        setMessage(builder.message);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public DeleteUpdateCartData() {
    }

    protected DeleteUpdateCartData(Parcel in) {
        this.success = in.readByte() != 0;
        this.message = in.readString();
    }

    public static final Creator<DeleteUpdateCartData> CREATOR =
            new Creator<DeleteUpdateCartData>() {
        @Override
        public DeleteUpdateCartData createFromParcel(Parcel source) {
            return new DeleteUpdateCartData(source);
        }

        @Override
        public DeleteUpdateCartData[] newArray(int size) {
            return new DeleteUpdateCartData[size];
        }
    };

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

        public DeleteUpdateCartData build() {
            return new DeleteUpdateCartData(this);
        }
    }
}
