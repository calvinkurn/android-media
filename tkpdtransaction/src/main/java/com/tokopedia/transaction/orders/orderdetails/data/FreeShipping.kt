
package com.tokopedia.transaction.orders.orderdetails.data

import com.google.gson.annotations.SerializedName

import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
class FreeShipping {

    @SerializedName("imageUrl")
    var imageUrl: String? = null

    @SerializedName("isEligible")
    var isEligible: Boolean = false

    override fun toString(): String {
        return "FreeShipping{" +
                "imageUrl = '" + imageUrl + '\''.toString() +
                ",isEligible = '" + isEligible + '\''.toString() +
                "}"
    }
} 