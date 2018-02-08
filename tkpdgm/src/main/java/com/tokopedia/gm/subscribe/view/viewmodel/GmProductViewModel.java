package com.tokopedia.gm.subscribe.view.viewmodel;

import com.tokopedia.gm.subscribe.domain.product.model.GmProductDomainModel;

/**
 * Created by sebastianuskh on 1/26/17.
 */
public class GmProductViewModel {
    private String productId;
    private String price;
    private boolean bestDeal;
    private String nextInv;
    private String freeDays;
    private String lastPrice;
    private String name;
    private String notes;

    public GmProductViewModel(GmProductDomainModel domainModel) {
        setProductId(String.valueOf(domainModel.getProductId()));
        setName(domainModel.getName());
        setNotes(domainModel.getNotes());
        setPrice(domainModel.getPrice());
        setBestDeal(domainModel.isBestDeal());
        setFreeDays(domainModel.getFreeDays());
        setNextInv(domainModel.getNextInv());
        setLastPrice(domainModel.getLastPrice());
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
