package com.tokopedia.seller.gmsubscribe.view.checkout.viewmodel;

import com.tokopedia.seller.gmsubscribe.domain.product.model.GMProductDomainModel;

/**
 * Created by sebastianuskh on 1/27/17.
 */
public class GMCheckoutCurrentSelectedViewModel {
    private String title;
    private String description;
    private String price;

    public GMCheckoutCurrentSelectedViewModel(GMProductDomainModel domainModel) {
        setTitle(domainModel.getName());
        setPrice(domainModel.getNotes());
        setPrice(domainModel.getPrice());
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
