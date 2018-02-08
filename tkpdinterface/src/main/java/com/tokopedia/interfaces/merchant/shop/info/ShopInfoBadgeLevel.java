
package com.tokopedia.interfaces.merchant.shop.info;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopInfoBadgeLevel {

    @SerializedName("level")
    @Expose
    private long level;
    @SerializedName("set")
    @Expose
    private long set;

    public long getLevel() {
        return level;
    }

    public void setLevel(long level) {
        this.level = level;
    }

    public long getSet() {
        return set;
    }

    public void setSet(long set) {
        this.set = set;
    }

}
