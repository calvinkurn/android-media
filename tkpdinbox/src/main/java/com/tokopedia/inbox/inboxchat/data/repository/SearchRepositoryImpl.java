package com.tokopedia.inbox.inboxchat.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.inboxchat.data.factory.SearchFactory;
import com.tokopedia.inbox.inboxchat.domain.model.search.SearchedMessage;
import com.tokopedia.inbox.inboxchat.viewmodel.InboxChatViewModel;

import rx.Observable;

/**
 * Created by stevenfredian on 10/18/17.
 */

public class SearchRepositoryImpl implements SearchRepository{

    private SearchFactory searchFactory;

    public SearchRepositoryImpl(SearchFactory searchFactory){
        this.searchFactory = searchFactory;
    }

    @Override
    public Observable<InboxChatViewModel> searchChat(TKPDMapParam<String, Object> parameters) {
        return searchFactory.createCloudSearchDataSource().searchChat(parameters);
    }
}
