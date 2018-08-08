package com.tokopedia.transaction.bcaoneklik.model.bcaoneclick;


/**
 * Created by kris on 7/25/17. Tokopedia
 */

public class BcaOneClickRegisterData {

    private String tokenId;

    private String credentialType;

    private String credentialNumber;

    private String maxLimit;

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getCredentialType() {
        return credentialType;
    }

    public void setCredentialType(String credentialType) {
        this.credentialType = credentialType;
    }

    public String getCredentialNumber() {
        return credentialNumber;
    }

    public void setCredentialNumber(String credentialNumber) {
        this.credentialNumber = credentialNumber;
    }

    public String getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(String maxLimit) {
        this.maxLimit = maxLimit;
    }
}
