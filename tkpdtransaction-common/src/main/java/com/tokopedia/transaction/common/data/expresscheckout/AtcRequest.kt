package com.tokopedia.transaction.common.data.expresscheckout

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 03/01/19.
 */

data class AtcRequest(
        @SerializedName("shop_id")
        var shopId: Int? = 0,

        @SerializedName("quantity")
        var quantity: Int? = 0,

        @SerializedName("notes")
        var notes: String? = null,

        @SerializedName("product_id")
        var productId: Int? = null
) : Parcelable {
        constructor(parcel: Parcel? = null) : this(
                parcel?.readValue(Int::class.java.classLoader) as? Int ?: 0,
                parcel?.readValue(Int::class.java.classLoader) as? Int ?: 0,
                parcel?.readString() ?: "",
                parcel?.readValue(Int::class.java.classLoader) as? Int ?: 0) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeValue(shopId)
                parcel.writeValue(quantity)
                parcel.writeString(notes)
                parcel.writeValue(productId)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<AtcRequest> {
                override fun createFromParcel(parcel: Parcel): AtcRequest {
                        return AtcRequest(parcel)
                }

                override fun newArray(size: Int): Array<AtcRequest?> {
                        return arrayOfNulls(size)
                }
        }
}