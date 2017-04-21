package com.tokopedia.seller.product.view.holder;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.seller.R;
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

public class ProductImageViewHolder {

    public static final int MIN_IMG_RESOLUTION = 300;
    private ImagesSelectView imagesSelectView;

    private Listener listener;

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
    }

    public void setListener(Listener listener) {
        this.listener = listener;
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

            if (selectModel.isPrimary()) {
                productPhotos.setProductDefaultPicture(i);
            }
            listImageViewModel.add(imageViewModel);
        }
        productPhotos.setPhotos(listImageViewModel);
        return productPhotos;
    }

    public interface Listener {
        void onAddImagePickerClicked(int position);

        void onImagePickerItemClicked(int position, boolean isPrimary);

        void onResolutionImageCheckFailed(String uri);
    }
}