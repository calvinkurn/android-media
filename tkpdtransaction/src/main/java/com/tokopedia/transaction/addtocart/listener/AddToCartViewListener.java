package com.tokopedia.transaction.addtocart.listener;

import com.tokopedia.core.network.retrofit.utils.DialogNoConnection;
import com.tokopedia.core.product.listener.ViewListener;
import com.tokopedia.transaction.addtocart.model.kero.Attribute;
import com.tokopedia.transaction.addtocart.model.responseatcform.AtcFormData;
import com.tokopedia.transaction.addtocart.model.responseatcform.Destination;
import com.tokopedia.transaction.addtocart.model.responseatcform.ProductDetail;
import com.tokopedia.transaction.addtocart.model.responseatcform.Shipment;

import java.util.List;

/**
 * Created by Angga.Prasetiyo on 14/03/2016.
 * Edited by Hafizh Herdi
 */
public interface AddToCartViewListener extends ViewListener {

    void initialOrderData(AtcFormData data);

    void renderFormProductInfo(ProductDetail data);

    void renderFormAddress(Destination data);

    @SuppressWarnings("Unused")
    void renderFormShipment(List<Shipment> datas);

    void renderFormShipmentRates(List<Attribute> datas);

    void renderProductPrice(String price);

    void disableAllForm();

    void disableBuyButton();

    void enableBuyButton();

    void showErrorMessage(String message);

    void showCalculateProductErrorMessage(String errorMessage);

    void showCalculateShippingErrorMessage();

    void showCalculateAddressShippingError();

    void showUpdateAddressShippingError(String messageError);

    void retryNoConnection(DialogNoConnection.ActionListener listener);

    void showInitLoading();

    void hideInitLoading();

    void onCartFailedLoading();

    String getGoogleMapLocation();

    void hideNetworkError();

    void showTickerGTM(String message);

    void hideTickerGTM();

    void showAddressErrorMessage();
}
