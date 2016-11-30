package com.tokopedia.transaction.cart.model.calculateshipment;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by alvarisi on 11/3/16.
 */

public class ShipmentPackage implements Parcelable {
    private String shipmentId;
    private String name;
    private String shipmentPackageId;
    private int isShowMap;
    private String price;
    private Integer packageAvailable;

    public ShipmentPackage() {
    }

    protected ShipmentPackage(Parcel in) {
        shipmentId = in.readString();
        name = in.readString();
        shipmentPackageId = in.readString();
        isShowMap = in.readInt();
        price = in.readString();
    }

    public static final Creator<ShipmentPackage> CREATOR = new Creator<ShipmentPackage>() {
        @Override
        public ShipmentPackage createFromParcel(Parcel in) {
            return new ShipmentPackage(in);
        }

        @Override
        public ShipmentPackage[] newArray(int size) {
            return new ShipmentPackage[size];
        }
    };

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
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The shipmentPackageId
     */
    public String getShipmentPackageId() {
        return shipmentPackageId;
    }

    /**
     * @param shipmentPackageId The sp_id
     */
    public void setShipmentPackageId(String shipmentPackageId) {
        this.shipmentPackageId = shipmentPackageId;
    }

    /**
     * @return The price
     */
    public String getPrice() {
        return price;
    }

    /**
     * @param price The price
     */
    public void setPrice(String price) {
        this.price = price;
    }

    public int getPackageAvailable() {
        return packageAvailable == null ? price == null || price.equals("0")
                || price.equals("") ? 0 : 1 : packageAvailable;
    }

    public void setPackageAvailable(int packageAvailable) {
        this.packageAvailable = packageAvailable;
    }

    public int getShowMap() {
        return isShowMap;
    }

    @Override
    public String toString() {
        return name;
    }

    public static ShipmentPackage createSelectionInfo(String info) {
        ShipmentPackage shipment = new ShipmentPackage();
        shipment.setName(info);
        shipment.setShipmentPackageId("0");
        shipment.setPrice("0");
        shipment.setShipmentId(String.valueOf(0));
        shipment.isShowMap = 0;
        return shipment;
    }

    public void setIsShowMap(int isShowMap) {
        this.isShowMap = isShowMap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(shipmentId);
        parcel.writeString(name);
        parcel.writeString(shipmentPackageId);
        parcel.writeInt(isShowMap);
        parcel.writeString(price);
    }
}