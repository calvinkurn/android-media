package com.tokopedia.seller.gmsubscribe.view.product.viewmodel;

/**
 * Created by sebastianuskh on 1/26/17.
 */
public class GMProductViewModel {
    private String productId;
    private String price;
    private boolean bestDeal;
    private String nextInv;
    private String freeDays;
    private String lastPrice;
    private String name;
    private String notes;

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public String getPrice() {
        return price;
    }

    public boolean isBestDeal() {
        return bestDeal;
    }

    public void setBestDeal(boolean bestDeal) {
        this.bestDeal = bestDeal;
    }

    public String getNextInv() {
        return nextInv;
    }

    public void setNextInv(String nextInv) {
        this.nextInv = nextInv;
    }

    public String getFreeDays() {
        return freeDays;
    }

    public void setFreeDays(String freeDays) {
        this.freeDays = freeDays;
    }

    public String getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(String lastPrice) {
        this.lastPrice = lastPrice;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public String getName() {
        return name;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
