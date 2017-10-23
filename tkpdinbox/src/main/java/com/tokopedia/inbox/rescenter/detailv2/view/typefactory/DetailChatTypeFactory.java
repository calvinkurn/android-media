package com.tokopedia.inbox.rescenter.detailv2.view.typefactory;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatLeftViewModel;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter.ChatRightViewModel;

/**
 * Created by yoasfs on 23/10/17.
 */

public interface DetailChatTypeFactory {

    int type(ChatLeftViewModel viewModel);

    int type(ChatRightViewModel viewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
