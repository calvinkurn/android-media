package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem;

/**
 * Created by yoasfs on 14/08/17.
 */

public class OrderViewModel {
    private OrderDetailViewModel detail;
    private ProductViewModel product;
    private ShippingViewModel shipping;

    public OrderViewModel(OrderDetailViewModel detail, ProductViewModel product, ShippingViewModel shipping) {
        this.detail = detail;
        this.product = product;
        this.shipping = shipping;
    }

    public OrderDetailViewModel getDetail() {
        return detail;
    }

    public void setDetail(OrderDetailViewModel detail) {
        this.detail = detail;
    }

    public ProductViewModel getProduct() {
        return product;
    }

    public void setProduct(ProductViewModel product) {
        this.product = product;
    }

    public ShippingViewModel getShipping() {
        return shipping;
    }

    public void setShipping(ShippingViewModel shipping) {
        this.shipping = shipping;
    }
}
