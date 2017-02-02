package com.tokopedia.seller.common.data;

import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * @author Kulomady on 1/3/17.
 */

public interface DbManagerOperation<T extends BaseModel, S> {

    void store(T data);

    void delete();

    boolean isExpired(long time);

    T getTable();

    S getData();

    boolean isQueryDataEmpty();
}
