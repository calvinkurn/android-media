package com.tokopedia.inbox.rescenter.createreso.data.pojo.productproblem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by yoasfs on 14/08/17.
 */

public class ProblemResponse {
    @SerializedName("type")
    @Expose
    private int type;
    @SerializedName("name")
    @Expose
    private String name;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ProblemResponse{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

