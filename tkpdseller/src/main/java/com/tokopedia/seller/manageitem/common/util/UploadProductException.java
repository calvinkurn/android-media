package com.tokopedia.seller.manageitem.common.util;

/**
 * Created by zulfikarrahman on 4/27/17.
 */

public class UploadProductException extends RuntimeException {

    private final Throwable throwable;
    private long draftProductId;

    public Throwable getThrowable() {
        return throwable;
    }

    public long getProductDraftId() {
        return draftProductId;
    }

    public void setProductDraftId(long draftProductId) {
        this.draftProductId = draftProductId;
    }

    public UploadProductException(long draftProductId, Throwable throwable) {
        super(throwable);
        this.draftProductId = draftProductId;
        this.throwable = throwable;
    }
}
