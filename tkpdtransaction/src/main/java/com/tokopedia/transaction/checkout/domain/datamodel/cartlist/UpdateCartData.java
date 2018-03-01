package com.tokopedia.transaction.checkout.domain.datamodel.cartlist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 20/02/18.
 */

public class UpdateCartData implements Parcelable {
    private boolean success;
    private String message;
    private int goTo;

    public UpdateCartData() {
    }

    public int getGoTo() {
        return goTo;
    }

    public void setGoTo(int goTo) {
        this.goTo = goTo;
    }

    private UpdateCartData(Builder builder) {
        setSuccess(builder.success);
        setMessage(builder.message);
        setGoTo(builder.goTo);
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


    public static final class Builder {
        private boolean success;
        private String message;
        private int goTo;

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

        public Builder goTo(int val) {
            goTo = val;
            return this;
        }

        public UpdateCartData build() {
            return new UpdateCartData(this);
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
        dest.writeInt(this.goTo);
    }

    protected UpdateCartData(Parcel in) {
        this.success = in.readByte() != 0;
        this.message = in.readString();
        this.goTo = in.readInt();
    }

    public static final Creator<UpdateCartData> CREATOR = new Creator<UpdateCartData>() {
        @Override
        public UpdateCartData createFromParcel(Parcel source) {
            return new UpdateCartData(source);
        }

        @Override
        public UpdateCartData[] newArray(int size) {
            return new UpdateCartData[size];
        }
    };
}
