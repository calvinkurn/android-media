package com.tokopedia.flight.search.view;

import com.tokopedia.abstraction.base.view.listener.CustomerView;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public interface FlightFilterCountView extends CustomerView {
    void onErrorGetCount(Throwable throwable);
    void onSuccessGetCount(int count);
}
