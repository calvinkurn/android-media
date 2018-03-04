package com.tokopedia.seller.product.variant.view.adapter.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.design.intdef.CurrencyEnum;
import com.tokopedia.design.label.LabelView;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseViewHolder;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPictureViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantcombination.ProductVariantCombinationViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantoption.ProductVariantOptionChild;
import com.tokopedia.seller.product.variant.view.model.ProductVariantDashboardNewViewModel;

import java.util.List;

/**
 * @author normansyahputa on 5/26/17.
 */
public class ProductVariantDashboardNewViewHolder extends BaseViewHolder<ProductVariantDashboardNewViewModel> {

    private ImageView imageView;
    private TextView titleTextView;
    private TextView tvSubtitle;
    private TextView tvSeeDetail;
    private LabelView lvPrice;
    private LabelView lvStock;
    private @CurrencyTypeDef int currencyType;

    public ProductVariantDashboardNewViewHolder(View itemView, @CurrencyTypeDef int currencyType) {
        super(itemView);
        imageView = itemView.findViewById(R.id.image_view);
        titleTextView = itemView.findViewById(R.id.text_view_title);
        tvSeeDetail = itemView.findViewById(R.id.tv_see_detail);
        lvPrice = itemView.findViewById(R.id.label_view_variant_price);
        lvStock = itemView.findViewById(R.id.label_view_variant_stock);
        tvSubtitle = itemView.findViewById(R.id.tv_subtitle);
        this.currencyType = currencyType;
    }

    @Override
    public void bindObject(final ProductVariantDashboardNewViewModel model) {
        Context context = imageView.getContext();
        ProductVariantOptionChild childLvl1Model = model.getProductVariantOptionChildLv1();
        List<ProductPictureViewModel> productPictureViewModelList = childLvl1Model.getProductPictureViewModelList();
        ProductPictureViewModel pictureViewModel = null;
        if (productPictureViewModelList!= null && productPictureViewModelList.size() > 0) {
            pictureViewModel = productPictureViewModelList.get(0);
        }

        if (pictureViewModel!= null && !TextUtils.isEmpty(pictureViewModel.getUrlOriginal())) {
            imageView.setBackgroundColor(Color.TRANSPARENT);
            ImageHandler.LoadImage(imageView, pictureViewModel.getUrlOriginal());
        } else if (!TextUtils.isEmpty(childLvl1Model.getHex())) {
            imageView.setBackgroundColor(Color.parseColor(childLvl1Model.getHex()));
            imageView.setImageDrawable(null);
        } else {
            imageView.setBackgroundColor(Color.LTGRAY);
            imageView.setImageDrawable(null);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO change image
                Toast.makeText(imageView.getContext(),"Test", Toast.LENGTH_LONG).show();
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
            } else if (currencyType == CurrencyTypeDef.TYPE_IDR){
                lvPrice.setContent(String.format(CurrencyEnum.RP.getFormat(), currencyString));
            } else {
                lvPrice.setContent(currencyString);
            }

            lvPrice.setVisibility(View.VISIBLE);

            if (productVariantCombinationViewModel.isActive() && productVariantCombinationViewModel.getStock() == 0) {
                lvStock.setContent(context.getString(R.string.product_variant_stock_always_available));
            } else if (productVariantCombinationViewModel.getStock() == 0) {
                lvStock.setContent(context.getString(R.string.product_variant_stock_empty));
            } else {
                lvStock.setContent(String.valueOf(productVariantCombinationViewModel.getStock()));
            }

            lvStock.setVisibility(View.VISIBLE);
        } else {
            lvPrice.setVisibility(View.GONE);
            lvStock.setVisibility(View.GONE);
        }
        //TODO populate tvSubtitle from variant list
        titleTextView.setText(childLvl1Model.getValue());
    }
}