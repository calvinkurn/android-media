package com.tokopedia.seller.shop.open.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by normansyahputa on 12/21/17.
 */

public class GoogleLocationViewModel implements Parcelable {

    private String latitude;
    private String longitude;
    private String manualAddress;
    private String generatedAddress;

    protected GoogleLocationViewModel(Parcel in) {
        latitude = in.readString();
        longitude = in.readString();
        manualAddress = in.readString();
        generatedAddress = in.readString();
    }

    public static final Parcelable.Creator<GoogleLocationViewModel> CREATOR = new Creator<GoogleLocationViewModel>() {
        @Override
        public GoogleLocationViewModel createFromParcel(Parcel in) {
            return new GoogleLocationViewModel(in);
        }

        @Override
        public GoogleLocationViewModel[] newArray(int size) {
            return new GoogleLocationViewModel[size];
        }
    };

    public GoogleLocationViewModel() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(latitude);
        parcel.writeString(longitude);
        parcel.writeString(manualAddress);
        parcel.writeString(generatedAddress);
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getManualAddress() {
        return manualAddress;
    }

    public void setManualAddress(String manualAddress) {
        this.manualAddress = manualAddress;
    }

    public void setGeneratedAddress(String generatedAddress) {
        this.generatedAddress = generatedAddress;
    }

    public String getGeneratedAddress() {
        return generatedAddress;
    }

    public static class Builder {
        private String latitude;
        private String longitude;
        private String manualAddress;
        private String generatedAddress;

        public Builder() {
        }

        public static Builder aGoogleLocationViewModel() {
            return new Builder();
        }

        public Builder setLatitude(String latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder setLongitude(String longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder setManualAddress(String manualAddress) {
            this.manualAddress = manualAddress;
            return this;
        }

        public Builder setGeneratedAddress(String generatedAddress) {
            this.generatedAddress = generatedAddress;
            return this;
        }

        public Builder but() {
            return aGoogleLocationViewModel()
                    .setLatitude(latitude)
                    .setLongitude(longitude)
                    .setManualAddress(manualAddress)
                    .setGeneratedAddress(generatedAddress);
        }

        public GoogleLocationViewModel build() {
            GoogleLocationViewModel locationPass = new GoogleLocationViewModel();
            locationPass.setLatitude(latitude);
            locationPass.setLongitude(longitude);
            locationPass.setManualAddress(manualAddress);
            locationPass.setGeneratedAddress(generatedAddress);
            return locationPass;
        }
    }
}
