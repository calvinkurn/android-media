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
import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.imageeditor.ImageEditorActivity;
import com.tokopedia.seller.product.edit.view.adapter.ImageSelectorAdapter;
import com.tokopedia.seller.product.edit.view.model.ImageSelectModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPictureViewModel;
import com.tokopedia.seller.product.edit.view.widget.ImagesSelectView;

import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.core.newgallery.GalleryActivity.INSTAGRAM_SELECT_REQUEST_CODE;

/**
 * Created by nathan on 4/11/17.
 */

public class ProductImageViewHolder extends ProductViewHolder {

    public interface Listener {
        void onAddImagePickerClicked(int position);

        void onImagePickerItemClicked(int position, boolean isPrimary);

        void onResolutionImageCheckFailed(String uri);

        void onTotalImageUpdated(int total);

        void onImageResolutionChanged(long maxSize);

        void onImageEditor(String uriOrPath);

        void onRemovePreviousPath(String uri);
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
            public void onItemClick(int position, final ImageSelectModel imageSelectModel, boolean isPrimary) {
                if (listener != null) {
                    listener.onImagePickerItemClicked(position, isPrimary);
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
                listener.onRemovePreviousPath(uri);
            }
        });
        imagesSelectView.setOnImageChanged(new ImagesSelectView.OnImageChanged() {
            @Override
            public void onTotalImageUpdated(int total) {
                listener.onTotalImageUpdated(total);
                updateImageResolution();
            }

            private void updateImageResolution() {
                List<ImageSelectModel> productPhotoListViewModel = imagesSelectView.getImageList();
                int imageCount = productPhotoListViewModel.size();
                if (imageCount > 0) {
                    ImageSelectModel imageProductInputViewModel = productPhotoListViewModel.get(0);
                    listener.onImageResolutionChanged(imageProductInputViewModel.getMinResolution());
                }
            }
        });
    }

    public ImagesSelectView getImagesSelectView() {
        return imagesSelectView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == com.tokopedia.core.ImageGallery.TOKOPEDIA_GALLERY || (
                requestCode == INSTAGRAM_SELECT_REQUEST_CODE && resultCode == Activity.RESULT_OK)) &&
                data != null) {
            String imageUrl = data.getStringExtra(GalleryActivity.IMAGE_URL);
            if (!TextUtils.isEmpty(imageUrl)) {
                addOrChangeImage(imageUrl);
            } else {
                ArrayList<String> imageUrls = data.getStringArrayListExtra(GalleryActivity.IMAGE_URLS);
                if (imageUrls != null) {
                    if (imageUrls.size() > 1) {
                        imagesSelectView.addImagesString(imageUrls);
                    } else {
                        addOrChangeImage(imageUrls.get(0));
                    }
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
            if (imagesSelectView.getSelectedImageIndex() < 0) {
                imagesSelectView.addImageString(imageUrl);
            } else {
                imagesSelectView.changeImagePath(imageUrl);
            }
        }
    }

    public List<ProductPictureViewModel> getProductPhotos() {
        List<ProductPictureViewModel> listImageViewModel = new ArrayList<>();

        List<ImageSelectModel> selectModelList = imagesSelectView.getImageList();
        for (int i = 0; i < selectModelList.size(); i++) {
            ProductPictureViewModel productPictureViewModel = new ProductPictureViewModel();
            ImageSelectModel selectModel = selectModelList.get(i);
            productPictureViewModel.setDescription(selectModel.getDescription());
            productPictureViewModel.setX(selectModel.getWidth());
            productPictureViewModel.setY(selectModel.getHeight());
            productPictureViewModel.setId(selectModel.getId());

            if (selectModel.getId() > 0) { // means file still from server, no change from local
                productPictureViewModel.setFilePath(selectModel.getServerFilePath());
            } else {
                productPictureViewModel.setFilePath(selectModel.getUriOrPath());
            }
            productPictureViewModel.setFileName(selectModel.getServerFileName());

            listImageViewModel.add(productPictureViewModel);
        }
        return listImageViewModel;
    }

    public void setProductPhotos(List<ProductPictureViewModel> productPhotos) {
        ArrayList<ImageSelectModel> images = new ArrayList<>();
        for (int i = 0; i < productPhotos.size(); i++) {
            ProductPictureViewModel productPhoto = productPhotos.get(i);
            String url = productPhoto.getUrlOriginal();
            ImageSelectModel image = new ImageSelectModel(
                    url,
                    productPhoto.getDescription(),
                    productPhoto.getX(),
                    productPhoto.getY(),
                    productPhoto.getId(),
                    productPhoto.getFilePath(),
                    productPhoto.getFileName()
            );
            images.add(image);
        }
        imagesSelectView.setImage(images);
    }

    @Override
    public Pair<Boolean, String> isDataValid() {
        if (getProductPhotos().size() < 1) {
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