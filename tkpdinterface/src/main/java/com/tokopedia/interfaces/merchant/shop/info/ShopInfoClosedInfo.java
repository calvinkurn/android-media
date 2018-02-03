
package com.tokopedia.interfaces.merchant.shop.info;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopInfoClosedInfo {

    @SerializedName("note")
    @Expose
    private long note;
    @SerializedName("reason")
    @Expose
    private long reason;
    @SerializedName("until")
    @Expose
    private long until;

    public long getNote() {
        return note;
    }

    public void setNote(long note) {
        this.note = note;
    }

    public long getReason() {
        return reason;
    }

    public void setReason(long reason) {
        this.reason = reason;
    }

    public long getUntil() {
        return until;
    }

    public void setUntil(long until) {
        this.until = until;
    }

}
