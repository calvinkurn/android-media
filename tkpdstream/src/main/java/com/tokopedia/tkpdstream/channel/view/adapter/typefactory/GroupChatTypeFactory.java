package com.tokopedia.tkpdstream.channel.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdstream.channel.view.viewmodel.MyChatViewModel;
import com.tokopedia.tkpdstream.channel.view.viewmodel.OppositeChatViewModel;

/**
 * @author by nisie on 2/7/18.
 */

public interface GroupChatTypeFactory {

    AbstractViewHolder createViewHolder(View view, int viewType);

    int type(OppositeChatViewModel oppositeChatViewModel);

    int type(MyChatViewModel myChatViewModel);

}
