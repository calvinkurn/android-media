package com.tokopedia.events.domain.postusecase;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.events.data.entity.response.checkoutreponse.CheckoutResponse;
import com.tokopedia.events.data.entity.response.verifyresponse.Cart;
import com.tokopedia.events.data.entity.response.verifyresponse.VerifyCartResponse;
import com.tokopedia.events.domain.EventRepository;
import com.tokopedia.events.domain.model.request.cart.CartItem;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by pranaymohapatra on 15/12/17.
 */

public class PostPaymentUseCase extends UseCase<CheckoutResponse> {

    Cart verfiedCart;
    private final EventRepository eventRepository;

    @Inject
    public PostPaymentUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, EventRepository eventRepository) {
        super(threadExecutor, postExecutionThread);
        this.eventRepository = eventRepository;
    }

    @Override
    public Observable<CheckoutResponse> createObservable(RequestParams requestParams) {
        JsonElement jsonElement = new JsonParser().parse(new Gson().toJson(verfiedCart));
        JsonObject requestBody = jsonElement.getAsJsonObject();
        return eventRepository.checkoutCart(requestBody);
    }

    public void setVerfiedCart(Cart cart){
        this.verfiedCart = cart;
    }
}
