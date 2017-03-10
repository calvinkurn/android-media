package com.tokopedia.session.changephonenumber.data;

/**
 * Created by nisie on 3/10/17.
 */

public class ChangePhoneNumberRequestPass {
    String uploadIdPath;
    String uploadBankBookPath;

    public String getUploadIdPath() {
        return uploadIdPath;
    }

    public void setUploadIdPath(String uploadIdPath) {
        this.uploadIdPath = uploadIdPath;
    }

    public String getUploadBankBookPath() {
        return uploadBankBookPath;
    }

    public void setUploadBankBookPath(String uploadBankBookPath) {
        this.uploadBankBookPath = uploadBankBookPath;
    }
}
