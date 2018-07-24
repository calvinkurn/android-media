package com.tokopedia.seller.product.variant.view.fragment;

import android.text.TextUtils;

import com.tokopedia.product.common.model.edit.VariantPictureViewModel;
import com.tokopedia.product.common.model.variantbyprd.variantoption.ProductVariantOptionChild;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hendry on 4/3/17.
 */

public abstract class BaseVariantImageFragment extends BaseImageFragment {

    public abstract ProductVariantOptionChild getProductVariantOptionChild();

    public void changeModelBasedImageUrlOrPath(String imageUrl){
        ProductVariantOptionChild productVariantOptionChild = getProductVariantOptionChild();
        if (productVariantOptionChild == null) {
            return;
        }
        if (TextUtils.isEmpty(imageUrl)) {
            productVariantOptionChild.setProductPictureViewModelList(null);
            return;
        }
        if (productVariantOptionChild.getProductPictureViewModelList() == null ||
                productVariantOptionChild.getProductPictureViewModelList().size() == 0) {
            List<VariantPictureViewModel> productPictureViewModelList = new ArrayList<>();
            VariantPictureViewModel variantPictureViewModel = new VariantPictureViewModel();
            variantPictureViewModel.setFilePath(imageUrl);
            productPictureViewModelList.add(variantPictureViewModel);
            productVariantOptionChild.setProductPictureViewModelList(productPictureViewModelList);
        } else {
            VariantPictureViewModel variantPictureViewModel =
                    productVariantOptionChild.getProductPictureViewModelList().get(0);
            variantPictureViewModel.setId(0);
            variantPictureViewModel.setUrlOriginal("");
            variantPictureViewModel.setUrlThumbnail("");
            variantPictureViewModel.setFilePath(imageUrl);
        }
    }

}