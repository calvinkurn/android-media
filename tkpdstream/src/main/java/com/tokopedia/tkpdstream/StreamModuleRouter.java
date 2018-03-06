package com.tokopedia.tkpdstream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.tkpdstream.chatroom.view.activity.GroupChatActivity;

/**
 * @author by nisie on 3/1/18.
 */

public interface StreamModuleRouter {

    Intent getHomeIntent(Context context);

    Intent getInboxChannelsIntent(Context context);

    void openRedirectUrl(Activity activity, String url);

    Intent getLoginIntent(Context context);
}
