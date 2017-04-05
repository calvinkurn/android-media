package com.tokopedia.inbox.rescenter.discussion.data.pojo.generatehost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nisie on 4/3/17.
 */

public class GenerateHostEntity {

    @SerializedName("generated_host")
    @Expose
    private GeneratedHost generatedHost;

    public GeneratedHost getGeneratedHost() {
        return generatedHost;
    }

    public void setGeneratedHost(GeneratedHost generatedHost) {
        this.generatedHost = generatedHost;
    }

}
