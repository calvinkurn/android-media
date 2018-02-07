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
    private String partialOrderInfo;
    private String dropshipperInfo;
    private String deliveryPriceTotal;
    private String shipmentInfo;

    public ShipmentDetailData() {
    }

    protected ShipmentDetailData(Parcel in) {
        id = in.readString();
        shipmentItemData = in.createTypedArrayList(ShipmentItemData.CREATOR);
        address = in.readString();
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
        partialOrderInfo = in.readString();
        dropshipperInfo = in.readString();
        deliveryPriceTotal = in.readString();
        shipmentInfo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeTypedList(shipmentItemData);
        dest.writeString(address);
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
        dest.writeString(partialOrderInfo);
        dest.writeString(dropshipperInfo);
        dest.writeString(deliveryPriceTotal);
        dest.writeString(shipmentInfo);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getShipmentInfo() {
        return shipmentInfo;
    }

    public void setShipmentInfo(String shipmentInfo) {
        this.shipmentInfo = shipmentInfo;
    }

}
