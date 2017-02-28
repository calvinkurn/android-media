package com.tokopedia.seller.topads.data.source.cloud.interceptor;

import com.google.gson.Gson;
import com.tokopedia.seller.topads.data.source.cloud.response.Error;
import com.tokopedia.seller.topads.data.source.cloud.response.TkpdResponseError;
import com.tokopedia.seller.topads.exception.ResponseErrorException;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author Hendry on 28.02.2017.
 */
public class TkpdErrorResponseInterceptor implements Interceptor {
    private static final int BYTE_COUNT = 2048;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());

        ResponseBody responseBody = null;
        String responseBodyString = "";
        if (null!= response && response.isSuccessful()) {
            responseBody = response.peekBody(BYTE_COUNT);
            responseBodyString = responseBody.string();
            Gson gson = new Gson();
            TkpdResponseError tkpdResponseError = gson.fromJson(
                    responseBodyString, TkpdResponseError.class);
            if (null== tkpdResponseError) { // no error object
                return response;
            }
            else {
                List<Error> errorList = tkpdResponseError.getErrors();
                if (null == errorList) { // no error List
                    return response;
                }
                else { // has error list
                    throw new ResponseErrorException(errorList);
                }
            }
        }
        return response;
    }

}
