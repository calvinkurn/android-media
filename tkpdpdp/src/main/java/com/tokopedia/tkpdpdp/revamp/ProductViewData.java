package com.tokopedia.tkpdpdp.revamp;

import com.tokopedia.tkpdpdp.courier.CourierViewData;

import java.util.ArrayList;

public class ProductViewData {
    private String productId;
    private ArrayList<CourierViewData> courierList;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public ArrayList<CourierViewData> getCourierList() {
        return courierList;
    }

    public void setCourierList(ArrayList<CourierViewData> courierList) {
        this.courierList = courierList;
    }
}
