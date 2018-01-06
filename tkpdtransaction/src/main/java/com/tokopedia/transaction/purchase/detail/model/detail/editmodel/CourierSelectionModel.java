package com.tokopedia.transaction.purchase.detail.model.detail.editmodel;

/**
 * Created by kris on 1/5/18. Tokopedia
 */

public class CourierSelectionModel {

    private String courierId;

    private String serviceId;

    private String courierName;

    private String serviceName;


    public String getCourierId() {
        return courierId;
    }

    public void setCourierId(String courierId) {
        this.courierId = courierId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
