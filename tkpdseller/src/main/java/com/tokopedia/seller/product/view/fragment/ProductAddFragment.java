package com.tokopedia.seller.product.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.di.component.DaggerProductAddComponent;
import com.tokopedia.seller.product.di.module.ProductAddModule;
import com.tokopedia.seller.product.view.activity.YoutubeAddVideoActivity;
import com.tokopedia.seller.product.view.holder.ProductAdditionalInfoViewHolder;
import com.tokopedia.seller.product.view.holder.ProductDetailViewHolder;
import com.tokopedia.seller.product.view.holder.ProductImageViewHolder;
import com.tokopedia.seller.product.view.holder.ProductInfoViewHolder;
import com.tokopedia.seller.product.view.holder.ProductScoreViewHolder;
import com.tokopedia.seller.product.view.model.scoringproduct.DataScoringProductView;
import com.tokopedia.seller.product.view.model.scoringproduct.ValueIndicatorScoreModel;
import com.tokopedia.seller.product.view.model.upload.UploadProductInputViewModel;
import com.tokopedia.seller.product.view.presenter.ProductAddPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by nathan on 4/3/17.
 */

public class ProductAddFragment extends BaseDaggerFragment
        implements ProductAddView, ProductAdditionalInfoViewHolder.Listener {

    public static final String TAG = ProductAddFragment.class.getSimpleName();
    @Inject
    public ProductAddPresenter presenter;
    private ProductScoreViewHolder productScoreViewHolder;
    private ProductImageViewHolder productImageViewHolder;
    private ProductDetailViewHolder productDetailViewHolder;
    private ProductAdditionalInfoViewHolder productAdditionalInfoViewHolder;
    private ProductInfoViewHolder productInfoViewHolder;

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
        productAdditionalInfoViewHolder.setListener(this);
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
//        viewModel.setProductEtalaseId(productDetailViewHolder.);
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
            case YoutubeAddVideoView.REQUEST_CODE_GET_VIDEO:
                productAdditionalInfoViewHolder.onActivityResult(requestCode, resultCode, data);
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

    @Override
    public void startYoutubeVideoActivity(ArrayList<String> videoIds) {
        Intent intent = new Intent(getActivity(), YoutubeAddVideoActivity.class);
        if (CommonUtils.checkCollectionNotNull(videoIds))
            intent.putStringArrayListExtra(
                    YoutubeAddVideoView.KEY_VIDEOS_LINK, videoIds);
        startActivityForResult(intent, YoutubeAddVideoView.REQUEST_CODE_GET_VIDEO);
    }
}