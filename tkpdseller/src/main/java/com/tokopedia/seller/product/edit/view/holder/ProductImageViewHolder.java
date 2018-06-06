package com.tokopedia.seller.product.edit.view.holder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.imageeditor.ImageEditorActivity;
import com.tokopedia.seller.product.common.utils.UrlUtils;
import com.tokopedia.seller.product.edit.view.adapter.ImageSelectorAdapter;
import com.tokopedia.seller.product.edit.view.model.ImageSelectModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPictureViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.edit.view.widget.ImagesSelectView;

import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS;
import static com.tokopedia.seller.product.edit.view.fragment.BaseProductAddEditFragment.REQUEST_CODE_ADD_PRODUCT_IMAGE;

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

        Activity getActivity();
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

    public ProductImageViewHolder(View view, Listener listener) {
        imagesSelectView = (ImagesSelectView) view.findViewById(R.id.image_select_view);
        imagesSelectView.setOnImageSelectionListener(new ImageSelectorAdapter.OnImageSelectionListener() {
            @Override
            public void onAddClick(int position) {
                if (ProductImageViewHolder.this.listener != null) {
                    ProductImageViewHolder.this.listener.onAddImagePickerClicked(position);
                }
            }

            @Override
            public void onItemClick(int position, final ImageSelectModel imageSelectModel, boolean isPrimary) {
                if (ProductImageViewHolder.this.listener != null) {
                    ProductImageViewHolder.this.listener.onImagePickerItemClicked(position, isPrimary);
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
                if (ProductImageViewHolder.this.listener != null) {
                    ProductImageViewHolder.this.listener.onResolutionImageCheckFailed(uri);
                }
            }

            @Override
            public void removePreviousPath(String uri) {
                ProductImageViewHolder.this.listener.onRemovePreviousPath(uri);
            }
        });
        imagesSelectView.setOnImageChanged(new ImagesSelectView.OnImageChanged() {
            @Override
            public void onTotalImageUpdated(int total) {
                ProductImageViewHolder.this.listener.onTotalImageUpdated(total);
                updateImageResolution();
            }

            private void updateImageResolution() {
                List<ImageSelectModel> productPhotoListViewModel = imagesSelectView.getImageList();
                int imageCount = productPhotoListViewModel.size();
                if (imageCount > 0) {
                    ImageSelectModel imageProductInputViewModel = productPhotoListViewModel.get(0);
                    ProductImageViewHolder.this.listener.onImageResolutionChanged(imageProductInputViewModel.getMinResolution());
                }
            }
        });

        setListener(listener);
    }

    @Override
    public void renderData(ProductViewModel productViewModel) {
        ArrayList<ImageSelectModel> images = new ArrayList<>();

        for (ProductPictureViewModel productPictureViewModel: productViewModel.getProductPictureViewModelList()) {
            String url = productPictureViewModel.getUrlOriginal();
            if (TextUtils.isEmpty(url)) {
                url = productPictureViewModel.getFilePath();
            }
            if (TextUtils.isEmpty(url)) {
                continue;
            }
            ImageSelectModel image = new ImageSelectModel(
                    url,
                    productPictureViewModel.getDescription(),
                    productPictureViewModel.getX(),
                    productPictureViewModel.getY(),
                    productPictureViewModel.getId(),
                    productPictureViewModel.getFilePath(),
                    productPictureViewModel.getFileName()
            );
            images.add(image);
        }
        imagesSelectView.setImage(images);
    }

    @Override
    public void updateModel(ProductViewModel productViewModel) {
        productViewModel.setProductPictureViewModelList(getProductPhotoList());
    }

    public ImagesSelectView getImagesSelectView() {
        return imagesSelectView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ADD_PRODUCT_IMAGE && resultCode == Activity.RESULT_OK &&
                data != null) {
            ArrayList<String> imageUrlOrPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS);

            ArrayList<ImageSelectModel> imageSelectModelList = imagesSelectView.getImageList();

            imagesSelectView.setImagesString(imageUrlOrPathList);
        }
    }

    public List<ProductPictureViewModel> getProductPhotoList() {
        List<ProductPictureViewModel> pictureViewModelList = new ArrayList<>();
        List<ImageSelectModel> selectModelList = imagesSelectView.getImageList();
        for (int i = 0; i < selectModelList.size(); i++) {
            ProductPictureViewModel productPictureViewModel = new ProductPictureViewModel();
            ImageSelectModel selectModel = selectModelList.get(i);
            productPictureViewModel.setDescription(selectModel.getDescription());
            productPictureViewModel.setX(selectModel.getWidth());
            productPictureViewModel.setY(selectModel.getHeight());

            // Update image to server
            if (!UrlUtils.isValidURL(selectModel.getUriOrPath())) {
                productPictureViewModel.setId(0);
                productPictureViewModel.setFilePath(selectModel.getUriOrPath());
            } else {
                productPictureViewModel.setId(selectModel.getId());
                productPictureViewModel.setFilePath(selectModel.getServerFilePath());
                productPictureViewModel.setUrlOriginal(selectModel.getUriOrPath());
                productPictureViewModel.setUrlThumbnail(selectModel.getUriOrPath());
            }
            productPictureViewModel.setFileName(selectModel.getServerFileName());
            pictureViewModelList.add(productPictureViewModel);
        }
        return pictureViewModelList;
    }

    @Override
    public boolean isDataValid() {
        if (getProductPhotoList().size() < 1) {
            Activity activity = listener.getActivity();
            NetworkErrorHelper.showRedCloseSnackbar(activity, activity.getString(R.string.product_error_product_picture_empty));
            UnifyTracking.eventAddProductError(AppEventTracking.AddProduct.FIELDS_OPTIONAL_PICTURE);
            return false;
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onViewStateRestored(@NonNull Bundle savedInstanceState) {

    }
}