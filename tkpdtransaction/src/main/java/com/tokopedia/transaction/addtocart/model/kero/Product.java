package com.tokopedia.transaction.addtocart.model.kero;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Herdi_WORK on 22.09.16.
 */

public class Product implements Parcelable {
    @SerializedName("shipper_product_id")
    @Expose
    private String shipperProductId;
    @SerializedName("shipper_product_name")
    @Expose
    private String shipperProductName;
    @SerializedName("shipper_product_desc")
    @Expose
    private String shipperProductDesc;
    @SerializedName("is_show_map")
    @Expose
    private Integer isShowMap;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("formatted_price")
    @Expose
    private String formattedPrice;
    @SerializedName("check_sum")
    @Expose
    private String checkSum;
    @SerializedName("ut")
    @Expose
    private String ut;
    @SerializedName("insurance_price")
    @Expose
    private Integer insurancePrice;
    @SerializedName("insurance_type")
    @Expose
    private Integer insuranceMode;
    @SerializedName("insurance_type_info")
    @Expose
    private String insuranceTypeInfo;
    @SerializedName("weight_product")
    @Expose
    private int weightProduct;
    @SerializedName("weight_order_spid")
    @Expose
    private int weightOrderSpid;
    @SerializedName("insurance_used_type")
    @Expose
    private int insuranceUsedType;
    @SerializedName("insurance_used_info")
    @Expose
    private String insuranceUsedInfo;
    @SerializedName("insurance_used_default")
    @Expose
    private int insuranceUsedDefault;
    @SerializedName("max_hours_id")
    @Expose
    private String maxHoursId;
    @SerializedName("desc_hours_id")
    @Expose
    private String descHoursId;

    public Product() {
    }

    /**
     * @return The shipperProductId
     */
    public String getShipperProductId() {
        return shipperProductId;
    }

    /**
     * @param shipperProductId The shipper_product_id
     */
    public void setShipperProductId(String shipperProductId) {
        this.shipperProductId = shipperProductId;
    }

    /**
     * @return The shipperProductName
     */
    public String getShipperProductName() {
        return shipperProductName;
    }

    /**
     * @param shipperProductName The shipper_product_name
     */
    public void setShipperProductName(String shipperProductName) {
        this.shipperProductName = shipperProductName;
    }

    /**
     * @return The shipperProductDesc
     */
    public String getShipperProductDesc() {
        return shipperProductDesc;
    }

    /**
     * @param shipperProductDesc The shipper_product_desc
     */
    public void setShipperProductDesc(String shipperProductDesc) {
        this.shipperProductDesc = shipperProductDesc;
    }

    /**
     * @return The isShowMap
     */
    public Integer getIsShowMap() {
        return isShowMap;
    }

    /**
     * @param isShowMap The is_show_map
     */
    public void setIsShowMap(Integer isShowMap) {
        this.isShowMap = isShowMap;
    }

    /**
     * @return The price
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * @param price The price
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    /**
     * @return The formattedPrice
     */
    public String getFormattedPrice() {
        return formattedPrice;
    }

    /**
     * @param formattedPrice The formatted_price
     */
    public void setFormattedPrice(String formattedPrice) {
        this.formattedPrice = formattedPrice;
    }

    /**
     * @return The checkSum
     */
    public String getCheckSum() {
        return checkSum;
    }

    /**
     * @param checkSum The check_sum
     */
    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    /**
     * @return The ut
     */
    public String getUt() {
        return ut;
    }

    /**
     * @param ut The ut
     */
    public void setUt(String ut) {
        this.ut = ut;
    }

    public String getMaxHoursId() {
        return maxHoursId;
    }

    public void setMaxHoursId(String maxHoursId) {
        this.maxHoursId = maxHoursId;
    }

    public String getDescHoursId() {
        return descHoursId;
    }

    public void setDescHoursId(String descHoursId) {
        this.descHoursId = descHoursId;
    }

    public Integer getInsurancePrice() {
        return insurancePrice;
    }

    public void setInsurancePrice(Integer insurancePrice) {
        this.insurancePrice = insurancePrice;
    }

    public Integer getInsuranceMode() {
        return insuranceMode;
    }

    public void setInsuranceMode(Integer insuranceMode) {
        this.insuranceMode = insuranceMode;
    }

    public String getInsuranceTypeInfo() {
        return insuranceTypeInfo;
    }

    public void setInsuranceTypeInfo(String insuranceTypeInfo) {
        this.insuranceTypeInfo = insuranceTypeInfo;
    }

    public Integer getWeightProduct() {
        return weightProduct;
    }

    public void setWeightProduct(Integer weightProduct) {
        this.weightProduct = weightProduct;
    }

    public Integer getWeightOrderSpid() {
        return weightOrderSpid;
    }

    public void setWeightOrderSpid(Integer weightOrderSpid) {
        this.weightOrderSpid = weightOrderSpid;
    }

    public Integer getInsuranceUsedType() {
        return insuranceUsedType;
    }

    public void setInsuranceUsedType(Integer insuranceUsedType) {
        this.insuranceUsedType = insuranceUsedType;
    }

    public String getInsuranceUsedInfo() {
        return insuranceUsedInfo;
    }

    public void setInsuranceUsedInfo(String insuranceUsedInfo) {
        this.insuranceUsedInfo = insuranceUsedInfo;
    }

    public Integer getInsuranceUsedDefault() {
        return insuranceUsedDefault;
    }

    public void setInsuranceUsedDefault(Integer insuranceUsedDefault) {
        this.insuranceUsedDefault = insuranceUsedDefault;
    }

    @Override
    public String toString() {
        return getShipperProductName();
    }

    public static Product createSelectionInfo(String info) {
        Product shipment = new Product();
        shipment.setShipperProductName(info);
        shipment.setShipperProductId("0");
        shipment.setIsShowMap(0);
        shipment.setPrice(0);
        shipment.setFormattedPrice("0");
        return shipment;
    }

    protected Product(Parcel in) {
        shipperProductId = in.readString();
        shipperProductName = in.readString();
        shipperProductDesc = in.readString();
        isShowMap = in.readByte() == 0x00 ? null : in.readInt();
        price = in.readByte() == 0x00 ? null : in.readInt();
        formattedPrice = in.readString();
        checkSum = in.readString();
        ut = in.readString();
        maxHoursId = in.readString();
        descHoursId = in.readString();
        insurancePrice = in.readByte() == 0x00 ? null : in.readInt();
        insuranceMode = in.readByte() == 0x00 ? null : in.readInt();
        insuranceTypeInfo = in.readString();
        weightProduct = in.readInt();
        weightOrderSpid = in.readInt();
        insuranceUsedType = in.readInt();
        insuranceUsedInfo = in.readString();
        insuranceUsedDefault = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shipperProductId);
        dest.writeString(shipperProductName);
        dest.writeString(shipperProductDesc);
        if (isShowMap == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(isShowMap);
        }
        if (price == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(price);
        }
        dest.writeString(formattedPrice);
        dest.writeString(checkSum);
        dest.writeString(ut);
        dest.writeString(maxHoursId);
        dest.writeString(descHoursId);
        if (insurancePrice == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(insurancePrice);
        }
        if (insuranceMode == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(insuranceMode);
        }
        dest.writeString(insuranceTypeInfo);
        dest.writeInt(weightProduct);
        dest.writeInt(weightOrderSpid);
        dest.writeInt(insuranceUsedType);
        dest.writeString(insuranceUsedInfo);
        dest.writeInt(insuranceUsedDefault);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
