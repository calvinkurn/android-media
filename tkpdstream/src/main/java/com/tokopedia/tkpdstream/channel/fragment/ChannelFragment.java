package com.tokopedia.tkpdchat.channel.fragment;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.tkpdchat.common.analytics.ChannelAnalytics;

/**
 * @author by nisie on 2/1/18.
 */


public class ChannelFragment extends BaseDaggerFragment {

    public static Fragment createInstance() {
        return new ChannelFragment();
    }

    @Override
    protected String getScreenName() {
        return ChannelAnalytics.Screen.CHANNEL_LIST;
    }

    @Override
    protected void initInjector() {


    }
}
