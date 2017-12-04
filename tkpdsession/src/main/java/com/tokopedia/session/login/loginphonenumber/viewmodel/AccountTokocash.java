package com.tokopedia.session.login.loginphonenumber.viewmodel;

/**
 * @author by nisie on 12/4/17.
 */

public class AccountTokocash {

    String userId;
    String name;
    String email;
    String avatarUrl;

    public AccountTokocash(String userId, String name, String email, String avatarUrl) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.avatarUrl = avatarUrl;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
