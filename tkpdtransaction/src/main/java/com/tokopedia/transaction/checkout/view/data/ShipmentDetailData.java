package com.tokopedia.transaction.checkout.view.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Irfan Khoirul on 25/01/18.
 */

public class ShipmentDetailData implements Parcelable {

    private List<ShipmentItemData> shipmentItemData;
    private String shipmentTickerInfo;
    private ShipmentCartData shipmentCartData;
    private ShipmentItemData selectedShipment;
    private CourierItemData selectedCourier;

    public ShipmentDetailData() {
    }

    protected ShipmentDetailData(Parcel in) {
        shipmentItemData = in.createTypedArrayList(ShipmentItemData.CREATOR);
        shipmentTickerInfo = in.readString();
        shipmentCartData = in.readParcelable(ShipmentCartData.class.getClassLoader());
        selectedShipment = in.readParcelable(ShipmentItemData.class.getClassLoader());
        selectedCourier = in.readParcelable(CourierItemData.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(shipmentItemData);
        dest.writeString(shipmentTickerInfo);
        dest.writeParcelable(shipmentCartData, flags);
        dest.writeParcelable(selectedShipment, flags);
        dest.writeParcelable(selectedCourier, flags);
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

    public List<ShipmentItemData> getShipmentItemData() {
        return shipmentItemData;
    }

    public void setShipmentItemData(List<ShipmentItemData> shipmentItemData) {
        this.shipmentItemData = shipmentItemData;
    }

    public String getShipmentTickerInfo() {
        return shipmentTickerInfo;
    }

    public void setShipmentTickerInfo(String shipmentTickerInfo) {
        this.shipmentTickerInfo = shipmentTickerInfo;
    }

    public ShipmentCartData getShipmentCartData() {
        return shipmentCartData;
    }

    public void setShipmentCartData(ShipmentCartData shipmentCartData) {
        this.shipmentCartData = shipmentCartData;
    }

    public ShipmentItemData getSelectedShipment() {
        return selectedShipment;
    }

    public void setSelectedShipment(ShipmentItemData selectedShipment) {
        this.selectedShipment = selectedShipment;
    }

    public CourierItemData getSelectedCourier() {
        return selectedCourier;
    }

    public void setSelectedCourier(CourierItemData selectedCourier) {
        this.selectedCourier = selectedCourier;
    }
}
