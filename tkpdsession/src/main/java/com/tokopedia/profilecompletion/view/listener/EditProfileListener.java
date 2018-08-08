package com.tokopedia.profilecompletion.view.listener;

/**
 * Created by stevenfredian on 6/22/17.
 */

public interface EditProfileListener {
    void onSuccessEditProfile(int edit);

    void onFailedEditProfile(String errorMessage);
}
