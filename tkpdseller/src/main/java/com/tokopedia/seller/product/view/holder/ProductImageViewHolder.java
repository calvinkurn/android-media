package com.tokopedia.seller.product.view.holder;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.utils.ScoringProductHelper;
import com.tokopedia.seller.product.view.adapter.ImageSelectorAdapter;
import com.tokopedia.seller.product.view.model.ImageSelectModel;
import com.tokopedia.seller.product.view.model.upload.ImageProductInputViewModel;
import com.tokopedia.seller.product.view.model.upload.ProductPhotoListViewModel;
import com.tokopedia.seller.product.view.widget.ImagesSelectView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 4/11/17.
 */

public class ProductImageViewHolder extends ProductViewHolder {

    public interface Listener {
        void onAddImagePickerClicked(int position);

        void onImagePickerItemClicked(int position, boolean isPrimary);

        void onResolutionImageCheckFailed(String uri);

        void onTotalImageUpdated(int total);

        void onImageResolutionChanged(int maxSize);
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
                    listener.onImagePickerItemClicked(position, imageSelectModel.isPrimary());
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
            imageViewModel.setImageResolution(selectModel.getMinResolution());

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
        for (int i = 0; i < productPhotos.getPhotos().size(); i++) {
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

    @Override
    public boolean isDataValid() {
        if (getProductPhotos().getPhotos().size() < 1) {
            Snackbar.make(imagesSelectView.getRootView().findViewById(android.R.id.content), R.string.product_error_product_picture_empty, Snackbar.LENGTH_LONG)
                    .setActionTextColor(ContextCompat.getColor(imagesSelectView.getContext(), R.color.green_400))
                    .show();
            return false;
        }
        return true;
    }
}