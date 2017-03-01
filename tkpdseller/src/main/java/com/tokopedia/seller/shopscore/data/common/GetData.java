package com.tokopedia.seller.shopscore.data.common;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by sebastianuskh on 3/1/17.
 */
public class GetData<T> implements Func1<Response<T>, T> {
    @Override
    public T call(Response<T> tResponse) {
        return tResponse.body();
    }
}
