package com.tokopedia.transaction.purchase.model.response.txlist;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 21/04/2016.
 */
public class OrderButton implements Parcelable {
    private static final String TAG = OrderButton.class.getSimpleName();

    @SerializedName("button_ask_seller")
    @Expose
    private String buttonAskSeller;
    @SerializedName("button_open_dispute")
    @Expose
    private String buttonOpenDispute;
    @SerializedName("button_res_center_url")
    @Expose
    private String buttonResCenterUrl;
    @SerializedName("button_open_time_left")
    @Expose
    private String buttonOpenTimeLeft;
    @SerializedName("button_res_center_go_to")
    @Expose
    private String buttonResCenterGoTo;
    @SerializedName("button_upload_proof")
    @Expose
    private String buttonUploadProof;
    @SerializedName("button_open_complaint_received")
    @Expose
    private String buttonComplaintReceived;
    @SerializedName("button_open_complaint_not_received")
    @Expose
    private String buttonComplaintNotReceived;
    @SerializedName("button_cancel_request")
    @Expose
    private String buttonCancelRequest;
    @SerializedName("button_cancel_replacement")
    @Expose
    private String buttonCancelReplacement;

    public String getButtonOpenDispute() {
        return buttonOpenDispute;
    }

    public void setButtonOpenDispute(String buttonOpenDispute) {
        this.buttonOpenDispute = buttonOpenDispute;
    }

    public String getButtonResCenterUrl() {
        return buttonResCenterUrl;
    }

    public void setButtonResCenterUrl(String buttonResCenterUrl) {
        this.buttonResCenterUrl = buttonResCenterUrl;
    }

    public String getButtonOpenTimeLeft() {
        return buttonOpenTimeLeft;
    }

    public void setButtonOpenTimeLeft(String buttonOpenTimeLeft) {
        this.buttonOpenTimeLeft = buttonOpenTimeLeft;
    }

    public String getButtonResCenterGoTo() {
        return buttonResCenterGoTo;
    }

    public void setButtonResCenterGoTo(String buttonResCenterGoTo) {
        this.buttonResCenterGoTo = buttonResCenterGoTo;
    }

    public String getButtonUploadProof() {
        return buttonUploadProof;
    }

    public void setButtonUploadProof(String buttonUploadProof) {
        this.buttonUploadProof = buttonUploadProof;
    }

    public String getButtonAskSeller() {
        return buttonAskSeller;
    }

    public void setButtonAskSeller(String buttonAskSeller) {
        this.buttonAskSeller = buttonAskSeller;
    }

    public String getButtonComplaintReceived() {
        return buttonComplaintReceived;
    }

    public void setButtonComplaintReceived(String buttonComplaintReceived) {
        this.buttonComplaintReceived = buttonComplaintReceived;
    }

    public String getButtonComplaintNotReceived() {
        return buttonComplaintNotReceived;
    }

    public void setButtonComplaintNotReceived(String buttonComplaintNotReceived) {
        this.buttonComplaintNotReceived = buttonComplaintNotReceived;
    }

    public String getButtonCancelRequest() {
        return buttonCancelRequest;
    }

    public void setButtonCancelRequest(String buttonCancelRequest) {
        this.buttonCancelRequest = buttonCancelRequest;
    }

    public String getButtonCancelReplacement() {
        return buttonCancelReplacement;
    }

    public void setButtonCancelReplacement(String buttonCancelReplacement) {
        this.buttonCancelReplacement = buttonCancelReplacement;
    }

    public OrderButton() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(buttonAskSeller);
        dest.writeString(buttonOpenDispute);
        dest.writeString(buttonResCenterUrl);
        dest.writeString(buttonOpenTimeLeft);
        dest.writeString(buttonResCenterGoTo);
        dest.writeString(buttonUploadProof);
        dest.writeString(buttonComplaintReceived);
        dest.writeString(buttonCancelRequest);
        dest.writeString(buttonCancelReplacement);
        dest.writeString(buttonComplaintNotReceived);
    }

    protected OrderButton(Parcel in) {
        buttonAskSeller = in.readString();
        buttonOpenDispute = in.readString();
        buttonResCenterUrl = in.readString();
        buttonOpenTimeLeft = in.readString();
        buttonResCenterGoTo = in.readString();
        buttonUploadProof = in.readString();
        buttonComplaintReceived = in.readString();
        buttonCancelRequest = in.readString();
        buttonCancelReplacement = in.readString();
        buttonComplaintNotReceived = in.readString();
    }

    public static final Creator<OrderButton> CREATOR = new Creator<OrderButton>() {
        @Override
        public OrderButton createFromParcel(Parcel in) {
            return new OrderButton(in);
        }

        @Override
        public OrderButton[] newArray(int size) {
            return new OrderButton[size];
        }
    };
}
