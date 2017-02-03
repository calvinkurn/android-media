package com.tokopedia.seller.gmsubscribe.view.checkout.viewmodel;

/**
 * Created by sebastianuskh on 1/27/17.
 */
public class GMCheckoutCurrentSelectedViewModel {
    private String title;
    private String description;
    private String price;

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
