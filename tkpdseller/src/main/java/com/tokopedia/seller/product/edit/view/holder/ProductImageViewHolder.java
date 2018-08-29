package com.tokopedia.seller.product.edit.view.holder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;

import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.view.adapter.ImageSelectorAdapter;
import com.tokopedia.seller.product.edit.view.model.ImageSelectModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPictureViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.edit.view.widget.ImagesSelectView;

import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity.RESULT_IS_EDITTED;
import static com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity.RESULT_PREVIOUS_IMAGE;
import static com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS;
import static com.tokopedia.seller.product.edit.view.fragment.BaseProductAddEditFragment.REQUEST_CODE_ADD_PRODUCT_IMAGE;
import static com.tokopedia.seller.product.edit.view.fragment.BaseProductAddEditFragment.REQUEST_CODE_EDIT_IMAGE;

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

        for (ProductPictureViewModel productPictureViewModel : productViewModel.getProductPictureViewModelList()) {
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

    @SuppressWarnings("unchecked")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ADD_PRODUCT_IMAGE ) {
            if (resultCode == Activity.RESULT_OK &&
                    data != null) {
                ArrayList<String> imageUrlOrPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS);
                if (imageUrlOrPathList == null || imageUrlOrPathList.isEmpty()) {
                    imagesSelectView.setImage(new ArrayList<ImageSelectModel>());
                } else {
                    ArrayList<String> editOriginalUrlList = data.getStringArrayListExtra(RESULT_PREVIOUS_IMAGE);
                    ArrayList<Boolean> resultIsEdittedList = (ArrayList<Boolean>) data.getSerializableExtra(RESULT_IS_EDITTED);

                    ArrayList<ImageSelectModel> prevImageSelectModelList = imagesSelectView.getImageList();

                    // LOGIC to retain HTTP url:
                    // check with the previous imageSelectView
                    // if the new Image Path's original Image is HTTP and exist in the prevImageSelectModel.getUri and editted = false
                    // then the data in previous model will be retained, and the uri will be the previous model
                    // Otherwise, set the new ImagePath to ImageSelectView
                    // example: prev data: "http://a.jpg", "http://b.jpg"
                    // example: result data: { "http://a.jpg" edit=false }, "data://b_edit.jpg"
                    // example: final data: "http://a.jpg", "data://b_edit.jpg"
                    ArrayList<ImageSelectModel> newImageSelectModelList = new ArrayList<>();
                    for (int i = 0, sizei = imageUrlOrPathList.size(); i < sizei; i++) {
                        String imagePath = imageUrlOrPathList.get(i);
                        if (editOriginalUrlList != null && prevImageSelectModelList != null && prevImageSelectModelList.size() > 0) {
                            String editOriginalPath = editOriginalUrlList.get(i);
                            boolean hasAnyEdit = resultIsEdittedList.get(i);

                            if (URLUtil.isNetworkUrl(editOriginalPath) && !hasAnyEdit) {
                                boolean existInPrevModel = false;
                                for (ImageSelectModel prevImageSelectModel : prevImageSelectModelList) {
                                    if (prevImageSelectModel.getUriOrPath().equals(editOriginalPath)) {
                                        // HTTP, no edit, exists in prev model, add with prev model
                                        newImageSelectModelList.add(prevImageSelectModel);
                                        existInPrevModel = true;
                                        break;
                                    }
                                }
                                if (!existInPrevModel) { // HTTP AND no edit, but not exists in prev model
                                    newImageSelectModelList.add(new ImageSelectModel(imagePath));
                                }
                            } else { // not HTTP OR has any edit
                                newImageSelectModelList.add(new ImageSelectModel(imagePath));
                            }
                        } else {
                            newImageSelectModelList.add(new ImageSelectModel(imagePath));
                        }
                    }
                    imagesSelectView.setImage(newImageSelectModelList);
                }
            }
        } else if (requestCode == REQUEST_CODE_EDIT_IMAGE){
            if (resultCode == Activity.RESULT_OK &&
                    data != null) {
                ArrayList<String> imageUrlOrPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS);
                if (imageUrlOrPathList != null && imageUrlOrPathList.size() > 0) {
                    String imagePath = imageUrlOrPathList.get(0);
                    if (!TextUtils.isEmpty(imagePath) && imagesSelectView.getSelectedImageIndex() >= 0) {
                        imagesSelectView.changeImagePath(imagePath);
                    }
                }
            }
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
            if (!URLUtil.isNetworkUrl(selectModel.getUriOrPath())) {
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