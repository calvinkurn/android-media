package com.tokopedia.tkpdstream.channel.view.fragment;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.tkpdstream.channel.view.listener.ChannelContract;
import com.tokopedia.tkpdstream.channel.view.presenter.ChannelPresenter;
import com.tokopedia.tkpdstream.common.analytics.ChannelAnalytics;
import com.tokopedia.tkpdstream.common.di.component.StreamComponent;

import javax.inject.Inject;

/**
 * @author by nisie on 2/1/18.
 */


public class ChannelFragment extends BaseDaggerFragment implements ChannelContract.View{


    @Inject
    ChannelPresenter presenter;

    public static Fragment createInstance() {
        return new ChannelFragment();
    }

    @Override
    protected String getScreenName() {
        return ChannelAnalytics.Screen.CHANNEL_LIST;
    }

    @Override
    protected void initInjector() {
        DaggerStreamComponent.builder()
                .appComponent(getComponent(StreamComponent.class))
                .build().inject(this);
        presenter.attachView(this);
    }
}
