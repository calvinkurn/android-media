package com.tokopedia.tkpdstream.channel.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * @author by nisie on 2/3/18.
 */

public interface ChannelContract {

    interface View extends CustomerView {

    }

    interface Presenter extends CustomerPresenter<View> {

    }
}
