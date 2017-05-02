package com.tokopedia.seller.product.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.model.upload.ImageProductInputViewModel;
import com.tokopedia.seller.product.view.model.upload.ProductPhotoListViewModel;
import com.tokopedia.seller.product.view.model.upload.UploadProductInputViewModel;
import com.tokopedia.seller.product.view.model.upload.intdef.ProductStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 4/27/17.
 */

public class ProductDraftEditFragment extends ProductDraftAddFragment {

    private String productId;
    private ProductPhotoListViewModel productPhotosBeforeEdit;

    public static Fragment createInstance(String productDraftId) {
        ProductDraftEditFragment fragment = new ProductDraftEditFragment();
        Bundle args = new Bundle();
        args.putString(DRAFT_PRODUCT_ID, productDraftId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getStatusUpload() {
        return ProductStatus.EDIT;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            view.findViewById(R.id.button_save_and_add).setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onSuccessLoadProduct(UploadProductInputViewModel model) {
        productId = model.getProductId();
        productPhotosBeforeEdit = model.getProductPhotos();
        if (model.getProductNameEditable() == 0){
            productInfoViewHolder.setNameAvailable(false);
        }
        super.onSuccessLoadProduct(model);
    }


    @Override
    protected void getCategoryRecommendation(String productName) {
        // Do nothing
    }

    @Override
    protected UploadProductInputViewModel collectDataFromView() {
        UploadProductInputViewModel viewModel = super.collectDataFromView();
        viewModel.setProductId(productId);
        if (proccessEditImage(viewModel.getProductPhotos(), productPhotosBeforeEdit)){
            viewModel.setProductChangePhoto(1);
        } else {
            viewModel.setProductChangePhoto(0);
        }
        return viewModel;
    }

    private boolean proccessEditImage(ProductPhotoListViewModel productPhotos, ProductPhotoListViewModel productPhotosBeforeEdit) {
        boolean isChanging = false;
        List<ImageProductInputViewModel> photos = productPhotos.getPhotos();
        int defaultImage = productPhotos.getProductDefaultPicture();

        List<ImageProductInputViewModel> newPhotosList = new ArrayList<>();

        // loop in photo before edit
        // if there is a photo without url existed in new photo list, the prepare it to be deleted
        for (ImageProductInputViewModel viewModel : productPhotosBeforeEdit.getPhotos()) {
            try {
                findImage(viewModel.getUrl(), productPhotos.getPhotos());
            } catch (RuntimeException e){
                viewModel.setUrl("");
                newPhotosList.add(viewModel);
                isChanging = true;
            }
        }

        // loop in new photos
        // if found as path, then ready to upload,
        // if not found as path, find the model before edit to get the photos id
        for (int i = 0; i < photos.size(); i++) {
            ImageProductInputViewModel viewModel = photos.get(i);
            String url = viewModel.getUrl();
            if (StringUtils.isNotBlank(url)) {
                viewModel = findImage(url, productPhotosBeforeEdit.getPhotos());
            } else {
                isChanging = true;
            }
            if (i == productPhotos.getProductDefaultPicture()){
                newPhotosList.add(0, viewModel);
                defaultImage = 0;
            } else {
                newPhotosList.add(viewModel);
            }
        }

        productPhotos.setProductDefaultPicture(defaultImage);
        productPhotos.setPhotos(newPhotosList);
        return isChanging;
    }

    private ImageProductInputViewModel findImage(String url, List<ImageProductInputViewModel> photoList) {
        for (ImageProductInputViewModel viewModel : photoList) {
            if (viewModel.getUrl().equals(url)) {
                return viewModel;
            }
        }
        throw new RuntimeException("photo with image url not found");

    }
}
