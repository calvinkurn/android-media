package com.tokopedia.seller.gmsubscribe.domain.product.model;

import java.util.List;

/**
 * Created by sebastianuskh on 1/26/17.
 */

public class GMProductDomainModelGroup {
    private List<GMProductDomainModel> currentProduct;
    private List<GMProductDomainModel> extendProduct;
    private String paymentMethod;

    public void setCurrentProduct(List<GMProductDomainModel> currentProduct) {
        this.currentProduct = currentProduct;
    }

    public void setExtendProduct(List<GMProductDomainModel> extendProduct) {
        this.extendProduct = extendProduct;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<GMProductDomainModel> getCurrentProduct() {
        return currentProduct;
    }

    public List<GMProductDomainModel> getExtendProduct() {
        return extendProduct;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
}
