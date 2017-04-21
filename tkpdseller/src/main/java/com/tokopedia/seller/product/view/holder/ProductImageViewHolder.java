package com.tokopedia.seller.product.view.holder;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.adapter.ImageSelectorAdapter;
import com.tokopedia.seller.product.view.dialog.ImageDescriptionDialog;
import com.tokopedia.seller.product.view.dialog.ImageEditDialogFragment;
import com.tokopedia.seller.product.view.fragment.ProductAddFragment;
import com.tokopedia.seller.product.view.model.ImageSelectModel;
import com.tokopedia.seller.product.view.model.upload.ImageProductInputViewModel;
import com.tokopedia.seller.product.view.model.upload.ProductPhotoListViewModel;
import com.tokopedia.seller.product.view.widget.ImagesSelectView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 4/11/17.
 */

public class ProductImageViewHolder {

    public static final int MIN_IMG_RESOLUTION = 300;
    private ImagesSelectView imagesSelectView;
    private ProductAddFragment fragment;

    public ProductImageViewHolder(final ProductAddFragment fragment, View view) {
        this.fragment = fragment;
        imagesSelectView = (ImagesSelectView) view.findViewById(R.id.image_select_view);

        imagesSelectView.setOnImageSelectionListener(new ImageSelectorAdapter.OnImageSelectionListener() {
            @Override
            public void onAddClick(int position) {
                fragment.goToGalleryPermissionCheck(position);
            }

            @Override
            public void onItemClick(int position, final ImageSelectModel imageSelectModel) {
                showEditImageDialog(fragment.getActivity().getSupportFragmentManager(), position,
                        imageSelectModel.isPrimary());
            }
        });
        imagesSelectView.setOnCheckResolutionListener(new ImagesSelectView.OnCheckResolutionListener() {
            @Override
            public boolean isResolutionCorrect(String uri) {
                return (new ImageSelectModel(uri).getMinResolution() >= MIN_IMG_RESOLUTION);
            }

            @Override
            public void resolutionCheckFailed(String uri) {
                if (fragment.getView() == null) {
                    return;
                }
                Snackbar.make(fragment.getView(),
                        fragment.getString(R.string.error_image_resolution), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public ImagesSelectView getImagesSelectView() {
        return imagesSelectView;
    }

    public void showEditImageDialog(FragmentManager fm, int position, boolean isPrimary) {
        DialogFragment dialogFragment = ImageEditDialogFragment.newInstance(position, isPrimary);
        dialogFragment.show(fm, ImageEditDialogFragment.FRAGMENT_TAG);
        ((ImageEditDialogFragment) dialogFragment).setOnImageEditListener(new ImageEditDialogFragment.OnImageEditListener() {
            @Override
            public void clickEditImagePath(int position) {
                GalleryActivity.moveToImageGallery((AppCompatActivity) fragment.getActivity(), position, 1, true);
            }

            @Override
            public void clickEditImageDesc(int position) {
                String currentDescription = imagesSelectView.getImageAt(position).getDescription();
                ImageDescriptionDialog fragment = ImageDescriptionDialog.newInstance(currentDescription);
                fragment.show(fragment.getActivity().getSupportFragmentManager(), ImageDescriptionDialog.TAG);
                fragment.setListener(new ImageDescriptionDialog.OnImageDescDialogListener() {
                    @Override
                    public void onImageDescDialogOK(String newDescription) {
                        imagesSelectView.changeImageDesc(newDescription);
                    }
                });
            }

            @Override
            public void clickEditImagePrimary(int position) {
                imagesSelectView.changeImagePrimary(true, position);
            }

            @Override
            public void clickRemoveImage(int positions) {
                imagesSelectView.removeImage();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == com.tokopedia.core.ImageGallery.TOKOPEDIA_GALLERY && data != null) {
            int position = data.getIntExtra(GalleryActivity.ADD_PRODUCT_IMAGE_LOCATION, GalleryActivity.ADD_PRODUCT_IMAGE_LOCATION_DEFAULT);
            String imageUrl = data.getStringExtra(GalleryActivity.IMAGE_URL);
            if (!TextUtils.isEmpty(imageUrl)) {
                if (position == imagesSelectView.getSelectedImageIndex()) {
                    imagesSelectView.changeImagePath(imageUrl);
                } else {
                    imagesSelectView.addImageString(imageUrl);
                }
            }

            ArrayList<String> imageUrls = data.getStringArrayListExtra(GalleryActivity.IMAGE_URLS);
            if (imageUrls != null) {
                imagesSelectView.addImagesString(imageUrls);
            }
        }
    }

    public ProductPhotoListViewModel getProductPhotos() {
        ProductPhotoListViewModel productPhotos = new ProductPhotoListViewModel();
        List<ImageProductInputViewModel> listImageViewModel = new ArrayList<>();

        List<ImageSelectModel> selectModelList = imagesSelectView.getImageList();
        for (int i = 0; i < selectModelList.size(); i++) {
            ImageProductInputViewModel imageViewModel = new ImageProductInputViewModel();
            ImageSelectModel selectModel = selectModelList.get(i);

            imageViewModel.setImagePath(selectModel.getUri());
            imageViewModel.setImageDescription(selectModel.getDescription());

            if (selectModel.isPrimary()) {
                productPhotos.setProductDefaultPicture(i);
            }
            listImageViewModel.add(imageViewModel);
        }
        productPhotos.setPhotos(listImageViewModel);
        return productPhotos;
    }

    public void setProductPhotos(ProductPhotoListViewModel productPhotos) {
        List<ImageSelectModel> images = new ArrayList<>();
        int defaultPicture = productPhotos.getProductDefaultPicture();
        for (int i = 0; i < productPhotos.getPhotos().size(); i ++){
            ImageProductInputViewModel productPhoto = productPhotos.getPhotos().get(i);
            ImageSelectModel image = new ImageSelectModel(
                    productPhoto.getUrl(),
                    productPhoto.getImageDescription(),
                    i == defaultPicture
            );
            images.add(image);
        }
        imagesSelectView.setImage(images);
    }
}