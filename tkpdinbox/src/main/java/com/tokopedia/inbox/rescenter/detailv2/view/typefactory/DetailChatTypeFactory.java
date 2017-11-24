package com.tokopedia.inbox.rescenter.detailv2.view.typefactory;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatActionFinalLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatCreateLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatInputAddressLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatInputAddressRightViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatNotSupportedLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatNotSupportedRightViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatCommonLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatRightViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatSystemLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatSystemRightViewModel;

/**
 * Created by yoasfs on 23/10/17.
 */

public interface DetailChatTypeFactory {

    int type(ChatLeftViewModel viewModel);

    int type(ChatRightViewModel viewModel);

    int type(ChatCreateLeftViewModel viewModel);

    int type(ChatNotSupportedLeftViewModel viewModel);

    int type(ChatNotSupportedRightViewModel viewModel);

    int type(ChatSystemLeftViewModel viewModel);

    int type(ChatSystemRightViewModel viewModel);

    int type(ChatCommonLeftViewModel viewModel);

    int type(ChatInputAddressLeftViewModel viewModel);

    int type(ChatInputAddressRightViewModel viewModel);

    int type(ChatActionFinalLeftViewModel viewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
