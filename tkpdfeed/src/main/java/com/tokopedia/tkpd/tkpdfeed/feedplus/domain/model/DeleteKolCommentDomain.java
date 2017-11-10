package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model;

/**
 * @author by nisie on 11/10/17.
 */

public class DeleteKolCommentDomain {
    boolean isSuccess;

    public DeleteKolCommentDomain(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
