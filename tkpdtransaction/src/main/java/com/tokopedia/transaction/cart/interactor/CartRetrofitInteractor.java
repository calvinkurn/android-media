package com.tokopedia.transaction.cart.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.transaction.cart.model.cartdata.CartData;

import java.util.Map;

/**
 * Created by Kris on 4/8/2016.
 */
public interface CartRetrofitInteractor {
    void getCartInfo(@NonNull Context context,
                     @NonNull Map<String, String> params,
                     @NonNull final OnGetCartInfo listener);

    void cancelCart(@NonNull Context context,
                    @NonNull Map<String, String> params,
                    @NonNull OnCancelCart listener);

    void destroyObservable();

    interface OnGetCartInfo {
        void onSuccess(CartData model);

        void onFailed(String error);

        void onTimeout(String timeout);

        void onError(String errorMessage);

        void onNoConnection();
    }

    interface OnCancelCart {
        void onSuccess(String message, CartData data);

        void onFailedCancelCart(String causes);

        void onFailedRefreshCart(String causes);

        void onError(String message);
    }
}
