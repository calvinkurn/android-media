package com.tokopedia.events.domain.postusecase;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.events.data.entity.response.ValidateResponse;
import com.tokopedia.events.domain.EventRepository;
import com.tokopedia.events.domain.model.request.verify.ValidateShow;
import com.tokopedia.events.view.viewmodel.PackageViewModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by pranaymohapatra on 07/12/17.
 */

public class PostValidateShowUseCase extends UseCase<ValidateResponse> {

    ValidateShow validateShowModel;
    private final EventRepository eventRepository;

    @Inject
    public PostValidateShowUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, EventRepository eventRepository) {
        super(threadExecutor, postExecutionThread);
        this.eventRepository = eventRepository;
    }

    @Override
    public Observable<ValidateResponse> createObservable(RequestParams requestParams) {

        JsonElement jsonElement = new JsonParser().parse(new Gson().toJson(validateShowModel));
        JsonObject requestBody = jsonElement.getAsJsonObject();
        return eventRepository.validateShow(requestBody) ;
    }

    public void setValidateShowModel(ValidateShow model){
        this.validateShowModel = model;
    }
}
