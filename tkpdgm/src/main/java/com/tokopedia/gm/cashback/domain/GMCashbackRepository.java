package com.tokopedia.gm.cashback.domain;

import rx.Observable;

/**
 * Created by zulfikarrahman on 10/2/17.
 */

public interface GMCashbackRepository {
    Observable<Boolean> setCashback(String product_id, String cashback);
}
