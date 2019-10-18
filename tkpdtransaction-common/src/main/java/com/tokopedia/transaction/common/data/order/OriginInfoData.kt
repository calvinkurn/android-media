package com.tokopedia.transaction.common.data.order

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by fwidjaja on 2019-10-18.
 */
data class OriginInfoData (
        val originAddress: String = ""): Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(originAddress)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OriginInfoData> {
        override fun createFromParcel(parcel: Parcel): OriginInfoData {
            return OriginInfoData(parcel)
        }

        override fun newArray(size: Int): Array<OriginInfoData?> {
            return arrayOfNulls(size)
        }
    }
}