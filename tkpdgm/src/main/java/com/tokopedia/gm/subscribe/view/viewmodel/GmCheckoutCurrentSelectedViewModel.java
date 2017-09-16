package com.tokopedia.gm.subscribe.view.viewmodel;

import com.tokopedia.gm.subscribe.domain.product.model.GmProductDomainModel;

/**
 * Created by sebastianuskh on 1/27/17.
 */
public class GmCheckoutCurrentSelectedViewModel {
    private String title;
    private String description;
    private String price;

    public GmCheckoutCurrentSelectedViewModel(GmProductDomainModel domainModel) {
        setTitle(domainModel.getName());
        setDescription(domainModel.getNotes());
        if (domainModel.getLastPrice().isEmpty()) {
            setPrice(domainModel.getPrice());
        } else {
            setPrice(domainModel.getLastPrice());
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
