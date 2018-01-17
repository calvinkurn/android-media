package com.tokopedia.inbox.inboxchat.domain.usecase.template;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.inboxchat.data.repository.template.TemplateRepository;
import com.tokopedia.inbox.inboxchat.viewmodel.GetTemplateViewModel;

import rx.Observable;

/**
 * Created by stevenfredian on 11/27/17.
 */

public class GetTemplateUseCase extends UseCase<GetTemplateViewModel> {

    private final TemplateRepository templateRepository;

    public GetTemplateUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, TemplateRepository templateRepository) {
        super(threadExecutor, postExecutionThread);
        this.templateRepository = templateRepository;
    }

    @Override
    public Observable<GetTemplateViewModel> createObservable(RequestParams requestParams) {
        return templateRepository.getTemplate(requestParams.getParameters());
    }

    public static RequestParams generateParam() {
        RequestParams requestParams = RequestParams.create();
        return requestParams;
    }
}
