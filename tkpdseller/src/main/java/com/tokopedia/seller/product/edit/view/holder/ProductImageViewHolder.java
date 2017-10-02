package com.tokopedia.seller.product.edit.view.holder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.core.myproduct.utils.FileUtils;
import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.imageeditor.ImageEditorActivity;
import com.tokopedia.seller.common.imageeditor.ImageEditorFragment;
import com.tokopedia.seller.product.edit.view.adapter.ImageSelectorAdapter;
import com.tokopedia.seller.product.edit.view.model.ImageSelectModel;
import com.tokopedia.seller.product.edit.view.model.upload.ImageProductInputViewModel;
import com.tokopedia.seller.product.edit.view.model.upload.ProductPhotoListViewModel;
import com.tokopedia.seller.product.edit.view.widget.ImagesSelectView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 4/11/17.
 */

public class ProductImageViewHolder extends ProductViewHolder {

    public interface Listener {
        void onAddImagePickerClicked(int position);

        void onImagePickerItemClicked(int position, boolean isPrimary, boolean allowDelete);

        void onResolutionImageCheckFailed(String uri);

        void onTotalImageUpdated(int total);

        void onImageResolutionChanged(int maxSize);

        void onImageEditor(String uriOrPath);
    }

    public static final int MIN_IMG_RESOLUTION = 300;
    private ImagesSelectView imagesSelectView;

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setImages(ArrayList<String> images) {
        imagesSelectView.addImagesString(images);
    }

    public ProductImageViewHolder(View view) {
        imagesSelectView = (ImagesSelectView) view.findViewById(R.id.image_select_view);
        imagesSelectView.setOnImageSelectionListener(new ImageSelectorAdapter.OnImageSelectionListener() {
            @Override
            public void onAddClick(int position) {
                if (listener != null) {
                    listener.onAddImagePickerClicked(position);
                }
            }

            @Override
            public void onItemClick(int position, final ImageSelectModel imageSelectModel) {
                if (listener != null) {
                    listener.onImagePickerItemClicked(position, imageSelectModel.isPrimary(), imageSelectModel.allowDelete());
                }
            }
        });
        imagesSelectView.setOnCheckResolutionListener(new ImagesSelectView.OnCheckResolutionListener() {
            @Override
            public boolean isResolutionCorrect(String uri) {
                return (new ImageSelectModel(uri).getMinResolution() >= MIN_IMG_RESOLUTION);
            }

            @Override
            public void resolutionCheckFailed(String uri) {
                if (listener != null) {
                    listener.onResolutionImageCheckFailed(uri);
                }
            }

            @Override
            public void removePreviousPath(String uri) {
                if (!TextUtils.isEmpty(uri)) {
                    FileUtils.deleteAllCacheTkpdFile(uri);
                }
            }
        });
        imagesSelectView.setOnImageChanged(new ImagesSelectView.OnImageChanged() {
            @Override
            public void onTotalImageUpdated(int total) {
                listener.onTotalImageUpdated(total);
                updateImageResolution();
            }

            private void updateImageResolution() {
                ProductPhotoListViewModel productPhotoListViewModel = getProductPhotos();
                int imageCount = productPhotoListViewModel.getPhotos().size();
                if (imageCount > 0) {
                    ImageProductInputViewModel imageProductInputViewModel = productPhotoListViewModel.getPhotos().get(productPhotoListViewModel.getProductDefaultPicture());
                    listener.onImageResolutionChanged(imageProductInputViewModel.getImageResolution());
                }
            }
        });
    }

    public ImagesSelectView getImagesSelectView() {
        return imagesSelectView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == com.tokopedia.core.ImageGallery.TOKOPEDIA_GALLERY && data != null) {
            String imageUrl = data.getStringExtra(GalleryActivity.IMAGE_URL);
            if (!TextUtils.isEmpty(imageUrl)) {
                listener.onImageEditor(imageUrl);
            } else {
                ArrayList<String> imageUrls = data.getStringArrayListExtra(GalleryActivity.IMAGE_URLS);
                if (imageUrls != null) {
                    imagesSelectView.addImagesString(imageUrls);
                }
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == ImageEditorActivity.REQUEST_CODE && data != null) {
            List<String> resultImageUrl = data.getStringArrayListExtra(ImageEditorActivity.RESULT_IMAGE_PATH);
            if (resultImageUrl!= null && resultImageUrl.size() > 0) {
                String imageUrl = resultImageUrl.get(0);
                if (!TextUtils.isEmpty(imageUrl)) {
                    if (imagesSelectView.getSelectedImageIndex() < 0) {
                        imagesSelectView.addImageString(imageUrl);
                    } else {
                        imagesSelectView.changeImagePath(imageUrl);
                    }
                }
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

            if (selectModel.isValidURL()) {
                imageViewModel.setUrl(selectModel.getUriOrPath());
            } else {
                imageViewModel.setImagePath(selectModel.getUriOrPath());
            }

            imageViewModel.setImageDescription(selectModel.getDescription());
            imageViewModel.setImageResolution(selectModel.getMinResolution());
            imageViewModel.setCanDelete(selectModel.allowDelete());

            if (selectModel.isPrimary()) {
                productPhotos.setProductDefaultPicture(i);
            }
            listImageViewModel.add(imageViewModel);
        }
        productPhotos.setPhotos(listImageViewModel);
        return productPhotos;
    }

    public void setProductPhotos(ProductPhotoListViewModel productPhotos, boolean isEditMode) {
        ArrayList<ImageSelectModel> images = new ArrayList<>();
        int defaultPicture = productPhotos.getProductDefaultPicture();
        for (int i = 0; i < productPhotos.getPhotos().size(); i++) {
            ImageProductInputViewModel productPhoto = productPhotos.getPhotos().get(i);
            String url = productPhoto.getUrl();
            String path = productPhoto.getImagePath();
            if(StringUtils.isBlank(url)){
                if (StringUtils.isBlank(path)) {
                    continue;
                } else {
                    url = productPhoto.getImagePath();
                }
            }
            ImageSelectModel image = new ImageSelectModel(
                    url,
                    productPhoto.getImageDescription(),
                    i == defaultPicture,
                    isEditMode ? productPhoto.canDelete() : true
            );
            images.add(image);
        }
        imagesSelectView.setImage(images);
    }

    @Override
    public Pair<Boolean, String> isDataValid() {
        if (getProductPhotos().getPhotos().size() < 1) {
            Snackbar.make(imagesSelectView.getRootView().findViewById(android.R.id.content), R.string.product_error_product_picture_empty, Snackbar.LENGTH_LONG)
                    .setActionTextColor(ContextCompat.getColor(imagesSelectView.getContext(), R.color.green_400))
                    .show();
            return new Pair<>(false, AppEventTracking.AddProduct.FIELDS_OPTIONAL_PICTURE);
        }
        return new Pair<>(true, "");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onViewStateRestored(@NonNull Bundle savedInstanceState) {

    }
}