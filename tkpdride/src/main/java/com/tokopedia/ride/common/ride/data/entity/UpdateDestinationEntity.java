package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 3/24/17.
 */

public class UpdateDestinationEntity {
    @SerializedName("pending_payment")
    @Expose
    PendingPaymentEntity pendingPayment;


    public UpdateDestinationEntity() {
    }

    public PendingPaymentEntity getPendingPayment() {
        return pendingPayment;
    }
}
