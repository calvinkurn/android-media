package com.tokopedia.seller.product.variant.util;

import android.text.TextUtils;

import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantUnit;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantValue;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.VariantSubmitOption;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.VariantUnitSubmit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 8/19/17.
 */

public class ProductVariantUtils {

    private static final String VARIANT_TITLE_SEPARATOR = ",";

    public static String getTitle(int level, VariantUnitSubmit variantUnitSubmit,
                                   ArrayList<ProductVariantByCatModel> productVariantByCatModelList) {
        String title = "";
        if (variantUnitSubmit == null) {
            return title;
        }
        for (VariantSubmitOption variantSubmitOption : variantUnitSubmit.getVariantSubmitOptionList()) {
            if (TextUtils.isEmpty(variantSubmitOption.getCustomText())) {
                // Check variant option title from server
                ProductVariantValue productVariantValue = getProductVariantByCatModelByVariantId(
                        level, variantSubmitOption.getVariantUnitValueId(), productVariantByCatModelList);
                if (productVariantValue != null) {
                    title = getAdditionalTitleText(title, productVariantValue.getValue());
                }
            } else {
                // Get custom name
                title = getAdditionalTitleText(title, variantSubmitOption.getCustomText());
            }
        }
        return title;
    }

    private static String getAdditionalTitleText(String title, String text) {
        if (!TextUtils.isEmpty(title)) {
            title += VARIANT_TITLE_SEPARATOR + " ";
        }
        title += text;
        return title;
    }

    private static ProductVariantValue getProductVariantByCatModelByVariantId(
            long level, long optionId, ArrayList<ProductVariantByCatModel> productVariantByCatModelList) {
        for (ProductVariantByCatModel productVariantByCatModel : productVariantByCatModelList) {
            if (productVariantByCatModel.getStatus() == level) {
                List<ProductVariantUnit> productVariantUnitList = productVariantByCatModel.getUnitList();
                for (ProductVariantUnit productVariantUnit : productVariantUnitList) {
                    List<ProductVariantValue> productVariantValueList = productVariantUnit.getProductVariantValueList();
                    for (ProductVariantValue productVariantValue : productVariantValueList) {
                        if (productVariantValue.getValueId() == optionId) {
                            return productVariantValue;
                        }
                    }
                }
            }
        }
        return null;
    }
}
