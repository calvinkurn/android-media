package com.tokopedia.seller.product.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultUploadImage {

        @SerializedName("file_path")
        @Expose
        private String filePath;
        @SerializedName("pic_id")
        @Expose
        private int picId;
        @SerializedName("pic_obj")
        @Expose
        private String picObj;

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public int getPicId() {
            return picId;
        }

        public void setPicId(int picId) {
            this.picId = picId;
        }

        public String getPicObj() {
            return picObj;
        }

        public void setPicObj(String picObj) {
            this.picObj = picObj;
        }
}