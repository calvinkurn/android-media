package com.tokopedia.transaction.checkout.view.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Irfan Khoirul on 25/01/18.
 */

public class ShipmentDetailData implements Parcelable {

    private String id;
    private List<ShipmentItemData> shipmentItemData;
    private String address;
    private double latitude;
    private double longitude;
    private String shipmentInfo;
    private String insuranceInfo;
    private String partialOrderInfo;
    private String dropshipperInfo;

    public ShipmentDetailData() {
    }

    protected ShipmentDetailData(Parcel in) {
        id = in.readString();
        shipmentItemData = in.createTypedArrayList(ShipmentItemData.CREATOR);
        address = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        shipmentInfo = in.readString();
        insuranceInfo = in.readString();
        partialOrderInfo = in.readString();
        dropshipperInfo = in.readString();
    }

    public static final Creator<ShipmentDetailData> CREATOR = new Creator<ShipmentDetailData>() {
        @Override
        public ShipmentDetailData createFromParcel(Parcel in) {
            return new ShipmentDetailData(in);
        }

        @Override
        public ShipmentDetailData[] newArray(int size) {
            return new ShipmentDetailData[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ShipmentItemData> getShipmentItemData() {
        return shipmentItemData;
    }

    public void setShipmentItemData(List<ShipmentItemData> shipmentItemData) {
        this.shipmentItemData = shipmentItemData;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getShipmentInfo() {
        return shipmentInfo;
    }

    public void setShipmentInfo(String shipmentInfo) {
        this.shipmentInfo = shipmentInfo;
    }

    public String getInsuranceInfo() {
        return insuranceInfo;
    }

    public void setInsuranceInfo(String insuranceInfo) {
        this.insuranceInfo = insuranceInfo;
    }

    public String getPartialOrderInfo() {
        return partialOrderInfo;
    }

    public void setPartialOrderInfo(String partialOrderInfo) {
        this.partialOrderInfo = partialOrderInfo;
    }

    public String getDropshipperInfo() {
        return dropshipperInfo;
    }

    public void setDropshipperInfo(String dropshipperInfo) {
        this.dropshipperInfo = dropshipperInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeTypedList(shipmentItemData);
        dest.writeString(address);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(shipmentInfo);
        dest.writeString(insuranceInfo);
        dest.writeString(partialOrderInfo);
        dest.writeString(dropshipperInfo);
    }
}
