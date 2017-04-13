package com.tokopedia.seller.product.data.source.cloud.model;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class AddProductSubmitInputServiceModel {
    public static final String FILE_UPLOADED = "file_uploaded";
    public static final String POST_KEY = "post_key";
    public static final String PRODUCT_ETALASE_ID = "product_etalase_id";
    public static final String PRODUCT_ETALASE_NAME = "product_etalase_name";
    public static final String PRODUCT_UPLOAD_TO = "product_upload_to";
    private String fileUploaded;
    private String postKey;
    private String productEtalaseName;
    private int productEtalseId;
    private int productUploadTo;

    public TKPDMapParam<String, String> generateMapParam() {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put(FILE_UPLOADED, getFileUploaded());
        params.put(POST_KEY, getPostKey());
        params.put(PRODUCT_ETALASE_ID, String.valueOf(getProductEtalseId()));
        params.put(PRODUCT_ETALASE_NAME, getProductEtalaseName());
        params.put(PRODUCT_UPLOAD_TO, String.valueOf(getProductUploadTo()));
        return params;
    }

    public String getFileUploaded() {
        return fileUploaded;
    }

    public String getPostKey() {
        return postKey;
    }

    public int getProductEtalseId() {
        return productEtalseId;
    }

    public String getProductEtalaseName() {
        return productEtalaseName;
    }

    public int getProductUploadTo() {
        return productUploadTo;
    }

    public void setFileUploaded(String fileUploaded) {
        this.fileUploaded = fileUploaded;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public void setProductEtalseId(int productEtalseId) {
        this.productEtalseId = productEtalseId;
    }

    public void setProductEtalaseName(String productEtalaseName) {
        this.productEtalaseName = productEtalaseName;
    }

    public void setProductUploadTo(int productUploadTo) {
        this.productUploadTo = productUploadTo;
    }

}
