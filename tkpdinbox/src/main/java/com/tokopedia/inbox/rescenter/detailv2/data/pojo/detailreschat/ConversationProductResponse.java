package com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailreschat;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ConversationProductResponse {

    @SerializedName("image")
    public ConversationAttachmentResponse image;

    @SerializedName("resId")
    public int resId;

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
}
