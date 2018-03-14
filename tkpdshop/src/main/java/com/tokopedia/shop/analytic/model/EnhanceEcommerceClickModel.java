
package com.example;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseSaveShopDesc {

    @SerializedName("click")
    @Expose
    private Click click;

    public Click getClick() {
        return click;
    }

    public void setClick(Click click) {
        this.click = click;
    }

}
