package com.tokopedia.seller.shop.open.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 3/22/17.
 */

public class UploadShopImageModel{

    @SerializedName("data")
    @Expose
    Data data;
    @SerializedName("server_process_time")
    @Expose
    String serverProcessTime;
    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("message_error")
    @Expose
    List<String> messageError = new ArrayList<String>();
    @SerializedName("result")
    @Expose
    String result;

    /**
     * @return The data
     */
    public Data getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(Data data) {
        this.data = data;
    }

    /**
     * @return The serverProcessTime
     */
    public String getServerProcessTime() {
        return serverProcessTime;
    }

    /**
     * @param serverProcessTime The server_process_time
     */
    public void setServerProcessTime(String serverProcessTime) {
        this.serverProcessTime = serverProcessTime;
    }

    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getMessageError() {
        return messageError;
    }

    public void setMessageError(List<String> messageError) {
        this.messageError = messageError;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public class Data {

        @SerializedName("upload")
        @Expose
        Upload upload;
        @SerializedName("image")
        @Expose
        Image image;

        /**
         * @return The image
         */
        public Image getImage() {
            return image;
        }

        /**
         * @param image The image
         */
        public void setImage(Image image) {
            this.image = image;
        }

        /**
         * @return The upload
         */
        public Upload getUpload() {
            return upload;
        }

        /**
         * @param upload The upload
         */
        public void setUpload(Upload upload) {
            this.upload = upload;
        }

    }

    public class Upload {

        @SerializedName("message_status")
        @Expose
        String messageStatus;
        @SerializedName("src")
        @Expose
        String src;
        @SerializedName("success")
        @Expose
        String success;

        /**
         * @return The messageStatus
         */
        public String getMessageStatus() {
            return messageStatus;
        }

        /**
         * @param messageStatus The message_status
         */
        public void setMessageStatus(String messageStatus) {
            this.messageStatus = messageStatus;
        }

        /**
         * @return The src
         */
        public String getSrc() {
            return src;
        }

        /**
         * @param src The src
         */
        public void setSrc(String src) {
            this.src = src;
        }

        /**
         * @return The success
         */
        public String getSuccess() {
            return success;
        }

        /**
         * @param success The success
         */
        public void setSuccess(String success) {
            this.success = success;
        }

    }

    public class Image {

        @SerializedName("message_status")
        @Expose
        String messageStatus;
        @SerializedName("pic_code")
        @Expose
        String picCode;
        @SerializedName("pic_src")
        @Expose
        String picSrc;
        @SerializedName("success")
        @Expose
        String success;

        /**
         * @return The messageStatus
         */
        public String getMessageStatus() {
            return messageStatus;
        }

        /**
         * @param messageStatus The message_status
         */
        public void setMessageStatus(String messageStatus) {
            this.messageStatus = messageStatus;
        }

        /**
         * @return The picCode
         */
        public String getPicCode() {
            return picCode;
        }

        /**
         * @param picCode The pic_code
         */
        public void setPicCode(String picCode) {
            this.picCode = picCode;
        }

        /**
         * @return The picSrc
         */
        public String getPicSrc() {
            return picSrc;
        }

        /**
         * @param picSrc The pic_src
         */
        public void setPicSrc(String picSrc) {
            this.picSrc = picSrc;
        }

        /**
         * @return The success
         */
        public String getSuccess() {
            return success;
        }

        /**
         * @param success The success
         */
        public void setSuccess(String success) {
            this.success = success;
        }

    }
}
