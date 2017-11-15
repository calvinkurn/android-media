package com.tokopedia.events.data;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.events.data.entity.EventResponseEntity;
import com.tokopedia.events.domain.EventRepository;
import com.tokopedia.events.domain.model.CategoryEntity;

import java.util.List;

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
    public Observable<List<CategoryEntity>> getEvents(TKPDMapParam<String, Object> params) {

        return eventsDataStoreFactory
                .createCloudDataStore()
                .getEvents(params).map(new Func1<EventResponseEntity, List<CategoryEntity>>() {
                    @Override
                    public List<CategoryEntity> call(EventResponseEntity eventResponseEntity) {
                        CommonUtils.dumper("inside EventResponseEntity = "+eventResponseEntity);
                        EventEntityMaper eventEntityMaper=new EventEntityMaper();

                        return eventEntityMaper.tranform(eventResponseEntity);
                    }
                });


    }
}
