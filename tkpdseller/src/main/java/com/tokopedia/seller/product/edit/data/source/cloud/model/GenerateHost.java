package com.tokopedia.seller.product.edit.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenerateHost {

        @SerializedName("upload_host")
        @Expose
        String uploadHost;

        @SerializedName("server_id")
        @Expose
        String serverId;

        @SerializedName("user_id")
        @Expose
        String userId;

        public String getUploadHost() {
            return uploadHost;
        }

        public void setUploadHost(String uploadHost) {
            this.uploadHost = uploadHost;
        }

        public String getServerId() {
            return serverId;
        }

        public void setServerId(String serverId) {
            this.serverId = serverId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
}