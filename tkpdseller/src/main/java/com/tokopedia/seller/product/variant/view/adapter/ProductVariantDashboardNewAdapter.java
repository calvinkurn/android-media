package com.tokopedia.seller.product.variant.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.edit.view.model.edit.VariantPictureViewModel;
import com.tokopedia.seller.product.variant.view.adapter.viewholder.ProductVariantDashboardNewViewHolder;
import com.tokopedia.seller.product.variant.view.model.ProductVariantDashboardViewModel;

/**
 * @author normansyahputa on 5/26/17.
 */

public class ProductVariantDashboardNewAdapter extends BaseListAdapter<ProductVariantDashboardViewModel> {

    private int currencyType;
    private String level2String;

    private OnProductVariantDashboardNewAdapterListener onProductVariantDashboardNewAdapterListener;
    public interface OnProductVariantDashboardNewAdapterListener{
        void onImageViewVariantClicked(ProductVariantDashboardViewModel model,
                                       VariantPictureViewModel pictureViewModel,
                                       int position);
    }


    public ProductVariantDashboardNewAdapter(@CurrencyTypeDef int currencyType,
                                             OnProductVariantDashboardNewAdapterListener listener) {
        this.currencyType = currencyType;
        this.onProductVariantDashboardNewAdapterListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ProductVariantDashboardViewModel.TYPE:
                return new ProductVariantDashboardNewViewHolder(getLayoutView(parent,
                        R.layout.item_product_variant_manage_new),
                        currencyType, onProductVariantDashboardNewAdapterListener,
                        level2String
                        );
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    public void setLevel2String(String level2String) {
        this.level2String = level2String;
    }
}