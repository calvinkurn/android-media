package com.tokopedia.gm.subscribe.domain.product.model;

import java.util.List;

/**
 * Created by sebastianuskh on 1/26/17.
 */

public class GmProductDomainModelGroup {
    private List<GmProductDomainModel> currentProduct;
    private List<GmProductDomainModel> extendProduct;
    private String paymentMethod;

    public List<GmProductDomainModel> getCurrentProduct() {
        return currentProduct;
    }

    public void setCurrentProduct(List<GmProductDomainModel> currentProduct) {
        this.currentProduct = currentProduct;
    }

    public List<GmProductDomainModel> getExtendProduct() {
        return extendProduct;
    }

    public void setExtendProduct(List<GmProductDomainModel> extendProduct) {
        this.extendProduct = extendProduct;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
