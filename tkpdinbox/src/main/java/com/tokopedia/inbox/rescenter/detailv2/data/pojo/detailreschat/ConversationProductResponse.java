package com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ConversationProductResponse {

    @SerializedName("image")
    private List<ConversationAttachmentResponse> image;

    @SerializedName("resId")
    private int resId;

    @SerializedName("message")
    private String message;

    @SerializedName("name")
    private String name;

    public List<ConversationAttachmentResponse> getImage() {
        return image;
    }

    public void setImage(List<ConversationAttachmentResponse> image) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
