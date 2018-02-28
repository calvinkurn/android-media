package com.tokopedia.transaction.checkout.view.view.multipleaddressform;

import android.content.Context;

import com.tokopedia.transaction.checkout.view.data.MultipleAddressAdapterData;

import java.util.List;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

public interface IMultipleAddressPresenter {

    void sendData(Context context, List<MultipleAddressAdapterData> dataList);

    void onUnsubscribe();

}
