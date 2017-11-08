package com.tokopedia.events.data;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.events.data.entity.EventResponseEntity;
import com.tokopedia.events.domain.EventRepository;
import com.tokopedia.events.domain.model.Event;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by ashwanityagi on 07/11/17.
 */


public class EventRepositoryData implements EventRepository {

    private EventsDataStoreFactory eventsDataStoreFactory;
    public EventRepositoryData(EventsDataStoreFactory eventsDataStoreFactory){
        this.eventsDataStoreFactory=eventsDataStoreFactory;

    }
    @Override
    public Observable<Event> getEvents(TKPDMapParam<String, Object> params) {

        return eventsDataStoreFactory
                .createCloudDataStore()
                .getEvents(params).map(new Func1<EventResponseEntity, Event>() {
                    @Override
                    public Event call(EventResponseEntity eventResponseEntity) {
                        CommonUtils.dumper("inside EventResponseEntity = "+eventResponseEntity);
                        return null;
                    }
                });


    }
}
