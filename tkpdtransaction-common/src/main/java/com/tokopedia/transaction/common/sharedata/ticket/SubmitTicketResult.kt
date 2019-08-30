package com.tokopedia.transaction.common.sharedata.ticket

import android.os.Parcel
import android.os.Parcelable

data class SubmitTicketResult(
        var status: Boolean = true,
        var message: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readByte() != 0.toByte(),
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (status) 1 else 0)
        parcel.writeString(message)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SubmitTicketResult> {
        override fun createFromParcel(parcel: Parcel): SubmitTicketResult {
            return SubmitTicketResult(parcel)
        }

        override fun newArray(size: Int): Array<SubmitTicketResult?> {
            return arrayOfNulls(size)
        }
    }
}