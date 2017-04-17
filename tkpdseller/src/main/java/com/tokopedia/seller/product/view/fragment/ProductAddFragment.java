package com.tokopedia.seller.product.view.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.di.component.DaggerProductAddComponent;
import com.tokopedia.seller.product.di.module.ProductAddModule;
import com.tokopedia.seller.product.view.holder.ProductAdditionalInfoViewHolder;
import com.tokopedia.seller.product.view.holder.ProductDetailViewHolder;
import com.tokopedia.seller.product.view.holder.ProductImageViewHolder;
import com.tokopedia.seller.product.view.holder.ProductInfoViewHolder;
import com.tokopedia.seller.product.view.model.upload.UploadProductInputViewModel;
import com.tokopedia.seller.product.view.presenter.ProductAddPresenter;
import com.tokopedia.seller.product.view.model.AddUrlVideoModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by nathan on 4/3/17.
 */

@RuntimePermissions
public class ProductAddFragment extends BaseDaggerFragment implements ProductAddView {

    public static final String TAG = ProductAddFragment.class.getSimpleName();

    private ProductInfoViewHolder productInfoViewHolder;
    private ProductImageViewHolder productImageViewHolder;
    private ProductDetailViewHolder productDetailViewHolder;
    private ProductAdditionalInfoViewHolder productAdditionalInfoViewHolder;

    @Inject
    public ProductAddPresenter presenter;

    public static ProductAddFragment createInstance() {
        ProductAddFragment fragment = new ProductAddFragment();
        return fragment;
    }

    @Override
    protected void initInjector() {
        DaggerProductAddComponent
                .builder()
                .productAddModule(new ProductAddModule())
                .appComponent(getComponent(AppComponent.class))
                .build()
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_add, container, false);
        productInfoViewHolder = new ProductInfoViewHolder(this, view);
        productImageViewHolder = new ProductImageViewHolder(this, view);
        productDetailViewHolder = new ProductDetailViewHolder(this, view);
        productAdditionalInfoViewHolder = new ProductAdditionalInfoViewHolder(view);
        setSubmitButtonListener(view);

        presenter.attachView(this);

        return view;
    }

    private void setSubmitButtonListener(View view) {
        view.findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadProductInputViewModel viewModel = collectDataFromView();
                presenter.saveDraft(viewModel);
            }
        });
    }

    private UploadProductInputViewModel collectDataFromView() {
        UploadProductInputViewModel viewModel = new UploadProductInputViewModel();
        viewModel.setProductName(productInfoViewHolder.getName());
        return viewModel;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ProductInfoViewHolder.REQUEST_CODE_CATEGORY:
                productInfoViewHolder.onActivityResult(requestCode, resultCode, data);
                break;
            case ProductInfoViewHolder.REQUEST_CODE_CATALOG:
                productInfoViewHolder.onActivityResult(requestCode, resultCode, data);
                break;
            case com.tokopedia.core.ImageGallery.TOKOPEDIA_GALLERY:
                productImageViewHolder.onActivityResult(requestCode, resultCode, data);
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    public void add(AddUrlVideoModel addUrlVideoModel) {
        productAdditionalInfoViewHolder.addAddUrlVideModel(addUrlVideoModel);
    }

    public void goToGalleryPermissionCheck(int imagePosition){
        ProductAddFragmentPermissionsDispatcher.goToGalleryWithCheck(this, imagePosition);
    }

    @TargetApi(16)
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void goToGallery(int imagePosition){
        int remainingEmptySlot = productImageViewHolder.getImagesSelectView().getRemainingEmptySlot();
        GalleryActivity.moveToImageGallery(getActivity(), this, imagePosition, remainingEmptySlot, true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        ProductAddFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @TargetApi(16)
    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showDeniedForExternalStorage() {
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @TargetApi(16)
    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showNeverAskForExternalStorage() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @TargetApi(16)
    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showRationaleForExternalStorage(final PermissionRequest request) {
        request.proceed();
    }
}