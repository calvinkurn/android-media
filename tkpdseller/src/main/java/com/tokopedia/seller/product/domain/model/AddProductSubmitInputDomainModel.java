package com.tokopedia.seller.product.domain.model;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class AddProductSubmitInputDomainModel {
    private String postKey;
    private String fileUploadedTo;
    private int productEtalaseId;
    private int productUploadTo;

    public String getPostKey() {
        return postKey;
    }

    public String getFileUploadedTo() {
        return fileUploadedTo;
    }

    public int getProductEtalaseId() {
        return productEtalaseId;
    }

    public int getProductUploadTo() {
        return productUploadTo;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public void setFileUploadedTo(String fileUploadedTo) {
        this.fileUploadedTo = fileUploadedTo;
    }

    public void setProductEtalaseId(int productEtalaseId) {
        this.productEtalaseId = productEtalaseId;
    }

    public void setProductUploadTo(int productUploadTo) {
        this.productUploadTo = productUploadTo;
    }
}
