package com.tokopedia.tkpdpdp.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appsflyer.AFInAppEventType;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.product.intentservice.ProductInfoIntentService;
import com.tokopedia.core.product.listener.DetailFragmentInteractionListener;
import com.tokopedia.core.product.model.goldmerchant.VideoData;
import com.tokopedia.core.product.model.productdetail.ProductCampaign;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.mosthelpful.Review;
import com.tokopedia.core.product.model.productother.ProductOther;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.router.home.SimpleHomeRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.router.transactionmodule.TransactionCartRouter;
import com.tokopedia.core.router.transactionmodule.passdata.ProductCartPass;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.share.ShareActivity;
import com.tokopedia.core.util.AppIndexHandler;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.core.webview.listener.DeepLinkWebViewHandleListener;
import com.tokopedia.tkpdpdp.CourierActivity;
import com.tokopedia.tkpdpdp.DescriptionActivity;
import com.tokopedia.tkpdpdp.InstallmentActivity;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.WholesaleActivity;
import com.tokopedia.tkpdpdp.customview.ButtonBuyView;
import com.tokopedia.tkpdpdp.customview.DetailInfoView;
import com.tokopedia.tkpdpdp.customview.HeaderInfoView;
import com.tokopedia.tkpdpdp.customview.LastUpdateView;
import com.tokopedia.tkpdpdp.customview.NewShopView;
import com.tokopedia.tkpdpdp.customview.OtherProductsView;
import com.tokopedia.tkpdpdp.customview.PictureView;
import com.tokopedia.tkpdpdp.customview.PriceSimulationView;
import com.tokopedia.tkpdpdp.customview.RatingTalkCourierView;
import com.tokopedia.tkpdpdp.customview.ShopInfoViewV2;
import com.tokopedia.tkpdpdp.customview.TransactionDetailView;
import com.tokopedia.tkpdpdp.customview.VideoDescriptionLayout;
import com.tokopedia.tkpdpdp.dialog.ReportProductDialogFragment;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;
import com.tokopedia.tkpdpdp.presenter.ProductDetailPresenter;
import com.tokopedia.tkpdpdp.presenter.ProductDetailPresenterImpl;

import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static com.tokopedia.core.router.productdetail.ProductDetailRouter.WIHSLIST_STATUS_IS_WISHLIST;
import static com.tokopedia.core.router.productdetail.ProductDetailRouter.WISHLIST_STATUS_UPDATED_POSITION;

/**
 * ProductDetailFragment
 * Created by Angga.Prasetiyo on 22/10/2015.
 * Edited by alifa, rohmadi, henry for v2
 */
@RuntimePermissions
public class ProductDetailFragment extends BasePresenterFragment<ProductDetailPresenter>
        implements ProductDetailView {
    public static final int REQUEST_CODE_SHOP_INFO = 998;
    public static final int REQUEST_CODE_TALK_PRODUCT = 1;
    public static final int REQUEST_CODE_EDIT_PRODUCT = 2;
    public static final int REQUEST_CODE_LOGIN = 561;
    public static final int STATUS_IN_WISHLIST = 1;
    public static final int STATUS_NOT_WISHLIST = 0;
    public static final String STATE_DETAIL_PRODUCT = "STATE_DETAIL_PRODUCT";
    public static final String STATE_OTHER_PRODUCTS = "STATE_OTHER_PRODUCTS";
    public static final String STATE_VIDEO = "STATE_VIDEO";
    public static final String STATE_PRODUCT_CAMPAIGN = "STATE_PRODUCT_CAMPAIGN";
    public static final int INIT_REQUEST = 1;
    public static final int RE_REQUEST = 2;

    private static final String ARG_PARAM_PRODUCT_PASS_DATA = "ARG_PARAM_PRODUCT_PASS_DATA";
    private static final String ARG_FROM_DEEPLINK = "ARG_FROM_DEEPLINK";
    private static final String TAG = ProductDetailFragment.class.getSimpleName();

    private CoordinatorLayout coordinatorLayout;
    private HeaderInfoView headerInfoView;
    private DetailInfoView detailInfoView;
    private PictureView pictureView;
    private RatingTalkCourierView ratingTalkCourierView;
    private PriceSimulationView priceSimulationView;
    private ShopInfoViewV2 shopInfoView;
    private TransactionDetailView transactionDetailView;
    private VideoDescriptionLayout videoDescriptionLayout;
    private OtherProductsView otherProductsView;
    private NewShopView newShopView;
    private ButtonBuyView buttonBuyView;
    private LastUpdateView lastUpdateView;
    private ProgressBar progressBar;

    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton fabWishlist;
    private TextView tvTickerGTM;

    private ProductPass productPass;
    private ProductDetailData productData;
    private List<ProductOther> productOthers;
    private VideoData videoData;
    private ProductCampaign productCampaign;
    private AppIndexHandler appIndexHandler;
    private ProgressDialog loading;

    private DetailFragmentInteractionListener interactionListener;
    private DeepLinkWebViewHandleListener webViewHandleListener;
    private Menu menu;

    private ReportProductDialogFragment fragment;
    private Bundle recentBundle;

    public static ProductDetailFragment newInstance(@NonNull ProductPass productPass) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_PRODUCT_PASS_DATA, productPass);
        fragment.setArguments(args);
        return fragment;
    }

    public static ProductDetailFragment newInstanceForDeeplink(@NonNull ProductPass productPass) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_PRODUCT_PASS_DATA, productPass);
        args.putBoolean(ARG_FROM_DEEPLINK, true);
        fragment.setArguments(args);
        return fragment;
    }

    public ProductDetailFragment() {
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        this.presenter = new ProductDetailPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) throws ClassCastException {
        interactionListener = (DetailFragmentInteractionListener) activity;
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        productPass = arguments.getParcelable(ARG_PARAM_PRODUCT_PASS_DATA);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_product_detail_page;
    }

    @Override
    protected void initView(View view) {
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator);
        tvTickerGTM = (TextView) view.findViewById(R.id.tv_ticker_gtm);
        videoDescriptionLayout = (VideoDescriptionLayout) view.findViewById(R.id.video_layout);
        headerInfoView = (HeaderInfoView) view.findViewById(R.id.view_header);
        detailInfoView = (DetailInfoView) view.findViewById(R.id.view_detail);
        pictureView = (PictureView) view.findViewById(R.id.view_picture);
        shopInfoView = (ShopInfoViewV2) view.findViewById(R.id.view_shop_info);
        otherProductsView = (OtherProductsView) view.findViewById(R.id.view_other_products);
        ratingTalkCourierView = (RatingTalkCourierView) view.findViewById(R.id.view_rating);
        newShopView = (NewShopView) view.findViewById(R.id.view_new_shop);
        buttonBuyView = (ButtonBuyView) view.findViewById(R.id.view_buy);
        lastUpdateView = (LastUpdateView) view.findViewById(R.id.view_last_update);
        progressBar = (ProgressBar) view.findViewById(R.id.view_progress);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);
        collapsingToolbarLayout
                = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        transactionDetailView
                = (TransactionDetailView) view.findViewById(R.id.view_transaction_detail);
        priceSimulationView
                = (PriceSimulationView) view.findViewById(R.id.view_price_simulation);
        fabWishlist = (FloatingActionButton) view.findViewById(R.id.fab_detail);
        collapsingToolbarLayout.setTitle("");
        toolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appBarLayout.addOnOffsetChangedListener(onAppbarOffsetChange());
        initStatusBarDark();
        setHasOptionsMenu(true);
        initToolbarTransparant();
    }

    @Override
    protected void setViewListener() {
        headerInfoView.setListener(this);
        pictureView.setListener(this);
        buttonBuyView.setListener(this);
        ratingTalkCourierView.setListener(this);
        detailInfoView.setListener(this);
        lastUpdateView.setListener(this);
        otherProductsView.setListener(this);
        newShopView.setListener(this);
        shopInfoView.setListener(this);
        videoDescriptionLayout.setListener(this);
        transactionDetailView.setListener(this);
        priceSimulationView.setListener(this);
        fabWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (productData != null) {
                    presenter.processWishList(context, productData);
                }
            }
        });
    }

    @Override
    protected void initialVar() {
        appIndexHandler = new AppIndexHandler(getActivity());
        loading = new ProgressDialog(context);
        loading.setCancelable(false);
        loading.setMessage("Loading");
        if (presenter != null)
            presenter.initRetrofitInteractor();
        else
            initialPresenter();
    }

    @Override
    protected void setActionVar() {
    }

    @Override
    public void onProductDepartmentClicked(@NonNull Bundle bundle) {
        presenter.processToBrowseProduct(context, bundle);
    }

    @Override
    public void onProductCatalogClicked(@NonNull String catalogId) {
        presenter.processToCatalog(context, catalogId);
    }

    @Override
    public void onProductEtalaseClicked(@NonNull Bundle bundle) {
        presenter.processToShopInfo(context, bundle);
    }

    @Override
    public void onProductTalkClicked(@NonNull Bundle bundle) {
        presenter.processToTalk(context, bundle);
    }

    @Override
    public void onProductReviewClicked(@NonNull Bundle bundle) {
        presenter.processToReputation(context, bundle);
    }

    @Override
    public void onProductManagePromoteClicked(ProductDetailData productData) {
        presenter.requestPromoteProduct(context, productData);
    }

    @Override
    public void onProductManageToEtalaseClicked(int productId) {
        presenter.requestMoveToEtalase(context, productId);
    }

    @Override
    public void onProductManageEditClicked(@NonNull Bundle bundle) {
        presenter.processToEditProduct(context, bundle);
    }

    @Override
    public void onProductManageSoldOutClicked(int productId) {
        presenter.requestMoveToWarehouse(context, productId);
    }

    @Override
    public void onProductShopAvatarClicked(@NonNull Bundle bundle) {
        presenter.processToShopInfo(context, bundle);
    }

    @Override
    public void onProductOtherClicked(@NonNull ProductPass productPass) {
        interactionListener.jumpOtherProductDetail(productPass);
    }

    @Override
    public void onProductShopNameClicked(@NonNull Bundle bundle) {
        presenter.processToShopInfo(context, bundle);
    }

    @Override
    public void onProductShareClicked(@NonNull ShareData data) {
        Intent intent = new Intent(getActivity(), ShareActivity.class);
        intent.putExtra(ShareData.TAG, data);
        startActivity(intent);
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void shareProduct(ShareData data) {
        interactionListener.shareProductInfo(data);
    }

    @Override
    public void onProductRatingClicked(@NonNull Bundle bundle) {
        presenter.processToReputation(context, bundle);
    }

    @Override
    public void onCourierClicked(@NonNull Bundle bundle) {
        Intent intent = new Intent(context, CourierActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        getActivity().overridePendingTransition(0,0);
    }

    @Override
    public void onWholesaleClicked(@NonNull Bundle bundle) {
        Intent intent = new Intent(context, WholesaleActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        getActivity().overridePendingTransition(0,0);
    }

    @Override
    public void onDescriptionClicked(@NonNull Bundle bundle) {
        Intent intent = new Intent(context, DescriptionActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        getActivity().overridePendingTransition(0,0);
    }

    @Override
    public void onInstallmentClicked(@NonNull Bundle bundle) {
        Intent intent = new Intent(context, InstallmentActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        getActivity().overridePendingTransition(0,0);
    }

    @Override
    public void onProductShopInfoError() {
        buttonBuyView.setVisibility(View.GONE);
    }

    @Override
    public void onProductStatusError() {
        buttonBuyView.setVisibility(View.GONE);
    }

    @Override
    public void onProductNewShopClicked() {
        presenter.processToCreateShop(context);
    }

    @Override
    public void onProductBuySessionLogin(@NonNull ProductCartPass data) {
        presenter.processToCart(context, data);
    }

    @Override
    public void onProductBuySessionNotLogin(@NonNull Bundle bundle) {
        presenter.processToLogin(context, bundle);
    }

    @Override
    public void renderTempProductData(ProductPass productPass) {
        this.headerInfoView.renderTempData(productPass);
        this.pictureView.renderTempData(productPass);
    }

    @Override
    public void onProductDetailLoaded(@NonNull ProductDetailData successResult) {
        presenter.processGetGTMTicker();
        this.productData = successResult;
        this.headerInfoView.renderData(successResult);
        this.pictureView.renderData(successResult);
        this.buttonBuyView.renderData(successResult);
        this.ratingTalkCourierView.renderData(successResult);
        this.transactionDetailView.renderData(successResult);
        this.detailInfoView.renderData(successResult);
        this.lastUpdateView.renderData(successResult);
        this.shopInfoView.renderData(successResult);
        this.otherProductsView.renderData(successResult);
        this.newShopView.renderData(successResult);
        this.videoDescriptionLayout.renderData(successResult);
        this.priceSimulationView.renderData(successResult);
        this.interactionListener.onProductDetailLoaded(successResult);
        this.presenter.sendAnalytics(successResult);
        this.presenter.sendAppsFlyerData(context, successResult, AFInAppEventType.CONTENT_VIEW);
        this.presenter.sendLocalytics(context, successResult);
        this.presenter.startIndexingApp(appIndexHandler, successResult);
        this.refreshMenu();
        this.updateWishListStatus(productData.getInfo().getProductAlreadyWishlist());
    }

    @Override
    public void onProductPictureClicked(@NonNull Bundle bundle) {
        presenter.processToPicturePreview(context, bundle);
    }

    @Override
    public void onOtherProductLoaded(List<ProductOther> productOthers) {
        this.productOthers = productOthers;
        otherProductsView.renderOtherProduct(productOthers);
    }

    @Override
    public void onProductShopMessageClicked(@NonNull Bundle bundle) {
        presenter.processToSendMessage(context, bundle);
    }

    @Override
    public void onProductHasEdited() {
        presenter.requestProductDetail(context, productPass, RE_REQUEST, true);
    }

    @Override
    public void onProductTalkUpdated() {
        presenter.requestProductDetail(context, productPass, RE_REQUEST, true);
    }

    @Override
    public void onShopFavoriteUpdated(int statFave) {
        shopInfoView.reverseFavorite();
    }

    @Override
    public void onProductShopFaveClicked(String shopId) {
        presenter.requestFaveShop(context, shopId);
    }

    @Override
    public void onProductShopRatingClicked(Bundle bundle) {
        presenter.processToShopInfo(context, bundle);
    }

    @Override
    public void finishLoadingWishList() {
        loading.dismiss();
    }

    @Override
    public void loadingWishList() {
        loading.show();
    }

    @Override
    public void updateWishListStatus(int status) {
        this.productData.getInfo().setProductAlreadyWishlist(status);
        if (productData.getShopInfo().getShopIsOwner() == 1 || productData.getShopInfo().getShopIsAllowManage()==1) {
            fabWishlist.setImageDrawable(getResources().getDrawable(R.drawable.icon_wishlist_plain));
            fabWishlist.setOnClickListener(new EditClick(productData));
        } else if (status == 1) {
            fabWishlist.setImageDrawable(getResources().getDrawable(R.drawable.ic_wishlist_red));
        } else {
            fabWishlist.setImageDrawable(getResources().getDrawable(R.drawable.ic_wishlist));
        }
        fabWishlist.setVisibility(View.VISIBLE);

        Intent resultIntent = new Intent();
        resultIntent.putExtra(WISHLIST_STATUS_UPDATED_POSITION,
                getActivity().getIntent().getIntExtra(WISHLIST_STATUS_UPDATED_POSITION, -1));
        resultIntent.putExtra(WIHSLIST_STATUS_IS_WISHLIST, status == STATUS_IN_WISHLIST);
        getActivity().setResult(Activity.RESULT_CANCELED, resultIntent);

    }

    @Override
    public void loadVideo(VideoData data) {
        this.videoDescriptionLayout.renderVideoData(data);
        videoData = data;
    }

    @Override
    public void refreshMenu() {
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void showProductDetailRetry() {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(),
                initializationErrorListener()).showRetrySnackbar();

    }

    @Override
    public void showProductOthersRetry() {

    }

    @Override
    public void showFaveShopRetry() {
        NetworkErrorHelper.showSnackbar(getActivity());
    }

    @Override
    public void showWishListRetry(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(),errorMessage);
    }

    @Override
    public void showPromoteRetry() {
        NetworkErrorHelper.showSnackbar(getActivity());
    }

    @Override
    public void onNullData() {
        Boolean isFromDeeplink = getArguments().getBoolean(ARG_FROM_DEEPLINK, false);
        if (isFromDeeplink) {
            ProductPass pass = (ProductPass) getArguments().get(ARG_PARAM_PRODUCT_PASS_DATA);
            webViewHandleListener = (DeepLinkWebViewHandleListener) getActivity();
            webViewHandleListener.catchToWebView(pass != null ? pass.getProductUri() : "");
        } else {
            showToastMessage("Produk tidak ditemukan!");
            closeView();
        }

    }

    @Override
    public void showReportDialog() {
        fragment = ReportProductDialogFragment.createInstance(productData, recentBundle);
        fragment.setListener(this);
        fragment.show(getFragmentManager(), "ReportProductDialogFragment");
    }

    @Override
    public void onProductReportClicked() {
        presenter.reportProduct(context);
    }

    @Override
    public void showTickerGTM(String message) {
        tvTickerGTM.setText(message);
        tvTickerGTM.setVisibility(View.VISIBLE);
        tvTickerGTM.setAutoLinkMask(0);
        Linkify.addLinks(tvTickerGTM, Linkify.WEB_URLS);
        tvTickerGTM.setText(MethodChecker.fromHtml(tvTickerGTM.getText().toString()));
    }

    @Override
    public void hideTickerGTM() {
        tvTickerGTM.setVisibility(View.GONE);
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void showProgressLoading() {
  
    }

    @Override
    public void hideProgressLoading() {
       
    }

    @Override
    public void showToastMessage(String message) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout,
                message.replace("\n"," "),
                Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView tv = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setMaxLines(7);
        snackbar.show();
    }

    @Override
    public void showDialog(Dialog dialog) {
        dialog.show();
    }

    @Override
    public void dismissDialog(Dialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void closeView() {
        this.getActivity().finish();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        interactionListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView(context);
        destroyVideoLayout();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.product_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        presenter.prepareOptionMenu(menu, context, productData);
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {
        Log.d(TAG, "onFirstTimeLaunched");
        if (productData != null) {
            onProductDetailLoaded(productData);
            Log.d(TAG, "productData != null");
        } else {
            Log.d(TAG, "productData == null");
            presenter.processDataPass(productPass);
            presenter.requestProductDetail(context, productPass, INIT_REQUEST, false);
        }
    }

    @Override
    public void onSaveState(Bundle outState) {
        Log.d(TAG, "onSaveState");
        presenter.saveStateProductDetail(outState, STATE_DETAIL_PRODUCT, productData);
        presenter.saveStateProductOthers(outState, STATE_OTHER_PRODUCTS, productOthers);
        presenter.saveStateVideoData(outState, STATE_VIDEO, videoData);
        presenter.saveStateProductCampaign(outState, STATE_PRODUCT_CAMPAIGN, productCampaign);
    }

    @Override
    public void onRestoreState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreState");
        presenter.processStateData(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_share) {
            if (productData != null) {
                ShareData shareData = ShareData.Builder.aShareData()
                        .setName(productData.getInfo().getProductName())
                        .setDescription(productData.getInfo().getProductDescription())
                        .setImgUri(productData.getProductImages().get(0).getImageSrc())
                        .setPrice(productData.getInfo().getProductPrice())
                        .setUri(productData.getInfo().getProductUrl())
                        .setType(ShareData.PRODUCT_TYPE)
                        .build();
                onProductShareClicked(shareData);
            }
            return true;
        } else if (item.getItemId() == R.id.action_cart) {
            if (!SessionHandler.isV4Login(getActivity())) {
                Intent intent = SessionRouter.getLoginActivityIntent(context);
                intent.putExtra(Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
                intent.putExtra("product_id", String.valueOf(productData.getInfo().getProductId()));
                navigateToActivityRequest(intent, ProductDetailFragment.REQUEST_CODE_LOGIN);
            } else {
                startActivity(TransactionCartRouter.createInstanceCartActivity(getActivity()));
            }
            return true;
        } else if (item.getItemId() == R.id.action_report) {
            presenter.reportProduct(context);
            return true;
        } else if (item.getItemId() == R.id.action_warehouse) {
            presenter.requestMoveToWarehouse(context, productData.getInfo().getProductId());
            return true;
        } else if (item.getItemId() == R.id.action_etalase) {
            presenter.requestMoveToEtalase(context, productData.getInfo().getProductId());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(ProductDetailFragment.class.getSimpleName(), "onActivityResult requestCode " + requestCode + " resultCode " + resultCode);
        switch (requestCode) {
            case REQUEST_CODE_EDIT_PRODUCT:
                presenter.processResultEdit(resultCode, data);
                break;
            case REQUEST_CODE_SHOP_INFO:
                presenter.processResultShop(resultCode, data);
                break;
            case REQUEST_CODE_TALK_PRODUCT:
                presenter.processResultTalk(resultCode, data);
                break;
            case REQUEST_CODE_LOGIN:
                videoDescriptionLayout.refreshVideo();
                presenter.requestProductDetail(context, productPass, RE_REQUEST, true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (productData != null) {
            presenter.startIndexingApp(appIndexHandler, productData);
            this.newShopView.renderData(productData);
            refreshMenu();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.stopIndexingApp(appIndexHandler);
    }

    public void onSuccessAction(Bundle resultData, int resultCode) {
        if (fragment != null) fragment.dismiss();
        recentBundle = null;
        SnackbarManager.make(getActivity(),
                resultData.getString(ProductInfoIntentService.EXTRA_RESULT),
                Snackbar.LENGTH_LONG).show();
    }

    public void onErrorAction(Bundle resultData, int resultCode) {
        if (fragment != null) {
            recentBundle = resultData.getBundle(ProductInfoIntentService.EXTRA_BUNDLE);
            fragment.onErrorAction(resultData.getString(ProductInfoIntentService.EXTRA_RESULT));
        } else {
            SnackbarManager.make(getActivity(),
                    resultData.getString(ProductInfoIntentService.EXTRA_RESULT),
                    Snackbar.LENGTH_LONG).show();
        }
    }

    public void setRecentBundle(Bundle recentBundle) {
        this.recentBundle = recentBundle;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ProductDetailFragmentPermissionsDispatcher.onRequestPermissionsResult(ProductDetailFragment.this,
                requestCode, grantResults);
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showRationaleForStorage(final PermissionRequest request) {
        RequestPermissionUtil.onShowRationale(getActivity(), request, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showDeniedForStorage() {
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showNeverAskForStorage() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
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
    public void showFullScreenError() {
        NetworkErrorHelper.showEmptyState(getActivity(),
                getActivity().findViewById(R.id.root_view),
                initializationErrorListener());
    }

    @Override
    public void moveToEditFragment(boolean isEdit, String productId) {
        if(getActivity().getApplication() instanceof TkpdCoreRouter){
            Intent intent = ((TkpdCoreRouter)getActivity().getApplication()).goToEditProduct(context, isEdit, productId);
            navigateToActivityRequest(intent, ProductDetailFragment.REQUEST_CODE_EDIT_PRODUCT);
        }
    }

    @Override
    public void showSuccessWishlistSnackBar() {
        Snackbar.make(coordinatorLayout, context.getString(R.string.msg_add_wishlist), Snackbar.LENGTH_LONG)
                .setAction(context.getString(R.string.go_to_wishlist), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, SimpleHomeRouter.getSimpleHomeActivityClass());
                        intent.putExtra(
                                SimpleHomeRouter.FRAGMENT_TYPE,
                                SimpleHomeRouter.WISHLIST_FRAGMENT);

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        navigateToActivity(intent);
                        closeView();
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.tkpd_main_green ))
                .show();
    }

    @Override
    public void showProductCampaign(ProductCampaign productCampaign) {
        this.productCampaign = productCampaign;
        headerInfoView.renderProductCampaign(this.productCampaign);
    }

    @Override
    public void showMostHelpfulReview(List<Review> reviews) {
        Log.d("alifanuraniputri", "alifanuraniputri: ");
    }

    private void destroyVideoLayout() {
        if (videoDescriptionLayout != null && videoDescriptionLayout.isShown()) {
            videoDescriptionLayout.destroyVideoLayoutProcess();
        }
    }

    private AppBarLayout.OnOffsetChangedListener onAppbarOffsetChange() {
        return new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    initStatusBarLight();
                    initToolbarLight();
                    fabWishlist.hide();
                } else {
                    initStatusBarDark();
                    initToolbarTransparant();
                    fabWishlist.show();
                }
            }
        };
    }

    private void initToolbarLight() {
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.grey_toolbar_icon));
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        toolbar.setTitleTextColor(getResources().getColor(R.color.grey_toolbar_icon));
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_icon_back_black);
        if (menu != null && menu.size() > 2) {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.share_thin_black));
            LocalCacheHandler Cache = new LocalCacheHandler(getActivity(), DrawerHelper.DRAWER_CACHE);
            int CartCache = Cache.getInt(DrawerNotification.IS_HAS_CART);
            if (CartCache > 0) {
                menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.cart_active_black));
            } else {
                menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_icon_cart_green_black));
            }
        }
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_more_vert_black));

    }

    private void initToolbarTransparant() {
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        toolbar.setBackgroundColor(Color.TRANSPARENT);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_icon_back);
        if (menu != null && menu.size() > 1) {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(context, R.drawable.share_thin_white));
            LocalCacheHandler Cache = new LocalCacheHandler(getActivity(), DrawerHelper.DRAWER_CACHE);
            int CartCache = Cache.getInt(DrawerNotification.IS_HAS_CART);
            if (CartCache > 0) {
                menu.getItem(1).setIcon(ContextCompat.getDrawable(context, R.drawable.cart_active_white));
            } else {
                menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_icon_cart_green_white));
            }
            toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_more_vert_white));
        }
    }

    private void initStatusBarDark() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.black));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                View decor = getActivity().getWindow().getDecorView();
                decor.setSystemUiVisibility(0);

            }
        }
    }

    private void initStatusBarLight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getActivity().getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }
    }

    private class EditClick implements View.OnClickListener {
        private final ProductDetailData data;

        public EditClick(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("is_edit", true);
            bundle.putString("product_id", String.valueOf(data.getInfo().getProductId()));
            onProductManageEditClicked(bundle);
        }
    }

}
