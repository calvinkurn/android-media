package com.tokopedia.inbox.rescenter.detailv2.view.typefactory;

import android.view.View;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard.ChatActionEarlyLeftViewHolder;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard.ChatActionFinalLeftViewHolder;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard.ChatActionResetLeftViewHolder;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard.ChatAwbLeftViewHolder;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard.ChatAwbRightViewHolder;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard.ChatCommonLeftViewHolder;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard.ChatCreateLeftViewHolder;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard.ChatInputAddressLeftViewHolder;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard.ChatInputAddressRightViewHolder;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard.ChatLeftViewHolder;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard.ChatRightViewHolder;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard.ChatSystemLeftViewHolder;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.chatadaptercard.ChatSystemRightViewHolder;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatActionEarlyLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatActionFinalLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatActionResetLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatAwbLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatAwbRightViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatCommonLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatCreateLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatInputAddressLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatInputAddressRightViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatRightViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatSystemLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatSystemRightViewModel;

/**
 * Created by yoasfs on 23/10/17.
 */

public class DetailChatTypeFactoryImpl extends BaseAdapterTypeFactory implements DetailChatTypeFactory {
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
    public int type(ChatSystemLeftViewModel viewModel) {
        return ChatSystemLeftViewHolder.LAYOUT;
    }

    @Override
    public int type(ChatSystemRightViewModel viewModel) {
        return ChatSystemRightViewHolder.LAYOUT;
    }

    @Override
    public int type(ChatCommonLeftViewModel viewModel) {
        return ChatCommonLeftViewHolder.LAYOUT;
    }

    @Override
    public int type(ChatInputAddressLeftViewModel viewModel) {
        return ChatInputAddressLeftViewHolder.LAYOUT;
    }

    @Override
    public int type(ChatInputAddressRightViewModel viewModel) {
        return ChatInputAddressRightViewHolder.LAYOUT;
    }

    @Override
    public int type(ChatActionFinalLeftViewModel viewModel) {
        return ChatActionFinalLeftViewHolder.LAYOUT;
    }

    @Override
    public int type(ChatAwbLeftViewModel viewModel) {
        return ChatAwbLeftViewHolder.LAYOUT;
    }

    @Override
    public int type(ChatAwbRightViewModel viewModel) {
        return ChatAwbRightViewHolder.LAYOUT;
    }

    @Override
    public int type(ChatActionResetLeftViewModel viewModel) {
        return ChatActionResetLeftViewHolder.LAYOUT;
    }

    @Override
    public int type(ChatActionEarlyLeftViewModel viewModel) {
        return ChatActionEarlyLeftViewHolder.LAYOUT;
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
        } else if (type == ChatSystemRightViewHolder.LAYOUT) {
            viewHolder = new ChatSystemRightViewHolder(view, mainView);
        } else if (type == ChatSystemLeftViewHolder.LAYOUT) {
            viewHolder = new ChatSystemLeftViewHolder(view, mainView);
        } else if (type == ChatInputAddressLeftViewHolder.LAYOUT) {
            viewHolder = new ChatInputAddressLeftViewHolder(view, mainView);
        } else if (type == ChatInputAddressRightViewHolder.LAYOUT) {
            viewHolder = new ChatInputAddressRightViewHolder(view, mainView);
        } else if (type == ChatCommonLeftViewHolder.LAYOUT) {
            viewHolder = new ChatCommonLeftViewHolder(view, mainView);
        } else if (type == ChatActionFinalLeftViewHolder.LAYOUT) {
            viewHolder = new ChatActionFinalLeftViewHolder(view, mainView);
        } else if (type == ChatAwbRightViewHolder.LAYOUT) {
            viewHolder = new ChatAwbRightViewHolder(view, mainView);
        } else if (type == ChatActionResetLeftViewHolder.LAYOUT) {
            viewHolder = new ChatActionResetLeftViewHolder(view, mainView);
        } else if (type == ChatAwbLeftViewHolder.LAYOUT) {
            viewHolder = new ChatAwbLeftViewHolder(view, mainView);
        } else if (type == ChatActionEarlyLeftViewHolder.LAYOUT) {
            viewHolder = new ChatActionEarlyLeftViewHolder(view, mainView);
        } else {
            viewHolder = super.createViewHolder(view, type);
        }
        return viewHolder;
    }
}
