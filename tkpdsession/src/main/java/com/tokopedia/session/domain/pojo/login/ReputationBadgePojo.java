
package com.tokopedia.session.domain.pojo.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReputationBadgePojo {

    @SerializedName("level")
    @Expose
    private int level;
    @SerializedName("set")
    @Expose
    private int set;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSet() {
        return set;
    }

    public void setSet(int set) {
        this.set = set;
    }

}
