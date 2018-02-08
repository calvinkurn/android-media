package com.tokopedia.seller.product.edit.data.exception;

import com.tokopedia.seller.product.edit.view.model.upload.intdef.ProductStatus;

/**
 * Created by zulfikarrahman on 4/27/17.
 */

public class UploadProductException extends RuntimeException {

    private final Throwable throwable;
    private String productDraftId;
    @ProductStatus
    private int productStatus;

    public Throwable getThrowable() {
        return throwable;
    }

    public String getProductDraftId() {
        return productDraftId;
    }

    public void setProductDraftId(String productDraftId) {
        this.productDraftId = productDraftId;
    }

    @ProductStatus
    public int getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(int productStatus) {
        this.productStatus = productStatus;
    }

    public UploadProductException(String productDraftId, int productStatus, Throwable throwable) {
        super(throwable);
        this.productDraftId = productDraftId;
        this.productStatus = productStatus;
        this.throwable = throwable;
    }
}
