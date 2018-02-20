package com.tokopedia.tkpdstream.channel.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tkpdstream.channel.view.model.ChannelListViewModel;

/**
 * @author by nisie on 2/3/18.
 */

public interface ChannelContract {

    interface View extends CustomerView {

        Context getContext();

        void onFailedGetChannelFirstTime(String errorMessage);

        void onSuccessGetChannelFirstTime(ChannelListViewModel channelListViewModel);

        void onSuccessGetChannel(ChannelListViewModel channelListViewModel);

        void onFailedGetChannel(String errorMessage);
    }

    interface Presenter extends CustomerPresenter<View> {

        void getChannelList();
    }
}
