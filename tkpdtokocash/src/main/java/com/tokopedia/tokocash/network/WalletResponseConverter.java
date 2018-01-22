package com.tokopedia.tokocash.network;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by nabillasabbaha on 1/12/18.
 */

public class WalletResponseConverter extends Converter.Factory {
    private static final MediaType MEDIA_TYPE = MediaType.parse("text/plain");

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                            Annotation[] annotations,
                                                            Retrofit retrofit) {
        if (TkpdTokoCashResponse.class == type) {
            return new Converter<ResponseBody, TkpdTokoCashResponse>() {
                @Override
                public TkpdTokoCashResponse convert(ResponseBody value) throws IOException {
                    return TkpdTokoCashResponse.factory(value.string());
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
        if (TkpdTokoCashResponse.class == type) {
            return new Converter<TkpdTokoCashResponse, RequestBody>() {
                @Override
                public RequestBody convert(TkpdTokoCashResponse value) throws IOException {
                    return RequestBody.create(MEDIA_TYPE, value.getStrResponse());
                }
            };
        }
        return null;
    }

    public static WalletResponseConverter create() {
        return new WalletResponseConverter();
    }
}