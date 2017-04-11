package com.tokopedia.seller.product.domain.model;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class AddProductSubmitInputDomainModel {
    private String clickName;
    private String postKey;
    private String fileUploadedTo;
    private String productEtalaseName;
    private String productEtalaseId;
    private String productUploadTo;

    public String getClickName() {
        return clickName;
    }

    public String getPostKey() {
        return postKey;
    }

    public String getFileUploadedTo() {
        return fileUploadedTo;
    }

    public String getProductEtalaseName() {
        return productEtalaseName;
    }

    public String getProductEtalaseId() {
        return productEtalaseId;
    }

    public String getProductUploadTo() {
        return productUploadTo;
    }

    public void setClickName(String clickName) {
        this.clickName = clickName;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public void setFileUploadedTo(String fileUploadedTo) {
        this.fileUploadedTo = fileUploadedTo;
    }

    public void setProductEtalaseName(String productEtalaseName) {
        this.productEtalaseName = productEtalaseName;
    }

    public void setProductEtalaseId(String productEtalaseId) {
        this.productEtalaseId = productEtalaseId;
    }

    public void setProductUploadTo(String productUploadTo) {
        this.productUploadTo = productUploadTo;
    }
}
