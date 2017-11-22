package com.tokopedia.transaction.purchase.detail.model.detail.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kris on 11/20/17. Tokopedia
 */

public class Preorder {

    @SerializedName("is_preorder")
    @Expose
    private int isPreorder;

    @SerializedName("process_time")
    @Expose
    private int processTime;

    public int getIsPreorder() {
        return isPreorder;
    }

    public int getProcessTime() {
        return processTime;
    }
}
