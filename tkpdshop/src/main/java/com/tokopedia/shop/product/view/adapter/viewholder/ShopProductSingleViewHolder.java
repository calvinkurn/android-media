package com.tokopedia.shop.product.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;

import com.tokopedia.shop.R;
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

/**
 * Created by normansyahputa on 2/22/18.
 */

public class ShopProductSingleViewHolder extends ShopProductViewHolder {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_grid_single;

    public ShopProductSingleViewHolder(View itemView, ShopProductClickedListener shopProductClickedListener) {
        super(itemView, shopProductClickedListener);
    }

    @Override
    protected String getImageUrl(ShopProductViewModel shopProductViewModel) {
        String imageUrl = super.getImageUrl(shopProductViewModel);
        if (!TextUtils.isEmpty(shopProductViewModel.getImageUrl300())) {
            imageUrl = shopProductViewModel.getImageUrl300();
        }
        return imageUrl;
    }
}
