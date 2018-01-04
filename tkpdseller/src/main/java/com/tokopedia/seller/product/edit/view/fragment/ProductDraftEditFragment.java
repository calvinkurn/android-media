package com.tokopedia.seller.product.edit.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.constant.ImageStatusTypeDef;
import com.tokopedia.seller.product.edit.view.model.upload.ImageProductInputViewModel;
import com.tokopedia.seller.product.edit.view.model.upload.ProductPhotoListViewModel;
import com.tokopedia.seller.product.edit.view.model.upload.ProductWholesaleViewModel;
import com.tokopedia.seller.product.edit.view.model.upload.UploadProductInputViewModel;
import com.tokopedia.seller.product.edit.view.model.upload.intdef.ProductStatus;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 4/27/17.
 */

public class ProductDraftEditFragment extends ProductDraftAddFragment {

    private String productId;
    private ProductPhotoListViewModel productPhotosBeforeEdit;
    private List<ProductWholesaleViewModel> productWholesaleBeforeEdit;
    private long catalogIdBeforeEdit;

    public static Fragment createInstance(String productDraftId) {
        ProductDraftEditFragment fragment = new ProductDraftEditFragment();
        Bundle args = new Bundle();
        args.putString(DRAFT_PRODUCT_ID, productDraftId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getStatusUpload() {
        return ProductStatus.EDIT;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            view.findViewById(R.id.button_save_and_add).setVisibility(View.GONE);
            view.findViewById(R.id.label_switch_share).setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onSuccessLoadDraftProduct(UploadProductInputViewModel model) {
        hideLoading();
        productId = model.getProductId();
        productPhotosBeforeEdit = model.getProductPhotos();
        productWholesaleBeforeEdit = model.getProductWholesaleList();
        catalogIdBeforeEdit = model.getProductCatalogId();
        if (model.getProductNameEditable() == 0) {
            productInfoViewHolder.setNameEnabled(false);
        }
        super.onSuccessLoadDraftProduct(model);
    }

    @Override
    protected void getCategoryRecommendation(String productName) {
        // Do nothing
    }

    @Override
    protected UploadProductInputViewModel collectDataFromView() {
        UploadProductInputViewModel viewModel = super.collectDataFromView();
        viewModel.setProductId(productId);
        if (proccessEditImage(viewModel.getProductPhotos(), productPhotosBeforeEdit)) {
            viewModel.setProductChangePhoto(1);
        } else {
            viewModel.setProductChangePhoto(0);
        }
        if (processEditWholesale(viewModel.getProductWholesaleList(), productWholesaleBeforeEdit)) {
            viewModel.setProductChangeWholesale(1);
        } else {
            viewModel.setProductChangeWholesale(0);
        }
        if (viewModel.getProductCatalogId() != catalogIdBeforeEdit) {
            viewModel.setProductChangeCatalog(1);
        } else {
            viewModel.setProductChangeCatalog(0);
        }
        return viewModel;
    }

    private boolean processEditWholesale(List<ProductWholesaleViewModel> productWholesaleList, List<ProductWholesaleViewModel> productWholesaleBeforeEdit) {
        if (productWholesaleList == null || productWholesaleBeforeEdit == null) {
            return false;
        }
        if (productWholesaleList.size() != productWholesaleBeforeEdit.size()) {
            return true;
        }
        for (int i = 0; i < productWholesaleBeforeEdit.size(); i++) {
            if (productWholesaleList.get(i).getPrice() != productWholesaleBeforeEdit.get(i).getPrice()) {
                return true;
            }
            if (productWholesaleList.get(i).getQtyMin() != productWholesaleBeforeEdit.get(i).getQtyMin()) {
                return true;
            }
            if (productWholesaleList.get(i).getQtyMax() != productWholesaleBeforeEdit.get(i).getQtyMax()) {
                return true;
            }
        }
        return false;
    }

    private boolean proccessEditImage(ProductPhotoListViewModel productPhotos, ProductPhotoListViewModel productPhotosBeforeEdit) {
        boolean isChanging = false;
        List<ImageProductInputViewModel> photos = productPhotos.getPhotos();
        int defaultImage = productPhotos.getProductDefaultPicture();

        List<ImageProductInputViewModel> newPhotosList = new ArrayList<>();

        // loop in photo before edit
        // if there is a photo without url existed in new photo list, the prepare it to be deleted
        if (productPhotosBeforeEdit != null) {
            for (ImageProductInputViewModel viewModel : productPhotosBeforeEdit.getPhotos()) {
                try {
                    findImage(viewModel, productPhotos.getPhotos());
                } catch (RuntimeException e) {
                    viewModel.setStatus(ImageStatusTypeDef.WILL_BE_DELETED);
                    viewModel.setUrl("");
                    newPhotosList.add(viewModel);
                    isChanging = true;
                }
            }
        }

        // loop in new photos
        // if found as path, then ready to upload,
        // if not found as path, find the model before edit to get the photos id
        for (int i = 0; i < photos.size(); i++) {
            ImageProductInputViewModel viewModel = photos.get(i);
            try {
                String imageDesc = viewModel.getImageDescription();
                viewModel = findImage(viewModel, productPhotosBeforeEdit.getPhotos());
                viewModel.setImageDescription(imageDesc);
            } catch (RuntimeException e) {
                isChanging = true;
                viewModel.setStatus(ImageStatusTypeDef.WILL_BE_UPLOADED);
            }
            if (i == productPhotos.getProductDefaultPicture()) {
                newPhotosList.add(0, viewModel);
                defaultImage = 0;
            } else {
                newPhotosList.add(viewModel);
            }
        }

        // No need to reselect primary picture. already set from getPhotos.
        productPhotos.setProductDefaultPicture(defaultImage);
        productPhotos.setPhotos(newPhotosList);
        return isChanging;
    }

    private ImageProductInputViewModel findImage(@NonNull ImageProductInputViewModel oldViewModel, List<ImageProductInputViewModel> photoList) {
        for (ImageProductInputViewModel viewModel : photoList) {
            if (StringUtils.isNotBlank(oldViewModel.getUrl())
                    && StringUtils.isNotBlank(viewModel.getUrl())
                    && oldViewModel.getUrl().equals(viewModel.getUrl())
                    || StringUtils.isNotBlank(oldViewModel.getImagePath())
                    && StringUtils.isNotBlank(viewModel.getImagePath())
                    && oldViewModel.getImagePath().equals(viewModel.getImagePath())) {
                viewModel.setStatus(oldViewModel.getStatus());
                return viewModel;
            }
        }
        throw new RuntimeException("photo with image url not found");

    }
}
