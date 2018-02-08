package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.attachment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoasfs on 31/08/17.
 */

public class Attachment {
    public List<AttachmentViewModel> attachmentViewModelList = new ArrayList<>();
    public String information;

    public Attachment() {
        this.information = "";
        this.attachmentViewModelList = new ArrayList<>();
    }

    public List<AttachmentViewModel> getAttachmentViewModelList() {
        return attachmentViewModelList;
    }

    public void setAttachmentViewModelList(List<AttachmentViewModel> attachmentViewModelList) {
        this.attachmentViewModelList = attachmentViewModelList;
    }
}
