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
        this.attachmentViewModelList = new ArrayList<>();
    }
}
