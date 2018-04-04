package com.tokopedia.events.data;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.events.data.entity.response.EventLocationEntity;
import com.tokopedia.events.data.entity.response.EventResponseEntity;
import com.tokopedia.events.data.entity.response.SeatLayoutItem;
import com.tokopedia.events.data.entity.response.ValidateResponse;
import com.tokopedia.events.data.entity.response.checkoutreponse.CheckoutResponse;
import com.tokopedia.events.data.entity.response.seatlayoutresponse.SeatLayoutResponse;
import com.tokopedia.events.data.entity.response.verifyresponse.VerifyCartResponse;
import com.tokopedia.events.domain.EventRepository;
import com.tokopedia.events.domain.model.EventDetailsDomain;
import com.tokopedia.events.domain.model.EventLocationDomain;
import com.tokopedia.events.domain.model.EventsCategoryDomain;
import com.tokopedia.events.domain.model.searchdomainmodel.SearchDomainModel;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by ashwanityagi on 07/11/17.
 */


public class EventRepositoryData implements EventRepository {

    private EventsDataStoreFactory eventsDataStoreFactory;

    public EventRepositoryData(EventsDataStoreFactory eventsDataStoreFactory) {
        this.eventsDataStoreFactory = eventsDataStoreFactory;

    }

    @Override
    public Observable<List<EventsCategoryDomain>> getEvents(TKPDMapParam<String, Object> params) {

        return eventsDataStoreFactory
                .createCloudDataStore()
                .getEvents(params).map(new EventTransformMapper());
    }

    @Override
    public Observable<SearchDomainModel> getSearchEvents(TKPDMapParam<String, Object> params) {
        return eventsDataStoreFactory
                .createCloudDataStore()
                .getSearchEvents(params).map(new SearchResponseMapper());
    }

    @Override
    public Observable<SearchDomainModel> getSearchNext(String nextUrl) {
        return eventsDataStoreFactory
                .createCloudDataStore()
                .getSearchNext(nextUrl).map(new SearchResponseMapper());
    }

    @Override
    public Observable<List<EventLocationDomain>> getEventsLocationList(TKPDMapParam<String, Object> params) {
        return eventsDataStoreFactory
                .createCloudDataStore()
                .getEventsLocationList(params).map(new Func1<EventLocationEntity, List<EventLocationDomain>>() {
                    @Override
                    public List<EventLocationDomain> call(EventLocationEntity eventLocationEntity) {
                        EventEntityMaper eventEntityMaper = new EventEntityMaper();
                        return eventEntityMaper.tranformLocationList(eventLocationEntity.getLocationResponseEntity());
                    }
                });
    }

    @Override
    public Observable<List<EventsCategoryDomain>> getEventsListByLocation(String location) {
        return eventsDataStoreFactory
                .createCloudDataStore()
                .getEventsListByLocation(location).map(new Func1<EventResponseEntity, List<EventsCategoryDomain>>() {
                    @Override
                    public List<EventsCategoryDomain> call(EventResponseEntity eventResponseEntity) {
                        EventEntityMaper eventEntityMaper = new EventEntityMaper();
                        return eventEntityMaper.tranform(eventResponseEntity);
                    }
                });
    }

    @Override
    public Observable<EventDetailsDomain> getEventDetails(String url) {
        return eventsDataStoreFactory
                .createCloudDataStore()
                .getEventDetails(url).map(new EventDetailsResponseMapper());
    }

    @Override
    public Observable<ValidateResponse> validateShow(JsonObject requestBody) {
        return eventsDataStoreFactory
                .createCloudDataStore()
                .validateShow(requestBody);
    }

    @Override
    public Observable<VerifyCartResponse> verifyCard(JsonObject requestBody, boolean flag) {
        return eventsDataStoreFactory
                .createCloudDataStore()
                .verifyCart(requestBody, flag);
    }

    @Override
    public Observable<CheckoutResponse> checkoutCart(JsonObject requestBody) {
        return eventsDataStoreFactory
                .createCloudDataStore()
                .checkoutCart(requestBody);
    }

    @Override
    public Observable<SeatLayoutResponse> getSeatLayout(int category_id,
                                                        int product_id,
                                                        int schedule_id,
                                                        int group_id,
                                                        int package_id) {
        return eventsDataStoreFactory
                .createCloudDataStore()
                .getSeatLayout(category_id, product_id, schedule_id, group_id, package_id);
    }

    @Override
    public Observable<List<SeatLayoutItem>> getEventSeatLayout(String url) {
        return eventsDataStoreFactory
                .createCloudDataStore()
                .getEventSeatLayout(url);
    }


}
