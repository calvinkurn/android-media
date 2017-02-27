package com.tokopedia.seller.topads.utils;

import com.tkpd.library.utils.network.MessageErrorException;

import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * @author normansyahputa on 2/25/17.
 */

public class DefaultErrorSubscriber<T> extends Subscriber<T> {

    private ErrorNetworkListener errorNetworkListener;

    public DefaultErrorSubscriber(ErrorNetworkListener errorNetworkListener) {
        this.errorNetworkListener = errorNetworkListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        final StringBuilder textMessage = new StringBuilder("");
        if (e instanceof UnknownHostException) {
            textMessage.append("Tidak ada koneksi. \nSilahkan coba kembali");
        } else if (e instanceof MessageErrorException) {
            textMessage.append("Terjadi kesalahan koneksi. \nSilahkan coba kembali");
        } else {
            textMessage.append("Kesalahan tidak diketahui");
        }

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
