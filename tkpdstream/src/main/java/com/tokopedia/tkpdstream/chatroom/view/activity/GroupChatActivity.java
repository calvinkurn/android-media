package com.tokopedia.tkpdstream.chatroom.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.Toolbar;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseEmptyActivity;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.StreamModuleRouter;
import com.tokopedia.tkpdstream.channel.view.model.ChannelViewModel;
import com.tokopedia.tkpdstream.chatroom.view.fragment.GroupChatFragment;
import com.tokopedia.tkpdstream.common.applink.ApplinkConstant;
import com.tokopedia.tkpdstream.common.util.TransparentStatusBarHelper;

/**
 * @author by nisie on 2/6/18.
 */

public class GroupChatActivity extends BaseEmptyActivity {

    @DeepLink(ApplinkConstant.GROUPCHAT_ROOM)
    public static TaskStackBuilder getCallingTaskStack(Context context, Bundle extras) {
        String id = extras.getString(ApplinkConstant.PARAM_CHANNEL_ID);
        Intent homeIntent = ((StreamModuleRouter) context.getApplicationContext()).getHomeIntent(context);
        Intent detailsIntent = GroupChatActivity.getCallingIntent(context, id);
        Intent parentIntent = ((StreamModuleRouter) context.getApplicationContext())
                .getInboxChannelsIntent(context);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntent(homeIntent);
        taskStackBuilder.addNextIntent(parentIntent);
        taskStackBuilder.addNextIntent(detailsIntent);
        return taskStackBuilder;
    }

    public static final String EXTRA_CHANNEL_UUID = "CHANNEL_UUID";
    public static final String EXTRA_CHANNEL_INFO = "CHANNEL_INFO";
    private static final String EXTRA_SHOW_BOTTOM_DIALOG = "SHOW_BOTTOM";
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TransparentStatusBarHelper.assistActivity(this);
        }
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

    public static Intent getCallingIntent(Context context, String channelId) {
        Intent intent = new Intent(context, GroupChatActivity.class);
        intent.putExtra(EXTRA_CHANNEL_UUID, channelId);
        intent.putExtra(EXTRA_SHOW_BOTTOM_DIALOG, true);
        return intent;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_group_chat;
    }
}
