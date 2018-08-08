package com.tokopedia.seller.product.variant.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.design.intdef.CurrencyEnum;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantcombination.ProductVariantCombinationViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hendry on 8/22/2017.
 */
public class ProductVariantDetailLevel1ListAdapter extends
        RecyclerView.Adapter<ProductVariantDetailLevel1ListAdapter.ProductVariantDetaillevel1ListViewHolder> {

    private Context context;
    private List<ProductVariantCombinationViewModel> productVariantCombinationViewModelList;
    private @CurrencyTypeDef int currencyType;

    private OnProductVariantDetailLevel1ListAdapterListener onProductVariantDetailLevel1ListAdapterListener;
    public interface OnProductVariantDetailLevel1ListAdapterListener{
        void onClick(ProductVariantCombinationViewModel productVariantCombinationViewModel);
    }

    public class ProductVariantDetaillevel1ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvTitle;
        private View vStatus;
        private LabelView lvPrice;
        private LabelView lvStock;

        public ProductVariantDetaillevel1ListViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            vStatus = itemView.findViewById(R.id.tv_status);
            lvPrice = itemView.findViewById(R.id.label_view_variant_price);
            lvStock = itemView.findViewById(R.id.label_view_variant_stock);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ProductVariantCombinationViewModel productVariantCombinationViewModel =
                    productVariantCombinationViewModelList.get(getAdapterPosition());
            onProductVariantDetailLevel1ListAdapterListener.onClick(productVariantCombinationViewModel);
        }

        public void bind(ProductVariantCombinationViewModel productVariantCombinationViewModel) {
            tvTitle.setText(productVariantCombinationViewModel.getLevel2String());

            String currencyString = CurrencyFormatUtil.convertPriceValue(
                    productVariantCombinationViewModel.getPriceVar(),
                    currencyType == CurrencyTypeDef.TYPE_USD);

            if (currencyType == CurrencyTypeDef.TYPE_USD) {
                lvPrice.setContent(String.format(CurrencyEnum.USD.getFormat(), currencyString));
            } else if (currencyType == CurrencyTypeDef.TYPE_IDR) {
                lvPrice.setContent(String.format(CurrencyEnum.RP.getFormat(), currencyString));
            } else {
                lvPrice.setContent(currencyString);
            }

            if (productVariantCombinationViewModel.getStock() == 0) {
                if (productVariantCombinationViewModel.isActive()) {
                    lvStock.setContent(context.getString(R.string.product_variant_stock_always_available));
                    vStatus.setVisibility(View.GONE);
                } else {
                    lvStock.setContent(context.getString(R.string.product_variant_stock_empty));
                    vStatus.setVisibility(View.VISIBLE);
                }
            } else {
                lvStock.setContent(String.valueOf(productVariantCombinationViewModel.getStock()));
                vStatus.setVisibility(View.GONE);
            }
        }
    }

    public ProductVariantDetailLevel1ListAdapter(Context context,
                                                 List<ProductVariantCombinationViewModel> productVariantCombinationViewModelList,
                                                 OnProductVariantDetailLevel1ListAdapterListener onProductVariantDetailLevel1ListAdapterListener,
                                                 @CurrencyTypeDef int currencyType) {
        setProductVariantCombinationViewModelList(productVariantCombinationViewModelList);
        this.currencyType = currencyType;
        this.context = context;
        this.onProductVariantDetailLevel1ListAdapterListener = onProductVariantDetailLevel1ListAdapterListener;
    }

    public void setProductVariantCombinationViewModelList(List<ProductVariantCombinationViewModel> productVariantCombinationViewModelList) {
        if (productVariantCombinationViewModelList == null) {
            this.productVariantCombinationViewModelList = new ArrayList<>();
        } else {
            this.productVariantCombinationViewModelList = productVariantCombinationViewModelList;
        }
    }

    @Override
    public ProductVariantDetaillevel1ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_variant_detail_level1_list, parent, false);
        return new ProductVariantDetaillevel1ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductVariantDetaillevel1ListViewHolder holder, int position) {
        holder.bind(productVariantCombinationViewModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return productVariantCombinationViewModelList.size();
    }
}