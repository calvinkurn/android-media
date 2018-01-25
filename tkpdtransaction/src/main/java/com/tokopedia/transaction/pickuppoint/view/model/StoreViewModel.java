package com.tokopedia.transaction.pickuppoint.view.model;

import com.tokopedia.transaction.pickuppoint.domain.model.Store;

/**
 * Created by Irfan Khoirul on 27/12/17.
 */

public class StoreViewModel {

    private Store store;
    private boolean checked;
    private boolean visible;

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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
