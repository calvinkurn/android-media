package com.tokopedia.flight.booking.data.cloud;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.flight.booking.data.cloud.entity.CartEntity;
import com.tokopedia.flight.booking.data.cloud.entity.InsuranceEntity;
import com.tokopedia.flight.booking.data.cloud.requestbody.FlightCartRequest;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by alvarisi on 11/13/17.
 */

public class FlightCartDataSource {
    private FlightApi flightApi;
    private UserSession userSession;
    private Gson gson, gsonWithDeserializer;

    @Inject
    public FlightCartDataSource(FlightApi flightApi, UserSession userSession, Gson gson) {
        this.flightApi = flightApi;
        this.userSession = userSession;
        this.gson = gson;
        JsonDeserializer<CartEntity> cartEntityJsonDeserializer = new JsonDeserializer<CartEntity>() {
            @Override
            public CartEntity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                CartEntity cartEntity = new CartEntity();
                JsonObject rootJsonObject = json.getAsJsonObject();
                if (rootJsonObject.has("data")) {
                    JsonObject dataObject = rootJsonObject.getAsJsonObject("data");
                    cartEntity = FlightCartDataSource.this.gson.fromJson(rootJsonObject.get("data").getAsString(), CartEntity.class);
                    if (dataObject.has("relationships")) {
                        List<InsuranceEntity> insuranceEntities = new ArrayList<>();
                        if (rootJsonObject.has("included")) {
                            JsonArray includes = rootJsonObject.getAsJsonArray("included");
                            for (JsonElement include : includes) {
                                JsonObject jsonObject = include.getAsJsonObject();
                                if (jsonObject.get("type").getAsString().equalsIgnoreCase("insurance")) {
                                    // insurance
                                    InsuranceEntity insuranceEntity = FlightCartDataSource.this.gson.fromJson(jsonObject.get("attributes").getAsString(), InsuranceEntity.class);
                                    insuranceEntity.setId(jsonObject.get("id").getAsString());
                                    insuranceEntities.add(insuranceEntity);
                                }
                            }
                        }
                        cartEntity.setInsurances(insuranceEntities);
                    } else {
                        cartEntity.setInsurances(new ArrayList<InsuranceEntity>());
                    }
                }
                return cartEntity;
            }
        };
        this.gsonWithDeserializer = new GsonBuilder().registerTypeAdapter(CartEntity.class, cartEntityJsonDeserializer).create();

    }

    public Observable<CartEntity> addCart(FlightCartRequest request, String idEmpotencyKey) {
        return this.flightApi.addCart(new DataRequest<>(request), idEmpotencyKey, userSession.getUserId())
                .map(new Func1<Response<String>, CartEntity>() {
                    @Override
                    public CartEntity call(Response<String> stringResponse) {
                        return gsonWithDeserializer.fromJson(stringResponse.body(), CartEntity.class);
                    }
                });
    }
}
