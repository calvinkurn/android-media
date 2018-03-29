package com.tokopedia.transaction.checkout.domain.datamodel.shipmentrates;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Irfan Khoirul on 25/01/18.
 */

public class ShipmentDetailData implements Parcelable {

    private List<ShipmentItemData> shipmentItemData;
    private int totalQuantity;
    private String shipmentTickerInfo;
    private ShipmentCartData shipmentCartData;
    private ShipmentItemData selectedShipment;
    private CourierItemData selectedCourier;
    private boolean useInsurance;
    private boolean usePartialOrder;
    private boolean useDropshipper;
    private String dropshipperName;
    private String dropshipperPhone;

    public ShipmentDetailData() {
    }

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

    public Boolean getUseInsurance() {
        return useInsurance;
    }

    public void setUseInsurance(Boolean useInsurance) {
        this.useInsurance = useInsurance;
    }

    public Boolean getUsePartialOrder() {
        return usePartialOrder;
    }

    public void setUsePartialOrder(Boolean usePartialOrder) {
        this.usePartialOrder = usePartialOrder;
    }

    public Boolean getUseDropshipper() {
        return useDropshipper;
    }

    public void setUseDropshipper(Boolean useDropshipper) {
        this.useDropshipper = useDropshipper;
    }

    public String getDropshipperName() {
        return dropshipperName;
    }

    public void setDropshipperName(String dropshipperName) {
        this.dropshipperName = dropshipperName;
    }

    public String getDropshipperPhone() {
        return dropshipperPhone;
    }

    public void setDropshipperPhone(String dropshipperPhone) {
        this.dropshipperPhone = dropshipperPhone;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.shipmentItemData);
        dest.writeInt(this.totalQuantity);
        dest.writeString(this.shipmentTickerInfo);
        dest.writeParcelable(this.shipmentCartData, flags);
        dest.writeParcelable(this.selectedShipment, flags);
        dest.writeParcelable(this.selectedCourier, flags);
        dest.writeByte(this.useInsurance ? (byte) 1 : (byte) 0);
        dest.writeByte(this.usePartialOrder ? (byte) 1 : (byte) 0);
        dest.writeByte(this.useDropshipper ? (byte) 1 : (byte) 0);
        dest.writeString(this.dropshipperName);
        dest.writeString(this.dropshipperPhone);
    }

    protected ShipmentDetailData(Parcel in) {
        this.shipmentItemData = in.createTypedArrayList(ShipmentItemData.CREATOR);
        this.totalQuantity = in.readInt();
        this.shipmentTickerInfo = in.readString();
        this.shipmentCartData = in.readParcelable(ShipmentCartData.class.getClassLoader());
        this.selectedShipment = in.readParcelable(ShipmentItemData.class.getClassLoader());
        this.selectedCourier = in.readParcelable(CourierItemData.class.getClassLoader());
        this.useInsurance = in.readByte() != 0;
        this.usePartialOrder = in.readByte() != 0;
        this.useDropshipper = in.readByte() != 0;
        this.dropshipperName = in.readString();
        this.dropshipperPhone = in.readString();
    }

    public static final Creator<ShipmentDetailData> CREATOR = new Creator<ShipmentDetailData>() {
        @Override
        public ShipmentDetailData createFromParcel(Parcel source) {
            return new ShipmentDetailData(source);
        }

        @Override
        public ShipmentDetailData[] newArray(int size) {
            return new ShipmentDetailData[size];
        }
    };
}
