package com.tokopedia.tkpdstream.channel.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tkpdstream.channel.view.listener.ChannelContract;

import javax.inject.Inject;

/**
 * @author by nisie on 2/3/18.
 */

public class ChannelPresenter extends BaseDaggerPresenter<ChannelContract.View> implements
        ChannelContract.Presenter {

    @Inject
    public ChannelPresenter() {
    }
}
