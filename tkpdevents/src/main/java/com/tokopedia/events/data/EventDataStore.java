package com.tokopedia.events.data;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.events.data.entity.EventResponseEntity;

import rx.Observable;

/**
 * Created by ashwanityagi on 07/11/17.
 */

public interface EventDataStore {

    Observable<EventResponseEntity> getEvents(TKPDMapParam<String, Object> params);
}
