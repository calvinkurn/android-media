package com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail;

/**
 * @author by nisie on 8/24/17.
 */

public class ImageAttachmentViewModel {

    private int attachmentId;
    private String description;
    private String uriThumbnail;
    private String uriLarge;

    public ImageAttachmentViewModel(int attachmentId, String description,
                                 String uriThumbnail, String uriLarge) {
        this.attachmentId = attachmentId;
        this.description = description;
        this.uriThumbnail = uriThumbnail;
        this.uriLarge = uriLarge;
    }

    public int getAttachmentId() {
        return attachmentId;
    }

    public String getDescription() {
        return description;
    }

    public String getUriThumbnail() {
        return uriThumbnail;
    }

    public String getUriLarge() {
        return uriLarge;
    }
}
