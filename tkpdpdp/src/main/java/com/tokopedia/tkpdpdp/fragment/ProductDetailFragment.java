package com.tokopedia.tkpdpdp.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.net.Uri;

import com.appsflyer.AFInAppEventType;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tagmanager.DataLayer;
import com.google.gson.Gson;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.ActionInterfaces.ActionCreator;
import com.tokopedia.abstraction.ActionInterfaces.ActionUIDelegate;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.utils.FindAndReplaceHelper;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.product.model.productdetail.mosthelpful.ReviewImageAttachment;
import com.tokopedia.design.component.TextViewCompat;
import com.tokopedia.expresscheckout.common.view.errorview.ErrorBottomsheets;
import com.tokopedia.expresscheckout.common.view.errorview.ErrorBottomsheetsActionListener;
import com.tokopedia.expresscheckout.common.view.errorview.ErrorBottomsheetsActionListenerWithRetry;
import com.tokopedia.gallery.ImageReviewGalleryActivity;
import com.tokopedia.gallery.domain.GetImageReviewUseCase;
import com.tokopedia.gallery.viewmodel.ImageReviewItem;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.linker.model.LinkerData;
import com.tokopedia.product.share.ProductData;
import com.tokopedia.product.share.ProductShare;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;
import com.tokopedia.tkpdpdp.DescriptionActivityNew;
import com.tokopedia.tkpdpdp.ProductInfoShortDetailActivity;
import com.tokopedia.tkpdpdp.customview.ImageFromBuyerView;
import com.tokopedia.tkpdpdp.customview.ProductInfoAttributeView;
import com.tokopedia.tkpdpdp.customview.ProductInfoShortView;
import com.tokopedia.tkpdpdp.customview.RatingTalkCourierView;
import com.tokopedia.tkpdpdp.customview.VarianCourierSimulationView;
import com.tokopedia.tkpdpdp.customview.WholesaleInstallmentView;
import com.tokopedia.tkpdpdp.domain.GetMostHelpfulReviewUseCase;
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker;
import com.tokopedia.tkpdpdp.util.ProductNotFoundException;
import com.tokopedia.transactiondata.entity.shared.expresscheckout.AtcRequestParam;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.affiliatecommon.domain.GetProductAffiliateGqlUseCase;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragmentV4;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.model.share.ShareData;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.entity.variant.Child;
import com.tokopedia.core.network.entity.variant.Option;
import com.tokopedia.core.network.entity.variant.ProductVariant;
import com.tokopedia.core.product.intentservice.ProductInfoIntentService;
import com.tokopedia.core.product.interactor.CacheInteractor;
import com.tokopedia.core.product.interactor.CacheInteractorImpl;
import com.tokopedia.core.product.interactor.RetrofitInteractorImpl;
import com.tokopedia.core.product.listener.DetailFragmentInteractionListener;
import com.tokopedia.core.product.model.goldmerchant.VideoData;
import com.tokopedia.core.product.model.productdetail.ProductBreadcrumb;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.ProductImage;
import com.tokopedia.core.product.model.productdetail.ProductShopInfo;
import com.tokopedia.core.product.model.productdetail.discussion.LatestTalkViewModel;
import com.tokopedia.core.product.model.productdetail.mosthelpful.Review;
import com.tokopedia.core.product.model.productdetail.promowidget.PromoAttributes;
import com.tokopedia.core.product.model.productother.ProductOther;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.router.home.SimpleHomeRouter;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.router.reactnative.IReactNativeRouter;
import com.tokopedia.core.router.transactionmodule.TransactionCartRouter;
import com.tokopedia.core.router.transactionmodule.passdata.ProductCartPass;
import com.tokopedia.core.util.AppIndexHandler;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.webview.listener.DeepLinkWebViewHandleListener;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.design.component.badge.BadgeView;
import com.tokopedia.merchantvoucher.common.di.DaggerMerchantVoucherComponent;
import com.tokopedia.merchantvoucher.common.di.MerchantVoucherComponent;
import com.tokopedia.merchantvoucher.common.gql.data.MessageTitleErrorException;
import com.tokopedia.merchantvoucher.common.gql.data.UseMerchantVoucherQueryResult;
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel;
import com.tokopedia.merchantvoucher.voucherDetail.MerchantVoucherDetailActivity;
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListActivity;
import com.tokopedia.merchantvoucher.voucherList.presenter.MerchantVoucherListPresenter;
import com.tokopedia.merchantvoucher.voucherList.presenter.MerchantVoucherListView;
import com.tokopedia.merchantvoucher.voucherList.widget.MerchantVoucherListWidget;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.di.ShopCommonModule;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;
import com.tokopedia.tkpdpdp.CourierActivity;
import com.tokopedia.tkpdpdp.DinkFailedActivity;
import com.tokopedia.tkpdpdp.DinkSuccessActivity;
import com.tokopedia.tkpdpdp.InstallmentActivity;
import com.tokopedia.tkpdpdp.PreviewProductImageDetail;
import com.tokopedia.tkpdpdp.ProductInfoActivity;
import com.tokopedia.tkpdpdp.ProductModalActivity;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.VariantActivity;
import com.tokopedia.tkpdpdp.WholesaleActivity;
import com.tokopedia.tkpdpdp.constant.ConstantKey;
import com.tokopedia.tkpdpdp.courier.CourierViewData;
import com.tokopedia.tkpdpdp.customview.ButtonAffiliate;
import com.tokopedia.tkpdpdp.customview.ButtonBuyView;
import com.tokopedia.tkpdpdp.customview.CountDrawable;
import com.tokopedia.tkpdpdp.customview.FlingBehavior;
import com.tokopedia.tkpdpdp.customview.HeaderInfoView;
import com.tokopedia.tkpdpdp.customview.LastUpdateView;
import com.tokopedia.tkpdpdp.customview.LatestTalkView;
import com.tokopedia.tkpdpdp.customview.MostHelpfulReviewView;
import com.tokopedia.tkpdpdp.customview.NewShopView;
import com.tokopedia.tkpdpdp.customview.OtherProductsView;
import com.tokopedia.tkpdpdp.customview.PictureView;
import com.tokopedia.tkpdpdp.customview.PromoWidgetView;
import com.tokopedia.tkpdpdp.customview.ShopInfoViewV2;
import com.tokopedia.tkpdpdp.customview.VideoDescriptionLayout;
import com.tokopedia.tkpdpdp.customview.YoutubeThumbnailViewHolder;
import com.tokopedia.tkpdpdp.dialog.ReportProductDialogFragment;
import com.tokopedia.tkpdpdp.domain.GetWishlistCountUseCase;
import com.tokopedia.tkpdpdp.estimasiongkir.data.model.RatesModel;
import com.tokopedia.tkpdpdp.estimasiongkir.presentation.activity.RatesEstimationDetailActivity;
import com.tokopedia.tkpdpdp.listener.AppBarStateChangeListener;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;
import com.tokopedia.tkpdpdp.presenter.ProductDetailPresenter;
import com.tokopedia.tkpdpdp.presenter.ProductDetailPresenterImpl;
import com.tokopedia.tkpdpdp.presenter.di.DaggerProductDetailComponent;
import com.tokopedia.tkpdpdp.presenter.di.ProductDetailComponent;
import com.tokopedia.tkpdpdp.revamp.ProductViewData;
import com.tokopedia.tkpdpdp.tracking.ProductPageTracking;
import com.tokopedia.tkpdpdp.viewmodel.AffiliateInfoViewModel;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.Xparams;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.widget.TopAdsCarouselView;
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption;
import com.tokopedia.transaction.common.TransactionRouter;
import com.tokopedia.transaction.common.sharedata.AddToCartResult;
import com.tokopedia.transactionanalytics.CheckoutAnalyticsAddToCart;
import com.tokopedia.transactionanalytics.data.EnhancedECommerceCartMapData;
import com.tokopedia.transactionanalytics.data.EnhancedECommerceProductCartMapData;
import com.tokopedia.transactionanalytics.listener.ITransactionAnalyticsProductDetailPage;
import com.tokopedia.wishlist.common.listener.WishListActionListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import kotlin.Unit;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.tokopedia.core.product.model.productdetail.ProductInfo.PRD_STATE_PENDING;
import static com.tokopedia.core.router.productdetail.ProductDetailRouter.EXTRA_PRODUCT_ID;
import static com.tokopedia.core.router.productdetail.ProductDetailRouter.WIHSLIST_STATUS_IS_WISHLIST;
import static com.tokopedia.core.router.productdetail.ProductDetailRouter.WISHLIST_STATUS_UPDATED_POSITION;
import static com.tokopedia.core.share.DefaultShare.KEY_OTHER;
import static com.tokopedia.core.var.TkpdCache.Key.STATE_ORIENTATION_CHANGED;
import static com.tokopedia.core.var.TkpdCache.PRODUCT_DETAIL;
import static com.tokopedia.tkpdpdp.VariantActivity.KEY_LEVEL1_SELECTED;
import static com.tokopedia.tkpdpdp.VariantActivity.KEY_LEVEL2_SELECTED;
import static com.tokopedia.tkpdpdp.VariantActivity.KEY_PRODUCT_DETAIL_DATA;
import static com.tokopedia.tkpdpdp.VariantActivity.KEY_REMARK_FOR_SELLER;
import static com.tokopedia.tkpdpdp.VariantActivity.KEY_SELECTED_QUANTIY;
import static com.tokopedia.tkpdpdp.VariantActivity.KEY_STATE_OPEN_VARIANT;
import static com.tokopedia.tkpdpdp.VariantActivity.KEY_STATE_RESULT_VARIANT;
import static com.tokopedia.tkpdpdp.VariantActivity.KEY_VARIANT_DATA;
import static com.tokopedia.tkpdpdp.VariantActivity.SELECTED_VARIANT_RESULT_SKIP_TO_CART;
import static com.tokopedia.tkpdpdp.VariantActivity.SELECTED_VARIANT_RESULT_STAY_IN_PDP;
import static com.tokopedia.tkpdpdp.constant.ConstantKey.ARGS_STATE_RESULT_PDP_MODAL;
import static com.tokopedia.topads.sdk.domain.TopAdsParams.DEFAULT_KEY_EP;
import static com.tokopedia.topads.sdk.domain.TopAdsParams.SRC_PDP_VALUE;
import static com.tokopedia.transactiondata.entity.shared.expresscheckout.Constant.CHECKOUT_TYPE_EXPRESS;
import static com.tokopedia.transactiondata.entity.shared.expresscheckout.Constant.CHECKOUT_TYPE_DEFAULT;
import static com.tokopedia.transactiondata.entity.shared.expresscheckout.Constant.CHECKOUT_TYPE_OCS;
import static com.tokopedia.transactiondata.entity.shared.expresscheckout.Constant.EXTRA_MESSAGES_ERROR;
import static com.tokopedia.transactiondata.entity.shared.expresscheckout.Constant.RESULT_CODE_ERROR;
import static com.tokopedia.transactiondata.entity.shared.expresscheckout.Constant.RESULT_CODE_NAVIGATE_TO_NCF;
import static com.tokopedia.transactiondata.entity.shared.expresscheckout.Constant.RESULT_CODE_NAVIGATE_TO_OCS;


/**
 * ProductDetailFragment
 * Created by Angga.Prasetiyo on 22/10/2015.
 * Edited by alifa, rohmadi, henry for v2
 */
@RuntimePermissions
public class ProductDetailFragment extends BasePresenterFragmentV4<ProductDetailPresenter>
        implements ProductDetailView, TopAdsItemClickListener, TopAdsListener,
        ITransactionAnalyticsProductDetailPage, WishListActionListener, MerchantVoucherListView {

    private static final int FROM_COLLAPSED = 0;
    private static final int FROM_EXPANDED = 1;
    private static final int SCROLL_ELEVATION = 324;
    private static final int SHOWCASE_MARGIN = 10;
    private static final int SHOWCASE_HEIGHT = 100;

    public static final int REQUEST_CODE_SHOP_INFO = 998;
    public static final int REQUEST_CODE_TALK_PRODUCT = 1;
    public static final int REQUEST_CODE_EDIT_PRODUCT = 2;
    public static final int REQUEST_CODE_LOGIN = 561;
    public static final int REQUEST_CODE_LOGIN_USE_VOUCHER = 562;
    public static final int REQUEST_CODE_MERCHANT_VOUCHER_DETAIL = 563;
    public static final int REQUEST_CODE_MERCHANT_VOUCHER = 564;
    public static final int REQUEST_CODE_ATC_EXPRESS = 565;

    public static final int STATUS_IN_WISHLIST = 1;
    public static final int STATUS_NOT_WISHLIST = 0;
    public static final int REQUEST_VARIANT = 99;
    public static final int REQUEST_PRODUCT_MODAL = 101;
    public static final int INIT_REQUEST = 1;
    public static final int RE_REQUEST = 2;

    private static final String TAG = ProductDetailFragment.class.getSimpleName();
    private static final String ARG_PARAM_PRODUCT_PASS_DATA = "ARG_PARAM_PRODUCT_PASS_DATA";
    private static final String ARG_FROM_DEEPLINK = "ARG_FROM_DEEPLINK";
    private static final String ENABLE_VARIANT = "mainapp_discovery_enable_pdp_variant";
    private static final String ENABLE_MERCHANT_VOUCHER = "app_flag_merchant_voucher";
    private static final String NON_VARIANT = "non-variant";
    private static final String PRODUCT_ID = "{product_id}";
    private static final String AD_ID = "{ad_id}";

    public static final String STATE_DETAIL_PRODUCT = "STATE_DETAIL_PRODUCT";
    public static final String STATE_PRODUCT_VARIANT = "STATE_PRODUCT_VARIANT";
    public static final String STATE_PRODUCT_STOCK_NON_VARIANT = "STATE_PRODUCT_STOCK_NON_VARIANT";
    public static final String STATE_OTHER_PRODUCTS = "STATE_OTHER_PRODUCTS";
    public static final String STATE_VIDEO = "STATE_VIDEO";
    public static final String STATE_PROMO_WIDGET = "STATE_PROMO_WIDGET";
    public static final String STATE_APP_BAR_COLLAPSED = "STATE_APP_BAR_COLLAPSED";
    public static final String TAG_SHOWCASE_VARIANT = "-SHOWCASE_VARIANT";
    private static final String STATIC_VALUE_ENHANCE_NONE_OTHER = "none / other";
    private static final String PDP_TRACE = "pdp_trace";
    public static final int TYPE_BUTTON_BUY_CART = 10;
    public static final int TYPE_BUTTON_BUY_BELI = 20;
    public static final int TYPE_BUTTON_OPEN_VARIANT = 30;
    public static final int NUM_VOUCHER_TO_SHOW = 3;

    private CoordinatorLayout coordinatorLayout;
    private HeaderInfoView headerInfoView;
    private ProductInfoAttributeView productInfoAttributeView;
    private PictureView pictureView;
    private RatingTalkCourierView ratingTalkDescriptionView;
    private VarianCourierSimulationView varianCourierSimulationView;
    private PromoWidgetView promoWidgetView;
    private ImageFromBuyerView imageFromBuyerView;
    private ErrorBottomsheets errorBottomsheets;

    MerchantVoucherListPresenter voucherListPresenter;
    private MerchantVoucherListWidget merchantVoucherListWidget;
    private View promoContainer;

    private ProductInfoShortView productInfoShortView;
    private WholesaleInstallmentView wholesaleInstallmentView;
    private ShopInfoViewV2 shopInfoView;
    private VideoDescriptionLayout videoDescriptionLayout;
    private MostHelpfulReviewView mostHelpfulReviewView;
    private OtherProductsView otherProductsView;
    private NewShopView newShopView;
    private ButtonBuyView buttonBuyView;
    private LastUpdateView lastUpdateView;
    private LatestTalkView latestTalkView;
    private TopAdsCarouselView topAds;
    private ProgressBar progressBar;
    private NestedScrollView nestedScrollView;
    private ButtonAffiliate buttonAffiliate;

    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton fabWishlist;
    private TextViewCompat labelCod;
    private LinearLayout rootView;
    private boolean isAppBarCollapsed = false;
    private TextView tvTickerGTM;
    private AppIndexHandler appIndexHandler;
    private ProgressDialog loading;
    private DetailFragmentInteractionListener interactionListener;
    private DeepLinkWebViewHandleListener webViewHandleListener;
    private Menu menu;
    private YoutubeThumbnailViewHolder.YouTubeThumbnailLoadInProcess youTubeThumbnailLoadInProcessListener;
    private ReportProductDialogFragment fragment;
    private Bundle recentBundle;
    private com.tokopedia.abstraction.common.utils.LocalCacheHandler localCacheHandler;
    private PerformanceMonitoring performanceMonitoring;

    private ProductPass productPass;
    private ProductDetailData productData;
    private ProductViewData viewData;
    private boolean useVariant = true;
    private boolean useMerchantVoucherFeature = true;
    private ProductVariant productVariant;
    private Child productStockNonVariant;
    private List<ProductOther> productOthers;
    private VideoData videoData;
    private PromoAttributes promoAttributes;
    private Option variantLevel1;
    private Option variantLevel2;
    private boolean onClickBuyWhileRequestingVariant = false;
    private int selectedQuantity;
    private String selectedRemarkNotes;
    private CacheInteractor cacheInteractor;

    private boolean needLoadVoucher;

    private RemoteConfig remoteConfig;
    private ShowCaseDialog showCaseDialog;
    private CheckoutAnalyticsAddToCart checkoutAnalyticsAddToCart;
    private String lastStateOnClickBuyWhileRequestVariant;
    private RemoteConfig firebaseRemoteConfig;

    private boolean isCodShown = false;

    @Inject
    UserSession userSession;

    @Inject
    GetWishlistCountUseCase getWishlistCountUseCase;

    @Inject
    GetMostHelpfulReviewUseCase getMostHelpfulReviewUseCase;

    @Inject
    GetProductAffiliateGqlUseCase getAffiliateProductDataUseCase;

    @Inject
    GraphqlUseCase graphqlUseCase;

    @Inject
    ToggleFavouriteShopUseCase toggleFavouriteShopUseCase;

    @Inject
    GetImageReviewUseCase getImageReviewUseCase;

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
    public void onCreate(Bundle savedInstanceState) {
        performanceMonitoring = PerformanceMonitoring.start(PDP_TRACE);
        initInjector();
        super.onCreate(savedInstanceState);
        MerchantVoucherComponent merchantVoucherComponent = DaggerMerchantVoucherComponent.builder()
                .baseAppComponent(((BaseMainApplication) (getActivity().getApplication())).getBaseAppComponent())
                .shopCommonModule(new ShopCommonModule())
                .build();
        voucherListPresenter = merchantVoucherComponent.merchantVoucherListPresenter();
        voucherListPresenter.attachView(this);

        remoteConfig = new FirebaseRemoteConfigImpl(getActivity());
        if (!remoteConfig.getBoolean(ENABLE_VARIANT)) {
            useVariant = false;
        }
        if (!remoteConfig.getBoolean(ENABLE_MERCHANT_VOUCHER)) {
            useMerchantVoucherFeature = false;
        }
        cacheInteractor = new CacheInteractorImpl();
        localCacheHandler = new com.tokopedia.abstraction.common.utils.LocalCacheHandler(MainApplication.getAppContext(), PRODUCT_DETAIL);
        localCacheHandler.putBoolean(STATE_ORIENTATION_CHANGED, Boolean.FALSE);
        localCacheHandler.applyEditor();
    }

    private void initInjector() {
        AppComponent appComponent = ((MainApplication) getActivity().getApplication()).getAppComponent();
        ProductDetailComponent productDetailComponent = DaggerProductDetailComponent.builder()
                .appComponent(appComponent)
                .build();
        productDetailComponent.inject(this);
    }

    @Override
    protected void initialPresenter() {
        this.presenter = new ProductDetailPresenterImpl(
                getWishlistCountUseCase,
                this,
                this,
                new RetrofitInteractorImpl(),
                new CacheInteractorImpl(),
                getAffiliateProductDataUseCase,
                getImageReviewUseCase,
                getMostHelpfulReviewUseCase,
                toggleFavouriteShopUseCase);
        this.presenter.initGetRateEstimationUseCase();
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
        headerInfoView = (HeaderInfoView) view.findViewById(R.id.view_header);
        productInfoAttributeView = view.findViewById(R.id.view_product_info_attribute);
        pictureView = (PictureView) view.findViewById(R.id.view_picture);
        ratingTalkDescriptionView = (RatingTalkCourierView) view.findViewById(R.id.view_rating);
        newShopView = (NewShopView) view.findViewById(R.id.view_new_shop);
        videoDescriptionLayout = (VideoDescriptionLayout) view.findViewById(R.id.video_layout);
        shopInfoView = (ShopInfoViewV2) view.findViewById(R.id.view_shop_info);
        otherProductsView = (OtherProductsView) view.findViewById(R.id.view_other_products);
        promoWidgetView = view.findViewById(R.id.view_promo_widget);
        imageFromBuyerView = view.findViewById(R.id.view_image_from_buyer);
        promoContainer = view.findViewById(R.id.promoContainer);
        merchantVoucherListWidget = view.findViewById(R.id.merchantVoucherListWidget);
        mostHelpfulReviewView = (MostHelpfulReviewView) view.findViewById(R.id.view_most_helpful);
        buttonBuyView = (ButtonBuyView) view.findViewById(R.id.view_buy);
        lastUpdateView = (LastUpdateView) view.findViewById(R.id.view_last_update);
        latestTalkView = (LatestTalkView) view.findViewById(R.id.view_latest_discussion);
        progressBar = (ProgressBar) view.findViewById(R.id.view_progress);
        nestedScrollView = view.findViewById(R.id.nested_scroll_pdp);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);
        topAds = view.findViewById(R.id.topads);
        collapsingToolbarLayout
                = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        varianCourierSimulationView
                = view.findViewById(R.id.view_varian_courier_simulation);
        fabWishlist = (FloatingActionButton) view.findViewById(R.id.fab_detail);
        labelCod = view.findViewById(R.id.label_cod);
        rootView = (LinearLayout) view.findViewById(R.id.root_view);
        buttonAffiliate = view.findViewById(R.id.buttonAffiliate);
        productInfoShortView = view.findViewById(R.id.view_product_info_short);
        wholesaleInstallmentView = view.findViewById(R.id.view_wholesale_installment);
        collapsingToolbarLayout.setTitle("");
        toolbar.setTitle("");
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpByConfiguration(getResources().getConfiguration());

        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                switch (state) {
                    case COLLAPSED:
                        isAppBarCollapsed = true;
                        collapsedAppBar();
                        break;
                    case EXPANDED:
                        isAppBarCollapsed = false;
                        expandedAppBar();
                        break;
                }
            }
        });

        //appBarLayout.addOnOffsetChangedListener(onAppbarOffsetChange());
        merchantVoucherListWidget.setOnMerchantVoucherListWidgetListener(new MerchantVoucherListWidget.OnMerchantVoucherListWidgetListener() {
            @Override
            public void onMerchantUseVoucherClicked(MerchantVoucherViewModel merchantVoucherViewModel, int position) {
                if (getContext() == null) {
                    return;
                }
                if (productData != null) {
                    ProductPageTracking.eventClickMerchantVoucherUse(getActivity(), merchantVoucherViewModel, position);
                }
                //TOGGLE_MVC_ON use voucher is not ready, so we use copy instead. Keep below code for future release
                /*if (!voucherListPresenter.isLogin()) {
                    Intent intent = RouteManager.getIntent(getContext(), ApplinkConst.LOGIN);
                    startActivityForResult(intent, REQUEST_CODE_LOGIN_USE_VOUCHER);
                } else if (!isOwner()) {
                    showUseMerchantVoucherLoading();
                    voucherListPresenter.useMerchantVoucher(merchantVoucherViewModel.getVoucherCode(),
                            merchantVoucherViewModel.getVoucherId());
                }*/
                //TOGGLE_MVC_OFF
                if (getActivity() != null) {
                    showSnackBarClose(getActivity().getString(R.string.title_voucher_code_copied));
                }
            }

            @Override
            public void onItemClicked(MerchantVoucherViewModel merchantVoucherViewModel) {
                if (getContext() != null && productData != null) {
                    ProductPageTracking.eventClickMerchantVoucherSeeDetail(getActivity(), String.valueOf(productData.getInfo().getProductId()));
                    Intent intent = MerchantVoucherDetailActivity.createIntent(getContext(), merchantVoucherViewModel.getVoucherId(),
                            merchantVoucherViewModel, productData.getShopInfo().getShopId());
                    startActivityForResult(intent, REQUEST_CODE_MERCHANT_VOUCHER_DETAIL);
                }
            }

            @Override
            public void onSeeAllClicked() {
                if (getContext() != null && productData != null) {
                    ProductPageTracking.eventClickMerchantVoucherSeeAll(getActivity(), String.valueOf(productData.getInfo().getProductId()));
                    Intent intent = MerchantVoucherListActivity.createIntent(getContext(), productData.getShopInfo().getShopId(),
                            productData.getShopInfo().getShopName());
                    startActivityForResult(intent, REQUEST_CODE_MERCHANT_VOUCHER);
                }
            }

            @Override
            public boolean isOwner() {
                if (productData != null) {
                    return voucherListPresenter.isMyShop(productData.getShopInfo().getShopId());
                }
                return false;
            }
        });
        setHasOptionsMenu(true);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int viewId = v.getId();
            }
        });

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
            params.setBehavior(new FlingBehavior(R.id.nested_scroll_pdp));
        }

        errorBottomsheets = new ErrorBottomsheets();
    }

    private void showUseMerchantVoucherLoading() {
        if (loading == null) {
            loading = new ProgressDialog(getActivity());
            loading.setCancelable(false);
            loading.setMessage(getString(R.string.title_loading));
        }
        if (loading.isShowing()) {
            loading.dismiss();
        }
        loading.show();
    }

    private void hideUseMerchantVoucherLoading() {
        if (loading != null) {
            loading.dismiss();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setUpByConfiguration(newConfig);
    }

    private void setUpByConfiguration(Configuration configuration) {
        float widthScreen = getResources().getDisplayMetrics().widthPixels;
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
            layoutParams.height = (int) widthScreen / 3;
            appBarLayout.setVisibility(View.VISIBLE);
            if (!localCacheHandler.getBoolean(STATE_ORIENTATION_CHANGED).booleanValue()) {
                if (productData != null) {
                    UnifyTracking.eventPDPOrientationChanged(getActivity(), Integer.toString(productData.getInfo().getProductId()));
                } else {
                    UnifyTracking.eventPDPOrientationChanged(getActivity(), productPass.getProductId());
                }
                localCacheHandler.putBoolean(STATE_ORIENTATION_CHANGED, Boolean.TRUE);
                localCacheHandler.applyEditor();
            }
        } else if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
            layoutParams.height = (int) widthScreen;
            appBarLayout.setVisibility(View.VISIBLE);
        }
    }

    private void collapsedAppBar() {
        initStatusBarLight();
        initToolbarLight();
        fabWishlist.hide();
        labelCod.setVisibility(isCodShown ? View.INVISIBLE : View.GONE);
    }

    private void expandedAppBar() {
        initStatusBarDark();
        initToolbarTransparant();
        if (productData != null && productData.getInfo().getProductAlreadyWishlist() != null) {
            fabWishlist.show();
        }
        labelCod.setVisibility(isCodShown ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void setViewListener() {
        headerInfoView.setListener(this);
        pictureView.setListener(this);
        buttonBuyView.setListener(this);
        ratingTalkDescriptionView.setListener(this);
        productInfoShortView.setListener(this);
        wholesaleInstallmentView.setListener(this);
        lastUpdateView.setListener(this);
        otherProductsView.setListener(this);
        newShopView.setListener(this);
        shopInfoView.setListener(this);
        videoDescriptionLayout.setListener(this);
        promoWidgetView.setListener(this);
        mostHelpfulReviewView.setListener(this);
        varianCourierSimulationView.setListener(this);
        latestTalkView.setListener(this);
        buttonAffiliate.setListener(this);
        imageFromBuyerView.setListener(this);
        fabWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (productData != null) {
                    presenter.processWishList(getActivity(), productData);

                    if (isFromExploreAffiliate()) {
                        String productId = productData.getInfo() != null ?
                                String.valueOf(productData.getInfo().getProductId()) :
                                "";
                        ProductPageTracking.eventClickWishlistOnAffiliate(
                                getActivity(),
                                getUserId(),
                                productId
                        );
                    }
                }
            }
        });
        fabWishlist.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("tokopedia://product/266635420/imagereview"));
                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public void showPromoWidget(PromoAttributes promoAttributes) {
        this.promoAttributes = promoAttributes;
        this.promoWidgetView.renderData(promoAttributes);
        promoContainer.setVisibility(View.VISIBLE);
        merchantVoucherListWidget.setData(null);
    }

    @Override
    protected void initialVar() {
        checkoutAnalyticsAddToCart = new CheckoutAnalyticsAddToCart(getAnalyticTracker());
        appIndexHandler = new AppIndexHandler(getActivity());
        firebaseRemoteConfig = new FirebaseRemoteConfigImpl(getActivity());
        loading = new ProgressDialog(getActivity());
        loading.setCancelable(false);
        loading.setMessage(getString(R.string.title_loading));
        if (presenter != null)
            presenter.initRetrofitInteractor();
        else
            initialPresenter();

    }

    private AnalyticTracker getAnalyticTracker() {
        if (getActivity().getApplication() instanceof AbstractionRouter) {
            return ((AbstractionRouter) getActivity().getApplication()).getAnalyticTracker();
        }
        return null;
    }

    @Override
    protected void setActionVar() {
    }

    @Override
    public void onProductDepartmentClicked(@NonNull Bundle bundle) {
        presenter.processToBrowseProduct(getActivity(), bundle);
    }

    @Override
    public void onProductCatalogClicked(@NonNull String catalogId) {
        presenter.processToCatalog(getActivity(), catalogId);
    }

    @Override
    public void onProductEtalaseClicked(@NonNull Bundle bundle) {
        presenter.processToShopInfo(getActivity(), bundle);
    }

    @Override
    public void onProductTalkClicked(@NonNull Bundle bundle) {
        Intent intent = ((TkpdInboxRouter) MainApplication.getAppContext())
                .getAskSellerIntent(getActivityContext(),
                        String.valueOf(productData.getShopInfo().getShopId()),
                        productData.getShopInfo().getShopName(),
                        productData.getInfo().getProductName(),
                        productData.getInfo().getProductUrl(),
                        TkpdInboxRouter.PRODUCT,
                        productData.getShopInfo().getShopAvatar());
        bundle.putParcelable("intent_chat", intent);
        presenter.processToTalk(getActivity(), bundle);
    }

    @Override
    public void onProductRatingClicked(String productId, String shopId, String productName) {
        presenter.processToReputation(getActivity(), productId, productName);
    }

    @Override
    public void onProductReviewClicked(String productId, String shopId, String productName) {
        presenter.processToReputation(getActivity(), productId, productName);
    }

    @Override
    public void onProductManagePromoteClicked(ProductDetailData productData) {
        presenter.requestPromoteProduct(getActivity(), productData);
    }

    @Override
    public void onBuyClick(String source) {
        if (source.equals(SOURCE_BUTTON_BUY_PDP)) {
            switch (productData.getCheckoutType()) {
                case CHECKOUT_TYPE_DEFAULT:
                    checkVariant(source);
                    break;
                case CHECKOUT_TYPE_OCS:
                    checkVariant(source);
                    break;
                case CHECKOUT_TYPE_EXPRESS:
                    goToAtcExpress();
                    break;
                default:
                    checkVariant(source);
                    break;
            }
        } else {
            checkVariant(source);
        }
    }

    private void goToAtcExpress() {
        try {
            if (getActivity() != null) {
                AtcRequestParam atcRequestParam = new AtcRequestParam();
                atcRequestParam.setShopId(Integer.parseInt(productData.getShopInfo().getShopId()));
                atcRequestParam.setProductId(Integer.parseInt(productPass.getProductId()));
                atcRequestParam.setNotes("");
                int qty = 0;
                try {
                    qty = Integer.parseInt(productData.getInfo().getProductMinOrder());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                atcRequestParam.setQuantity(qty);
                Intent intent = ((PdpRouter) getActivity().getApplicationContext())
                        .getExpressCheckoutIntent(getActivity(), atcRequestParam);
                if (intent != null) {
                    startActivityForResult(intent, REQUEST_CODE_ATC_EXPRESS);
                    getActivity().overridePendingTransition(R.anim.pull_up, 0);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void checkVariant(String source) {
        if (productData.getInfo().getHasVariant()) {
            if (!onClickBuyWhileRequestingVariant && productVariant != null) {
                openVariantPage(generateStateVariant(source));
            } else {
                onClickBuyWhileRequestingVariant = true;
                lastStateOnClickBuyWhileRequestVariant = source;
                if (source.equals(ProductDetailView.SOURCE_BUTTON_BUY_PDP)) {
                    buttonBuyView.showLoadingBuyNow();
                } else if (source.equals(ProductDetailView.SOURCE_BUTTON_CART_PDP)) {
                    buttonBuyView.showLoadingAddToCart();
                }
            }
        } else {
            openProductModalActivity(generateStateVariant(source));
        }
    }

    @Override
    public void openLoginPage() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("login", true);
        onProductBuySessionNotLogin(bundle);
    }

    private ProductCartPass createProductCartPass(String source) {
        String weightProduct = "";
        switch (productData.getInfo().getProductWeightUnit()) {
            case "gr":
                weightProduct = String.valueOf((Float.parseFloat(productData.getInfo()
                        .getProductWeight())) / 1000);
                break;
            case "kg":
                weightProduct = productData.getInfo().getProductWeight();
                break;
        }
        ProductCartPass pass = ProductCartPass.Builder.aProductCartPass()
                .setImageUri(productData.getProductImages().get(0).getImageSrc300())
                .setMinOrder(Integer.parseInt(productData.getInfo().getProductMinOrder()))
                .setProductId(String.valueOf(productData.getInfo().getProductId()))
                .setProductName(productData.getInfo().getProductName())
                .setWeight(weightProduct)
                .setShopId(productData.getShopInfo().getShopId())
                .setPrice(String.valueOf(productData.getInfo().getProductPriceUnformatted()))
                .setShopType(generateShopType(productData.getShopInfo()))
                .setListName(productPass.getTrackerListName())
                .setHomeAttribution(productPass.getTrackerAttribution())
                .setNotes(selectedRemarkNotes)
                .setOrderQuantity(selectedQuantity)
                .setSkipToCart(source.equals(SOURCE_BUTTON_BUY_VARIANT) || source.equals(SOURCE_BUTTON_BUY_PDP))
                .setSourceAtc(source)
                .setBigPromo(productData.isBigPromo())
                .build();

        if (!productData.getBreadcrumb().isEmpty()) {
            pass.setProductCategory(productData.getBreadcrumb().get(0).getDepartmentName());
            pass.setCategoryId(productData.getBreadcrumb().get(0).getDepartmentId());
            pass.setCategoryLevelName(
                    productData.getBreadcrumb()
                            .get(0)
                            .getDepartmentIdentifier()
                            .replace("_", "/")
            );
        }

        return pass;
    }

    private int generateStateProductModal(String source) {
        switch (source) {
            case ProductDetailView.SOURCE_BUTTON_BUY_PDP:
                return ConstantKey.STATE_BUTTON_BUY;
            case ProductDetailView.SOURCE_BUTTON_CART_PDP:
                return ConstantKey.STATE_BUTTON_CART;
            default:
                return ConstantKey.STATE_VARIANT_DEFAULT;
        }
    }

    @Override
    public int generateStateVariant(String source) {
        switch (source) {
            case ProductDetailView.SOURCE_BUTTON_BUY_PDP:
                return VariantActivity.STATE_BUTTON_BUY;
            case ProductDetailView.SOURCE_BUTTON_CART_PDP:
                return VariantActivity.STATE_BUTTON_CART;
            default:
                return VariantActivity.STATE_VARIANT_DEFAULT;
        }
    }

    private String generateShopType(ProductShopInfo productShopInfo) {
        if (productShopInfo.getShopIsOfficial() == 1)
            return "official_store";
        else if (productShopInfo.getShopIsGold() == 1)
            return "gold_merchant";
        else return "reguler";
    }

    @Override
    public void updateButtonBuyListener() {
        buttonBuyView.removeLoading();
        if (onClickBuyWhileRequestingVariant) {
            onClickBuyWhileRequestingVariant = false;
            onBuyClick(lastStateOnClickBuyWhileRequestVariant);
        }
    }

    public ArrayList<String> getImageURIPaths() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (ProductImage productImage : productData.getProductImages()) {
            arrayList.add(productImage.getImageSrc());
        }
        if (productData.getInfo() != null && productData.getInfo().getHasVariant()
                && productVariant != null && productVariant.getChildren() != null) {
            for (Child child : productVariant.getChildren()) {
                if (!TextUtils.isEmpty(child.getPicture().getOriginal()) && child.getProductId() != productData.getInfo().getProductId()) {
                    arrayList.add(child.getPicture().getOriginal());
                }
            }
            Set<String> imagesSet = new LinkedHashSet<>(arrayList);
            ArrayList<String> finalImage = new ArrayList<>();
            finalImage.addAll(imagesSet);
            return finalImage;
        }
        return arrayList;
    }

    @Override
    public void onImageZoomClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(PreviewProductImageDetail.FILELOC, getImageURIPaths());
        bundle.putString("product_name", MethodChecker.fromHtml(productData.getInfo().getProductName()).toString());
        bundle.putString("product_price", MethodChecker.fromHtml(productData.getInfo().getProductPrice()).toString());
        bundle.putInt(PreviewProductImageDetail.IMG_POSITION, position);
        presenter.processToPicturePreview(getActivity(), bundle);
    }

    @Override
    public void onProductManageToEtalaseClicked(int productId) {
        presenter.requestMoveToEtalase(getActivity(), productId);
    }

    @Override
    public void onProductManageEditClicked(@NonNull Bundle bundle) {
        presenter.processToEditProduct(getActivity(), bundle);
    }

    @Override
    public void onProductManageSoldOutClicked(int productId) {
        presenter.requestMoveToWarehouse(getActivity(), productId);
    }

    @Override
    public void onProductShopAvatarClicked(@NonNull Bundle bundle) {
        presenter.processToShopInfo(getActivity(), bundle);
    }

    @Override
    public void onProductOtherClicked(@NonNull ProductPass productPass) {
        interactionListener.jumpOtherProductDetail(productPass);
    }

    @Override
    public void onProductShopNameClicked(@NonNull Bundle bundle) {
        presenter.processToShopInfo(getActivity(), bundle);
    }

    @Override
    public void onProductShareClicked(@NonNull ProductDetailData data) {

        ProductData productData = new ProductData();
        productData.setPriceText(data.getInfo().getProductPrice());
        productData.setCashbacktext(data.getCashBack().getProductCashback());
        productData.setProductId(data.getInfo().getProductId().toString());
        productData.setProductName(com.tokopedia.abstraction.common.utils.view.MethodChecker.fromHtml(data.getInfo().getProductName()).toString());
        productData.setProductShareDescription(productData.getProductName());
        productData.setProductUrl(data.getInfo().getProductUrl());
        productData.setProductImageUrl(data.getProductImages().get(0).getImageSrc());
        productData.setShopUrl(data.getShopInfo().getShopUrl());
        productData.setShopName(Html.fromHtml(data.getShopInfo().getShopName()).toString());

        checkAndExecuteReferralAction(productData);

    }

    private void checkAndExecuteReferralAction(ProductData productData){
        UserSession userSession = new UserSession(getActivity());
        String fireBaseRemoteMsgGuest = firebaseRemoteConfig.getString(RemoteConfigKey.fireBaseGuestShareMsgKey, "");
        if(!TextUtils.isEmpty(fireBaseRemoteMsgGuest)) productData.setProductShareDescription(fireBaseRemoteMsgGuest);

        if(userSession.isLoggedIn() && userSession.isMsisdnVerified()) {
            String fireBaseRemoteMsg = remoteConfig.getString(RemoteConfigKey.fireBaseShareMsgKey, "");
            if (!TextUtils.isEmpty(fireBaseRemoteMsg) && fireBaseRemoteMsg.contains(ProductData.PLACEHOLDER_REFERRAL_CODE)) {
                    doReferralShareAction(productData, fireBaseRemoteMsg);
            }
            else {
                executeProductShare(productData);
            }
        }
        else {
            executeProductShare(productData);
        }
    }

    private void doReferralShareAction(ProductData productData, String fireBaseRemoteMsg){
        ActionCreator actionCreator = new ActionCreator<String, Integer>(){
            @Override
            public void actionSuccess(int actionId, String dataObj) {
                if(!TextUtils.isEmpty(dataObj) && !TextUtils.isEmpty(fireBaseRemoteMsg)) {
                    productData.setProductShareDescription(FindAndReplaceHelper.findAndReplacePlaceHolders(fireBaseRemoteMsg,
                            ProductData.PLACEHOLDER_REFERRAL_CODE, dataObj));
                    TrackingUtils.sendMoEngagePDPReferralCodeShareEvent(getActivity(), KEY_OTHER);

                }
                executeProductShare(productData);
            }

            @Override
            public void actionError(int actionId, Integer dataObj) {
                executeProductShare(productData);
            }
        };

        ((PdpRouter)(getActivity().getApplicationContext())).getDynamicShareMessage(getActivity(), actionCreator,
                (ActionUIDelegate<String, String>) getActivity());

    }


    private void executeProductShare(ProductData productData){
        ProductShare productShare = new ProductShare(getActivity(), ProductShare.MODE_TEXT);
        productShare.share(productData, ()->{
            showProgressLoading();
            return Unit.INSTANCE;
        }, () -> {
            hideProgressLoading();
            return Unit.INSTANCE;
        });

    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void shareProduct(LinkerData data) {
        interactionListener.shareProductInfo(data);
    }

    @Override
    @Deprecated
    public void onCourierClicked(@NonNull Bundle bundle) {

    }

    @Override
    public void onCourierClicked(@NonNull String productId,
                                 @Nullable ArrayList<CourierViewData> arrayList) {
        startActivity(CourierActivity.createIntent(getActivity(), productId, arrayList));
        getActivity().overridePendingTransition(0, 0);

    }

    @Override
    public void onWholesaleClicked(@NonNull Bundle bundle) {
        Intent intent = new Intent(getActivity(), WholesaleActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        getActivity().overridePendingTransition(0, 0);
    }

    public void openProductModalActivity(int state) {
        if (getActivity() != null) {
            startActivityForResult(
                    ProductModalActivity.Companion.createActivity(
                            getActivity(),
                            productVariant,
                            productData,
                            selectedQuantity,
                            state,
                            selectedRemarkNotes
                    ),
                    REQUEST_PRODUCT_MODAL
            );
            getActivity().overridePendingTransition(com.tokopedia.core2.R.anim.pull_up, 0);
        }
    }

    @Override
    public void openVariantPage(int state) {
        if (productVariant == null) {
            return;
        }
        Intent intent = new Intent(getActivity(), VariantActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(VariantActivity.KEY_VARIANT_DATA, productVariant);
        bundle.putParcelable(VariantActivity.KEY_PRODUCT_DETAIL_DATA, productData);
        intent.putExtras(bundle);
        intent.putExtra(KEY_STATE_OPEN_VARIANT, state);
        intent.putExtra(VariantActivity.KEY_SELECTED_QUANTIY, selectedQuantity);
        intent.putExtra(VariantActivity.KEY_REMARK_FOR_SELLER, selectedRemarkNotes);
        intent.putExtra(KEY_LEVEL1_SELECTED, variantLevel1);
        intent.putExtra(KEY_LEVEL2_SELECTED, variantLevel2);
        if (productData.getShopInfo().getShopIsOwner() == 1
                || (productData.getShopInfo().getShopIsAllowManage() == 1
                || GlobalConfig.isSellerApp())) {
            intent.putExtra(VariantActivity.KEY_SELLER_MODE, true);
        }
        startActivityForResult(intent, REQUEST_VARIANT);
        getActivity().overridePendingTransition(com.tokopedia.core2.R.anim.pull_up, 0);
        if (productData.getInfo().getHasVariant()
                && productVariant != null
                && variantLevel1 != null) {
            UnifyTracking.eventClickVariant(getActivity(), generateVariantString());
        }

    }

    @Override
    public void onDescriptionClicked(Intent intent) {
        intent.setClass(getActivityContext(), DescriptionActivityNew.class);
        startActivity(intent);
        getActivity().overridePendingTransition(com.tokopedia.core2.R.anim.pull_up, 0);
    }

    @Override
    public void onProductInfoShortClicked(Intent intent){
        intent.setClass(getActivityContext(), ProductInfoShortDetailActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(com.tokopedia.core2.R.anim.pull_up, 0);
    }

    @Override
    public void onInstallmentClicked(@NonNull Bundle bundle) {
        Intent intent = new Intent(getActivity(), InstallmentActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        getActivity().overridePendingTransition(com.tokopedia.core2.R.anim.pull_up, 0);
    }

    @Override
    public void onDescriptionClicked(@NonNull Bundle bundle) {

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
        presenter.processToCreateShop(getActivity());
    }

    @Override
    public void onProductBuySessionLogin(@NonNull ProductCartPass data, String source) {
        if (source.equals(ProductDetailView.SOURCE_BUTTON_CART_PDP) || source.equals(ProductDetailView.SOURCE_BUTTON_CART_VARIANT)) {
            buttonBuyView.showLoadingAddToCart();
        } else if (source.equals(ProductDetailView.SOURCE_BUTTON_BUY_PDP) || source.equals(ProductDetailView.SOURCE_BUTTON_BUY_VARIANT)) {
            buttonBuyView.showLoadingBuyNow();
        }
        presenter.processToCart(getActivity(), data);
    }

    @Override
    public void onProductBuySessionNotLogin(@NonNull Bundle bundle) {
        presenter.processToLogin(getActivity(), bundle);
    }

    @Override
    public void renderTempProductData(ProductPass productPass) {
        this.headerInfoView.renderTempData(productPass);
        this.pictureView.renderTempData(productPass);
        this.ratingTalkDescriptionView.renderTempdata(productPass);
        if (productPass.isWishlist()) {
            fabWishlist.setImageDrawable(getResources().getDrawable(R.drawable.ic_wishlist_red_pdp));
        } else {
            fabWishlist.setImageDrawable(getResources().getDrawable(R.drawable.ic_wishlist_pdp));
        }
        fabWishlist.setVisibility(View.VISIBLE);
    }

    @Override
    public void onByMeClicked(AffiliateInfoViewModel affiliate, boolean isRegularPdp) {
        if (getActivity() != null) {
            if (isRegularPdp) {
                String shopId = productData.getShopInfo() != null ?
                        productData.getShopInfo().getShopId() :
                        "";
                ProductPageTracking.eventClickAffiliateRegularPdp(
                        getActivityContext(),
                        getUserId(),
                        shopId,
                        String.valueOf(affiliate.getProductId())
                );
            } else {
                ProductPageTracking.eventClickAffiliate(
                        getActivityContext(),
                        getUserId(),
                        String.valueOf(affiliate.getProductId())
                );
            }
            if (userSession.isLoggedIn()) {
                RouteManager.route(
                        getActivity(),
                        ApplinkConst.AFFILIATE_CREATE_POST
                                .replace(PRODUCT_ID, String.valueOf(affiliate.getProductId()))
                                .replace(AD_ID, String.valueOf(affiliate.getAdId()))
                );
            } else {
                Bundle bundle = new Bundle();
                bundle.putBoolean("login", true);
                presenter.processToLogin(getActivity(), bundle);
            }
        }
    }

    @Override
    public void renderAffiliateButton(AffiliateInfoViewModel affiliate) {
        if (isFromExploreAffiliate()) {
            buttonAffiliate.renderView(affiliate);
            buttonBuyView.setBuyNowLabelFull(true);
        } else {
            if (affiliate != null) {
                buttonBuyView.showByMeButton(true);
                buttonBuyView.setByMeButtonListener(affiliate);
                buttonBuyView.setBuyNowLabelFull(false);
            } else {
                buttonBuyView.setBuyNowLabelFull(true);
            }
        }
    }

    @Override
    public boolean isFromExploreAffiliate() {
        return productPass.isFromExploreAffiliate();
    }

    @Override
    public void showErrorAffiliate(String message) {
        if (getActivity() != null && isFromExploreAffiliate()) {
            if (TextUtils.isEmpty(message))
                message = getActivity().getString(R.string.error_no_connection2);
            Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG)
                    .setAction(
                            getString(R.string.title_try_again),
                            view -> presenter.requestAffiliateProductData(productData)
                    )
                    .show();
        }
    }

    @Override
    public void onImageReviewLoaded(List<ImageReviewItem> data) {
        imageFromBuyerView.renderData(data);
        imageFromBuyerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onWishlistCountLoaded(@NonNull String wishlistCountText) {
        productInfoAttributeView.renderWishlistCount(wishlistCountText);
    }

    @Override
    public void onProductDetailLoaded(@NonNull ProductDetailData successResult, ProductViewData viewData) {
        presenter.processGetGTMTicker();
        isCodShown = (!(successResult.getCampaign() != null && successResult.getCampaign().getActive())) && successResult.getInfo().isCod();
        labelCod.setVisibility(isCodShown ? View.VISIBLE : View.GONE);
        float weight = 0f;
        try {
            weight = getUnformattedWeight(successResult.getInfo().getProductWeight());
        } catch (Exception e) {
        }

        if ("gr".equalsIgnoreCase(successResult.getInfo().getProductWeightUnit())) {
            weight /= 1000;
        }

        presenter.getCostEstimation(getActivity(),
                weight, successResult.getShopInfo().getShopDomain());

        this.productData = successResult;
        this.viewData = viewData;
        this.headerInfoView.renderData(successResult);
        this.headerInfoView.renderProductCampaign(successResult);
        this.pictureView.renderData(successResult);
        if (!isFromExploreAffiliate()) {
            this.buttonBuyView.renderData(successResult);
        }
        this.productInfoShortView.renderProductData(productData);
        this.wholesaleInstallmentView.renderProductData(productData);
        this.varianCourierSimulationView.setProductDetailData(productData);
        this.productInfoAttributeView.renderData(successResult);
        this.ratingTalkDescriptionView.renderData(successResult, viewData);
        this.lastUpdateView.renderData(successResult);
        this.shopInfoView.renderData(successResult);
        this.otherProductsView.renderData(successResult);
        this.newShopView.renderData(successResult);
        this.videoDescriptionLayout.renderData(successResult);
        this.interactionListener.onProductDetailLoaded(successResult);
        this.presenter.sendAnalytics(successResult);
        this.presenter.sendAppsFlyerData(getActivity(), successResult, AFInAppEventType.CONTENT_VIEW);
        this.presenter.startIndexingApp(appIndexHandler, successResult);
        this.refreshMenu();
        this.updateWishListStatus(productData.getInfo().getProductAlreadyWishlist());
        try {
            this.selectedQuantity = Integer.parseInt(this.productData.getInfo().getProductMinOrder());
        } catch (NumberFormatException e) {
            this.selectedQuantity = 1;
        }
        if (isAllowShowCaseNcf()) {
            startShowCase();
        }
        renderTopAds(5, successResult);
    }

    private float getUnformattedWeight(String productWeight) {
        String unformatted = productWeight.replace(".", "");
        return Float.parseFloat(unformatted);
    }

    private boolean isAllowShowCaseNcf() {
        return buttonBuyView.getVisibility() == View.VISIBLE
                && buttonBuyView.containerNewButtonBuy.getVisibility() == View.VISIBLE;
    }

    @Override
    public void onOtherProductLoaded(List<ProductOther> productOthers) {
        this.productOthers = productOthers;
        otherProductsView.renderOtherProduct(productOthers);
    }

    @Override
    public void onProductShopMessageClicked(@NonNull Intent intent) {
        presenter.processToSendMessage(getActivity(), intent);
    }

    @Override
    public void onProductHasEdited() {
        presenter.requestProductDetail(getActivity(), productPass, RE_REQUEST, true, useVariant);
    }

    @Override
    public void onProductTalkUpdated() {
        presenter.requestProductDetail(getActivity(), productPass, RE_REQUEST, true, useVariant);
    }

    @Override
    public void onShopFavoriteUpdated(int statFave) {
        shopInfoView.reverseFavorite();
    }

    @Override
    public void onProductShopFaveClicked(String shopId, Integer productId) {
        presenter.requestFaveShop(getActivity(), shopId, productId);
    }

    @Override
    public void onProductShopRatingClicked(Bundle bundle) {
        presenter.processToShopInfo(getActivity(), bundle);
    }

    @Override
    public void finishLoadingWishList() {
        if (getActivity() != null && getView() != null) {
            loading.dismiss();
        }
    }

    @Override
    public void loadingWishList() {
        loading.show();
    }

    @Override
    public void updateWishListStatus(int status) {
        this.productData.getInfo().setProductAlreadyWishlist(status);
        if (productData.getShopInfo().getShopIsAllowManage() == 1) {
            fabWishlist.setImageDrawable(getResources().getDrawable(R.drawable.icon_wishlist_plain));
            if (!productData.getInfo().getProductStatus().equals(PRD_STATE_PENDING)) {
                fabWishlist.setOnClickListener(new EditClick(productData));
            } else {
                fabWishlist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showToastMessage(productData.getInfo().getProductStatusTitle());
                    }
                });
            }

        } else if (status == 1) {
            fabWishlist.setImageDrawable(getResources().getDrawable(R.drawable.ic_wishlist_red_pdp));
        } else {
            fabWishlist.setImageDrawable(getResources().getDrawable(R.drawable.ic_wishlist_pdp));
        }
        fabWishlist.setVisibility(View.VISIBLE);
        updateWishlistStatusVariant(status);

        Intent resultIntent = new Intent();
        resultIntent.putExtra(WISHLIST_STATUS_UPDATED_POSITION,
                getActivity().getIntent().getIntExtra(WISHLIST_STATUS_UPDATED_POSITION, -1));
        resultIntent.putExtra(WIHSLIST_STATUS_IS_WISHLIST, status == STATUS_IN_WISHLIST);
        resultIntent.putExtra(EXTRA_PRODUCT_ID, String.valueOf(productData.getInfo().getProductId()));
        getActivity().setResult(Activity.RESULT_CANCELED, resultIntent);

    }

    public void updateWishlistStatusVariant(int status) {
        if (productVariant != null && productVariant.getChildren() != null) {
            for (Child child : productVariant.getChildren()) {
                if (child.getProductId() == productData.getInfo().getProductId()) {
                    child.setWishlist(status == 1 ? true : false);
                    break;
                }
            }
        }
    }

    @Override
    public void loadVideo(VideoData data) {
        this.videoDescriptionLayout.renderVideoData(data, youTubeThumbnailLoadInProcessListener);
        this.ratingTalkDescriptionView.setVideoData(data, youTubeThumbnailLoadInProcessListener);
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
    public void showErrorVariant() {
        Snackbar snack = Snackbar.make(coordinatorLayout, getString(R.string.error_variant), Snackbar.LENGTH_INDEFINITE);
        TextView tv = snack.getView().findViewById(com.tokopedia.core2.R.id.snackbar_text);
        tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_54));
        tv.setMaxLines(5);

        Button snackBarAction = snack.getView().findViewById(android.support.design.R.id.snackbar_action);
        snackBarAction.setTextColor(ContextCompat.getColor(coordinatorLayout.getContext(), R.color.black_70));
        snackBarAction.setAllCaps(false);

        snack.getView().setBackground(getResources().getDrawable(R.drawable.bg_snackbar_variant));
        snack.setAction(getString(R.string.title_retry), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.requestProductDetail(getActivity(), productPass, INIT_REQUEST, false, useVariant);
            }
        });
        snack.show();
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
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
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
            String uri = pass != null ? pass.getProductUri() : "";
            if (!GlobalConfig.DEBUG) {
                Crashlytics.logException(new ProductNotFoundException(uri));
            }
            if (webViewHandleListener != null) {
                webViewHandleListener.catchToWebView(uri);
            }
        } else {
            showToastMessage("Produk tidak ditemukan!");
            closeView();
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof DeepLinkWebViewHandleListener) {
            webViewHandleListener = (DeepLinkWebViewHandleListener) activity;
        } else {
            throw new RuntimeException("Activity must implement DeepLinkWebViewHandleListener");
        }
        if (getActivity() instanceof YoutubeThumbnailViewHolder.YouTubeThumbnailLoadInProcess)
            youTubeThumbnailLoadInProcessListener = (YoutubeThumbnailViewHolder.YouTubeThumbnailLoadInProcess) getActivity();
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        if (activity instanceof DeepLinkWebViewHandleListener) {
            webViewHandleListener = (DeepLinkWebViewHandleListener) activity;
        } else {
            throw new RuntimeException("Activity must implement DeepLinkWebViewHandleListener");
        }
        if (getActivity() instanceof YoutubeThumbnailViewHolder.YouTubeThumbnailLoadInProcess)
            youTubeThumbnailLoadInProcessListener = (YoutubeThumbnailViewHolder.YouTubeThumbnailLoadInProcess) getActivity();
    }

    @Override
    public void showReportDialog() {
        fragment = ReportProductDialogFragment.createInstance(productData, recentBundle);
        fragment.setListener(this);
        fragment.show(getFragmentManager(), "ReportProductDialogFragment");
    }

    @Override
    public void onProductReportClicked() {
        presenter.reportProduct(getActivity());
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
        performanceMonitoring.stopTrace();
    }

    @Override
    public void showToastMessage(String message) {
        buttonBuyView.removeLoading();
        if (message == null) {
            message = getString(R.string.default_request_error_unknown_short);
        }
        Snackbar snackbar = Snackbar.make(coordinatorLayout,
                message.replace("\n", " "),
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
        presenter.onDestroyView(getActivity());
        voucherListPresenter.detachView();
        destroyVideoLayout();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!isAppBarCollapsed)
            inflater.inflate(R.menu.product_detail, menu);
        else
            inflater.inflate(R.menu.product_detail_dark, menu);

        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (getActivity() != null) {
            presenter.prepareOptionMenu(menu, getActivity(), productData);
            int cartCount = ((PdpRouter) getActivity().getApplicationContext()).getCartCount(getActivityContext());
            setDrawableCount(getContext(), cartCount);
        }
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {
        if (productData != null) {
            onProductDetailLoaded(productData, viewData);
        } else {
            presenter.processDataPass(productPass);
            presenter.requestProductDetail(getActivity(), productPass, INIT_REQUEST, false, useVariant);
        }
    }

    @Override
    public void onSaveState(Bundle outState) {
        presenter.saveStateProductDetail(outState, STATE_DETAIL_PRODUCT, productData);
        presenter.saveStateProductVariant(outState, STATE_PRODUCT_VARIANT, productVariant);
        presenter.saveStateProductStockNonVariant(outState, STATE_PRODUCT_STOCK_NON_VARIANT, productStockNonVariant);
        presenter.saveStateProductOthers(outState, STATE_OTHER_PRODUCTS, productOthers);
        presenter.saveStateVideoData(outState, STATE_VIDEO, videoData);
        presenter.saveStatePromoWidget(outState, STATE_PROMO_WIDGET, promoAttributes);
        presenter.saveStateAppBarCollapsed(outState, STATE_APP_BAR_COLLAPSED, isAppBarCollapsed);
    }

    @Override
    public void onRestoreState(Bundle savedInstanceState) {
        presenter.processStateData(savedInstanceState, getActivity());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        } else if (i == R.id.action_share) {
            if (productData != null) {
                onProductShareClicked(productData);
            }
            return true;
        } else if (i == R.id.action_cart) {
            if (!SessionHandler.isV4Login(getActivity())) {
                Intent intent = ((PdpRouter) MainApplication.getAppContext()).getLoginIntent
                        (getActivity());
                navigateToActivityRequest(intent, ProductDetailFragment.REQUEST_CODE_LOGIN);
                if (productData.getInfo().getHasVariant())
                    UnifyTracking.eventClickCartVariant(getActivity(), generateVariantString());
            } else {
                startActivity(TransactionCartRouter.createInstanceCartActivity(getActivity()));
                if (productData.getInfo().getHasVariant())
                    UnifyTracking.eventClickCartVariant(getActivity(), generateVariantString());
            }
            return true;
        } else if (i == R.id.action_report) {
            onProductReportClicked();
            return true;
        } else if (i == R.id.action_warehouse) {
            presenter.requestMoveToWarehouse(getActivity(), productData.getInfo().getProductId());
            return true;
        } else if (i == R.id.action_etalase) {
            presenter.requestMoveToEtalase(getActivity(), productData.getInfo().getProductId());
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                if (SessionHandler.isV4Login(getActivity()))
                    presenter.requestProductDetail(getActivity(), productPass, RE_REQUEST, true, useVariant);
                break;
            case REQUEST_PRODUCT_MODAL:
                if (resultCode == RESULT_OK) {
                    selectedQuantity = data.getIntExtra(ConstantKey.ARGS_RESULT_QUANTITY_PDP_MODAL, 1);
                    selectedRemarkNotes = data.getStringExtra(ConstantKey.ARGS_RESULT_REMARK_PDP_MODAL);
                    switch (data.getIntExtra(ARGS_STATE_RESULT_PDP_MODAL, 0)) {
                        case ConstantKey.SELECTED_VARIANT_RESULT_SKIP_TO_CART:
                            if (getActivity() != null && SessionHandler.isV4Login(getActivity())) {
                                onProductBuySessionLogin(createProductCartPass(SOURCE_BUTTON_BUY_PDP),
                                        SOURCE_BUTTON_BUY_PDP);
                            }
                            break;
                        case ConstantKey.SELECTED_VARIANT_RESULT_STAY_IN_PDP:
                            if (getActivity() != null && SessionHandler.isV4Login(getActivity())) {
                                onProductBuySessionLogin(createProductCartPass(SOURCE_BUTTON_CART_PDP),
                                        SOURCE_BUTTON_CART_PDP);
                            }
                            break;
                        case ConstantKey.KILL_PDP_BACKGROUND:
                            if (getActivity() != null) {
                                getActivity().finish();
                            }
                            break;
                        default:
                            break;
                    }
                } else if (resultCode == RESULT_CANCELED) {

                }
                break;
            case REQUEST_VARIANT:
                if (resultCode == RESULT_OK) {
                    if (data.getParcelableExtra(KEY_PRODUCT_DETAIL_DATA) != null) {
                        productData = data.getParcelableExtra(KEY_PRODUCT_DETAIL_DATA);
                    }
                    if (productVariant == null && data.getParcelableExtra(KEY_VARIANT_DATA) != null) {
                        productVariant = data.getParcelableExtra(KEY_VARIANT_DATA);
                    }
                    if (data != null && data.getParcelableExtra(KEY_LEVEL1_SELECTED) != null && data.getParcelableExtra(KEY_LEVEL1_SELECTED) instanceof Option) {
                        variantLevel1 = data.getParcelableExtra(KEY_LEVEL1_SELECTED);
                        if (data.getParcelableExtra(KEY_LEVEL2_SELECTED) != null && data.getParcelableExtra(KEY_LEVEL2_SELECTED) instanceof Option) {
                            variantLevel2 = data.getParcelableExtra(KEY_LEVEL2_SELECTED);
                        }
                        varianCourierSimulationView.updateVariant(generateVariantString());
                        if (productVariant != null) {
                            pictureView.renderData(productData);
                            headerInfoView.renderData(productData);
                            headerInfoView.renderProductCampaign(productData);
                            headerInfoView.renderStockAvailability(productData.getCampaign().getActive(),
                                    productData.getInfo());
                            shopInfoView.renderData(productData);
                            presenter.updateRecentView(getActivity(), productData.getInfo().getProductId());
                            ratingTalkDescriptionView.renderData(productData, viewData);
                            latestTalkView.renderData(productData);
                            buttonBuyView.updateButtonForVariantProduct(productVariant.getChildFromProductId(
                                    productData.getInfo().getProductId()).isIsBuyable(), productData);
                            updateWishListStatus(productData.getInfo().getProductAlreadyWishlist());
                            productPass.setProductId(Integer.toString(productData.getInfo().getProductId()));
                            selectedQuantity = data.getIntExtra(KEY_SELECTED_QUANTIY, 1);
                            selectedRemarkNotes = data.getStringExtra(KEY_REMARK_FOR_SELLER);
                        }

                        switch (data.getIntExtra(KEY_STATE_RESULT_VARIANT, 0)) {
                            case SELECTED_VARIANT_RESULT_SKIP_TO_CART:
                                if (SessionHandler.isV4Login(getActivity())) {
                                    onProductBuySessionLogin(createProductCartPass(SOURCE_BUTTON_BUY_VARIANT),
                                            SOURCE_BUTTON_BUY_VARIANT);
                                } else {
                                    ProductPageTracking.eventClickBuyInVariantNotLogin(
                                            getActivity(),
                                            String.valueOf(productData.getInfo().getProductId())
                                    );
                                    openLoginPage();
                                }
                                break;
                            case SELECTED_VARIANT_RESULT_STAY_IN_PDP:
                                if (SessionHandler.isV4Login(getActivity())) {
                                    onProductBuySessionLogin(createProductCartPass(SOURCE_BUTTON_CART_VARIANT),
                                            SOURCE_BUTTON_CART_VARIANT);
                                } else {
                                    ProductPageTracking.eventClickAtcInVariantNotLogin(
                                            getActivity(),
                                            String.valueOf(productData.getInfo().getProductId())
                                    );
                                    openLoginPage();
                                }
                                break;
                            case VariantActivity.KILL_PDP_BACKGROUND:
                                getActivity().finish();
                                break;
                            default:
                                break;
                        }
                    }
                }
            case REQUEST_CODE_LOGIN_USE_VOUCHER:
            case REQUEST_CODE_MERCHANT_VOUCHER:
            case REQUEST_CODE_MERCHANT_VOUCHER_DETAIL: {
                if (resultCode == Activity.RESULT_OK) {
                    needLoadVoucher = true;
                }
            }
            break;
            case REQUEST_CODE_ATC_EXPRESS:
                if (resultCode == RESULT_CODE_ERROR) {
                    String message = data.getStringExtra(EXTRA_MESSAGES_ERROR);
                    if (message != null && message.length() > 0) {
                        ToasterError.make(getView(), data.getStringExtra(EXTRA_MESSAGES_ERROR), BaseToaster.LENGTH_SHORT)
                                .show();
                    } else {
                        if (getActivity() != null && errorBottomsheets != null) {
                            errorBottomsheets.setData(
                                    getActivity().getString(R.string.bottomsheet_title_global_error),
                                    getActivity().getString(R.string.bottomsheet_message_global_error),
                                    getActivity().getString(R.string.bottomsheet_action_global_error),
                                    true
                            );
                            errorBottomsheets.setActionListener(new ErrorBottomsheetsActionListenerWithRetry() {
                                @Override
                                public void onActionButtonClicked() {
                                    errorBottomsheets.dismiss();
                                    checkVariant(SOURCE_BUTTON_BUY_PDP);
                                }

                                @Override
                                public void onRetryClicked() {
                                    errorBottomsheets.dismiss();
                                    goToAtcExpress();
                                }
                            });
                            errorBottomsheets.show(getFragmentManager(), "");
                        }
                    }
                } else if (resultCode == RESULT_CODE_NAVIGATE_TO_OCS) {
                    checkVariant(ProductDetailView.SOURCE_BUTTON_BUY_PDP);
                } else if (resultCode == RESULT_CODE_NAVIGATE_TO_NCF) {
                    checkVariant(ProductDetailView.SOURCE_BUTTON_BUY_PDP);
                }
                break;
            default:
                break;
        }
    }

    public String generateVariantString() {
        String variantText = variantLevel1 != null ? variantLevel1.getValue() : "";
        if (variantLevel2 != null && variantLevel2 instanceof Option) {
            variantText += (", " + ((Option) variantLevel2).getValue());
        }
        return variantText;
    }

    @Override
    public void onSuccessGetMerchantVoucherList(@NotNull ArrayList<MerchantVoucherViewModel> merchantVoucherViewModelList) {
        if (merchantVoucherViewModelList.size() == 0) {
            merchantVoucherListWidget.setData(null);
            promoWidgetView.setVisibility(View.GONE);
            promoContainer.setVisibility(View.GONE);
            return;
        }
        if (getActivity() != null && productData != null &&
                !voucherListPresenter.isMyShop(productData.getShopInfo().getShopId())) {
            ProductPageTracking.eventImpressionMerchantVoucherUse(getActivity(), merchantVoucherViewModelList);
        }
        merchantVoucherListWidget.setData(merchantVoucherViewModelList);
        promoWidgetView.setVisibility(View.GONE);
        promoContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onErrorGetMerchantVoucherList(@NotNull Throwable e) {
        merchantVoucherListWidget.setData(null);
        promoWidgetView.setVisibility(View.GONE);
        promoContainer.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (productData != null) {
            presenter.startIndexingApp(appIndexHandler, productData);
            this.newShopView.renderData(productData);
            refreshMenu();
            updateWishListStatus(productData.getInfo().getProductAlreadyWishlist());
        }
        if (needLoadVoucher) {
            voucherListPresenter.clearCache();
            loadPromo();
            needLoadVoucher = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.stopIndexingApp(appIndexHandler);
        destroyVideoLayout();
    }

    @SuppressWarnings("Range")
    public void onSuccessAction(Bundle resultData, int resultCode) {
        if (fragment != null) fragment.dismiss();
        recentBundle = null;
        SnackbarManager.make(getActivity(),
                resultData.getString(ProductInfoIntentService.EXTRA_RESULT),
                Snackbar.LENGTH_LONG).show();
    }

    @SuppressWarnings("Range")
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
                presenter.requestProductDetail(getActivity(), productPass, INIT_REQUEST, false, useVariant);
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
    public void moveToEditFragment(boolean isEdit) {
        if (getActivity().getApplication() instanceof TkpdCoreRouter) {
            Integer productId = productData.getInfo().getProductId();
            if (productData != null && productData.getInfo().getHasVariant() && productVariant != null) {
                productId = productVariant.getParentId();
            }
            Intent intent = ((TkpdCoreRouter) getActivity().getApplication()).goToEditProduct(getActivity(), isEdit, Integer.toString(productId));
            navigateToActivityRequest(intent, ProductDetailFragment.REQUEST_CODE_EDIT_PRODUCT);
        }
    }

    @Override
    public void showSuccessWishlistSnackBar() {
        Snackbar.make(coordinatorLayout, getActivity().getString(R.string.msg_add_wishlist), Snackbar.LENGTH_LONG)
                .setAction(getActivity().getString(R.string.go_to_wishlist), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isAdded() || getActivity() == null) {
                            return;
                        }
                        if (GlobalConfig.isSellerApp()) {
                            return;
                        }
                        Intent intent = new Intent(getActivity(), SimpleHomeRouter.getSimpleHomeActivityClass());
                        intent.putExtra(
                                SimpleHomeRouter.FRAGMENT_TYPE,
                                SimpleHomeRouter.WISHLIST_FRAGMENT);

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        navigateToActivity(intent);
                        closeView();
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.tkpd_main_green))
                .show();
    }

    @Override
    public void showDinkSuccess(String productName) {
        Intent intent = new Intent(getActivity(), DinkSuccessActivity.class);
        intent.putExtra(DinkSuccessActivity.EXTRA_PRODUCT, productName);
        startActivity(intent);
    }

    @Override
    public void showDinkFailed(String productName, String expired) {
        Intent intent = new Intent(getActivity(), DinkFailedActivity.class);
        intent.putExtra(DinkFailedActivity.EXTRA_PRODUCT, productName);
        intent.putExtra(DinkFailedActivity.EXTRA_TIME_EXP, expired);
        startActivity(intent);
    }

    @Override
    public void onPromoAdsClicked() {
        ((PdpRouter) getActivity().getApplication()).goToCreateTopadsPromo(getActivity(),
                String.valueOf(productData.getInfo().getProductId()), productData.getShopInfo().getShopId(),
                GlobalConfig.isSellerApp() ? TopAdsSourceOption.SA_PDP :
                        TopAdsSourceOption.MA_PDP);
    }

    @Override
    public void onPromoWidgetCopied() {
        showSnackBarClose(getActivity().getString(R.string.title_copied));
    }

    public void showSnackBarClose(String stringToShow) {
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, stringToShow,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(getActivity().getString(R.string.close), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.show();
    }

    @Override
    public void showProductCampaign() {
        if (headerInfoView != null && productData != null) {
            headerInfoView.renderProductCampaign(productData);
        }
    }

    @Override
    public void addProductVariant(ProductVariant productVariant) {
        if (productData != null) {
            this.productVariant = productVariant;
            this.varianCourierSimulationView.addProductVariant(productVariant, productData);
            if (variantLevel1 != null && variantLevel1 instanceof Option) {
                varianCourierSimulationView.updateVariant(generateVariantString());
            }
            int defaultChild = productVariant.getParentId() == productData.getInfo().getProductId()
                    ? productVariant.getDefaultChild() : productData.getInfo().getProductId();
            if (productVariant.getChildFromProductId(defaultChild).isEnabled()) {
                productData.getInfo().setProductStockWording(productVariant.getChildFromProductId(defaultChild).getStockWordingHtml());
                productData.getInfo().setLimitedStock(productVariant.getChildFromProductId(defaultChild).isLimitedStock());
                headerInfoView.renderStockAvailability(productData.getCampaign().getActive(),
                        productData.getInfo());
            }

            buttonBuyView.updateButtonForVariantProduct(productVariant.getChildFromProductId(
                    defaultChild).isIsBuyable(), productData);
        }
    }

    @Override
    public void setVariantFalse() {
        useVariant = false;
        productData.getInfo().setHasVariant(false);
    }

    @Override
    public void addProductStock(Child productStock) {
        productStockNonVariant = productStock;
        if (productData != null && productData.getInfo() != null && productStock.isEnabled()) {
            productData.getInfo().setProductStockWording(productStockNonVariant.getStockWordingHtml());
            productData.getInfo().setLimitedStock(productStockNonVariant.isLimitedStock());
            productData.getInfo().setAlwaysAvailable(productStock.isAlwaysAvailable());
            headerInfoView.renderStockAvailability(productData.getCampaign().getActive(),
                    productData.getInfo());
        }
    }

    @Override
    public void showMostHelpfulReview(List<Review> reviews) {
        this.productData.setReviewList(reviews);
        this.mostHelpfulReviewView.renderData(this.productData);
    }

    @Override
    public void actionSuccessAddToWishlist(Integer productId) {
        if (getActivity().getApplication() instanceof IReactNativeRouter) {
            IReactNativeRouter reactNativeRouter = (IReactNativeRouter) getActivity().getApplication();
            reactNativeRouter.sendAddWishlistEmitter(String.valueOf(productId), SessionHandler.getLoginID(getActivity()));
        }
    }

    @Override
    public void actionSuccessRemoveFromWishlist(Integer productId) {
        if (getActivity().getApplication() instanceof IReactNativeRouter) {
            IReactNativeRouter reactNativeRouter = (IReactNativeRouter) getActivity().getApplication();
            reactNativeRouter.sendRemoveWishlistEmitter(String.valueOf(productId), SessionHandler.getLoginID(getActivity()));
        }
    }

    @Override
    public void actionSuccessAddFavoriteShop(String shopId) {
        if (productData.getShopInfo().getShopAlreadyFavorited() == 1) {
            productData.getShopInfo().setShopAlreadyFavorited(0);
            if (getActivity().getApplication() instanceof IReactNativeRouter) {
                IReactNativeRouter reactNativeRouter = (IReactNativeRouter) getActivity().getApplication();
                reactNativeRouter.sendRemoveFavoriteEmitter(String.valueOf(shopId), SessionHandler.getLoginID(getActivity()));
            }
        } else {
            productData.getShopInfo().setShopAlreadyFavorited(1);
            if (getActivity().getApplication() instanceof IReactNativeRouter) {
                IReactNativeRouter reactNativeRouter = (IReactNativeRouter) getActivity().getApplication();
                reactNativeRouter.sendAddFavoriteEmitter(String.valueOf(shopId), SessionHandler.getLoginID(getActivity()));
            }
        }
    }

    private void destroyVideoLayout() {
        if (videoDescriptionLayout != null && videoDescriptionLayout.isShown()) {
            videoDescriptionLayout.destroyVideoLayoutProcess();
        }
    }

    private AppBarLayout.OnOffsetChangedListener onAppbarOffsetChange() {
        return new AppBarLayout.OnOffsetChangedListener() {
            int intColor = 0;
            int stateCollapsing = FROM_COLLAPSED;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                intColor = -verticalOffset;
                if (intColor >= SCROLL_ELEVATION + toolbar.getHeight() && isAdded() && stateCollapsing == FROM_EXPANDED) {
                    initToolbarLight();
                    initStatusBarLight();
                    fabWishlist.hide();
                    labelCod.setVisibility(View.GONE);
                    stateCollapsing = FROM_COLLAPSED;
                } else if (intColor < SCROLL_ELEVATION + toolbar.getHeight() && isAdded() && stateCollapsing == FROM_COLLAPSED) {
                    initStatusBarDark();
                    initToolbarTransparant();
                    if (productData != null && productData.getInfo().getProductAlreadyWishlist() != null) {
                        fabWishlist.show();
                    }
                    labelCod.setVisibility(isCodShown? View.VISIBLE : View.GONE);
                    stateCollapsing = FROM_EXPANDED;
                }
            }
        };
    }

    private void initToolbarLight() {
        if (isAdded()) {
            collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(getActivity(), R.color.grey_toolbar_icon));
            collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
            toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(), R.color.grey_toolbar_icon));
            toolbar.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_pdp_dark);
            if (menu != null && menu.size() > 2) {
                menu.findItem(R.id.action_share).setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_share_pdp_dark));
                int cartCount = ((PdpRouter) getActivity().getApplicationContext()).getCartCount(getActivityContext());
                menu.findItem(R.id.action_cart).setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_cart_counter_dark_pdp));
                if (cartCount > 0) {
                    setDrawableCount(getContext(), cartCount);
                }
            }
            toolbar.setOverflowIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_overflow_pdp_dark));
        }
    }

    private void initToolbarTransparant() {
        if (isAdded()) {
            collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
            toolbar.setBackgroundColor(Color.TRANSPARENT);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_pdp_light);
            if (menu != null && menu.size() > 1) {
                menu.findItem(R.id.action_share).setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_share_pdp_light));
                int cartCount = ((PdpRouter) getActivity().getApplicationContext()).getCartCount(getActivityContext());
                menu.findItem(R.id.action_cart).setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_cart_counter_light_pdp));
                if (cartCount > 0) {
                    setDrawableCount(getContext(), cartCount);
                }
            }
            toolbar.setOverflowIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_overflow_pdp_light));
        }
    }

    private void setCartBadge(View view, int count) {
        BadgeView badgeView = new BadgeView(getContext());
        badgeView.bindTarget(view);
        badgeView.setGravityOffset(-10, 3, true);
        badgeView.setBadgeGravity(Gravity.TOP | Gravity.END);
        badgeView.setBadgeNumber(count);
    }

    private void initStatusBarDark() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && getWindowValidation() && isAdded()) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private boolean getWindowValidation() {
        return getActivity() != null && getActivity().getWindow() != null;
    }

    private void initStatusBarLight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && getWindowValidation() && isAdded()) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.green_600));
        }
    }

    @Override
    public void onErrorAddWishList(String errorMessage, String productId) {
        finishLoadingWishList();
        showWishListRetry(errorMessage);
    }

    @Override
    public void onSuccessAddWishlist(String productId) {
        finishLoadingWishList();
        showSuccessWishlistSnackBar();
        updateWishListStatus(1);
        actionSuccessAddToWishlist(Integer.parseInt(productId));
        cacheInteractor.deleteProductDetail(Integer.parseInt(productId));
    }

    @Override
    public void onErrorRemoveWishlist(String errorMessage, String productId) {
        finishLoadingWishList();
        showWishListRetry(errorMessage);
    }

    @Override
    public void onSuccessRemoveWishlist(String productId) {
        finishLoadingWishList();
        showToastMessage(getString(R.string.msg_remove_wishlist));
        updateWishListStatus(ProductDetailFragment.STATUS_NOT_WISHLIST);
        actionSuccessRemoveFromWishlist(Integer.parseInt(productId));
        cacheInteractor.deleteProductDetail(Integer.parseInt(productId));
    }

    @Override
    public void onSuccessGetShopInfo(@NotNull ShopInfo shopInfo) {
        //no op
    }

    @Override
    public void onErrorGetShopInfo(@NotNull Throwable e) {
        //no op
    }

    @Override
    public void onSuccessUseVoucher(UseMerchantVoucherQueryResult useMerchantVoucherQueryResult) {
        hideUseMerchantVoucherLoading();
        if (getActivity() != null) {
            com.tokopedia.design.component.Dialog dialog = new com.tokopedia.design.component.Dialog(getActivity(),
                    com.tokopedia.design.component.Dialog.Type.PROMINANCE);
            dialog.setTitle(useMerchantVoucherQueryResult.getErrorMessageTitle());
            dialog.setDesc(useMerchantVoucherQueryResult.getErrorMessage());
            dialog.setBtnOk(getString(R.string.label_close));
            dialog.setOnOkClickListener(v -> dialog.dismiss());
            dialog.show();

            voucherListPresenter.clearCache();
            loadPromo();
        }
    }

    @Override
    public void onErrorUseVoucher(@NotNull Throwable e) {
        hideUseMerchantVoucherLoading();
        if (getActivity() != null) {
            if (e instanceof MessageTitleErrorException) {

                com.tokopedia.design.component.Dialog dialog = new com.tokopedia.design.component.Dialog(getActivity(),
                        com.tokopedia.design.component.Dialog.Type.PROMINANCE);
                dialog.setTitle(((MessageTitleErrorException) e).getErrorMessageTitle());
                dialog.setDesc(e.getMessage());
                dialog.setBtnOk(getString(R.string.label_close));
                dialog.setOnOkClickListener(v -> dialog.dismiss());
                dialog.show();
            } else {
                ToasterError.showClose(getActivity(), ErrorHandler.getErrorMessage(getActivity(), e));
            }
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

    @Override
    public void showLatestTalkView(LatestTalkViewModel latestTalkViewModel) {
        this.productData.setLatestTalkViewModel(latestTalkViewModel);
        this.latestTalkView.renderData(this.productData);
    }

    @Override
    public void restoreIsAppBarCollapsed(boolean isAppBarCollapsed) {
        this.isAppBarCollapsed = isAppBarCollapsed;
        if (isAppBarCollapsed) {
            collapsedAppBar();
        } else {
            expandedAppBar();
        }
    }

    @Override
    public void loadPromo() {
        if (productData != null) {
            if (useMerchantVoucherFeature) {
                voucherListPresenter.getVoucherList(productData.getShopInfo().getShopId(),
                        NUM_VOUCHER_TO_SHOW);
            } else {
                if (!GlobalConfig.isSellerApp()) {
                    presenter.getPromoWidget(getContext(), productData);
                }
            }
        }
    }

    @Override
    public boolean isSellerApp() {
        return GlobalConfig.isSellerApp();
    }

    @Override
    public void trackingEnhanceProductDetail() {
        Map<String, Object> detail;
        if (TextUtils.isEmpty(productPass.getTrackerListName())) {
            detail = DataLayer.mapOf(
                    "products", DataLayer.listOf(
                            DataLayer.mapOf(
                                    "name", productData.getInfo().getProductName(),
                                    "id", productData.getInfo().getProductId(),
                                    "price", productData.getInfo().getProductPriceUnformatted(),
                                    "brand", "none / other",
                                    "category", productData.getEnhanceCategoryFormatted(),
                                    "variant", getEnhanceVariant(),
                                    "dimension38", productPass.getTrackerAttribution()
                            )
                    )
            );
        } else {
            detail = DataLayer.mapOf(
                    "actionField", DataLayer.mapOf("list", productPass.getTrackerListName()),
                    "products", DataLayer.listOf(
                            DataLayer.mapOf(
                                    "name", productData.getInfo().getProductName(),
                                    "id", productData.getInfo().getProductId(),
                                    "price", productData.getInfo().getProductPriceUnformatted(),
                                    "brand", "none / other",
                                    "category", productData.getEnhanceCategoryFormatted(),
                                    "variant", getEnhanceVariant(),
                                    "dimension38", productPass.getTrackerAttribution()
                            )
                    )
            );
        }
        String categoryId = "0";
        if (productData.getBreadcrumb() != null && !productData.getBreadcrumb().isEmpty()) {
            categoryId = productData.getBreadcrumb().get(productData.getBreadcrumb().size() - 1).getDepartmentId();
        }
        ProductPageTracking.eventEnhanceProductDetail(
                getActivity(),
                DataLayer.mapOf(
                        "event", "viewProduct",
                        "eventCategory", "product page",
                        "eventAction", "view product page",
                        "eventLabel", String.format(
                                Locale.getDefault(),
                                "%s - %s - %s",
                                productData.getEnhanceShopType(), productData.getShopInfo().getShopName(), productData.getInfo().getProductName()
                        ),
                        "ecommerce", DataLayer.mapOf(
                                "currencyCode", "IDR",
                                "detail", detail
                        ),
                        "key", productData.getEnhanceUrl(productData.getInfo().getProductUrl()),
                        "shopName", productData.getShopInfo().getShopName(),
                        "shopId", productData.getShopInfo().getShopId(),
                        "shopDomain", productData.getShopInfo().getShopDomain(),
                        "shopLocation", productData.getShopInfo().getShopLocation(),
                        "shopIsGold", String.valueOf(productData.getShopInfo().shopIsGoldBadge() ? 1 : 0),
                        "categoryId", categoryId,
                        "url", productData.getInfo().getProductUrl(),
                        "shopType", productData.getEnhanceShopType()
                )
        );
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    @Override
    public void renderAddToCartSuccessOpenCheckout(AddToCartResult addToCartResult) {
        buttonBuyView.removeLoading();
        String productName = "";
        if (productData.getInfo() != null) {
            productName = productData.getInfo().getProductName();
        }
        String departmentName = "";
        if (productData.getBreadcrumb().size() > 0) {
            departmentName = productData.getBreadcrumb().get(0).getDepartmentName();
        }
        ProductPageTracking.eventAppsFlyer(
                String.valueOf(productData.getInfo().getProductId()),
                productData.getInfo().getProductPrice(),
                selectedQuantity, productName, departmentName
        );
        updateCartNotification();
        enhanceEcommerceAtc(addToCartResult);
        if (productData != null && getActivity() != null &&
                getActivity().getApplicationContext() instanceof PdpRouter) {
            if (productData.isBigPromo()) {
                Intent intent = ((PdpRouter) getActivity().getApplicationContext())
                        .getCartIntent(getActivity());
                startActivity(intent);
            } else {
                Intent intent = ((PdpRouter) getActivity().getApplicationContext())
                        .getCheckoutIntent(getActivity());
                startActivity(intent);
            }
        }
    }

    @Override
    public void renderAddToCartSuccess(AddToCartResult addToCartResult) {
        buttonBuyView.removeLoading();
        String productName = "";
        String cataglogName = "";
        if (productData.getInfo() != null) {
            productName = productData.getInfo().getProductName();
            cataglogName = productData.getInfo().getProductCatalogName();
        }
        ProductPageTracking.eventAppsFlyer(
                String.valueOf(productData.getInfo().getProductId()),
                productData.getInfo().getProductPrice(),
                selectedQuantity, productName, cataglogName
        );
        updateCartNotification();
        enhanceEcommerceAtc(addToCartResult);
        showSnackbarSuccessAtc(addToCartResult.getMessage(), String.valueOf(productData.getInfo().getProductId()));
    }

    private void showSnackbarSuccessAtc(String message, String productId) {
        if (getActivity() != null && getView() != null) {
            if (TextUtils.isEmpty(message)) {
                message = getString(R.string.default_request_error_unknown_short);
            }
            ToasterNormal.make(getView(), message.replace("\n", " "), BaseToaster.LENGTH_LONG)
                    .setAction(getActivity().getString(R.string.label_atc_open_cart), v -> {
                        checkoutAnalyticsAddToCart.eventAtcClickLihat(productId);
                        Intent intent = ((PdpRouter) getActivity().getApplicationContext())
                                .getCartIntent(getActivity());
                        startActivity(intent);
                    })
                    .show();
        }
    }

    private void enhanceEcommerceAtc(AddToCartResult addToCartResult) {
        EnhancedECommerceProductCartMapData enhancedECommerceProductCartMapData =
                new EnhancedECommerceProductCartMapData();
        enhancedECommerceProductCartMapData.setProductName(productData.getInfo().getProductName());
        enhancedECommerceProductCartMapData.setProductID(String.valueOf(productData.getInfo().getProductId()));
        enhancedECommerceProductCartMapData.setPrice(String.valueOf(productData.getInfo().getProductPriceUnformatted()));
        enhancedECommerceProductCartMapData.setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
        String categoryLevelStr = generateCategoryStringLevel(productData.getBreadcrumb());
        enhancedECommerceProductCartMapData.setCartId(addToCartResult.getCartId());
        enhancedECommerceProductCartMapData.setDimension45(addToCartResult.getCartId());
        enhancedECommerceProductCartMapData.setCategory(TextUtils.isEmpty(categoryLevelStr)
                ? EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                : categoryLevelStr);
        enhancedECommerceProductCartMapData.setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceProductCartMapData.setQty(Integer.parseInt(productData.getInfo().getProductMinOrder()));
        enhancedECommerceProductCartMapData.setShopId(productData.getShopInfo().getShopId());
        enhancedECommerceProductCartMapData.setShopType(generateShopType(productData.getShopInfo()));
        enhancedECommerceProductCartMapData.setShopName(productData.getShopInfo().getShopName());
        enhancedECommerceProductCartMapData.setCategoryId(generateCategoryId(productData.getBreadcrumb()));
        enhancedECommerceProductCartMapData.setAttribution(
                TextUtils.isEmpty(productPass.getTrackerAttribution())
                        ? EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                        : productPass.getTrackerAttribution()
        );
        enhancedECommerceProductCartMapData.setDimension38(
                TextUtils.isEmpty(productPass.getTrackerAttribution())
                        ? EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                        : productPass.getTrackerAttribution()
        );
        enhancedECommerceProductCartMapData.setListName(
                TextUtils.isEmpty(productPass.getTrackerListName())
                        ? EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                        : productPass.getTrackerListName()
        );
        enhancedECommerceProductCartMapData.setDimension40(
                TextUtils.isEmpty(productPass.getTrackerListName())
                        ? EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                        : productPass.getTrackerListName()
        );

        EnhancedECommerceCartMapData enhancedECommerceCartMapData = new EnhancedECommerceCartMapData();
        enhancedECommerceCartMapData.addProduct(enhancedECommerceProductCartMapData.getProduct());
        enhancedECommerceCartMapData.setCurrencyCode(EnhancedECommerceCartMapData.VALUE_CURRENCY_IDR);
        enhancedECommerceCartMapData.setAction(EnhancedECommerceCartMapData.ADD_ACTION);

        String eventAction;
        switch (addToCartResult.getSource()) {
            case SOURCE_BUTTON_BUY_PDP:
                eventAction = getString(R.string.event_action_click_beli);
                break;
            case SOURCE_BUTTON_CART_PDP:
                eventAction = getString(R.string.event_action_click_keranjang);
                break;
            case SOURCE_BUTTON_BUY_VARIANT:
                eventAction = getString(R.string.event_action_click_beli_in_variants_page);
                break;
            case SOURCE_BUTTON_CART_VARIANT:
                eventAction = getString(R.string.event_action_click_keranjang_in_variants_page);
                break;
            default:
                eventAction = productPass.getProductName();
                break;
        }
        sendAnalyticsOnAddToCartSuccess(enhancedECommerceCartMapData.getCartMap(),
                eventAction,
                (productData.getInfo().getHasVariant() ? productVariant.generateVariantValue(productData.getInfo().getProductId()) : "non variant")
        );
    }

    private String generateCategoryStringLevel(List<ProductBreadcrumb> breadcrumb) {
        Collections.sort(breadcrumb, new Comparator<ProductBreadcrumb>() {
            @Override
            public int compare(ProductBreadcrumb productBreadcrumb, ProductBreadcrumb t1) {
                return productBreadcrumb.getDepartmentTree().compareTo(t1.getDepartmentTree());
            }
        });
        StringBuilder stringBuilder = new StringBuilder();
        int size = breadcrumb.size();
        for (int i = 0; i < size; i++) {
            ProductBreadcrumb productBreadcrumb = breadcrumb.get(i);
            stringBuilder.append(productBreadcrumb.getDepartmentName());
            if (i != (size - 1)) {
                stringBuilder.append("/");
            }
        }
        return stringBuilder.toString();
    }


    private String generateCategoryId(List<ProductBreadcrumb> breadcrumb) {
        Collections.sort(breadcrumb, new Comparator<ProductBreadcrumb>() {
            @Override
            public int compare(ProductBreadcrumb productBreadcrumb, ProductBreadcrumb t1) {
                return productBreadcrumb.getDepartmentTree().compareTo(t1.getDepartmentTree());
            }
        });

        return breadcrumb.get(breadcrumb.size() - 1).getDepartmentId();
    }

    private void startShowCase() {
        final String showCaseTag = ProductInfoActivity.class.getName() + TAG_SHOWCASE_VARIANT;
        if (ShowCasePreference.hasShown(getActivity(), showCaseTag) || showCaseDialog != null) {
            return;
        }
        showCaseDialog = createShowCase();
        showCaseDialog.setShowCaseStepListener((previousStep, nextStep, showCaseObject) -> false);

        ArrayList<ShowCaseObject> showCaseObjectList = new ArrayList<>();
        showCaseObjectList.add(new ShowCaseObject(
                buttonBuyView.tvAddToCart,
                getResources().getString(R.string.title_show_case_pdp),
                getResources().getString(R.string.desc_show_case_pdp),
                ShowCaseContentPosition.TOP,
                R.color.tkpd_main_green));
        showCaseDialog.show(getActivity(), showCaseTag, showCaseObjectList);
    }

    private ShowCaseDialog createShowCase() {
        return new ShowCaseBuilder()
                .customView(R.layout.view_onboarding_variant)
                .titleTextColorRes(R.color.white)
                .spacingRes(R.dimen.spacing_show_case)
                .textColorRes(R.color.grey_400)
                .shadowColorRes(R.color.shadow)
                .backgroundContentColorRes(R.color.black)
                .textSizeRes(R.dimen.fontvs)
                .finishStringRes(R.string.title_understand)
                .useCircleIndicator(true)
                .clickable(true)
                .useArrow(true)
                .build();
    }

    private String getEnhanceVariant() {
        if (productVariant != null) {
            String variantValue = productVariant.generateVariantValue(productData.getInfo().getProductId());
            return variantValue.isEmpty() ? STATIC_VALUE_ENHANCE_NONE_OTHER : variantValue;
        } else {
            return STATIC_VALUE_ENHANCE_NONE_OTHER;
        }
    }

    private void updateCartNotification() {
        ((TransactionRouter) getActivity().getApplication())
                .updateMarketplaceCartCounter(new TransactionRouter.CartNotificationListener() {
                    @Override
                    public void onDataReady() {
                        if (isAdded()) {
                            if (isAppBarCollapsed) {
                                initToolbarLight();
                            } else {
                                initToolbarTransparant();
                            }
                        }
                    }
                });
    }

    @Override
    public void sendAnalyticsOnAddToCartSuccess(Map<String, Object> cartMap,
                                                String eventAction,
                                                String eventLabel) {
        checkoutAnalyticsAddToCart.eventClickAtcAddToCartClickBayarOnAtcSuccess();
        checkoutAnalyticsAddToCart.enhancedECommerceAddToCart(cartMap, eventLabel, eventAction);
        checkoutAnalyticsAddToCart.flushEnhancedECommerceAddToCart();
    }

    @Override
    public void refreshData() {
        presenter.requestProductDetail(getActivity(), productPass, INIT_REQUEST, true, useVariant);
    }

    @Override
    public void onErrorLoadRateEstimation() {
        varianCourierSimulationView.renderRateEstimation();
    }

    @Override
    public void onSuccesLoadRateEstimation(RatesModel ratesModel) {
        varianCourierSimulationView.renderRateEstimation(ratesModel);
    }

    @Override
    public void moveToEstimationDetail() {
        startActivity(RatesEstimationDetailActivity.createIntent(getActivity(), productData.getShopInfo().getShopDomain(),
                productData.getInfo().getProductWeight(), productData.getInfo().getProductWeightUnit()));
    }

    private void renderTopAds(int itemSize, ProductDetailData productData) {
        if (!firebaseRemoteConfig.getBoolean(TkpdCache.RemoteConfigKey.MAINAPP_SHOW_PDP_TOPADS, true))
            return;
        try {
            Xparams xparams = new Xparams();
            xparams.setProduct_id(productData.getInfo().getProductId());
            xparams.setProduct_name(productData.getInfo().getProductName());
            xparams.setSource_shop_id(Integer.parseInt(productData.getShopInfo().getShopId()));
            if (productData.getBreadcrumb().size() > 2) {
                xparams.setChild_cat_id(Integer.parseInt(productData.getBreadcrumb().get(2).getDepartmentId()));
            }
            TopAdsParams params = new TopAdsParams();
            params.getParam().put(TopAdsParams.KEY_SRC, SRC_PDP_VALUE);
            params.getParam().put(TopAdsParams.KEY_EP, DEFAULT_KEY_EP);
            params.getParam().put(TopAdsParams.KEY_ITEM, String.valueOf(itemSize));
            params.getParam().put(TopAdsParams.KEY_XPARAMS, new Gson().toJson(xparams));

            Config config = new Config.Builder()
                    .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                    .setUserId(SessionHandler.getLoginID(getActivity()))
                    .topAdsParams(params)
                    .build();

            topAds.setAdsItemClickListener(this);
            topAds.setAdsListener(this);
            topAds.setAdsItemImpressionListener(new TopAdsItemImpressionListener() {
                @Override
                public void onImpressionProductAdsItem(int position, Product product) {
                    TopAdsGtmTracker.eventProductDetailProductView(getContext(), product, position);
                }
            });
            topAds.setConfig(config);
            topAds.loadTopAds();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTopAdsLoaded(List<Item> list) {
        topAds.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTopAdsFailToLoad(int errorCode, String message) {
        topAds.setVisibility(View.GONE);
    }

    @Override
    public void onProductItemClicked(int position, Product product) {
        ProductItem data = new ProductItem();
        data.setId(product.getId());
        data.setName(product.getName());
        data.setPrice(product.getPriceFormat());
        data.setImgUri(product.getImage().getM_ecs());
        Bundle bundle = new Bundle();
        Intent intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(getActivity());
        bundle.putParcelable(ProductDetailRouter.EXTRA_PRODUCT_ITEM, data);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
        TopAdsGtmTracker.eventProductDetailProductClick(getContext(), product, position);
    }

    @Override
    public void onShopItemClicked(int position, Shop shop) {

    }

    @Override
    public void onAddFavorite(int position, Data data) {

    }

    @Override
    public void onImageFromBuyerClick(int viewType, String reviewId) {
        if(viewType == ImageFromBuyerView.VIEW_TYPE_IMAGE){
            ProductPageTracking.eventClickReviewOnBuyersImage(
                    getActivity(),
                    String.valueOf(productData.getInfo().getProductId()),
                    reviewId
            );
        } else if (viewType == ImageFromBuyerView.VIEW_TYPE_IMAGE_WITH_SEE_ALL_LAYER){
            ProductPageTracking.eventClickReviewOnSeeAllImage(
                    getActivity(),
                    String.valueOf(productData.getInfo().getProductId())
            );
        }

        routeToReviewGallery();
    }

    @Override
    public void onMostHelpfulImageClicked(List<ReviewImageAttachment> data, int position) {
        ArrayList<String> imageUrlList = new ArrayList<>();
        for (ReviewImageAttachment reviewImageAttachment : data) {
            imageUrlList.add(reviewImageAttachment.getUriLarge());
        }
        ImageReviewGalleryActivity.Companion.moveTo(getActivity(), imageUrlList, position);
    }

    public void routeToReviewGallery(){
        if (getActivity() != null) {
            ImageReviewGalleryActivity.Companion.moveTo(getActivity(),
                    productData.getInfo().getProductId());
        }
    }

    public void setDrawableCount(Context context, int count) {
        MenuItem menuItem = menu.findItem(R.id.action_cart);
        if (menuItem.getIcon() instanceof LayerDrawable) {
            LayerDrawable icon = (LayerDrawable) menuItem.getIcon();
            CountDrawable badge = new CountDrawable(context);
            if (count > 99) {
                badge.setCount(getString(R.string.pdp_label_cart_count_max));
            } else {
                badge.setCount(Integer.toString(count));
            }
            icon.mutate();
            icon.setDrawableByLayerId(R.id.ic_cart_count, badge);
        }
    }

    private String getUserId(){
        return userSession.getUserId();
    }
}
