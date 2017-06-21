package com.tokopedia.seller.product.view.model;

import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.view.model.upload.intdef.ProductStatus;

/**
 * Created by User on 6/21/2017.
 */

public class ProductDraftViewModel {
    private long productId;
    private String primaryImageUrl;
    private String productName;
    private int completion;
    private boolean isEdit;

    public ProductDraftViewModel(long productId,
                                 String primaryImageUrl, String productName, int completion, boolean isEdit) {
        this.productId = productId;
        this.primaryImageUrl = primaryImageUrl;
        this.productName = productName;
        this.completion = completion;
        this.isEdit = isEdit;
    }

    public ProductDraftViewModel(UploadProductInputDomainModel uploadProductInputDomainModel) {
        this.productId = uploadProductInputDomainModel.getId();
        this.productName = uploadProductInputDomainModel.getProductName();
        this.completion = completion;
        this.isEdit = (uploadProductInputDomainModel.getProductStatus() == ProductStatus.EDIT);
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getPrimaryImageUrl() {
        return primaryImageUrl;
    }

    public void setPrimaryImageUrl(String primaryImageUrl) {
        this.primaryImageUrl = primaryImageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getCompletion() {
        return completion;
    }

    public void setCompletion(int completion) {
        this.completion = completion;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }
}
