package com.tokopedia.session.changephonenumber.domain.model.changephonenumberrequest;

/**
 * Created by nisie on 3/10/17.
 */

public class ChangePhoneNumberRequestPass {
    String uploadIdPath;
    String uploadBankBookPath;

    String idHeight;
    String idWidth;

    String bankBookHeight;
    String bankBookWidth;

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

    public String getIdHeight() {
        return idHeight;
    }

    public void setIdHeight(String idHeight) {
        this.idHeight = idHeight;
    }

    public String getIdWidth() {
        return idWidth;
    }

    public void setIdWidth(String idWidth) {
        this.idWidth = idWidth;
    }

    public String getBankBookHeight() {
        return bankBookHeight;
    }

    public void setBankBookHeight(String bankBookHeight) {
        this.bankBookHeight = bankBookHeight;
    }

    public String getBankBookWidth() {
        return bankBookWidth;
    }

    public void setBankBookWidth(String bankBookWidth) {
        this.bankBookWidth = bankBookWidth;
    }
}
