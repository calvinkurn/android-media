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
    private Double latitude;
    private Double longitude;
    private String shipmentInfo;
    private String partialOrderInfo;
    private String dropshipperInfo;
    private String deliveryPriceTotal;
    private String additionalPrice;

    public ShipmentDetailData() {
    }

    protected ShipmentDetailData(Parcel in) {
        id = in.readString();
        shipmentItemData = in.createTypedArrayList(ShipmentItemData.CREATOR);
        address = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        shipmentInfo = in.readString();
        partialOrderInfo = in.readString();
        dropshipperInfo = in.readString();
        deliveryPriceTotal = in.readString();
        additionalPrice = in.readString();
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getShipmentInfo() {
        return shipmentInfo;
    }

    public void setShipmentInfo(String shipmentInfo) {
        this.shipmentInfo = shipmentInfo;
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

    public String getDeliveryPriceTotal() {
        return deliveryPriceTotal;
    }

    public void setDeliveryPriceTotal(String deliveryPriceTotal) {
        this.deliveryPriceTotal = deliveryPriceTotal;
    }

    public String getAdditionalPrice() {
        return additionalPrice;
    }

    public void setAdditionalPrice(String additionalPrice) {
        this.additionalPrice = additionalPrice;
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
        dest.writeString(partialOrderInfo);
        dest.writeString(dropshipperInfo);
        dest.writeString(deliveryPriceTotal);
        dest.writeString(additionalPrice);
    }
}
