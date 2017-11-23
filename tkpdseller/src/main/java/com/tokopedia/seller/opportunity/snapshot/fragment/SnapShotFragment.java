package com.tokopedia.seller.opportunity.snapshot.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.PreviewProductImage;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.snapshot.customview.DescriptionView;
import com.tokopedia.seller.opportunity.snapshot.customview.DetailInfoView;
import com.tokopedia.seller.opportunity.snapshot.customview.FreeReturnView;
import com.tokopedia.seller.opportunity.snapshot.customview.HeaderInfoView;
import com.tokopedia.seller.opportunity.snapshot.customview.PictureView;
import com.tokopedia.seller.opportunity.snapshot.customview.ShopInfoView;
import com.tokopedia.seller.opportunity.snapshot.listener.SnapShotFragmentView;
import com.tokopedia.seller.opportunity.snapshot.presenter.SnapShotFragmentImpl;
import com.tokopedia.seller.opportunity.snapshot.presenter.SnapShotFragmentPresenter;


/**
 * Created by hangnadi on 3/1/17.
 */
public class SnapShotFragment extends BasePresenterFragment<SnapShotFragmentPresenter>
        implements SnapShotFragmentView {

    private static final String ARG_PARAM_PRODUCT_PASS_DATA = "ARG_PARAM_PRODUCT_PASS_DATA";
    private static final String ARG_PASS_OPPORTUNITY_ID = "ARG_PASS_OPPORTUNITY_ID";
    private static final String ARG_PRODUCT_DATA = "ARG_PRODUCT_DATA";

    private static final String TAG = SnapShotFragment.class.getSimpleName();
    public static final int INIT_REQUEST = 1;
    public static final int RE_REQUEST = 2;

    HeaderInfoView headerInfoView;
    DetailInfoView detailInfoView;
    PictureView pictureView;
    DescriptionView descriptionView;
    ShopInfoView shopInfoView;
    ProgressBar progressBar;
    FreeReturnView freeReturnView;

    private ProductPass productPass;
    private ProductDetailData productData;
    private String opportunityId;


    public static Fragment newInstance(ProductPass productPass, String opportunityId) {
        SnapShotFragment fragment = new SnapShotFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_PRODUCT_PASS_DATA, productPass);
        args.putString(ARG_PASS_OPPORTUNITY_ID, opportunityId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

        headerInfoView = (HeaderInfoView) view.findViewById(R.id.view_header);
        detailInfoView = (DetailInfoView) view.findViewById(R.id.view_detail);
        pictureView = (PictureView) view.findViewById(R.id.view_picture);
        descriptionView = (DescriptionView) view.findViewById(R.id.view_desc);
        shopInfoView = (ShopInfoView) view.findViewById(R.id.view_shop_info);
        progressBar = (ProgressBar) view.findViewById(R.id.view_progress);
        freeReturnView = (FreeReturnView) view.findViewById(R.id.view_free_return);
        return view;


    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        if (productData != null) {
            onProductDetailLoaded(productData);
        } else {
            presenter.processDataPass(productPass);
            presenter.requestProductDetail(context, productPass, INIT_REQUEST, false);
        }
    }

    @Override
    public void renderTempProductData(ProductPass productPass) {
        this.headerInfoView.renderTempData(productPass);
        this.pictureView.renderTempData(productPass);
    }

    @Override
    public void onProductDetailLoaded(ProductDetailData productData) {
        this.productData = productData;
        this.headerInfoView.renderData(productData);
        this.pictureView.renderData(productData);
        this.detailInfoView.renderData(productData);
        this.descriptionView.renderData(productData);
        this.shopInfoView.renderData(productData);
        this.freeReturnView.renderData(productData);
    }

    @Override
    public void onSaveState(Bundle state) {
        state.putString(ARG_PASS_OPPORTUNITY_ID, opportunityId);
        state.putParcelable(ARG_PARAM_PRODUCT_PASS_DATA, productPass);
        state.putParcelable(ARG_PRODUCT_DATA, productData);
    }

    @Override
    public void onRestoreState(Bundle savedState) {

        productData = savedState.getParcelable(ARG_PRODUCT_DATA);

        productPass = savedState.getParcelable(ARG_PARAM_PRODUCT_PASS_DATA);
        opportunityId = savedState.getString(ARG_PASS_OPPORTUNITY_ID);

        onFirstTimeLaunched();

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new SnapShotFragmentImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        productPass = arguments.getParcelable(ARG_PARAM_PRODUCT_PASS_DATA);
        opportunityId = arguments.getString(ARG_PASS_OPPORTUNITY_ID);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_product_snapshot;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void setViewListener() {
        headerInfoView.setListener(this);
        pictureView.setListener(this);
        detailInfoView.setListener(this);
        descriptionView.setListener(this);
        shopInfoView.setListener(this);
        freeReturnView.setListener(this);

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onProductPictureClicked(@NonNull Bundle bundle) {
        Intent intent = new Intent(context, PreviewProductImage.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void showProgressLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProductDetailRetry() {
        if (getActivity() != null && productPass != null && presenter != null) {
            if (!productPass.getProductName().isEmpty()) {
                NetworkErrorHelper.createSnackbarWithAction(getActivity(),
                        initializationErrorListener()).showRetrySnackbar();
            } else if (getActivity().findViewById(R.id.root_view) != null) {
                NetworkErrorHelper.showEmptyState(getActivity(),
                        getActivity().findViewById(R.id.root_view),
                        initializationErrorListener());
            }
        }
    }

    private NetworkErrorHelper.RetryClickedListener initializationErrorListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.requestProductDetail(context, productPass, INIT_REQUEST, false);
            }
        };
    }

    @Override
    public void onNullData() {
        showToastMessage("Produk tidak ditemukan!");
        closeView();
    }

    @Override
    public void showToastMessage(String message) {
        CommonUtils.UniversalToast(getActivity(), message);
    }

    @Override
    public void closeView() {
        this.getActivity().finish();
    }

    @Override
    public void showFullScreenError() {
        NetworkErrorHelper.showEmptyState(getActivity(),
                getActivity().findViewById(R.id.root_view),
                initializationErrorListener());
    }

    private void navigateShopActivity(Bundle bundle) {
        Intent intent = new Intent(getActivity(), ShopInfoActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onProductShopNameClicked(Bundle bundle) {
        navigateShopActivity(bundle);
    }

    @Override
    public void onProductShopRatingClicked(Bundle bundle) {
        navigateShopActivity(bundle);
    }

    @Override
    public void onProductShopAvatarClicked(Bundle bundle) {
        navigateShopActivity(bundle);
    }

}
