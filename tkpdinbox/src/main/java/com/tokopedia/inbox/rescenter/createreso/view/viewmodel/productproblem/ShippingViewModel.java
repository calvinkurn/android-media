package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem;

/**
 * Created by yoasfs on 14/08/17.
 */

public class ShippingViewModel {
    public int id;
    public String name;
    public ShippingDetailViewModel detail;

    public ShippingViewModel(int id, String name, ShippingDetailViewModel detail) {
        this.id = id;
        this.name = name;
        this.detail = detail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ShippingDetailViewModel getDetail() {
        return detail;
    }

    public void setDetail(ShippingDetailViewModel detail) {
        this.detail = detail;
    }
}
