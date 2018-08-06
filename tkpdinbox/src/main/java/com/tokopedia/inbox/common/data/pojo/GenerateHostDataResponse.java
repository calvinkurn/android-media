package com.tokopedia.inbox.common.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 31/07/18.
 */
public class GenerateHostDataResponse {

    @SerializedName("generated_host")
    private GenerateHostResponse generateHostResponse;

    public GenerateHostResponse getGenerateHostResponse() {
        return generateHostResponse;
    }

    public void setGenerateHostResponse(GenerateHostResponse generateHostResponse) {
        this.generateHostResponse = generateHostResponse;
    }
}
