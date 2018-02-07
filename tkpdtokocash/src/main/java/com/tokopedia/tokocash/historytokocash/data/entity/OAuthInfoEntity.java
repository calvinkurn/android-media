package com.tokopedia.tokocash.historytokocash.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nabillasabbaha on 10/26/17.
 */

public class OAuthInfoEntity {

    @SerializedName("account_list")
    @Expose
    private List<AccountWalletListEntity> account_list;
    @SerializedName("tokopedia_user_id")
    @Expose
    private String tokopedia_user_id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobile")
    @Expose
    private String mobile;

    public List<AccountWalletListEntity> getAccount_list() {
        return account_list;
    }

    public void setAccount_list(List<AccountWalletListEntity> account_list) {
        this.account_list = account_list;
    }

    public String getTokopedia_user_id() {
        return tokopedia_user_id;
    }

    public void setTokopedia_user_id(String tokopedia_user_id) {
        this.tokopedia_user_id = tokopedia_user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
