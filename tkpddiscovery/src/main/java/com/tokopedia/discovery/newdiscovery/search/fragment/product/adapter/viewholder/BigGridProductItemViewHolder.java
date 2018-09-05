package com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.listener.ItemClickListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;

public class BigGridProductItemViewHolder extends GridProductItemViewHolder {
    @LayoutRes
    public static final int LAYOUT = R.layout.search_result_product_item_big_grid;
    private final Context context;

    private ImageView productImage;


    public BigGridProductItemViewHolder(View itemView, ItemClickListener itemClickListener) {
        super(itemView, itemClickListener);
        productImage = (ImageView) itemView.findViewById(R.id.product_image);
        context = itemView.getContext();
    }

    @Override
    public void bind(ProductItem productItem) {
        super.bind(productItem);
    }

    @Override
    public void setImageProduct(ProductItem productItem) {
        ImageHandler.loadImageSourceSize(context, productImage, productItem.getImageUrl700());
    }
}
