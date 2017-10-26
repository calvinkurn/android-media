package com.tokopedia.inbox.rescenter.detailv2.view.typefactory;

import android.view.View;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard.ChatCreateLeftViewHolder;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard.ChatLeftViewHolder;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard.ChatRightViewHolder;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatCreateLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatRightViewModel;

/**
 * Created by yoasfs on 23/10/17.
 */

public class DetailChatTypeFactoryImpl extends BaseAdapterTypeFactory  implements DetailChatTypeFactory {
    DetailResChatFragmentListener.View mainView;

    public DetailChatTypeFactoryImpl(DetailResChatFragmentListener.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public int type(ChatLeftViewModel viewModel) {
        return ChatLeftViewHolder.LAYOUT;
    }

    @Override
    public int type(ChatRightViewModel viewModel) {
        return ChatRightViewHolder.LAYOUT;
    }

    @Override
    public int type(ChatCreateLeftViewModel viewModel) {
        return ChatCreateLeftViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {
        AbstractViewHolder viewHolder;
        if (type == ChatLeftViewHolder.LAYOUT) {
            viewHolder = new ChatLeftViewHolder(view, mainView);
        } else if (type == ChatRightViewHolder.LAYOUT) {
            viewHolder = new ChatRightViewHolder(view, mainView);
        } else if (type == ChatCreateLeftViewHolder.LAYOUT) {
            viewHolder = new ChatCreateLeftViewHolder(view, mainView);
        } else {
            viewHolder = super.createViewHolder(view, type);
        }
        return viewHolder;
    }
}
