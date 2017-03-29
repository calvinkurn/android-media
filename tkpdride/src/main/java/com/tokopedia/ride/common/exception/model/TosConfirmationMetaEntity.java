package com.tokopedia.ride.common.exception.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 3/29/17.
 */

public class TosConfirmationMetaEntity {
    @SerializedName("tos_accept_confirmation")
    @Expose
    TosAcceptConfirmationEntity tosAcceptConfirmationEntity;

    public TosAcceptConfirmationEntity getTosAcceptConfirmationEntity() {
        return tosAcceptConfirmationEntity;
    }
}
