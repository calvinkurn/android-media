package com.tokopedia.transaction.common.sharedata;

import android.os.Bundle;

/**
 * Created by Irfan Khoirul on 08/03/19.
 * Store data needed to load Shipment Form / Checkout Page.
 */

public class ShipmentFormRequest {

    public static final String EXTRA_DEVICE_ID = "EXTRA_DEVICE_ID";
    private String deviceId;

    public ShipmentFormRequest(String deviceId) {
        this.deviceId = deviceId;
    }

    public Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_DEVICE_ID, deviceId);

        return bundle;
    }

    public static final class BundleBuilder {
        private String deviceId;

        public BundleBuilder() {
        }

        // Add this data for trade in feature
        public BundleBuilder deviceId(String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public ShipmentFormRequest build() {
            return new ShipmentFormRequest(deviceId);
        }
    }
}
