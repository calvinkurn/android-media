package com.tokopedia.transaction.checkout.view.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Irfan Khoirul on 25/01/18.
 */

public class ShipmentDetailData implements Parcelable {

    private List<ShipmentItemData> shipmentItemData;
    private String address;
    private String partialOrderInfo;
    private String dropshipperInfo;
    private String deliveryPriceTotal;
    private String shipmentInfo;
    private String shippingService;
    private String shippingNames;
    private String originDistrictId;
    private String originPostalCode;
    private Double originLatitude;
    private Double originLongitude;
    private String destinationDistrictId;
    private String destinationPostalCode;
    private Double destinationLatitude;
    private Double destinationLongitude;
    private float weight;
    private String token;
    private String ut;
    private int insurance;
    private int productInsurance;
    private int orderValue;
    private String categoryId;

    public ShipmentDetailData() {
    }

    protected ShipmentDetailData(Parcel in) {
        shipmentItemData = in.createTypedArrayList(ShipmentItemData.CREATOR);
        address = in.readString();
        partialOrderInfo = in.readString();
        dropshipperInfo = in.readString();
        deliveryPriceTotal = in.readString();
        shipmentInfo = in.readString();
        shippingService = in.readString();
        shippingNames = in.readString();
        originDistrictId = in.readString();
        originPostalCode = in.readString();
        if (in.readByte() == 0) {
            originLatitude = null;
        } else {
            originLatitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            originLongitude = null;
        } else {
            originLongitude = in.readDouble();
        }
        destinationDistrictId = in.readString();
        destinationPostalCode = in.readString();
        if (in.readByte() == 0) {
            destinationLatitude = null;
        } else {
            destinationLatitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            destinationLongitude = null;
        } else {
            destinationLongitude = in.readDouble();
        }
        weight = in.readFloat();
        token = in.readString();
        ut = in.readString();
        insurance = in.readInt();
        productInsurance = in.readInt();
        orderValue = in.readInt();
        categoryId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(shipmentItemData);
        dest.writeString(address);
        dest.writeString(partialOrderInfo);
        dest.writeString(dropshipperInfo);
        dest.writeString(deliveryPriceTotal);
        dest.writeString(shipmentInfo);
        dest.writeString(shippingService);
        dest.writeString(shippingNames);
        dest.writeString(originDistrictId);
        dest.writeString(originPostalCode);
        if (originLatitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(originLatitude);
        }
        if (originLongitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(originLongitude);
        }
        dest.writeString(destinationDistrictId);
        dest.writeString(destinationPostalCode);
        if (destinationLatitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(destinationLatitude);
        }
        if (destinationLongitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(destinationLongitude);
        }
        dest.writeFloat(weight);
        dest.writeString(token);
        dest.writeString(ut);
        dest.writeInt(insurance);
        dest.writeInt(productInsurance);
        dest.writeInt(orderValue);
        dest.writeString(categoryId);
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getShippingService() {
        return shippingService;
    }

    public void setShippingService(String shippingService) {
        this.shippingService = shippingService;
    }

    public String getShippingNames() {
        return shippingNames;
    }

    public void setShippingNames(String shippingNames) {
        this.shippingNames = shippingNames;
    }

    public String getOriginDistrictId() {
        return originDistrictId;
    }

    public void setOriginDistrictId(String originDistrictId) {
        this.originDistrictId = originDistrictId;
    }

    public String getOriginPostalCode() {
        return originPostalCode;
    }

    public void setOriginPostalCode(String originPostalCode) {
        this.originPostalCode = originPostalCode;
    }

    public Double getOriginLatitude() {
        return originLatitude;
    }

    public void setOriginLatitude(Double originLatitude) {
        this.originLatitude = originLatitude;
    }

    public Double getOriginLongitude() {
        return originLongitude;
    }

    public void setOriginLongitude(Double originLongitude) {
        this.originLongitude = originLongitude;
    }

    public String getDestinationDistrictId() {
        return destinationDistrictId;
    }

    public void setDestinationDistrictId(String destinationDistrictId) {
        this.destinationDistrictId = destinationDistrictId;
    }

    public String getDestinationPostalCode() {
        return destinationPostalCode;
    }

    public void setDestinationPostalCode(String destinationPostalCode) {
        this.destinationPostalCode = destinationPostalCode;
    }

    public Double getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(Double destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public Double getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(Double destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUt() {
        return ut;
    }

    public void setUt(String ut) {
        this.ut = ut;
    }

    public int getInsurance() {
        return insurance;
    }

    public void setInsurance(int insurance) {
        this.insurance = insurance;
    }

    public int getProductInsurance() {
        return productInsurance;
    }

    public void setProductInsurance(int productInsurance) {
        this.productInsurance = productInsurance;
    }

    public int getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(int orderValue) {
        this.orderValue = orderValue;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
