package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailchatadapter;


import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationActionDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationAttachmentDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationCreateTimeDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationFlagDomain;

import java.util.List;

/**
 * Created by yoasfs on 23/10/17.
 */

public class ChatLeftViewModel extends ChatRightViewModel {

    public ChatLeftViewModel() {

    }
    public ChatLeftViewModel(ConversationActionDomain action,
                              String message,
                              ConversationCreateTimeDomain createTime,
                              List<ConversationAttachmentDomain> attachment,
                              ConversationFlagDomain flag) {
        this.action = action;
        this.message = message;
        this.createTime = createTime;
        this.attachment = attachment;
        this.flag = flag;
    }
}
