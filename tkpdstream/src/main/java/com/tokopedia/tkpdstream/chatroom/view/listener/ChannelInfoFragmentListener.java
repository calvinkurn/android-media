package com.tokopedia.tkpdstream.chatroom.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.tkpdstream.channel.view.model.ChannelViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.SprintSaleViewModel;

/**
 * @author by milhamj on 20/03/18.
 */

public interface ChannelInfoFragmentListener {

    interface View extends CustomerView {
        Context getContext();

        void renderData(ChannelViewModel channelViewModel);
    }
}
