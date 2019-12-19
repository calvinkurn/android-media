package com.tokopedia.seller.orderstatus.interactor;

import android.content.Context;
import androidx.annotation.NonNull;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.Map;

/**
 * Created by kris on 1/30/17. Tokopedia
 */

public interface ShippingStatusDetailInteractor {

    void editRefNum(@NonNull Context context,
                    @NonNull Map<String, String> params,
                    @NonNull onEditRefNumListener listener);

    void retryCourierPickUp(@NonNull Context context,
                            @NonNull TKPDMapParam<String, String> params,
                            @NonNull onRetryPickupListener listener);

    void onViewDestroyed();

    interface onEditRefNumListener {
        void onSuccess(String refNum);

        void onFailed(String errorMsg);
    }

    interface onRetryPickupListener {
        void onSuccess(String successMessage);

        void onFailed(String errorMessage);

        void noConnection();
    }
}
