package com.tokopedia.tkpdstream.channel.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.tkpdstream.channel.view.fragment.GroupChatFragment;
import com.tokopedia.tkpdstream.common.BaseStreamActivity;

/**
 * @author by nisie on 2/6/18.
 */

public class GroupChatActivity extends BaseStreamActivity implements HasComponent {

    @Override
    protected Fragment getNewFragment() {
        return GroupChatFragment.createInstance();
    }

    @Override
    public BaseAppComponent getComponent() {
        return getComponent();
    }
}
