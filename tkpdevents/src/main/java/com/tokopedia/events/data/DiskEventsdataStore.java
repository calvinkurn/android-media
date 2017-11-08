package com.tokopedia.events.data;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.events.data.entity.EventResponseEntity;

import rx.Observable;

/**
 * Created by ashwanityagi on 06/11/17.
 */

public class DiskEventsdataStore implements EventDataStore {

    @Override
    public Observable<EventResponseEntity> getEvents(TKPDMapParam<String, Object> params) {
        return null;
    }
}
