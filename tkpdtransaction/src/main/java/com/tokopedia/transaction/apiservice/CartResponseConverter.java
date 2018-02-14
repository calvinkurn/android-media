package com.tokopedia.transaction.apiservice;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author anggaprasetiyo on 24/01/18.
 */

public class CartResponseConverter extends Converter.Factory {

    private static final MediaType MEDIA_TYPE = MediaType.parse("text/plain");

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                            Annotation[] annotations,
                                                            Retrofit retrofit) {
        if (CartResponse.class == type) {
            return new Converter<ResponseBody, CartResponse>() {
                @Override
                public CartResponse convert(ResponseBody value) throws IOException {
                    return CartResponse.factory(value.string());
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
        if (CartResponse.class == type) {
            return new Converter<CartResponse, RequestBody>() {
                @Override
                public RequestBody convert(CartResponse value) throws IOException {
                    return RequestBody.create(MEDIA_TYPE, value.getStrResponse());
                }
            };
        }
        return null;
    }

    public static Converter.Factory create() {
        return new CartResponseConverter();
    }
}
