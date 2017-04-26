package com.tokopedia.seller.product.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.seller.product.di.component.DaggerProductEditComponent;
import com.tokopedia.seller.product.di.module.ProductEditModule;
import com.tokopedia.seller.product.view.model.upload.ImageProductInputViewModel;
import com.tokopedia.seller.product.view.model.upload.ProductPhotoListViewModel;
import com.tokopedia.seller.product.view.model.upload.UploadProductInputViewModel;
import com.tokopedia.seller.product.view.model.upload.intdef.ProductStatus;
import com.tokopedia.seller.product.view.presenter.ProductEditPresenter;
import com.tokopedia.seller.product.view.presenter.ProductEditView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author sebastianuskh on 4/21/17.
 */

public class ProductEditFragment extends ProductDraftFragment implements ProductEditView {

    public static final String EDIT_PRODUCT_ID = "EDIT_PRODUCT_ID";

    @Inject
    public ProductEditPresenter presenter;
    private String productId;
    private ProductPhotoListViewModel productPhotosBeforeEdit;

    public static ProductEditFragment createInstance(String productId) {
        ProductEditFragment fragment = new ProductEditFragment();
        Bundle args = new Bundle();
        args.putString(EDIT_PRODUCT_ID, productId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initInjector() {
        DaggerProductEditComponent
                .builder()
                .appComponent(getComponent(AppComponent.class))
                .productEditModule(new ProductEditModule())
                .build()
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        presenter.attachView(this);
        String productId = getArguments().getString(EDIT_PRODUCT_ID);
        presenter.fetchEditProductData(productId);
        return view;
    }

    @Override
    public void onSuccessLoadProduct(UploadProductInputViewModel model) {
        productId = model.getProductId();
        productPhotosBeforeEdit = model.getProductPhotos();
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

    @ProductStatus
    @Override
    protected int getStatusUpload() {
        return ProductStatus.EDIT;
    }
}