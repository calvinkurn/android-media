package com.tokopedia.tkpdstream.channel.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * @author by nisie on 2/6/18.
 */

public interface GroupChatContract {

    interface View extends CustomerView {

        Context getContext();

    }

    interface Presenter extends CustomerPresenter<View> {

        void getMessagesFirstTime();
    }
}
