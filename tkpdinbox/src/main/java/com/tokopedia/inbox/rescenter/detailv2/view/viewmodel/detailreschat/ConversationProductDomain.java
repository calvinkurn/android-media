package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ConversationProductDomain {

    private ConversationAttachmentDomain image;
    private String message;
    private int resId;

    public ConversationProductDomain(ConversationAttachmentDomain image, String message, int resId) {
        this.image = image;
        this.message = message;
        this.resId = resId;
    }

    public ConversationAttachmentDomain getImage() {
        return image;
    }

    public void setImage(ConversationAttachmentDomain image) {
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
