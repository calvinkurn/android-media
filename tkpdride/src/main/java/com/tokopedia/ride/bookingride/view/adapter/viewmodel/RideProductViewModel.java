package com.tokopedia.ride.bookingride.view.adapter.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.ride.bookingride.view.adapter.factory.RideProductTypeFactory;

/**
 * Created by alvarisi on 3/16/17.
 */

public class RideProductViewModel implements Visitable<RideProductTypeFactory> {
    private String productId;
    private String productImage;
    private String productName;
    private String timeEstimate;
    private boolean surgePrice;
    private String productPrice;
    private String baseFare;

    public RideProductViewModel() {
    }

    @Override
    public int type(RideProductTypeFactory favoriteTypeFactory) {
        return favoriteTypeFactory.type(this);
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTimeEstimate() {
        return timeEstimate;
    }

    public void setTimeEstimate(String timeEstimate) {
        this.timeEstimate = timeEstimate;
    }

    public boolean isSurgePrice() {
        return surgePrice;
    }

    public void setSurgePrice(boolean surgePrice) {
        this.surgePrice = surgePrice;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(String baseFare) {
        this.baseFare = baseFare;
    }
}
