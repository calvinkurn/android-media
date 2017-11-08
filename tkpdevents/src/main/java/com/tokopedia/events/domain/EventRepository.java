package com.tokopedia.events.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.events.domain.model.Event;

import rx.Observable;

/**
 * Created by ashwanityagi on 03/11/17.
 */

public interface EventRepository {

    Observable<Event> getEvents(TKPDMapParam<String, Object> params);

}
