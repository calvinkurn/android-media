package com.tokopedia.inbox.inboxchat.domain.usecase.template;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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

public class SetAvailabilityTemplateUseCase extends UseCase<GetTemplateViewModel> {

    private final TemplateRepository templateRepository;

    public SetAvailabilityTemplateUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, TemplateRepository templateRepository) {
        super(threadExecutor, postExecutionThread);
        this.templateRepository = templateRepository;
    }

    @Override
    public Observable<GetTemplateViewModel> createObservable(RequestParams requestParams) {
        JsonObject object = (JsonObject) requestParams.getParameters().get("json");
        return templateRepository.setAvailabilityTemplate(object);
    }

    public static RequestParams generateParam(JsonArray list, boolean isEnabled) {
        RequestParams requestParams = RequestParams.create();
        JsonObject object = new JsonObject();
        if(list!=null) {
            object.add("position", list);
        }
        object.addProperty("is_enable", isEnabled);
        requestParams.putObject("json", object);
        return requestParams;
    }
}
