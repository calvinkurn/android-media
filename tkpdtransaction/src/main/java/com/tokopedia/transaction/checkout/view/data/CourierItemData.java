package com.tokopedia.transaction.checkout.view.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Irfan Khoirul on 25/01/18.
 */

public class CourierItemData implements Parcelable {
    private int shipperProductId;
    private String name;
    private String estimatedTimeDelivery;
    private String deliverySchedule;
    private String deliveryPrice;
    private int insurancePrice;
    private String additionalPrice;
    private String courierInfo;
    private int insuranceType;
    private int insuranceUsedType;
    private String insuranceUsedInfo;
    private int insuranceUsedDefault;
    private boolean allowDropshiper;
    private boolean usePinPoint;
    private boolean selected;

    public CourierItemData() {
    }

    protected CourierItemData(Parcel in) {
        shipperProductId = in.readInt();
        name = in.readString();
        estimatedTimeDelivery = in.readString();
        deliverySchedule = in.readString();
        deliveryPrice = in.readString();
        insurancePrice = in.readInt();
        additionalPrice = in.readString();
        courierInfo = in.readString();
        insuranceType = in.readInt();
        insuranceUsedType = in.readInt();
        insuranceUsedInfo = in.readString();
        insuranceUsedDefault = in.readInt();
        selected = in.readByte() != 0;
        usePinPoint = in.readByte() != 0;
        allowDropshiper = in.readByte() != 0;
    }

    public static final Creator<CourierItemData> CREATOR = new Creator<CourierItemData>() {
        @Override
        public CourierItemData createFromParcel(Parcel in) {
            return new CourierItemData(in);
        }

        @Override
        public CourierItemData[] newArray(int size) {
            return new CourierItemData[size];
        }
    };

    public int getShipperProductId() {
        return shipperProductId;
    }

    public void setShipperProductId(int shipperProductId) {
        this.shipperProductId = shipperProductId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEstimatedTimeDelivery() {
        return estimatedTimeDelivery;
    }

    public void setEstimatedTimeDelivery(String estimatedTimeDelivery) {
        this.estimatedTimeDelivery = estimatedTimeDelivery;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getDeliverySchedule() {
        return deliverySchedule;
    }

    public void setDeliverySchedule(String deliverySchedule) {
        this.deliverySchedule = deliverySchedule;
    }

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(String deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public int getInsurancePrice() {
        return insurancePrice;
    }

    public void setInsurancePrice(int insurancePrice) {
        this.insurancePrice = insurancePrice;
    }

    public int getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(int insuranceType) {
        this.insuranceType = insuranceType;
    }

    public int getInsuranceUsedType() {
        return insuranceUsedType;
    }

    public void setInsuranceUsedType(int insuranceUsedType) {
        this.insuranceUsedType = insuranceUsedType;
    }

    public String getInsuranceUsedInfo() {
        return insuranceUsedInfo;
    }

    public void setInsuranceUsedInfo(String insuranceUsedInfo) {
        this.insuranceUsedInfo = insuranceUsedInfo;
    }

    public int getInsuranceUsedDefault() {
        return insuranceUsedDefault;
    }

    public void setInsuranceUsedDefault(int insuranceUsedDefault) {
        this.insuranceUsedDefault = insuranceUsedDefault;
    }

    public String getAdditionalPrice() {
        return additionalPrice;
    }

    public void setAdditionalPrice(String additionalPrice) {
        this.additionalPrice = additionalPrice;
    }

    public String getCourierInfo() {
        return courierInfo;
    }

    public void setCourierInfo(String courierInfo) {
        this.courierInfo = courierInfo;
    }

    public boolean isAllowDropshiper() {
        return allowDropshiper;
    }

    public void setAllowDropshiper(boolean allowDropshiper) {
        this.allowDropshiper = allowDropshiper;
    }

    public boolean isUsePinPoint() {
        return usePinPoint;
    }

    public void setUsePinPoint(boolean usePinPoint) {
        this.usePinPoint = usePinPoint;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(shipperProductId);
        dest.writeString(name);
        dest.writeString(estimatedTimeDelivery);
        dest.writeString(deliverySchedule);
        dest.writeString(deliveryPrice);
        dest.writeInt(insurancePrice);
        dest.writeString(additionalPrice);
        dest.writeString(courierInfo);
        dest.writeInt(insuranceType);
        dest.writeInt(insuranceUsedType);
        dest.writeString(insuranceUsedInfo);
        dest.writeInt(insuranceUsedDefault);
        dest.writeByte((byte) (selected ? 1 : 0));
        dest.writeByte((byte) (allowDropshiper ? 1 : 0));
        dest.writeByte((byte) (usePinPoint ? 1 : 0));
    }
}
