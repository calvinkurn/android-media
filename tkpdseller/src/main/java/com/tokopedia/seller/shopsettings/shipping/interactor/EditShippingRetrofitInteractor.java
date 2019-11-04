package com.tokopedia.seller.shopsettings.shipping.interactor;

import android.content.Context;
import androidx.annotation.NonNull;
import com.tokopedia.seller.shopsettings.shipping.model.editshipping.EditShippingCouriers;
import com.tokopedia.seller.shopsettings.shipping.model.openshopshipping.OpenShopData;

import java.util.Map;


/**
 * Created by Kris on 2/22/2016.
 */
public interface EditShippingRetrofitInteractor {

    void getCourierList(@NonNull Context context,
                        @NonNull Map<String, String> params,
                        @NonNull CourierListListener listener);

    void initiateCourierList(@NonNull Context context,
                             @NonNull CourierListListener listener);

    void updateCourierChanges(@NonNull Context context,
                              @NonNull Map<String, String> params,
                              @NonNull ShippingUpdateListener listener);

    void getShippingDetailWebView(@NonNull Context context,
                                  @NonNull String webViewUrl,
                                  @NonNull Map<String, String> params,
                                  @NonNull getShippingDetailListener listener);

    void getOpenShopData(@NonNull Context context,
                         @NonNull Map<String, String> params,
                         @NonNull getOpenShopDataListener listener);

    interface CourierListListener{
        void onSuccess(EditShippingCouriers model);
        void onFailed(String error);
        void onTimeout(String timeoutError);
        void onNoConnection();
    }

    interface ShippingUpdateListener{
        void onSuccess(String messageStatus);
        void onFailed(String errorMessage);
        void onTimeout();
        void onNoConnection();
    }

    interface getShippingDetailListener{
        void onSuccess(String webResources);
        void onFailed();
        void onTimeout();
        void onNoConnection();
    }

    interface getOpenShopDataListener{
        void onSuccess(OpenShopData modeal);
        void onFailed();
        void onTimeout();
        void onNoConnection();
    }

    void onViewDestroyed();

}
