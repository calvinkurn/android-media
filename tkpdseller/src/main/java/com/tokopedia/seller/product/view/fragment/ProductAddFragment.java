package com.tokopedia.seller.product.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.di.component.DaggerProductAddComponent;
import com.tokopedia.seller.product.di.module.ProductAddModule;
import com.tokopedia.seller.product.view.holder.ProductAdditionalInfoViewHolder;
import com.tokopedia.seller.product.view.holder.ProductDetailViewHolder;
import com.tokopedia.seller.product.view.holder.ProductImageViewHolder;
import com.tokopedia.seller.product.view.holder.ProductInfoViewHolder;
import com.tokopedia.seller.product.view.model.scoringproduct.DataScoringProductView;
import com.tokopedia.seller.product.view.model.scoringproduct.ValueIndicatorScoreModel;
import com.tokopedia.seller.product.view.model.upload.UploadProductInputViewModel;
import com.tokopedia.seller.product.view.presenter.ProductAddPresenter;
import com.tokopedia.seller.product.view.model.AddUrlVideoModel;
import com.tokopedia.seller.product.view.holder.ProductScoreViewHolder;

import javax.inject.Inject;

/**
 * Created by nathan on 4/3/17.
 */

public class ProductAddFragment extends BaseDaggerFragment implements ProductAddView {

    public static final String TAG = ProductAddFragment.class.getSimpleName();

    private ProductScoreViewHolder productScoreViewHolder;
    private ProductImageViewHolder productImageViewHolder;
    private ProductDetailViewHolder productDetailViewHolder;
    private ProductAdditionalInfoViewHolder productAdditionalInfoViewHolder;
    private ProductInfoViewHolder productInfoViewHolder;

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
        productScoreViewHolder = new ProductScoreViewHolder(view, this);

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
        viewModel.setProductDepartmentId(productInfoViewHolder.getCategoryId());
//        viewModel.setProductCatalogId(productInfoViewHolder.);
        viewModel.setProductPhotos(productImageViewHolder.getProductPhotos());
        viewModel.setProductPriceCurrency(productDetailViewHolder.getPriceCurrency());
        viewModel.setProductPrice(productDetailViewHolder.getPriceValue());
//        viewModel.setProductWholesaleList();
        viewModel.setProductWeightUnit(productDetailViewHolder.getWeightUnit());
        viewModel.setProductWeight(productDetailViewHolder.getWeightValue());
        viewModel.setProductMinOrder(productDetailViewHolder.getMinimumOrder());
        viewModel.setProductUploadTo(productDetailViewHolder.getStatusStock());
        viewModel.setProductEtalaseId(productDetailViewHolder.getEtalaseId());
        viewModel.setProductCondition(productDetailViewHolder.getCondition());
        viewModel.setProductMustInsurance(productDetailViewHolder.getInsurance());
        viewModel.setProductReturnable(productDetailViewHolder.getFreeReturns());
        viewModel.setProductDescription(productAdditionalInfoViewHolder.getDescription());
//        viewModel youtube

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
                break;
            case ProductDetailViewHolder.REQUEST_CODE_ETALASE:
                productDetailViewHolder.onActivityResult(requestCode, resultCode, data);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onSuccessGetScoringProduct(DataScoringProductView dataScoringProductView) {
        productScoreViewHolder.setValueProductScoreToView(dataScoringProductView);
    }

    @Override
    public void updateProductScoring() {
        presenter.getProductScoring(getValueIndicatorScoreModel());
    }

    @Override
    public ValueIndicatorScoreModel getValueIndicatorScoreModel() {
        ValueIndicatorScoreModel valueIndicatorScoreModel = new ValueIndicatorScoreModel();
        return valueIndicatorScoreModel;
    }
}