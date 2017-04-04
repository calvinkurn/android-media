package com.tokopedia.inbox.rescenter.discussion.domain.model.generatehost;

/**
 * Created by nisie on 4/3/17.
 */

public class GenerateHostModel {

    private boolean success;
    private String messageError;
    private GenerateHostData generateHostData;
    private int responseCode;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public GenerateHostData getGenerateHostData() {
        return generateHostData;
    }

    public void setGenerateHostData(GenerateHostData generateHostData) {
        this.generateHostData = generateHostData;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}
