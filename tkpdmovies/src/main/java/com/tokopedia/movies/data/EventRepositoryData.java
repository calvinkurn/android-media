package com.tokopedia.movies.data;

import com.google.gson.JsonObject;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.movies.data.entity.response.EventLocationEntity;
import com.tokopedia.movies.data.entity.response.EventResponseEntity;
import com.tokopedia.movies.data.entity.response.EventsDetailsEntity;
import com.tokopedia.movies.data.entity.response.MovieSeatResponseEntity;
import com.tokopedia.movies.data.entity.response.SeatLayoutItem;
import com.tokopedia.movies.data.entity.response.ValidateResponse;
import com.tokopedia.movies.data.entity.response.checkoutreponse.CheckoutResponse;
import com.tokopedia.movies.data.entity.response.seatlayoutresponse.SeatLayoutResponse;
import com.tokopedia.movies.data.entity.response.verifyresponse.VerifyCartResponse;
import com.tokopedia.movies.domain.EventRepository;
import com.tokopedia.movies.domain.model.EventDetailsDomain;
import com.tokopedia.movies.domain.model.EventsCategoryDomain;
import com.tokopedia.movies.domain.model.EventLocationDomain;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;



public class EventRepositoryData implements EventRepository {

    private EventsDataStoreFactory moviesDataStoreFactory;

    public EventRepositoryData(EventsDataStoreFactory moviesDataStoreFactory) {
        this.moviesDataStoreFactory = moviesDataStoreFactory;

    }

    @Override
    public Observable<List<EventsCategoryDomain>> getEvents(TKPDMapParam<String, Object> params) {

        return moviesDataStoreFactory
                .createCloudDataStore()
                .getEvents(params).map(new Func1<EventResponseEntity, List<EventsCategoryDomain>>() {
                    @Override
                    public List<EventsCategoryDomain> call(EventResponseEntity eventResponseEntity) {
                        CommonUtils.dumper("inside EventResponseEntity = " + eventResponseEntity);
                        EventEntityMaper eventEntityMaper = new EventEntityMaper();

                        return eventEntityMaper.tranform(eventResponseEntity);
                    }
                });


    }

    @Override
    public Observable<List<EventsCategoryDomain>> getSearchEvents(TKPDMapParam<String, Object> params) {
        return moviesDataStoreFactory
                .createCloudDataStore()
                .getSearchEvents(params).map(new Func1<EventResponseEntity, List<EventsCategoryDomain>>() {
                    @Override
                    public List<EventsCategoryDomain> call(EventResponseEntity eventResponseEntity) {
                        CommonUtils.dumper("inside EventResponseEntity = " + eventResponseEntity);
                        EventEntityMaper eventEntityMaper = new EventEntityMaper();

                        return eventEntityMaper.tranform(eventResponseEntity);
                    }
                });
    }

    @Override
    public Observable<List<EventLocationDomain>> getEventsLocationList(TKPDMapParam<String, Object> params) {
        return moviesDataStoreFactory
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
        return moviesDataStoreFactory
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
        return moviesDataStoreFactory
                .createCloudDataStore()
                .getEventDetails(url).map(new Func1<EventsDetailsEntity, EventDetailsDomain>() {
                    @Override
                    public EventDetailsDomain call(EventsDetailsEntity moviesDetailsEntity) {
                        EventEntityMaper eventEntityMaper = new EventEntityMaper();
                        return eventEntityMaper.tranformEventDetails(moviesDetailsEntity);
                    }
                });
    }

    @Override
    public Observable<ValidateResponse> validateShow(JsonObject requestBody) {
        return moviesDataStoreFactory
                .createCloudDataStore()
                .validateShow(requestBody);
    }

    @Override
    public Observable<VerifyCartResponse> verifyCard(JsonObject requestBody) {
        return moviesDataStoreFactory
                .createCloudDataStore()
                .verifyCart(requestBody);
    }

    @Override
    public Observable<CheckoutResponse> checkoutCart(JsonObject requestBody) {
        return moviesDataStoreFactory
                .createCloudDataStore()
                .checkoutCart(requestBody);
    }

    @Override
    public Observable<List<SeatLayoutItem>> getSeatLayout(String url) {
        return moviesDataStoreFactory
                .createCloudDataStore()
                .getSeatLayout(url);
    }
}
