package com.tokopedia.seller.product.edit.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.edit.di.component.DaggerProductAddComponent;
import com.tokopedia.seller.product.edit.di.module.ProductAddModule;
import com.tokopedia.seller.product.edit.view.activity.ProductAddActivity;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.edit.view.model.upload.intdef.ProductStatus;
import com.tokopedia.seller.product.edit.view.presenter.ProductAddPresenter;

import java.util.ArrayList;

//import com.tokopedia.seller.product.edit.di.component.DaggerProductAddComponent;

/**
 * Created by nathan on 4/3/17.
 */

public class ProductAddFragment extends BaseProductAddEditFragment<ProductAddPresenter>  {

    /**
     * Url got from gallery or camera or other paths
     */
    protected ArrayList<String> imageUrlListFromArg;

    public static ProductAddFragment createInstance(ArrayList<String> tkpdImageUrls) {
        ProductAddFragment fragment = new ProductAddFragment();
        if (tkpdImageUrls != null && tkpdImageUrls.size() > 0) {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(ProductAddActivity.EXTRA_IMAGE_URLS, tkpdImageUrls);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void initInjector() {
        DaggerProductAddComponent
                .builder()
                .productAddModule(new ProductAddModule())
                .productComponent(getComponent(ProductComponent.class))
                .build()
                .inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null && args.containsKey(ProductAddActivity.EXTRA_IMAGE_URLS)) {
            imageUrlListFromArg = args.getStringArrayList(ProductAddActivity.EXTRA_IMAGE_URLS);
        }
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        saveDefaultModel();
        return view;
    }

    @Override
    public void onSuccessLoadShopInfo(boolean isGoldMerchant, boolean isFreeReturn, boolean officialStore) {
        super.onSuccessLoadShopInfo(isGoldMerchant, isFreeReturn, officialStore);
        saveDefaultModel();
    }

    private void saveDefaultModel(){
        //save default value here, so we can compare when we want to save draft
        // will be overriden when not adding product
        firstTimeViewModel = collectDataFromView();
    }

    @Override
    public boolean showDialogSaveDraftOnBack(){
        // check if this fragment has any data
        // will compare will the default value and the current value
        // if there is the difference, then assume that the data has been added.
        // will be overriden when not adding product
        ProductViewModel model = collectDataFromView();
        return !model.equals(firstTimeViewModel);
    }

    @Override
    public boolean isNeedGetCategoryRecommendation() {
        return true;
    }

    @ProductStatus
    public int getStatusUpload() {
        return ProductStatus.ADD;
    }

}