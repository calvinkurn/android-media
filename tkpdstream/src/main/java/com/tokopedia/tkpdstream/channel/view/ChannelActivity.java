package com.tokopedia.tkpdstream.channel.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.tkpdstream.channel.di.ChannelComponent;
import com.tokopedia.tkpdstream.channel.view.fragment.ChannelFragment;
import com.tokopedia.tkpdstream.common.BaseStreamActivity;
import com.tokopedia.tkpdstream.channel.di.DaggerChannelComponent;

public class ChannelActivity extends BaseStreamActivity implements HasComponent<ChannelComponent> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return ChannelFragment.createInstance();
    }

    @Override
    public ChannelComponent getComponent() {
        return DaggerChannelComponent.builder()
                .streamComponent(getStreamComponent())
                .build();
    }
}
