package com.tokopedia.seller.product.manage.view.adapter;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.viewholder.BaseMultipleCheckViewHolder;
import com.tokopedia.seller.product.common.utils.CurrencyUtils;
import com.tokopedia.seller.product.edit.constant.FreeReturnTypeDef;
import com.tokopedia.seller.product.manage.constant.ProductManagePreOrderDef;
import com.tokopedia.seller.product.manage.view.model.ProductManageViewModel;

/**
 * Created by zulfikarrahman on 9/25/17.
 */

public class ProductManageListViewHolder extends BaseMultipleCheckViewHolder<ProductManageViewModel> {

    public interface ClickOptionCallbackHolder {
        void onClickOptionItem(ProductManageViewModel productManageViewModel);
    }

    private ImageView productImageView;
    private TextView titleTextView;
    private TextView stockTextView;
    private TextView priceTextView;
    private ImageView featuredImageView;
    private TextView cashbackTextView;
    private TextView wholesaleTextView;
    private TextView preOrderTextView;
    private ImageView freeReturnImageView;
    private ImageButton optionImageButton;
    private CheckBox checkBoxProduct;
    private ClickOptionCallbackHolder clickOptionCallbackHolder;

    public ProductManageListViewHolder(View layoutView) {
        super(layoutView);
        productImageView = (ImageView) layoutView.findViewById(R.id.image_view_product);
        titleTextView = (TextView) layoutView.findViewById(R.id.text_view_title);
        stockTextView = (TextView) layoutView.findViewById(R.id.text_view_stock);
        priceTextView = (TextView) layoutView.findViewById(R.id.text_view_price);

        featuredImageView = (ImageView) layoutView.findViewById(R.id.image_view_featured);
        cashbackTextView = (TextView) layoutView.findViewById(R.id.text_view_cashback);
        wholesaleTextView = (TextView) layoutView.findViewById(R.id.text_view_wholesale);
        preOrderTextView = (TextView) layoutView.findViewById(R.id.text_view_pre_order);
        freeReturnImageView = (ImageView) layoutView.findViewById(R.id.image_view_free_return);
        checkBoxProduct = (CheckBox) layoutView.findViewById(R.id.check_box_product);
        optionImageButton = (ImageButton) layoutView.findViewById(R.id.image_button_option);
    }

    @Override
    public void bindObject(final ProductManageViewModel productManageViewModel, boolean checked) {
        bindObject(productManageViewModel);
        setChecked(checked);
        setBackground(checked);
        checkBoxProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedCallback != null) {
                    checkedCallback.onItemChecked(productManageViewModel, checkBoxProduct.isChecked());
                }
            }
        });
    }

    @Override
    public boolean isChecked() {
        return checkBoxProduct.isChecked();
    }

    @Override
    public void setChecked(boolean checked) {
        checkBoxProduct.setChecked(checked);
        setBackground(checked);
    }

    public void setBackground(boolean isChecked) {
        if (isChecked) {
            itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.light_green));
        } else {
            itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
        }
    }

    @Override
    public void bindObject(final ProductManageViewModel productManageViewModel) {
        ImageHandler.loadImageRounded2(
                productImageView.getContext(),
                productImageView,
                productManageViewModel.getImageUrl()
        );
        titleTextView.setText(productManageViewModel.getProductName());
        priceTextView.setText(priceTextView.getContext().getString(
                R.string.price_format_text, productManageViewModel.getProductCurrencySymbol(),
                CurrencyUtils.getPriceFormatted(productManageViewModel.getProductCurrencyId(), productManageViewModel.getProductPricePlain())));

        if (productManageViewModel.getProductCashback() > 0) {
            cashbackTextView.setText(cashbackTextView.getContext().getString(
                    R.string.product_manage_item_cashback, productManageViewModel.getProductCashback()));
            cashbackTextView.setVisibility(View.VISIBLE);
        } else {
            cashbackTextView.setVisibility(View.GONE);
        }
        preOrderTextView.setVisibility(
                productManageViewModel.getProductPreorder() == ProductManagePreOrderDef.PRE_ORDER ? View.VISIBLE : View.GONE);
        freeReturnImageView.setVisibility(
                productManageViewModel.getProductReturnable() == FreeReturnTypeDef.TYPE_ACTIVE ? View.VISIBLE : View.GONE);
        optionImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickOptionCallbackHolder != null) {
                    clickOptionCallbackHolder.onClickOptionItem(productManageViewModel);
                }
            }
        });
    }

    public void bindFeaturedProduct(boolean isFeaturedProduct) {
        if (isFeaturedProduct) {
            featuredImageView.setVisibility(View.VISIBLE);
        } else {
            featuredImageView.setVisibility(View.GONE);
        }
    }

    public void bindActionMode(boolean isActionMode) {
        if (isActionMode) {
            checkBoxProduct.setEnabled(true);
            checkBoxProduct.setVisibility(View.VISIBLE);
            optionImageButton.setVisibility(View.GONE);
        } else {
            checkBoxProduct.setEnabled(false);
            checkBoxProduct.setVisibility(View.GONE);
            optionImageButton.setVisibility(View.VISIBLE);
        }
    }

    public void setClickOptionCallbackHolder(ClickOptionCallbackHolder clickOptionCallbackHolder) {
        this.clickOptionCallbackHolder = clickOptionCallbackHolder;
    }
}
