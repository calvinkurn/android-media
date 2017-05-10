package com.tokopedia.seller.product.view.holder;

import android.os.Bundle;
import android.util.Pair;

import java.util.List;
import java.util.Map;

/**
 * Created by nathan on 4/21/17.
 */

public abstract class ProductViewHolder {

    /**
     * Check is data valid or not
     * @return
     */
    public abstract Pair<Boolean, String> isDataValid();

    public abstract void onSaveInstanceState(Bundle savedInstanceState);

    public abstract void onRestoreInstanceState(Bundle savedInstanceState);
}
