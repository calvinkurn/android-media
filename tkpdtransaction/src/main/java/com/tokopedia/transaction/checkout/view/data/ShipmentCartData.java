package com.tokopedia.transaction.checkout.view.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.transaction.checkout.view.data.cartshipmentform.ShopShipment;

import java.util.List;

/**
 * Created by Irfan Khoirul on 22/02/18.
 */

public class ShipmentCartData implements Parcelable {
    private List<ShopShipment> shopShipments;
    private int deliveryPriceTotal;
    private String shippingServices;
    private String shippingNames;
    private String originDistrictId;
    private String originPostalCode;
    private Double originLatitude;
    private Double originLongitude;
    private String destinationDistrictId;
    private String destinationPostalCode;
    private Double destinationLatitude;
    private Double destinationLongitude;
    private String destinationAddress;
    private double weight;
    private String token;
    private String ut;
    private int insurance;
    private int productInsurance;
    private double orderValue;
    private int insurancePrice;
    private int additionalFee;
    private String categoryIds;

    public ShipmentCartData() {
    }

    protected ShipmentCartData(Parcel in) {
        shopShipments = in.createTypedArrayList(ShopShipment.CREATOR);
        deliveryPriceTotal = in.readInt();
        shippingServices = in.readString();
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
        destinationAddress = in.readString();
        weight = in.readDouble();
        token = in.readString();
        ut = in.readString();
        insurance = in.readInt();
        productInsurance = in.readInt();
        orderValue = in.readDouble();
        orderValue = in.readInt();
        insurancePrice = in.readInt();
        additionalFee = in.readInt();
        categoryIds = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(shopShipments);
        dest.writeInt(deliveryPriceTotal);
        dest.writeString(shippingServices);
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
        dest.writeString(destinationAddress);
        dest.writeDouble(weight);
        dest.writeString(token);
        dest.writeString(ut);
        dest.writeInt(insurance);
        dest.writeInt(productInsurance);
        dest.writeDouble(orderValue);
        dest.writeInt(insurancePrice);
        dest.writeInt(additionalFee);
        dest.writeString(categoryIds);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShipmentCartData> CREATOR = new Creator<ShipmentCartData>() {
        @Override
        public ShipmentCartData createFromParcel(Parcel in) {
            return new ShipmentCartData(in);
        }

        @Override
        public ShipmentCartData[] newArray(int size) {
            return new ShipmentCartData[size];
        }
    };

    public List<ShopShipment> getShopShipments() {
        return shopShipments;
    }

    public void setShopShipments(List<ShopShipment> shopShipments) {
        this.shopShipments = shopShipments;
    }

    public int getDeliveryPriceTotal() {
        return deliveryPriceTotal;
    }

    public void setDeliveryPriceTotal(int deliveryPriceTotal) {
        this.deliveryPriceTotal = deliveryPriceTotal;
    }

    public String getShippingServices() {
        return shippingServices;
    }

    public void setShippingServices(String shippingServices) {
        this.shippingServices = shippingServices;
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

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
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

    public double getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(double orderValue) {
        this.orderValue = orderValue;
    }

    public int getAdditionalFee() {
        return additionalFee;
    }

    public void setAdditionalFee(int additionalFee) {
        this.additionalFee = additionalFee;
    }

    public int getInsurancePrice() {
        return insurancePrice;
    }

    public void setInsurancePrice(int insurancePrice) {
        this.insurancePrice = insurancePrice;
    }

    public String getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(String categoryIds) {
        this.categoryIds = categoryIds;
    }
}
