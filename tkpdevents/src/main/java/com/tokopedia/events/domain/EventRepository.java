package com.tokopedia.events.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.events.domain.model.CategoryEntity;

import java.util.List;

import rx.Observable;

/**
 * Created by ashwanityagi on 03/11/17.
 */

public interface EventRepository {

    Observable<List<CategoryEntity>> getEvents(TKPDMapParam<String, Object> params);

}
