package com.tokopedia.discovery.newdiscovery.data.mapper;

import com.google.gson.Gson;
import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.discovery.model.DynamicFilterModel;
import com.tokopedia.core.network.exception.RuntimeHttpErrorException;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by hangnadi on 10/16/17.
 */

public class DynamicAttributeMapper implements Func1<Response<String>, DynamicFilterModel> {

    private final Gson gson;

    public DynamicAttributeMapper(Gson gson) {
        this.gson = gson;
    }

    @Override
    public DynamicFilterModel call(Response<String> response) {
        if (response.isSuccessful()) {
            DynamicFilterModel pojo = gson.fromJson(response.body(), DynamicFilterModel.class);
            if (pojo != null) {
                return pojo;
            } else {
                throw new MessageErrorException(response.errorBody().toString());
            }
        } else {
            throw new RuntimeHttpErrorException(response.code());
        }
    }
}
