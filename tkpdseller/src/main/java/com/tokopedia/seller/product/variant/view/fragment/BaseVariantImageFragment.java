package com.tokopedia.seller.product.variant.view.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.tokopedia.core.myproduct.utils.FileUtils;
import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.seller.common.imageeditor.GalleryCropWatermarkActivity;
import com.tokopedia.seller.common.imageeditor.ImageEditorActivity;
import com.tokopedia.seller.common.imageeditor.ImageEditorWatermarkActivity;
import com.tokopedia.seller.instoped.InstopedSellerCropWatermarkActivity;
import com.tokopedia.seller.product.edit.view.dialog.ImageAddDialogFragment;
import com.tokopedia.seller.product.edit.view.dialog.ImageEditVariantDialogFragment;
import com.tokopedia.seller.product.edit.view.model.edit.VariantPictureViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantoption.ProductVariantOptionChild;

import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.tokopedia.core.newgallery.GalleryActivity.INSTAGRAM_SELECT_REQUEST_CODE;

/**
 * Created by hendry on 4/3/17.
 */

@RuntimePermissions
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
            variantPictureViewModel.setId("");
            variantPictureViewModel.setUrlOriginal("");
            variantPictureViewModel.setUrlThumbnail("");
            variantPictureViewModel.setFilePath(imageUrl);
        }
    }

}