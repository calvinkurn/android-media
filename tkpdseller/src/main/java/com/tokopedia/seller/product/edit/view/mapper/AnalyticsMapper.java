package com.tokopedia.seller.product.edit.view.mapper;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.seller.product.edit.constant.ProductStockTypeDef;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.ProductVariantViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantoption.ProductVariantOptionChild;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantoption.ProductVariantOptionParent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 5/10/17.
 */

public class AnalyticsMapper {

    public static List<String> mapViewToAnalytic(ProductViewModel viewModel, boolean isShare) {
        List<String> listOfFields = new ArrayList<>();
        if (viewModel.getProductPictureViewModelList().size() > 0) {
            listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_PICTURE);
        }
        if (viewModel.getProductWholesale() != null && viewModel.getProductWholesale().size() > 0) {
            listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_WHOLESALE);
        }
        if (viewModel.getProductStock() == ProductStockTypeDef.TYPE_ACTIVE) {
            listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_STOCK_MANAGEMENT);
        }
        if (viewModel.isProductFreeReturn()) {
            listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_FREE_RETURN);
        }
        if (StringUtils.isNotBlank(viewModel.getProductDescription())) {
            listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_DESCRIPTION);
        }
        if (viewModel.getProductVideo() != null && viewModel.getProductVideo().size() > 0) {
            listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_PRODUCT_VIDEO);
        }
        if (viewModel.getProductPreorder().getPreorderStatus() > 0) {
            listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_PREORDER);
        }
        if (isShare) {
            listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_SHARE);
        }

        if (viewModel.hasVariant()) {
            boolean hasCustomVariantLv1 = false;
            boolean hasCustomVariantLv2 = false;
            ProductVariantViewModel productVariantViewModel = viewModel.getProductVariant();
            ProductVariantOptionParent productVariantOptionParentLv1 = productVariantViewModel.getVariantOptionParent(0);
            ProductVariantOptionParent productVariantOptionParentLv2 = productVariantViewModel.getVariantOptionParent(1);
            if (productVariantOptionParentLv1 != null && productVariantOptionParentLv1.hasProductVariantOptionChild()) {
                List<ProductVariantOptionChild> productVariantOptionChildList = productVariantOptionParentLv1.getProductVariantOptionChild();
                for (ProductVariantOptionChild productVariantOptionChild : productVariantOptionChildList) {
                    if (productVariantOptionChild.isCustomVariant()) {
                        hasCustomVariantLv1 = true;
                        listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_VARIANT_LEVEL1_CUSTOM);
                        break;
                    }
                }
                if (!hasCustomVariantLv1) {
                    listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_VARIANT_LEVEL1);
                }

            }
            if (productVariantOptionParentLv2 != null && productVariantOptionParentLv2.hasProductVariantOptionChild()) {
                List<ProductVariantOptionChild> productVariantOptionChildList = productVariantOptionParentLv2.getProductVariantOptionChild();
                for (ProductVariantOptionChild productVariantOptionChild : productVariantOptionChildList) {
                    if (productVariantOptionChild.isCustomVariant()) {
                        hasCustomVariantLv2 = true;
                        listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_VARIANT_LEVEL2_CUSTOM);
                        break;
                    }
                }
                if (!hasCustomVariantLv2) {
                    listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_VARIANT_LEVEL2);
                }
            }
        }

        if (listOfFields.isEmpty()) {
            listOfFields.add(AppEventTracking.AddProduct.FIELDS_OPTIONAL_EMPTY);
        }

        return listOfFields;
    }
}
