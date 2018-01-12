
package com.tokopedia.seller.shop.open.data.model.response.isreservedomain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class Shipment implements Parcelable {

    @SerializedName("addr_street")
    @Expose
    private String addrStreet;
    @SerializedName("diff_district")
    @Expose
    private String diffDistrict;
    @SerializedName("district_id")
    @Expose
    private int districtId;
    @SerializedName("geolocation_checksum")
    @Expose
    private String geolocationChecksum;
    @SerializedName("i_drop")
    @Expose
    private String iDrop;
    @SerializedName("jne_fee_flag")
    @Expose
    private String jneFeeFlag;
    @SerializedName("jne_fee_value")
    @Expose
    private int jneFeeValue;
    @SerializedName("jne_min_weight_value")
    @Expose
    private int jneMinWeightValue;
    @SerializedName("jne_tiket")
    @Expose
    private String jneTiket;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("min_weight")
    @Expose
    private int minWeight;
    @SerializedName("min_weight_flag")
    @Expose
    private String minWeightFlag;
    @SerializedName("min_weight_val")
    @Expose
    private int minWeightVal;
    @SerializedName("package_list")
    @Expose
    private Object packageList;
    @SerializedName("postal")
    @Expose
    private int postal;
    @SerializedName("shipment_options")
    @Expose
    private Object shipmentOptions;
    @SerializedName("shippings")
    @Expose
    private String shippings;
    @SerializedName("shop_id")
    @Expose
    private int shopId;
    @SerializedName("tiki_fee_flag")
    @Expose
    private String tikiFeeFlag;
    @SerializedName("tiki_fee_value")
    @Expose
    private int tikiFeeValue;
    @SerializedName("user_id")
    @Expose
    private int userId;

    public String getAddrStreet() {
        return addrStreet;
    }

    public void setAddrStreet(String addrStreet) {
        this.addrStreet = addrStreet;
    }

    public String getDiffDistrict() {
        return diffDistrict;
    }

    public void setDiffDistrict(String diffDistrict) {
        this.diffDistrict = diffDistrict;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getGeolocationChecksum() {
        return geolocationChecksum;
    }

    public void setGeolocationChecksum(String geolocationChecksum) {
        this.geolocationChecksum = geolocationChecksum;
    }

    public String getIDrop() {
        return iDrop;
    }

    public void setIDrop(String iDrop) {
        this.iDrop = iDrop;
    }

    public String getJneFeeFlag() {
        return jneFeeFlag;
    }

    public void setJneFeeFlag(String jneFeeFlag) {
        this.jneFeeFlag = jneFeeFlag;
    }

    public int getJneFeeValue() {
        return jneFeeValue;
    }

    public void setJneFeeValue(int jneFeeValue) {
        this.jneFeeValue = jneFeeValue;
    }

    public int getJneMinWeightValue() {
        return jneMinWeightValue;
    }

    public void setJneMinWeightValue(int jneMinWeightValue) {
        this.jneMinWeightValue = jneMinWeightValue;
    }

    public String getJneTiket() {
        return jneTiket;
    }

    public void setJneTiket(String jneTiket) {
        this.jneTiket = jneTiket;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getMinWeight() {
        return minWeight;
    }

    public void setMinWeight(int minWeight) {
        this.minWeight = minWeight;
    }

    public String getMinWeightFlag() {
        return minWeightFlag;
    }

    public void setMinWeightFlag(String minWeightFlag) {
        this.minWeightFlag = minWeightFlag;
    }

    public int getMinWeightVal() {
        return minWeightVal;
    }

    public void setMinWeightVal(int minWeightVal) {
        this.minWeightVal = minWeightVal;
    }

    public JSONObject getPackageList() {
        try {
            return packageList == null ? null : new JSONObject(packageList.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getPostal() {
        return postal;
    }

    public void setPostal(int postal) {
        this.postal = postal;
    }

    public JSONObject getShipmentOptions() {
        try {
            return shipmentOptions == null ? null : new JSONObject(shipmentOptions.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getShippings() {
        return shippings;
    }

    public void setShippings(String shippings) {
        this.shippings = shippings;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getTikiFeeFlag() {
        return tikiFeeFlag;
    }

    public void setTikiFeeFlag(String tikiFeeFlag) {
        this.tikiFeeFlag = tikiFeeFlag;
    }

    public int getTikiFeeValue() {
        return tikiFeeValue;
    }

    public void setTikiFeeValue(int tikiFeeValue) {
        this.tikiFeeValue = tikiFeeValue;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.addrStreet);
        dest.writeString(this.diffDistrict);
        dest.writeInt(this.districtId);
        dest.writeString(this.geolocationChecksum);
        dest.writeString(this.iDrop);
        dest.writeString(this.jneFeeFlag);
        dest.writeInt(this.jneFeeValue);
        dest.writeInt(this.jneMinWeightValue);
        dest.writeString(this.jneTiket);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeInt(this.minWeight);
        dest.writeString(this.minWeightFlag);
        dest.writeInt(this.minWeightVal);
        dest.writeString(this.packageList==null?"":this.packageList.toString());
        dest.writeInt(this.postal);
        dest.writeString(this.shipmentOptions==null?"":this.shipmentOptions.toString());
        dest.writeString(this.shippings);
        dest.writeInt(this.shopId);
        dest.writeString(this.tikiFeeFlag);
        dest.writeInt(this.tikiFeeValue);
        dest.writeInt(this.userId);
    }

    public Shipment() {
    }

    protected Shipment(Parcel in) {
        this.addrStreet = in.readString();
        this.diffDistrict = in.readString();
        this.districtId = in.readInt();
        this.geolocationChecksum = in.readString();
        this.iDrop = in.readString();
        this.jneFeeFlag = in.readString();
        this.jneFeeValue = in.readInt();
        this.jneMinWeightValue = in.readInt();
        this.jneTiket = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.minWeight = in.readInt();
        this.minWeightFlag = in.readString();
        this.minWeightVal = in.readInt();
        this.packageList = in.readString();
        this.postal = in.readInt();
        this.shipmentOptions = in.readString();
        this.shippings = in.readString();
        this.shopId = in.readInt();
        this.tikiFeeFlag = in.readString();
        this.tikiFeeValue = in.readInt();
        this.userId = in.readInt();
    }

    public static final Creator<Shipment> CREATOR = new Creator<Shipment>() {
        @Override
        public Shipment createFromParcel(Parcel source) {
            return new Shipment(source);
        }

        @Override
        public Shipment[] newArray(int size) {
            return new Shipment[size];
        }
    };
}
