package com.tokopedia.transaction.checkout.view.view.shipmentform;

import android.content.Intent;
import android.support.annotation.Nullable;

import com.tokopedia.transaction.checkout.view.data.CartItemData;

import java.util.List;

/**
 * @author anggaprasetiyo on 29/01/18.
 */

public interface ICartShipmentActivity {

    void closeWithResult(int resultCode, @Nullable Intent intent);

}
