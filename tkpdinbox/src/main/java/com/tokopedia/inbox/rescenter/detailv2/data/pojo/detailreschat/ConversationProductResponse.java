package com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ConversationProductResponse {

    @SerializedName("image")
    private ConversationAttachmentResponse image;

    @SerializedName("resId")
    private int resId;

    @SerializedName("message")
    private String message;

    public ConversationAttachmentResponse getImage() {
        return image;
    }

    public void setImage(ConversationAttachmentResponse image) {
        this.image = image;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
