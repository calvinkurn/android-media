
package com.tokopedia.flight.review.domain.verifybooking.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MetaData {

    @SerializedName("cart_id")
    @Expose
    private String cartId;
    @SerializedName("invoice_id")
    @Expose
    private String invoiceId;


    public String getCartId() {
        return cartId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }
}
