package com.tokopedia.tkpdstream.chatroom.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.tokopedia.abstraction.base.view.activity.BaseEmptyActivity;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.channel.view.model.ChannelViewModel;
import com.tokopedia.tkpdstream.chatroom.view.fragment.GroupChatFragment;

/**
 * @author by nisie on 2/6/18.
 */

public class GroupChatActivity extends BaseEmptyActivity {

    public static final String EXTRA_CHANNEL_UUID = "CHANNEL_UUID";
    public static final String EXTRA_CHANNEL_INFO = "CHANNEL_INFO";
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }

    private void initView() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }

        Fragment fragment = getSupportFragmentManager().findFragmentByTag
                (GroupChatFragment.class.getSimpleName());

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            fragment = GroupChatFragment.createInstance(bundle);
        }
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }

    public static Intent getCallingIntent(Context context, ChannelViewModel channelViewModel) {
        Intent intent = new Intent(context, GroupChatActivity.class);
        intent.putExtra(EXTRA_CHANNEL_INFO, channelViewModel);
        intent.putExtra(EXTRA_CHANNEL_UUID, channelViewModel.getId());
        return intent;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_group_chat;
    }
}
