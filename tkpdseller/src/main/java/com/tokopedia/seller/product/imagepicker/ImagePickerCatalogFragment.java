package com.tokopedia.seller.product.imagepicker;

import android.os.Bundle;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.seller.product.imagepicker.di.DaggerImagePickerCatalogComponent;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.seller.product.imagepicker.di.ImagePickerCatalogModule;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 6/5/18.
 */

public class ImagePickerCatalogFragment extends BaseListFragment<CatalogModelView, CatalogAdapterTypeFactory> implements ImagePickerCatalogContract.View {

    @Inject
    ImagePickerCatalogPresenter imagePickerCatalogPresenter;
    private String catalogId;

    public static ImagePickerCatalogFragment createInstance(String catalogId){
        ImagePickerCatalogFragment imagePickerCatalogFragment = new ImagePickerCatalogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CatalogConstant.CATALOG_ID_EXTRAS, catalogId);
        imagePickerCatalogFragment.setArguments(bundle);
        return imagePickerCatalogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        catalogId = getArguments().getString(CatalogConstant.CATALOG_ID_EXTRAS);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        DaggerImagePickerCatalogComponent
                .builder()
                .imagePickerCatalogModule(new ImagePickerCatalogModule())
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build()
                .inject(this);
        imagePickerCatalogPresenter.attachView(this);
    }

    @Override
    public void onItemClicked(CatalogModelView catalogModelView) {

    }

    @Override
    public void loadData(int page) {
        imagePickerCatalogPresenter.getCatalogImage(catalogId);
    }

    @Override
    protected CatalogAdapterTypeFactory getAdapterTypeFactory() {
        return new CatalogAdapterTypeFactory();
    }
}
