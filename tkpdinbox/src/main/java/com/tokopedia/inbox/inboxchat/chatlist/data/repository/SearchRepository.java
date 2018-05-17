package com.tokopedia.inbox.inboxchat.chatlist.data.repository;


import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.inboxchat.chatlist.viewmodel.InboxChatViewModel;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public interface SearchRepository {

    Observable<InboxChatViewModel> searchChat(TKPDMapParam<String, Object> parameters);
}
