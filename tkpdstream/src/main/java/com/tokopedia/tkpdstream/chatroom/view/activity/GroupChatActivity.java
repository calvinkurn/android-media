package com.tokopedia.tkpdstream.chatroom.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.tkpdstream.chatroom.view.fragment.GroupChatFragment;
import com.tokopedia.tkpdstream.common.BaseStreamActivity;

/**
 * @author by nisie on 2/6/18.
 */

public class GroupChatActivity extends BaseStreamActivity {

    public static final String EXTRA_CHANNEL_URL = "CHANNEL_URL";

    @Override
    protected Fragment getNewFragment() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null)
            bundle.putAll(getIntent().getExtras());
        return GroupChatFragment.createInstance(bundle);
    }

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, GroupChatActivity.class);
        intent.putExtra(EXTRA_CHANNEL_URL, "pub1");
        return intent;
    }
}
