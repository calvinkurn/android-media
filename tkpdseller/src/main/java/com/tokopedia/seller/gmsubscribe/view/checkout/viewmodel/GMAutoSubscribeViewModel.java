package com.tokopedia.seller.gmsubscribe.view.checkout.viewmodel;

/**
 * Created by sebastianuskh on 1/30/17.
 */
public class GMAutoSubscribeViewModel {
    private String title;
    private String price;
    private String nextAutoSubscribe;
    private String paymentMethod;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setNextAutoSubscribe(String nextAutoSubscribe) {
        this.nextAutoSubscribe = nextAutoSubscribe;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getNextAutoSubscribe() {
        return nextAutoSubscribe;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
}
