package com.tokopedia.inbox.inboxchat.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.inboxchat.data.repository.SearchRepository;
import com.tokopedia.inbox.inboxchat.data.repository.SearchRepositoryImpl;
import com.tokopedia.inbox.inboxchat.domain.model.search.SearchedMessage;
import com.tokopedia.inbox.inboxchat.viewmodel.InboxChatViewModel;

import rx.Observable;

/**
 * Created by stevenfredian on 10/18/17.
 */

public class SearchMessageUseCase extends UseCase<InboxChatViewModel> {

    public final static String PARAM_BY_REPLY = "reply";

    private final SearchRepository searchRepository;

    public SearchMessageUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, SearchRepository searchRepository) {
        super(threadExecutor, postExecutionThread);
        this.searchRepository = searchRepository;
    }

    @Override
    public Observable<InboxChatViewModel> createObservable(RequestParams requestParams) {
        return searchRepository.searchChat(requestParams.getParameters());
    }

    public static RequestParams generateParam(String keyword, int page, String by)
    {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("keyword", keyword);
        requestParams.putInt("page", page);
        if(page==1) by = "";
        requestParams.putString("by", by);
        return requestParams;
    }

    public static RequestParams generateParam(String keyword)
    {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("keyword", keyword);
        requestParams.putString("by", "");
        return requestParams;
    }
}
