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
import com.tokopedia.seller.product.edit.view.dialog.ProductAddImageDialogFragment;
import com.tokopedia.seller.product.edit.view.dialog.ProductAddImageEditVariantDialogFragment;
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
public abstract class BaseVariantImageFragment extends Fragment {

    public abstract boolean needRetainImage();
    public abstract ProductVariantOptionChild getProductVariantOptionChild();
    public abstract void refreshVariantImage();

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        onActivityResultImage(requestCode, resultCode, data);
    }

    public void onActivityResultImage(int requestCode, int resultCode, Intent data) {
        if ((requestCode == com.tokopedia.core.ImageGallery.TOKOPEDIA_GALLERY || (
                requestCode == INSTAGRAM_SELECT_REQUEST_CODE && resultCode == Activity.RESULT_OK)) &&
                data != null) {
            String imageUrl = data.getStringExtra(GalleryActivity.IMAGE_URL);
            if (!TextUtils.isEmpty(imageUrl)) {
                addOrChangeImage(imageUrl);
            } else {
                ArrayList<String> imageUrls = data.getStringArrayListExtra(GalleryActivity.IMAGE_URLS);
                if (imageUrls != null) {
                    addOrChangeImage(imageUrls.get(0));
                }
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == ImageEditorActivity.REQUEST_CODE && data != null) {
            List<String> resultImageUrl = data.getStringArrayListExtra(ImageEditorActivity.RESULT_IMAGE_PATH);
            if (resultImageUrl != null && resultImageUrl.size() > 0) {
                String imageUrl = resultImageUrl.get(0);
                addOrChangeImage(imageUrl);
            }
        }
    }


    private void addOrChangeImage(String imageUrl) {
        if (!TextUtils.isEmpty(imageUrl)) {
            ProductVariantOptionChild productVariantOptionChild = getProductVariantOptionChild();
                    // productVariantViewModel.getProductVariantOptionChild(0).get(clickedPosition);
            if (productVariantOptionChild == null) {
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
            refreshVariantImage();
        }
    }

    protected void showAddImageDialog(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        ProductAddImageDialogFragment dialogFragment = ProductAddImageDialogFragment.newInstance(0);
        dialogFragment.show(fm, ProductAddImageDialogFragment.FRAGMENT_TAG);
        dialogFragment.setOnImageAddListener(new ProductAddImageDialogFragment.OnImageAddListener() {
            @Override
            public void clickAddProductFromCamera(int position) {
                BaseVariantImageFragmentPermissionsDispatcher.goToCameraWithCheck(BaseVariantImageFragment.this);
            }

            @Override
            public void clickAddProductFromGallery(int position) {
                BaseVariantImageFragmentPermissionsDispatcher.goToGalleryWithCheck(BaseVariantImageFragment.this);
            }

            @Override
            public void clickAddProductFromInstagram(int position) {
                InstopedSellerCropWatermarkActivity.startInstopedActivityForResult(getContext(),
                        BaseVariantImageFragment.this,
                        INSTAGRAM_SELECT_REQUEST_CODE, 1);
            }
        });
    }

    protected void showEditImageDialog(final VariantPictureViewModel pictureViewModel){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        DialogFragment dialogFragment = ProductAddImageEditVariantDialogFragment.newInstance();
        dialogFragment.show(fm, ProductAddImageEditVariantDialogFragment.FRAGMENT_TAG);
        ((ProductAddImageEditVariantDialogFragment) dialogFragment).setOnImageEditListener(new ProductAddImageEditVariantDialogFragment.OnImageEditListener() {

            @Override
            public void clickEditImagePathFromCamera() {
                GalleryCropWatermarkActivity.moveToImageGalleryCamera(getActivity(), BaseVariantImageFragment.this, 0,
                        true, 1,true);
            }

            @Override
            public void clickEditImagePathFromGallery() {
                GalleryCropWatermarkActivity.moveToImageGallery(getActivity(), BaseVariantImageFragment.this, 0,
                        1, true);
            }

            @Override
            public void clickEditImagePathFromInstagram() {
                InstopedSellerCropWatermarkActivity.startInstopedActivityForResult(getContext(), BaseVariantImageFragment.this,
                        INSTAGRAM_SELECT_REQUEST_CODE, 1);
            }

            @Override
            public void clickImageEditor() {
                String uriOrPath = pictureViewModel.getUriOrPath();
                if (!TextUtils.isEmpty(uriOrPath)) {
                    onImageEditor(uriOrPath);
                }
            }

            @Override
            public void clickRemoveImage() {
                String path = pictureViewModel.getUriOrPath();
                getProductVariantOptionChild().setProductPictureViewModelList(null);
                refreshVariantImage();

                if (!TextUtils.isEmpty(path) && !needRetainImage()) {
                    FileUtils.deleteAllCacheTkpdFile(path);
                }
            }
        });
    }

    public void onImageEditor(String uriOrPath) {
        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add(uriOrPath);
        ImageEditorWatermarkActivity.start(getContext(),
                BaseVariantImageFragment.this, imageUrls,
                !needRetainImage());
    }

    @TargetApi(16)
    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void goToGallery() {
        GalleryCropWatermarkActivity.moveToImageGallery(getActivity(), this,
                0, 1, true);
    }

    @TargetApi(16)
    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void goToCamera() {
        GalleryCropWatermarkActivity.moveToImageGalleryCamera(getActivity(), this, 0,
                true, 1,true);

    }


}