package com.tokopedia.transaction.cart.model.cartdata;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartShipments implements Parcelable {

    @SerializedName("shipment_image")
    @Expose
    private String shipmentImage;
    @SerializedName("shipment_notes")
    @Expose
    private String shipmentNotes;
    @SerializedName("shipment_id")
    @Expose
    private String shipmentId;
    @SerializedName("shipment_package_name")
    @Expose
    private String shipmentPackageName;
    @SerializedName("shipment_name")
    @Expose
    private String shipmentName;
    @SerializedName("shipment_package_id")
    @Expose
    private String shipmentPackageId;
    @SerializedName("is_pick_up")
    @Expose
    private int isPickUp;

    public String getShipmentImage() {
        return shipmentImage;
    }

    public void setShipmentImage(String shipmentImage) {
        this.shipmentImage = shipmentImage;
    }

    public String getShipmentNotes() {
        return shipmentNotes;
    }

    public void setShipmentNotes(String shipmentNotes) {
        this.shipmentNotes = shipmentNotes;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getShipmentPackageName() {
        return shipmentPackageName;
    }

    public void setShipmentPackageName(String shipmentPackageName) {
        this.shipmentPackageName = shipmentPackageName;
    }

    public String getShipmentName() {
        return shipmentName;
    }

    public void setShipmentName(String shipmentName) {
        this.shipmentName = shipmentName;
    }

    public String getShipmentPackageId() {
        return shipmentPackageId;
    }

    public int getPickUp() {
        return isPickUp;
    }

    public void setShipmentPackageId(String shipmentPackageId) {
        this.shipmentPackageId = shipmentPackageId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shipmentImage);
        dest.writeString(this.shipmentNotes);
        dest.writeString(this.shipmentId);
        dest.writeString(this.shipmentPackageName);
        dest.writeString(this.shipmentName);
        dest.writeString(this.shipmentPackageId);
        dest.writeInt(this.isPickUp);
    }

    public CartShipments() {
    }

    protected CartShipments(Parcel in) {
        this.shipmentImage = in.readString();
        this.shipmentNotes = in.readString();
        this.shipmentId = in.readString();
        this.shipmentPackageName = in.readString();
        this.shipmentName = in.readString();
        this.shipmentPackageId = in.readString();
        this.isPickUp = in.readInt();
    }

    public static final Creator<CartShipments> CREATOR = new Creator<CartShipments>() {
        @Override
        public CartShipments createFromParcel(Parcel source) {
            return new CartShipments(source);
        }

        @Override
        public CartShipments[] newArray(int size) {
            return new CartShipments[size];
        }
    };
}
