package com.tokopedia.transaction.cart.interactor.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvarisi on 11/30/16.
 */
public class ShipmentEntity implements Parcelable {

    @SerializedName("shipping_max_add_fee")
    @Expose
    private Integer shippingMaxAddFee;
    @SerializedName("shipment_id")
    @Expose
    private String shipmentId;
    @SerializedName("shipment_package")
    @Expose
    private List<ShipmentPackageEntity> ShipmentPackageEntity = new ArrayList<ShipmentPackageEntity>();
    @SerializedName("shipment_available")
    @Expose
    private Integer shipmentAvailable;
    @SerializedName("shipment_image")
    @Expose
    private String shipmentImage;
    @SerializedName("shipment_name")
    @Expose
    private String shipmentName;


    public ShipmentEntity() {

    }

    protected ShipmentEntity(Parcel in) {
        shipmentId = in.readString();
        shipmentImage = in.readString();
        shipmentName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shipmentId);
        dest.writeString(shipmentImage);
        dest.writeString(shipmentName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShipmentEntity> CREATOR = new Creator<ShipmentEntity>() {
        @Override
        public ShipmentEntity createFromParcel(Parcel in) {
            return new ShipmentEntity(in);
        }

        @Override
        public ShipmentEntity[] newArray(int size) {
            return new ShipmentEntity[size];
        }
    };

    /**
     * @return The shippingMaxAddFee
     */
    public Integer getShippingMaxAddFee() {
        return shippingMaxAddFee;
    }

    /**
     * @param shippingMaxAddFee The shipping_max_add_fee
     */
    public void setShippingMaxAddFee(Integer shippingMaxAddFee) {
        this.shippingMaxAddFee = shippingMaxAddFee;
    }

    /**
     * @return The shipmentId
     */
    public String getShipmentId() {
        return shipmentId;
    }

    /**
     * @param shipmentId The shipment_id
     */
    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    /**
     * @return The ShipmentPackageEntity
     */
    public List<ShipmentPackageEntity> getShipmentPackageEntity() {
        return ShipmentPackageEntity;
    }

    /**
     * @param ShipmentPackageEntity The shipment_package
     */
    public void setShipmentPackageEntity(List<ShipmentPackageEntity> ShipmentPackageEntity) {
        this.ShipmentPackageEntity = ShipmentPackageEntity;
    }

    /**
     * @return The shipmentAvailable
     */
    public Integer getShipmentAvailable() {
        return shipmentAvailable;
    }

    /**
     * @param shipmentAvailable The shipment_available
     */
    public void setShipmentAvailable(Integer shipmentAvailable) {
        this.shipmentAvailable = shipmentAvailable;
    }

    /**
     * @return The shipmentImage
     */
    public String getShipmentImage() {
        return shipmentImage;
    }

    /**
     * @param shipmentImage The shipment_image
     */
    public void setShipmentImage(String shipmentImage) {
        this.shipmentImage = shipmentImage;
    }

    /**
     * @return The shipmentName
     */
    public String getShipmentName() {
        return shipmentName;
    }

    /**
     * @param shipmentName The shipment_name
     */
    public void setShipmentName(String shipmentName) {
        this.shipmentName = shipmentName;
    }

    @Override
    public String toString() {
        return shipmentName;
    }
}

