package com.tokopedia.seller.product.edit.data.exception;

/**
 * Created by zulfikarrahman on 4/27/17.
 */

public class UploadProductException extends RuntimeException {

    private final Throwable throwable;
    private long productDraftId;

    public Throwable getThrowable() {
        return throwable;
    }

    public long getProductDraftId() {
        return productDraftId;
    }

    public void setProductDraftId(long productDraftId) {
        this.productDraftId = productDraftId;
    }

    public UploadProductException(long productDraftId, Throwable throwable) {
        super(throwable);
        this.productDraftId = productDraftId;
        this.throwable = throwable;
    }
}
