package com.tokopedia.transaction.cart.model.calculateshipment;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by alvarisi on 11/3/16.
 */

public class ShipmentPackage implements Parcelable {

    @SerializedName("price_total")
    @Expose
    private String priceTotal;
    @SerializedName("shipment_id")
    @Expose
    private String shipmentId;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("sp_id")
    @Expose
    private String shipmentPackageId;
    @SerializedName("is_show_map")
    @Expose
    private int isShowMap;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("package_available")
    @Expose
    private Integer packageAvailable;

    protected ShipmentPackage(Parcel in) {
        priceTotal = in.readString();
        shipmentId = in.readString();
        desc = in.readString();
        name = in.readString();
        shipmentPackageId = in.readString();
        isShowMap = in.readInt();
        price = in.readString();
    }

    public static final Creator<ShipmentPackage> CREATOR = new Creator<ShipmentPackage>() {
        @Override
        public ShipmentPackage createFromParcel(Parcel in) {
            return new ShipmentPackage(in);
        }

        @Override
        public ShipmentPackage[] newArray(int size) {
            return new ShipmentPackage[size];
        }
    };

    public ShipmentPackage() {
    }

    /**
     * @return The priceTotal
     */
    public String getPriceTotal() {
        return priceTotal;
    }

    /**
     * @param priceTotal The price_total
     */
    public void setPriceTotal(String priceTotal) {
        this.priceTotal = priceTotal;
    }

    /**
     * @return The shipmentId
     */
    public String getShipmentId() {
        return shipmentId;
    }

    /**
     * @param shipmentId The shipment_id
     */
    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    /**
     * @return The desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc The desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The shipmentPackageId
     */
    public String getShipmentPackageId() {
        return shipmentPackageId;
    }

    /**
     * @param shipmentPackageId The sp_id
     */
    public void setShipmentPackageId(String shipmentPackageId) {
        this.shipmentPackageId = shipmentPackageId;
    }

    /**
     * @return The price
     */
    public String getPrice() {
        return price;
    }

    /**
     * @param price The price
     */
    public void setPrice(String price) {
        this.price = price;
    }

    public int getPackageAvailable() {
        return packageAvailable == null ? price == null || price.equals("0")
                || price.equals("") ? 0 : 1 : packageAvailable;
    }

    public void setPackageAvailable(int packageAvailable) {
        this.packageAvailable = packageAvailable;
    }

    public int getShowMap() {
        return isShowMap;
    }

    @Override
    public String toString() {
        return name;
    }

    public static ShipmentPackage createSelectionInfo(String info) {
        ShipmentPackage shipment = new ShipmentPackage();
        shipment.setName(info);
        shipment.setShipmentPackageId("0");
        shipment.setPrice("0");
        shipment.setShipmentId(String.valueOf(0));
        shipment.isShowMap = 0;
        return shipment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(priceTotal);
        parcel.writeString(shipmentId);
        parcel.writeString(desc);
        parcel.writeString(name);
        parcel.writeString(shipmentPackageId);
        parcel.writeInt(isShowMap);
        parcel.writeString(price);
    }

    public int getIsShowMap() {
        return isShowMap;
    }

    public void setIsShowMap(int isShowMap) {
        this.isShowMap = isShowMap;
    }
}