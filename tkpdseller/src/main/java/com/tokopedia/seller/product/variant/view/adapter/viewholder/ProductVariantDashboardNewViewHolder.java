package com.tokopedia.seller.product.variant.view.adapter.viewholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.design.intdef.CurrencyEnum;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseViewHolder;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.edit.view.model.edit.VariantPictureViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantcombination.ProductVariantCombinationViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantoption.ProductVariantOptionChild;
import com.tokopedia.seller.product.variant.view.adapter.ProductVariantDashboardNewAdapter;
import com.tokopedia.seller.product.variant.view.model.ProductVariantDashboardNewViewModel;
import com.tokopedia.seller.product.variant.view.widget.VariantImageView;

import java.util.List;

/**
 * @author normansyahputa on 5/26/17.
 */
public class ProductVariantDashboardNewViewHolder extends BaseViewHolder<ProductVariantDashboardNewViewModel> {

    private VariantImageView variantImageView;
    private TextView titleTextView;
    private TextView tvSubtitle;
    private LabelView lvPrice;
    private LabelView lvStock;
    private @CurrencyTypeDef
    int currencyType;

    private String level2String;

    private ProductVariantDashboardNewAdapter.OnProductVariantDashboardNewAdapterListener listener;

    public ProductVariantDashboardNewViewHolder(View itemView, @CurrencyTypeDef int currencyType,
                                                ProductVariantDashboardNewAdapter.OnProductVariantDashboardNewAdapterListener onProductVariantDashboardNewViewHolderListener,
                                                String level2String) {
        super(itemView);
        variantImageView = itemView.findViewById(R.id.variant_image_view);
        titleTextView = itemView.findViewById(R.id.text_view_title);
        lvPrice = itemView.findViewById(R.id.label_view_variant_price);
        lvStock = itemView.findViewById(R.id.label_view_variant_stock);
        tvSubtitle = itemView.findViewById(R.id.tv_subtitle);
        this.currencyType = currencyType;
        this.level2String = level2String;

        this.listener = onProductVariantDashboardNewViewHolderListener;
    }

    @Override
    public void bindObject(final ProductVariantDashboardNewViewModel model) {
        final Context context = titleTextView.getContext();
        ProductVariantOptionChild childLvl1Model = model.getProductVariantOptionChildLv1();
        List<VariantPictureViewModel> productPictureViewModelList = childLvl1Model.getProductPictureViewModelList();
        VariantPictureViewModel pictureViewModel = null;
        if (productPictureViewModelList != null && productPictureViewModelList.size() > 0) {
            pictureViewModel = productPictureViewModelList.get(0);
        }

        variantImageView.setImage(pictureViewModel, childLvl1Model.getHex());
        variantImageView.setOnImageClickListener(new VariantImageView.OnImageClickListener() {
            @Override
            public void onImageVariantClicked() {
                if (listener != null) {
                    listener.onImageViewVariantClicked(model, (VariantPictureViewModel)
                                    variantImageView.getBasePictureViewModel(),
                            getAdapterPosition());
                }
            }
        });

        if (model.has1LevelOnly()) {
            ProductVariantCombinationViewModel productVariantCombinationViewModel =
                    model.getProductVariantCombinationViewModelList().get(0);
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

            lvPrice.setVisibility(View.VISIBLE);

            if (productVariantCombinationViewModel.getStock() == 0) {
                if (productVariantCombinationViewModel.isActive()) {
                    lvStock.setContent(context.getString(R.string.product_variant_stock_always_available));
                } else {
                    lvStock.setContent(context.getString(R.string.product_variant_stock_empty));
                }
            } else {
                lvStock.setContent(String.valueOf(productVariantCombinationViewModel.getStock()));
            }

            lvStock.setVisibility(View.VISIBLE);
        } else {
            lvPrice.setVisibility(View.GONE);
            lvStock.setVisibility(View.GONE);
        }

        titleTextView.setText(childLvl1Model.getValue());

        List<ProductVariantCombinationViewModel> productVariantCombinationViewModelList =
                model.getProductVariantCombinationViewModelList();
        if (productVariantCombinationViewModelList != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0, sizei = productVariantCombinationViewModelList.size(); i < sizei; i++) {
                ProductVariantCombinationViewModel variantCombinationViewModel =
                        productVariantCombinationViewModelList.get(i);
                if (variantCombinationViewModel.getStock() > 0 || variantCombinationViewModel.isActive()) {
                    if (stringBuilder.length() > 0) {
                        stringBuilder.append(", ");
                    }
                    stringBuilder.append(variantCombinationViewModel.getLevel2String());
                }
            }
            String activeVariantItemString = stringBuilder.toString();
            if (TextUtils.isEmpty(activeVariantItemString)) {
                variantImageView.setStockEmpty(true);
                if (TextUtils.isEmpty(level2String)) {
                    tvSubtitle.setText(null);
                } else {
                    String level2EmptyString = level2String + " " + context.getString(R.string.product_variant_stock_empty);
                    tvSubtitle.setText(level2EmptyString);
                }
            } else {
                variantImageView.setStockEmpty(false);
                tvSubtitle.setText(activeVariantItemString);
            }
        }
    }
}