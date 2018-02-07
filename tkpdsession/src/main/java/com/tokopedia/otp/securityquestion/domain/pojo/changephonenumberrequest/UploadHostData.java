package com.tokopedia.otp.securityquestion.domain.pojo.changephonenumberrequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nisie on 3/9/17.
 */

public class UploadHostData {

    @SerializedName("generated_host")
    @Expose
    GeneratedHost generatedHost;

    public GeneratedHost getGeneratedHost() {
        return generatedHost;
    }

    public void setGeneratedHost(GeneratedHost generatedHost) {
        this.generatedHost = generatedHost;
    }
}
