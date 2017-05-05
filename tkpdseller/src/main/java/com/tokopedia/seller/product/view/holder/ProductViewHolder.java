package com.tokopedia.seller.product.view.holder;

import android.os.Bundle;

/**
 * Created by nathan on 4/21/17.
 */

public abstract class ProductViewHolder {

    /**
     * Check is data valid or not
     * @return
     */
    public abstract boolean isDataValid();

    public abstract void onSaveInstanceState(Bundle savedInstanceState);

    public abstract void onRestoreInstanceState(Bundle savedInstanceState);
}
