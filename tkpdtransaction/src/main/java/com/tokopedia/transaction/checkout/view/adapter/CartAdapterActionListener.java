package com.tokopedia.transaction.checkout.view.adapter;

import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemPromoHolderData;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemTickerErrorHolderData;

/**
 * @author anggaprasetiyo on 13/03/18.
 */

public interface CartAdapterActionListener {

    void onCartPromoSuggestionActionClicked(CartPromoSuggestion cartPromoSuggestion, int position);

    void onCartPromoSuggestionButtonCloseClicked(CartPromoSuggestion cartPromoSuggestion, int position);

    void onCartPromoUseVoucherPromoClicked(CartItemPromoHolderData cartPromo, int position);

    void onCartPromoCancelVoucherPromoClicked(CartItemPromoHolderData cartPromo, int position);

    void onCartPromoTrackingSuccess(CartItemPromoHolderData cartPromo, int position);

    void onCartPromoTrackingCancelled(CartItemPromoHolderData cartPromo, int position);

    void onCartDataEnableToCheckout();

    void onCartDataDisableToCheckout();

    void onCartItemTickerErrorActionClicked(CartItemTickerErrorHolderData data, int position);

}
