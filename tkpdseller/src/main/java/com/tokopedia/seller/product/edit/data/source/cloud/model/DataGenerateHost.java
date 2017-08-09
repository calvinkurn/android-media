package com.tokopedia.seller.product.edit.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataGenerateHost {

        @SerializedName("generated_host")
        @Expose
        GenerateHost generateHost;

        public GenerateHost getGenerateHost() {
            return generateHost;
        }

        public void setGenerateHost(GenerateHost generateHost) {
            this.generateHost = generateHost;
        }
}