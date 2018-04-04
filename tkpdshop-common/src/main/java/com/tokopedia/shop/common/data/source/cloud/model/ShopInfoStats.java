
package com.tokopedia.shop.common.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopInfoStats {

    @SerializedName("favorite_count")
    @Expose
    private long favoriteCount;
    @SerializedName("hide_rate")
    @Expose
    private long hideRate;
    @SerializedName("rate_failure")
    @Expose
    private double rateFailure;
    @SerializedName("rate_success")
    @Expose
    private long rateSuccess;
    @SerializedName("shop_accuracy_description")
    @Expose
    private long shopAccuracyDescription;
    @SerializedName("shop_accuracy_rate")
    @Expose
    private long shopAccuracyRate;
    @SerializedName("shop_badge_level")
    @Expose
    private ShopInfoBadgeLevel shopBadgeLevel;
    @SerializedName("shop_item_sold")
    @Expose
    private String shopItemSold;
    @SerializedName("shop_last_one_month")
    @Expose
    private ShopInfoLastScore shopLastOneMonth;
    @SerializedName("shop_last_six_months")
    @Expose
    private ShopInfoLastScore shopLastSixMonths;
    @SerializedName("shop_last_twelve_months")
    @Expose
    private ShopInfoLastScore shopLastTwelveMonths;
    @SerializedName("shop_reputation_score")
    @Expose
    private String shopReputationScore;
    @SerializedName("shop_service_description")
    @Expose
    private long shopServiceDescription;
    @SerializedName("shop_service_rate")
    @Expose
    private long shopServiceRate;
    @SerializedName("shop_speed_description")
    @Expose
    private long shopSpeedDescription;
    @SerializedName("shop_speed_rate")
    @Expose
    private long shopSpeedRate;
    @SerializedName("shop_total_etalase")
    @Expose
    private String shopTotalEtalase;
    @SerializedName("shop_total_product")
    @Expose
    private String shopTotalProduct;
    @SerializedName("shop_total_transaction")
    @Expose
    private String shopTotalTransaction;
    @SerializedName("shop_total_transaction_canceled")
    @Expose
    private String shopTotalTransactionCanceled;
    @SerializedName("tx_count")
    @Expose
    private String txCount;
    @SerializedName("tx_count_success")
    @Expose
    private String txCountSuccess;

    public long getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(long favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public long getHideRate() {
        return hideRate;
    }

    public void setHideRate(long hideRate) {
        this.hideRate = hideRate;
    }

    public double getRateFailure() {
        return rateFailure;
    }

    public void setRateFailure(double rateFailure) {
        this.rateFailure = rateFailure;
    }

    public long getRateSuccess() {
        return rateSuccess;
    }

    public void setRateSuccess(long rateSuccess) {
        this.rateSuccess = rateSuccess;
    }

    public long getShopAccuracyDescription() {
        return shopAccuracyDescription;
    }

    public void setShopAccuracyDescription(long shopAccuracyDescription) {
        this.shopAccuracyDescription = shopAccuracyDescription;
    }

    public long getShopAccuracyRate() {
        return shopAccuracyRate;
    }

    public void setShopAccuracyRate(long shopAccuracyRate) {
        this.shopAccuracyRate = shopAccuracyRate;
    }

    public ShopInfoBadgeLevel getShopBadgeLevel() {
        return shopBadgeLevel;
    }

    public void setShopBadgeLevel(ShopInfoBadgeLevel shopBadgeLevel) {
        this.shopBadgeLevel = shopBadgeLevel;
    }

    public String getShopItemSold() {
        return shopItemSold;
    }

    public void setShopItemSold(String shopItemSold) {
        this.shopItemSold = shopItemSold;
    }

    public ShopInfoLastScore getShopLastOneMonth() {
        return shopLastOneMonth;
    }

    public void setShopLastOneMonth(ShopInfoLastScore shopLastOneMonth) {
        this.shopLastOneMonth = shopLastOneMonth;
    }

    public ShopInfoLastScore getShopLastSixMonths() {
        return shopLastSixMonths;
    }

    public void setShopLastSixMonths(ShopInfoLastScore shopLastSixMonths) {
        this.shopLastSixMonths = shopLastSixMonths;
    }

    public ShopInfoLastScore getShopLastTwelveMonths() {
        return shopLastTwelveMonths;
    }

    public void setShopLastTwelveMonths(ShopInfoLastScore shopLastTwelveMonths) {
        this.shopLastTwelveMonths = shopLastTwelveMonths;
    }

    public String getShopReputationScore() {
        return shopReputationScore;
    }

    public void setShopReputationScore(String shopReputationScore) {
        this.shopReputationScore = shopReputationScore;
    }

    public long getShopServiceDescription() {
        return shopServiceDescription;
    }

    public void setShopServiceDescription(long shopServiceDescription) {
        this.shopServiceDescription = shopServiceDescription;
    }

    public long getShopServiceRate() {
        return shopServiceRate;
    }

    public void setShopServiceRate(long shopServiceRate) {
        this.shopServiceRate = shopServiceRate;
    }

    public long getShopSpeedDescription() {
        return shopSpeedDescription;
    }

    public void setShopSpeedDescription(long shopSpeedDescription) {
        this.shopSpeedDescription = shopSpeedDescription;
    }

    public long getShopSpeedRate() {
        return shopSpeedRate;
    }

    public void setShopSpeedRate(long shopSpeedRate) {
        this.shopSpeedRate = shopSpeedRate;
    }

    public String getShopTotalEtalase() {
        return shopTotalEtalase;
    }

    public void setShopTotalEtalase(String shopTotalEtalase) {
        this.shopTotalEtalase = shopTotalEtalase;
    }

    public String getShopTotalProduct() {
        return shopTotalProduct;
    }

    public void setShopTotalProduct(String shopTotalProduct) {
        this.shopTotalProduct = shopTotalProduct;
    }

    public String getShopTotalTransaction() {
        return shopTotalTransaction;
    }

    public void setShopTotalTransaction(String shopTotalTransaction) {
        this.shopTotalTransaction = shopTotalTransaction;
    }

    public String getShopTotalTransactionCanceled() {
        return shopTotalTransactionCanceled;
    }

    public void setShopTotalTransactionCanceled(String shopTotalTransactionCanceled) {
        this.shopTotalTransactionCanceled = shopTotalTransactionCanceled;
    }

    public String getTxCount() {
        return txCount;
    }

    public void setTxCount(String txCount) {
        this.txCount = txCount;
    }

    public String getTxCountSuccess() {
        return txCountSuccess;
    }

    public void setTxCountSuccess(String txCountSuccess) {
        this.txCountSuccess = txCountSuccess;
    }

}
