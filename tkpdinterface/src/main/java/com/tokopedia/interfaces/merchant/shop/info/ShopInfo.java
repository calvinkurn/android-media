
package com.tokopedia.interfaces.merchant.shop.info;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopInfo {

    @SerializedName("address")
    @Expose
    private List<ShopInfoAddress> address = null;
    @SerializedName("closed_info")
    @Expose
    private ShopInfoClosedInfo closedInfo;
    @SerializedName("info")
    @Expose
    private ShopInfoDetail info;
    @SerializedName("is_open")
    @Expose
    private long isOpen;
    @SerializedName("owner")
    @Expose
    private ShopInfoOwner owner;
    @SerializedName("payment")
    @Expose
    private List<ShopInfoPayment> payment = null;
    @SerializedName("ratings")
    @Expose
    private ShopInfoRatings ratings;
    @SerializedName("shipment")
    @Expose
    private List<ShopInfoShipment> shipment = null;
    @SerializedName("shop_tx_stats")
    @Expose
    private ShopInfoTxStats shopTxStats;
    @SerializedName("stats")
    @Expose
    private ShopInfoStats stats;
    @SerializedName("use_ace")
    @Expose
    private long useAce;

    public List<ShopInfoAddress> getAddress() {
        return address;
    }

    public void setAddress(List<ShopInfoAddress> address) {
        this.address = address;
    }

    public ShopInfoClosedInfo getClosedInfo() {
        return closedInfo;
    }

    public void setClosedInfo(ShopInfoClosedInfo closedInfo) {
        this.closedInfo = closedInfo;
    }

    public ShopInfoDetail getInfo() {
        return info;
    }

    public void setInfo(ShopInfoDetail info) {
        this.info = info;
    }

    public long getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(long isOpen) {
        this.isOpen = isOpen;
    }

    public ShopInfoOwner getOwner() {
        return owner;
    }

    public void setOwner(ShopInfoOwner owner) {
        this.owner = owner;
    }

    public List<ShopInfoPayment> getPayment() {
        return payment;
    }

    public void setPayment(List<ShopInfoPayment> payment) {
        this.payment = payment;
    }

    public ShopInfoRatings getRatings() {
        return ratings;
    }

    public void setRatings(ShopInfoRatings ratings) {
        this.ratings = ratings;
    }

    public List<ShopInfoShipment> getShipment() {
        return shipment;
    }

    public void setShipment(List<ShopInfoShipment> shipment) {
        this.shipment = shipment;
    }

    public ShopInfoTxStats getShopTxStats() {
        return shopTxStats;
    }

    public void setShopTxStats(ShopInfoTxStats shopTxStats) {
        this.shopTxStats = shopTxStats;
    }

    public ShopInfoStats getStats() {
        return stats;
    }

    public void setStats(ShopInfoStats stats) {
        this.stats = stats;
    }

    public long getUseAce() {
        return useAce;
    }

    public void setUseAce(long useAce) {
        this.useAce = useAce;
    }

}
