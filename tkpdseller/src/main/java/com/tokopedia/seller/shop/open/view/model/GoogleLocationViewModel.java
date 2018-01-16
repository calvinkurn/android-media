package com.tokopedia.seller.shop.open.view.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Created by normansyahputa on 12/21/17.
 */

public class GoogleLocationViewModel implements Parcelable {

    private String latitude;
    private String longitude;
    private String manualAddress;
    private String generatedAddress;
    private String checkSum;

    public GoogleLocationViewModel() {

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

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    public String getGeneratedAddress() {
        return generatedAddress;
    }

    public boolean isLatLongEmpty(){
        return TextUtils.isEmpty(latitude) || TextUtils.isEmpty(longitude) ||
                Double.valueOf(latitude) == -999 || Double.valueOf(longitude) == -999;
    }

    public void clearData(){
        longitude = null;
        latitude = null;
        checkSum = null;
        generatedAddress = "";
        manualAddress = "";
    }

    @Override
    public String toString() {
        return "GoogleLocationViewModel{" +
                "latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", manualAddress='" + manualAddress + '\'' +
                ", generatedAddress='" + generatedAddress + '\'' +
                ", checkSum='" + checkSum + '\'' +
                '}';
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeString(this.manualAddress);
        dest.writeString(this.generatedAddress);
        dest.writeString(this.checkSum);
    }

    protected GoogleLocationViewModel(Parcel in) {
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.manualAddress = in.readString();
        this.generatedAddress = in.readString();
        this.checkSum = in.readString();
    }

    public static final Creator<GoogleLocationViewModel> CREATOR = new Creator<GoogleLocationViewModel>() {
        @Override
        public GoogleLocationViewModel createFromParcel(Parcel source) {
            return new GoogleLocationViewModel(source);
        }

        @Override
        public GoogleLocationViewModel[] newArray(int size) {
            return new GoogleLocationViewModel[size];
        }
    };
}
