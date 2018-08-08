package com.tokopedia.transaction.bcaoneklik.model.bcaoneclick;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kris on 8/2/17. Tokopedia
 */

public class BcaOneClickUserModel implements Parcelable {
    @SerializedName("token_id")
    String tokenId;

    @SerializedName("credential_type")
    String credentialType;

    @SerializedName("credential_no")
    String credentialNo;

    @SerializedName("max_limit")
    String maxLimit;

    public BcaOneClickUserModel() {
    }

    protected BcaOneClickUserModel(Parcel in) {
        tokenId = in.readString();
        credentialType = in.readString();
        credentialNo = in.readString();
        maxLimit = in.readString();
    }

    public static final Creator<BcaOneClickUserModel> CREATOR = new Creator<BcaOneClickUserModel>() {
        @Override
        public BcaOneClickUserModel createFromParcel(Parcel in) {
            return new BcaOneClickUserModel(in);
        }

        @Override
        public BcaOneClickUserModel[] newArray(int size) {
            return new BcaOneClickUserModel[size];
        }
    };

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

    public String getCredentialNo() {
        return credentialNo;
    }

    public void setCredentialNo(String credentialNo) {
        this.credentialNo = credentialNo;
    }

    public String getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(String maxLimit) {
        this.maxLimit = maxLimit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tokenId);
        dest.writeString(credentialType);
        dest.writeString(credentialNo);
        dest.writeString(maxLimit);
    }
}
