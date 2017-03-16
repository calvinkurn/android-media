package com.tokopedia.transaction.cart.model.cartdata;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nabilla Sabbaha on 2/22/2017.
 */

public class CartDonation implements Parcelable {

    @SerializedName("donation_note_info")
    @Expose
    private String donationNoteInfo;
    @SerializedName("donation_note_title")
    @Expose
    private String donationNoteTitle;
    @SerializedName("donation_popup_info")
    @Expose
    private String donationPopupInfo;
    @SerializedName("donation_popup_image")
    @Expose
    private String donationPopupImg;
    @SerializedName("donation_popup_title")
    @Expose
    private String donationPopupTitle;
    @SerializedName("donation_value")
    @Expose
    private String donationValue;
    @SerializedName("donation_value_idr")
    @Expose
    private String donationValueIdr;
    @SerializedName("donation_name")
    @Expose
    private String donationName;

    protected CartDonation(Parcel in) {
        donationNoteInfo = in.readString();
        donationNoteTitle = in.readString();
        donationPopupInfo = in.readString();
        donationPopupImg = in.readString();
        donationPopupTitle = in.readString();
        donationValue = in.readString();
        donationValueIdr = in.readString();
        donationName = in.readString();
    }

    public static final Creator<CartDonation> CREATOR = new Creator<CartDonation>() {
        @Override
        public CartDonation createFromParcel(Parcel in) {
            return new CartDonation(in);
        }

        @Override
        public CartDonation[] newArray(int size) {
            return new CartDonation[size];
        }
    };

    public String getDonationNoteInfo() {
        return donationNoteInfo;
    }

    public void setDonationNoteInfo(String donationNoteInfo) {
        this.donationNoteInfo = donationNoteInfo;
    }

    public String getDonationNoteTitle() {
        return donationNoteTitle;
    }

    public void setDonationNoteTitle(String donationNoteTitle) {
        this.donationNoteTitle = donationNoteTitle;
    }

    public String getDonationPopupInfo() {
        return donationPopupInfo;
    }

    public void setDonationPopupInfo(String donationPopupInfo) {
        this.donationPopupInfo = donationPopupInfo;
    }

    public String getDonationPopupImg() {
        return donationPopupImg;
    }

    public void setDonationPopupImg(String donationPopupImg) {
        this.donationPopupImg = donationPopupImg;
    }

    public String getDonationPopupTitle() {
        return donationPopupTitle;
    }

    public void setDonationPopupTitle(String donationPopupTitle) {
        this.donationPopupTitle = donationPopupTitle;
    }

    public String getDonationValue() {
        return donationValue;
    }

    public void setDonationValue(String donationValue) {
        this.donationValue = donationValue;
    }

    public String getDonationValueIdr() {
        return donationValueIdr;
    }

    public void setDonationValueIdr(String donationValueIdr) {
        this.donationValueIdr = donationValueIdr;
    }

    public String getDonationName() {
        return donationName;
    }

    public void setDonationName(String donationName) {
        this.donationName = donationName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(donationNoteInfo);
        dest.writeString(donationNoteTitle);
        dest.writeString(donationPopupInfo);
        dest.writeString(donationPopupImg);
        dest.writeString(donationPopupTitle);
        dest.writeString(donationValue);
        dest.writeString(donationValueIdr);
        dest.writeString(donationName);
    }
}