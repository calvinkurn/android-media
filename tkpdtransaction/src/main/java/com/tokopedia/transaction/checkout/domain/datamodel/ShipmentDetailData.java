package com.tokopedia.transaction.checkout.domain.datamodel;

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
    private Boolean useInsurance;
    private Boolean usePartialOrder;
    private Boolean useDropshipper;
    private String dropshipperName;
    private String dropshipperPhone;

    public ShipmentDetailData() {
    }

    protected ShipmentDetailData(Parcel in) {
        shipmentItemData = in.createTypedArrayList(ShipmentItemData.CREATOR);
        totalQuantity = in.readInt();
        shipmentTickerInfo = in.readString();
        shipmentCartData = in.readParcelable(ShipmentCartData.class.getClassLoader());
        selectedShipment = in.readParcelable(ShipmentItemData.class.getClassLoader());
        selectedCourier = in.readParcelable(CourierItemData.class.getClassLoader());
        byte tmpUseInsurance = in.readByte();
        useInsurance = tmpUseInsurance == 0 ? null : tmpUseInsurance == 1;
        byte tmpUsePartialOrder = in.readByte();
        usePartialOrder = tmpUsePartialOrder == 0 ? null : tmpUsePartialOrder == 1;
        byte tmpUseDropshipper = in.readByte();
        useDropshipper = tmpUseDropshipper == 0 ? null : tmpUseDropshipper == 1;
        dropshipperName = in.readString();
        dropshipperPhone = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(shipmentItemData);
        dest.writeInt(totalQuantity);
        dest.writeString(shipmentTickerInfo);
        dest.writeParcelable(shipmentCartData, flags);
        dest.writeParcelable(selectedShipment, flags);
        dest.writeParcelable(selectedCourier, flags);
        dest.writeByte((byte) (useInsurance == null ? 0 : useInsurance ? 1 : 2));
        dest.writeByte((byte) (usePartialOrder == null ? 0 : usePartialOrder ? 1 : 2));
        dest.writeByte((byte) (useDropshipper == null ? 0 : useDropshipper ? 1 : 2));
        dest.writeString(dropshipperName);
        dest.writeString(dropshipperPhone);
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

}
