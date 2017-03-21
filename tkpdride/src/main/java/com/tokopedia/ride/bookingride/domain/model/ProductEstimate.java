package com.tokopedia.ride.bookingride.domain.model;

import com.tokopedia.ride.common.ride.domain.model.Product;
import com.tokopedia.ride.common.ride.domain.model.TimesEstimate;

/**
 * Created by alvarisi on 3/20/17.
 */

public class ProductEstimate {
    private Product product;
    private TimesEstimate timesEstimate;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public TimesEstimate getTimesEstimate() {
        return timesEstimate;
    }

    public void setTimesEstimate(TimesEstimate timesEstimate) {
        this.timesEstimate = timesEstimate;
    }
}
