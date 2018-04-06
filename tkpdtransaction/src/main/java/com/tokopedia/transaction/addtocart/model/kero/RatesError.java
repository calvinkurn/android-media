package com.tokopedia.transaction.addtocart.model.kero;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sachinbansal on 4/6/18.
 */

public class RatesError {

    @SerializedName("errors")
    @Expose
    private List<Error> errors = null;

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

}
