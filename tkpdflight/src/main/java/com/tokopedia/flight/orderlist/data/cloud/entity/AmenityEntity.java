package com.tokopedia.flight.orderlist.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by alvarisi on 12/6/17.
 */

public class AmenityEntity {
    @SerializedName("cabin_baggage")
    @Expose
    private BaggageEntity cabinBaggage;
    @SerializedName("free_baggage")
    @Expose
    private BaggageEntity freeBaggage;
    @SerializedName("meal")
    @Expose
    private boolean meal;
    @SerializedName("usb_port")
    @Expose
    private boolean usbPort;
    @SerializedName("wifi")
    @Expose
    private boolean wifi;

    public AmenityEntity() {
    }

    public BaggageEntity getCabinBaggage() {
        return cabinBaggage;
    }

    public BaggageEntity getFreeBaggage() {
        return freeBaggage;
    }

    public boolean isMeal() {
        return meal;
    }

    public boolean isUsbPort() {
        return usbPort;
    }

    public boolean isWifi() {
        return wifi;
    }
}
