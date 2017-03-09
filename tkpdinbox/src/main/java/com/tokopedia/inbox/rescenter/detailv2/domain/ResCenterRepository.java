package com.tokopedia.inbox.rescenter.detailv2.domain;

import rx.Observable;

/**
 * Created by hangnadi on 3/9/17.
 */

public interface ResCenterRepository {

    Observable<DetailResCenter> getDetail();

    Observable<DetailResCenter> getConversation();

    Observable<Object> getConversationMore();
}
