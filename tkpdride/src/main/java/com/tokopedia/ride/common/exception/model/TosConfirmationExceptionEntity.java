package com.tokopedia.ride.common.exception.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alvarisi on 3/29/17.
 */

public class TosConfirmationExceptionEntity {
    @SerializedName("errors")
    @Expose
    List<ErrorHttpEntity> errors;

    @SerializedName("meta")
    @Expose
    TosConfirmationMetaEntity meta;

    public TosConfirmationExceptionEntity() {
    }

    public List<ErrorHttpEntity> getErrors() {
        return errors;
    }

    public TosConfirmationMetaEntity getMeta() {
        return meta;
    }
}
