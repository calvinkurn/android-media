package com.tokopedia.events.domain.postusecase;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.events.data.entity.response.verifyresponse.VerifyCartResponse;
import com.tokopedia.events.domain.EventRepository;
import com.tokopedia.events.domain.model.request.cart.CartItems;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by pranaymohapatra on 07/12/17.
 */

public class PostVerifyCartUseCase extends UseCase<VerifyCartResponse> {

    private final EventRepository eventRepository;

    @Inject
    public PostVerifyCartUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, EventRepository eventRepository) {
        super(threadExecutor, postExecutionThread);
        this.eventRepository = eventRepository;
    }

    @Override
    public Observable<VerifyCartResponse> createObservable(RequestParams requestParams) {
        CartItems cartItems = (CartItems) requestParams.getObject("checkoutdata");
        boolean flag = requestParams.getBoolean("ispromocodecase", false);
        JsonElement jsonElement = new JsonParser().parse(new Gson().toJson(cartItems));
        JsonObject requestBody = jsonElement.getAsJsonObject();
        return eventRepository.verifyCard(requestBody, flag);
    }
}
