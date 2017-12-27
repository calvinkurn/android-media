package com.tokopedia.transaction.pickup.alfamart.view.model;

import com.tokopedia.transaction.pickup.alfamart.domain.model.Store;

/**
 * Created by Irfan Khoirul on 27/12/17.
 */

public class StoreViewModel {

    private Store store;
    private boolean checked;

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

}
