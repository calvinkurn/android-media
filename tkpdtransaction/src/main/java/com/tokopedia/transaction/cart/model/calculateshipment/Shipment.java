package com.tokopedia.transaction.cart.model.calculateshipment;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by alvarisi on 11/3/16.
 */

public class Shipment implements Parcelable {
    private String shipmentId;
    private List<ShipmentPackage> shipmentPackage = new ArrayList<ShipmentPackage>();
    private Integer shipmentAvailable;
    private String shipmentName;

    public Shipment() {

    }

    protected Shipment(Parcel in) {
        shipmentId = in.readString();
        shipmentPackage = in.createTypedArrayList(ShipmentPackage.CREATOR);
        shipmentName = in.readString();
    }

    public static final Creator<Shipment> CREATOR = new Creator<Shipment>() {
        @Override
        public Shipment createFromParcel(Parcel in) {
            return new Shipment(in);
        }

        @Override
        public Shipment[] newArray(int size) {
            return new Shipment[size];
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
     * @return The shipmentPackage
     */
    public List<ShipmentPackage> getShipmentPackage() {
        return shipmentPackage;
    }

    /**
     * @param shipmentPackage The shipment_package
     */
    public void setShipmentPackage(List<ShipmentPackage> shipmentPackage) {
        this.shipmentPackage = shipmentPackage;
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

    public static Shipment createSelectionInfo(String info) {
        Shipment shipment = new Shipment();
        shipment.setShipmentName(info);
        shipment.setShipmentId("0");
        shipment.setShipmentAvailable(0);
        shipment.setShipmentPackage(new ArrayList<ShipmentPackage>());
        return shipment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(shipmentId);
        parcel.writeTypedList(shipmentPackage);
        parcel.writeString(shipmentName);
    }
}
