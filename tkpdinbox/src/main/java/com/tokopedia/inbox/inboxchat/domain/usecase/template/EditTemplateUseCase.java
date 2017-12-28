package com.tokopedia.inbox.inboxchat.domain.usecase.template;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.inboxchat.data.repository.template.EditTemplateRepository;
import com.tokopedia.inbox.inboxchat.viewmodel.EditTemplateViewModel;

import rx.Observable;

/**
 * Created by stevenfredian on 12/27/17.
 */

public class EditTemplateUseCase extends UseCase<EditTemplateViewModel>{

    private final EditTemplateRepository templateRepository;

    public EditTemplateUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, EditTemplateRepository templateRepository) {
        super(threadExecutor, postExecutionThread);
        this.templateRepository = templateRepository;
    }

    @Override
    public Observable<EditTemplateViewModel> createObservable(RequestParams requestParams) {
        JsonObject object = (JsonObject) requestParams.getParameters().get("json");
        return templateRepository.editTemplate(object);
    }

    public static RequestParams generateParam(JsonArray list, boolean isEnabled) {
        RequestParams requestParams = RequestParams.create();
        JsonObject object = new JsonObject();
        object.add("templates", list);
        object.addProperty("is_enable", isEnabled);
        requestParams.putObject("json", object);
        return requestParams;
    }
}
