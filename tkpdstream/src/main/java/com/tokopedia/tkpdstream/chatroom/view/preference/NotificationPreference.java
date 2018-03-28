package com.tokopedia.tkpdstream.chatroom.view.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;

import javax.inject.Inject;

/**
 * @author by milhamj on 27/03/18.
 */

public class NotificationPreference {
    private static final String USER_GROUP_CHAT_NOTIFICATION = "user_group_chat_notification";

    private final SharedPreferences sharedPrefs;

    @Inject
    public NotificationPreference(@ApplicationContext Context context) {
        this.sharedPrefs = context.getSharedPreferences(
                USER_GROUP_CHAT_NOTIFICATION,
                Context.MODE_PRIVATE);
    }

    public boolean isFirstTimeUser(String userId) {
        return sharedPrefs.getBoolean(userId, true);
    }

    public void setFirstTime(String userId) {
        sharedPrefs.edit().putBoolean(userId, false).apply();
    }
}
