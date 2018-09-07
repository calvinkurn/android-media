package com.tokopedia.loyalty.domain.apiservice;

import com.tokopedia.loyalty.domain.entity.response.TokoPointResponse;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public class TokoPointResponseConverter extends Converter.Factory {
    private static final MediaType MEDIA_TYPE = MediaType.parse("text/plain");

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                            Annotation[] annotations,
                                                            Retrofit retrofit) {
        if (TokoPointResponse.class == type) {
            return new Converter<ResponseBody, TokoPointResponse>() {
                @Override
                public TokoPointResponse convert(ResponseBody value) throws IOException {
                    return TokoPointResponse.factory(value.string());
                }
            };
        }
        return null;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations,
                                                          Retrofit retrofit) {
        if (TokoPointResponse.class == type) {
            return new Converter<TokoPointResponse, RequestBody>() {
                @Override
                public RequestBody convert(TokoPointResponse value) throws IOException {
                    return RequestBody.create(MEDIA_TYPE, value.getStrResponse());
                }
            };
        }
        return null;
    }

    public static TokoPointResponseConverter create() {
        return new TokoPointResponseConverter();
    }
}
