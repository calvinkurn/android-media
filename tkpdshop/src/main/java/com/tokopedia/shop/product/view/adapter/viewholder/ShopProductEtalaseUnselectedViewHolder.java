package com.tokopedia.shop.product.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.shop.R;

/**
 * Created by normansyahputa on 2/24/18.
 */

public class ShopProductEtalaseUnselectedViewHolder extends ShopProductEtalaseSelectedViewHolder {
    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_etalase_picker_unselected;

    public ShopProductEtalaseUnselectedViewHolder(View itemView) {
        super(itemView);
    }
}
