package com.tokopedia.inbox.rescenter.createreso.domain.model.solution;

import javax.annotation.Nullable;

/**
 * Created by yoasfs on 24/08/17.
 */

public class RequireDomain {

    @Nullable
    private boolean attachment;

    public RequireDomain(@Nullable boolean attachment) {
        this.attachment = attachment;
    }

    @Nullable
    public boolean isAttachment() {
        return attachment;
    }

    public void setAttachment(@Nullable boolean attachment) {
        this.attachment = attachment;
    }
}
