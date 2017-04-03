package com.tokopedia.seller.category.domain;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface CategoryRepository {
    Observable<Boolean> checkVersion();
}
