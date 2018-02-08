package com.tokopedia.seller.common.utils;


import android.support.v4.util.ArrayMap;

import com.tkpd.library.utils.network.MessageErrorException;

import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * @author normansyahputa on 2/25/17.
 */

@Deprecated
public class DefaultErrorSubscriber<T> extends Subscriber<T> {

    private ErrorNetworkListener errorNetworkListener;
    private ArrayMap defaultDataTypes = new ArrayMap(2);
    private String defaultErrorException;

    public DefaultErrorSubscriber(ErrorNetworkListener errorNetworkListener) {
        this.errorNetworkListener = errorNetworkListener;
        defaultDataTypes.put(UnknownHostException.class, "Terjadi kesalahan koneksi. \nSilahkan coba lagi");
        defaultDataTypes.put(MessageErrorException.class, "Terjadi kesalahan koneksi. \nSilahkan coba lagi");
        defaultErrorException = "Kesalahan tidak diketahui";

    }

    public DefaultErrorSubscriber(ErrorNetworkListener errorNetworkListener, String defaultErrorException) {
        this(errorNetworkListener);
        this.defaultErrorException = defaultErrorException;
    }

    public DefaultErrorSubscriber(
            ErrorNetworkListener errorNetworkListener,
            ArrayMap<Class<?>, String> dataTypes,
            String defaultErrorException
    ) {
        this(errorNetworkListener);
        defaultDataTypes = dataTypes;
        this.defaultErrorException = defaultErrorException;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        final StringBuilder textMessage = new StringBuilder("");
        if (e instanceof UnknownHostException) {
            textMessage.append(defaultDataTypes.get(UnknownHostException.class));
        } else if (e instanceof MessageErrorException) {
            textMessage.append(defaultDataTypes.get(MessageErrorException.class));
        } else {
            textMessage.append(defaultErrorException);
        }

        showMessageError(textMessage);
    }

    protected void showMessageError(StringBuilder textMessage) {
        if (errorNetworkListener != null) {
            errorNetworkListener.showMessageError(textMessage.toString());
        }
    }

    @Override
    public void onNext(T t) {

    }

    public interface ErrorNetworkListener {
        void showMessageError(String errorMessage);
    }
}
