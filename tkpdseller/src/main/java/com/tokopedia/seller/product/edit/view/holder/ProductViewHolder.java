package com.tokopedia.seller.product.edit.view.holder;

import android.os.Bundle;
import android.util.Pair;

import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;

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

    public abstract void onViewStateRestored(Bundle savedInstanceState);

    public abstract void renderData(ProductViewModel model);

    public abstract void updateModel(ProductViewModel model);

}
