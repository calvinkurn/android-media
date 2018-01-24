package com.tokopedia.transaction.pickuppoint.view.mapper;

import com.tokopedia.transaction.pickuppoint.domain.model.Store;
import com.tokopedia.transaction.pickuppoint.view.model.StoreViewModel;

/**
 * Created by Irfan Khoirul on 27/12/17.
 */

public class PickupPointViewModelMapper {

    public StoreViewModel transform(Store store){
        StoreViewModel storeViewModel = null;
        if (store != null) {
            storeViewModel = new StoreViewModel();
            storeViewModel.setStore(store);
            storeViewModel.setChecked(false);
        }
        return storeViewModel;
    }

}
