package com.tokopedia.inbox.rescenter.detailv2.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.DetailResCenter;

import rx.Observable;

/**
 * Created by hangnadi on 3/9/17.
 */

public interface ResCenterRepository {

    Observable<DetailResCenter> getDetail(TKPDMapParam<String, Object> parameters);

    Observable<DetailResCenter> getConversation();

    Observable<Object> getConversationMore();
}
