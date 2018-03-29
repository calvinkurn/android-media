package com.tokopedia.tkpdstream.chatroom.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChannelInfoViewModel;

/**
 * @author by milhamj on 20/03/18.
 */

public interface ChannelInfoFragmentListener {

    interface View extends CustomerView {
        Context getContext();

        void renderData(ChannelInfoViewModel channelInfoViewModel);

        interface ChannelPartnerViewHolderListener {
            void channelPartnerClicked(String url, String partnerName);
        }
    }
}
