package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ConversationProductDomain {

    public ConversationAttachmentDomain image;
    public int resId;

    public ConversationProductDomain(ConversationAttachmentDomain image, int resId) {
        this.image = image;
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
}
