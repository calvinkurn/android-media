package com.tokopedia.seller.product.data.source.cloud.model;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class AddProductSubmitInputServiceModel {
    public static final String CLICK_NAME = "click_name";
    public static final String FILE_UPLOADED = "file_uploaded";
    public static final String POST_KEY = "post_key";
    public static final String PRODUCT_ETALASE_ID = "product_etalase_id";
    public static final String PRODUCT_ETALASE_NAME = "product_etalase_name";
    public static final String PRODUCT_UPLOAD_TO = "product_upload_to";
    private String clickName;
    private String fileUploaded;
    private String postKey;
    private String productEtalseId;
    private String productEtalaseName;
    private String productUploadTo;

    public TKPDMapParam<String, String> generateMapParam() {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put(CLICK_NAME, getClickName());
        params.put(FILE_UPLOADED, getFileUploaded());
        params.put(POST_KEY, getPostKey());
        params.put(PRODUCT_ETALASE_ID, getProductEtalseId());
        params.put(PRODUCT_ETALASE_NAME, getProductEtalaseName());
        params.put(PRODUCT_UPLOAD_TO, getProductUploadTo());
        return params;
    }

    public String getClickName() {
        return clickName;
    }

    public String getFileUploaded() {
        return fileUploaded;
    }

    public String getPostKey() {
        return postKey;
    }

    public String getProductEtalseId() {
        return productEtalseId;
    }

    public String getProductEtalaseName() {
        return productEtalaseName;
    }

    public String getProductUploadTo() {
        return productUploadTo;
    }

    public void setClickName(String clickName) {
        this.clickName = clickName;
    }

    public void setFileUploaded(String fileUploaded) {
        this.fileUploaded = fileUploaded;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public void setProductEtalseId(String productEtalseId) {
        this.productEtalseId = productEtalseId;
    }

    public void setProductEtalaseName(String productEtalaseName) {
        this.productEtalaseName = productEtalaseName;
    }

    public void setProductUploadTo(String productUploadTo) {
        this.productUploadTo = productUploadTo;
    }

}
