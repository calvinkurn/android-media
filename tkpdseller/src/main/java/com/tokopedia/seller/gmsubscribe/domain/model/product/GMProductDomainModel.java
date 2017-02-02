package com.tokopedia.seller.gmsubscribe.domain.model.product;

/**
 * Created by sebastianuskh on 2/2/17.
 */
public class GMProductDomainModel {
    private Integer productId;
    private String price;
    private boolean bestDeal;
    private String nextInv;
    private String freeDays;
    private String lastPrice;
    private String name;
    private String notes;

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setBestDeal(boolean bestDeal) {
        this.bestDeal = bestDeal;
    }

    public void setNextInv(String nextInv) {
        this.nextInv = nextInv;
    }

    public void setFreeDays(String freeDays) {
        this.freeDays = freeDays;
    }

    public void setLastPrice(String lastPrice) {
        this.lastPrice = lastPrice;
    }

    public Integer getProductId() {
        return productId;
    }

    public String getPrice() {
        return price;
    }

    public boolean isBestDeal() {
        return bestDeal;
    }

    public String getNextInv() {
        return nextInv;
    }

    public String getFreeDays() {
        return freeDays;
    }

    public String getLastPrice() {
        return lastPrice;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
