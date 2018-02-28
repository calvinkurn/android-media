package com.tokopedia.transaction.checkout.domain.datamodel.cartmultipleshipment;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 21/02/18.
 */

public class SetShippingAddressData implements Parcelable {
    private boolean success;

    private SetShippingAddressData(Builder builder) {
        success = builder.success;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.success ? (byte) 1 : (byte) 0);
    }

    public SetShippingAddressData() {
    }

    protected SetShippingAddressData(Parcel in) {
        this.success = in.readByte() != 0;
    }

    public static final Creator<SetShippingAddressData> CREATOR = new Creator<SetShippingAddressData>() {
        @Override
        public SetShippingAddressData createFromParcel(Parcel source) {
            return new SetShippingAddressData(source);
        }

        @Override
        public SetShippingAddressData[] newArray(int size) {
            return new SetShippingAddressData[size];
        }
    };

    public static final class Builder {
        private boolean success;

        public Builder() {
        }

        public Builder success(boolean val) {
            success = val;
            return this;
        }

        public SetShippingAddressData build() {
            return new SetShippingAddressData(this);
        }
    }
}
