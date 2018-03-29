package com.tokopedia.tkpdpdp.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appsflyer.AFInAppEventType;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.entity.variant.Child;
import com.tokopedia.core.network.entity.variant.Option;
import com.tokopedia.core.network.entity.variant.ProductVariant;
import com.tokopedia.core.product.intentservice.ProductInfoIntentService;
import com.tokopedia.core.product.listener.DetailFragmentInteractionListener;
import com.tokopedia.core.product.model.goldmerchant.VideoData;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.ProductImage;
import com.tokopedia.core.product.model.productdetail.ProductShopInfo;
import com.tokopedia.core.product.model.productdetail.discussion.LatestTalkViewModel;
import com.tokopedia.core.product.model.productdetail.mosthelpful.Review;
import com.tokopedia.core.product.model.productdetail.promowidget.PromoAttributes;
import com.tokopedia.core.product.model.productother.ProductOther;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.router.home.SimpleHomeRouter;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.router.reactnative.IReactNativeRouter;
import com.tokopedia.core.router.transactionmodule.TransactionCartRouter;
import com.tokopedia.core.router.transactionmodule.passdata.ProductCartPass;
import com.tokopedia.core.share.ShareActivity;
import com.tokopedia.core.util.AppIndexHandler;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.webview.listener.DeepLinkWebViewHandleListener;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseContentPosition;
import com.tokopedia.showcase.ShowCaseDialog;
import com.tokopedia.showcase.ShowCaseObject;
import com.tokopedia.showcase.ShowCasePreference;
import com.tokopedia.tkpdpdp.CourierActivity;
import com.tokopedia.tkpdpdp.DescriptionActivity;
import com.tokopedia.tkpdpdp.DinkFailedActivity;
import com.tokopedia.tkpdpdp.DinkSuccessActivity;
import com.tokopedia.tkpdpdp.InstallmentActivity;
import com.tokopedia.tkpdpdp.PreviewProductImageDetail;
import com.tokopedia.tkpdpdp.ProductInfoActivity;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.VariantActivity;
import com.tokopedia.tkpdpdp.WholesaleActivity;
import com.tokopedia.tkpdpdp.customview.ButtonBuyView;
import com.tokopedia.tkpdpdp.customview.DetailInfoView;
import com.tokopedia.tkpdpdp.customview.FlingBehavior;
import com.tokopedia.tkpdpdp.customview.HeaderInfoView;
import com.tokopedia.tkpdpdp.customview.LastUpdateView;
import com.tokopedia.tkpdpdp.customview.LatestTalkView;
import com.tokopedia.tkpdpdp.customview.MostHelpfulReviewView;
import com.tokopedia.tkpdpdp.customview.NewShopView;
import com.tokopedia.tkpdpdp.customview.OtherProductsView;
import com.tokopedia.tkpdpdp.customview.PictureView;
import com.tokopedia.tkpdpdp.customview.PriceSimulationView;
import com.tokopedia.tkpdpdp.customview.PromoWidgetView;
import com.tokopedia.tkpdpdp.customview.RatingTalkCourierView;
import com.tokopedia.tkpdpdp.customview.ShopInfoViewV2;
import com.tokopedia.tkpdpdp.customview.TransactionDetailView;
import com.tokopedia.tkpdpdp.customview.VideoDescriptionLayout;
import com.tokopedia.tkpdpdp.customview.YoutubeThumbnailViewHolder;
import com.tokopedia.tkpdpdp.dialog.ReportProductDialogFragment;
import com.tokopedia.tkpdpdp.listener.AppBarStateChangeListener;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;
import com.tokopedia.tkpdpdp.presenter.ProductDetailPresenter;
import com.tokopedia.tkpdpdp.presenter.ProductDetailPresenterImpl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static com.tokopedia.core.product.model.productdetail.ProductInfo.PRD_STATE_PENDING;
import static com.tokopedia.core.router.productdetail.ProductDetailRouter.EXTRA_PRODUCT_ID;
import static com.tokopedia.core.router.productdetail.ProductDetailRouter.WIHSLIST_STATUS_IS_WISHLIST;
import static com.tokopedia.core.router.productdetail.ProductDetailRouter.WISHLIST_STATUS_UPDATED_POSITION;
import static com.tokopedia.tkpdpdp.VariantActivity.KEY_LEVEL1_SELECTED;
import static com.tokopedia.tkpdpdp.VariantActivity.KEY_LEVEL2_SELECTED;
import static com.tokopedia.tkpdpdp.VariantActivity.KEY_PRODUCT_DETAIL_DATA;
import static com.tokopedia.tkpdpdp.VariantActivity.KEY_VARIANT_DATA;

/**
 * ProductDetailFragment
 * Created by Angga.Prasetiyo on 22/10/2015.
 * Edited by alifa, rohmadi, henry for v2
 */
@RuntimePermissions
public class ProductDetailFragment extends BasePresenterFragment<ProductDetailPresenter>
        implements ProductDetailView {

    private static final int FROM_COLLAPSED = 0;
    private static final int FROM_EXPANDED = 1;
    private static final int SCROLL_ELEVATION = 324;
    private static final int SHOWCASE_MARGIN = 10;
    private static final int SHOWCASE_HEIGHT = 100;

    public static final int REQUEST_CODE_SHOP_INFO = 998;
    public static final int REQUEST_CODE_TALK_PRODUCT = 1;
    public static final int REQUEST_CODE_EDIT_PRODUCT = 2;
    public static final int REQUEST_CODE_LOGIN = 561;
    public static final int STATUS_IN_WISHLIST = 1;
    public static final int STATUS_NOT_WISHLIST = 0;
    public static final int REQUEST_VARIANT = 99;
    public static final int INIT_REQUEST = 1;
    public static final int RE_REQUEST = 2;

    private static final String TAG = ProductDetailFragment.class.getSimpleName();
    private static final String ARG_PARAM_PRODUCT_PASS_DATA = "ARG_PARAM_PRODUCT_PASS_DATA";
    private static final String ARG_FROM_DEEPLINK = "ARG_FROM_DEEPLINK";
    private static final String ENABLE_VARIANT = "mainapp_discovery_enable_pdp_variant";
    private static final String NON_VARIANT = "non-variant";

    public static final String STATE_DETAIL_PRODUCT = "STATE_DETAIL_PRODUCT";
    public static final String STATE_PRODUCT_VARIANT = "STATE_PRODUCT_VARIANT";
    public static final String STATE_OTHER_PRODUCTS = "STATE_OTHER_PRODUCTS";
    public static final String STATE_VIDEO = "STATE_VIDEO";
    public static final String STATE_PROMO_WIDGET = "STATE_PROMO_WIDGET";
    public static final String STATE_APP_BAR_COLLAPSED = "STATE_APP_BAR_COLLAPSED";
    public static final String TAG_SHOWCASE_VARIANT = "-SHOWCASE_VARIANT";

    private CoordinatorLayout coordinatorLayout;
    private HeaderInfoView headerInfoView;
    private DetailInfoView detailInfoView;
    private PictureView pictureView;
    private RatingTalkCourierView ratingTalkCourierView;
    private PriceSimulationView priceSimulationView;
    private PromoWidgetView promoWidgetView;
    private ShopInfoViewV2 shopInfoView;
    private TransactionDetailView transactionDetailView;
    private VideoDescriptionLayout videoDescriptionLayout;
    private MostHelpfulReviewView mostHelpfulReviewView;
    private OtherProductsView otherProductsView;
    private NewShopView newShopView;
    private ButtonBuyView buttonBuyView;
    private LastUpdateView lastUpdateView;
    private LatestTalkView latestTalkView;
    private ProgressBar progressBar;
    private NestedScrollView nestedScrollView;

    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton fabWishlist;
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

    private ProductPass productPass;
    private ProductDetailData productData;
    private boolean useVariant = true;
    private ProductVariant productVariant;
    private List<ProductOther> productOthers;
    private VideoData videoData;
    private PromoAttributes promoAttributes;
    private Option variantLevel1;
    private Option variantLevel2;
    private boolean onClickBuyWhileRequestingVariant = false;

    private RemoteConfig remoteConfig;
    private ShowCaseDialog showCaseDialog;

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
        remoteConfig = new FirebaseRemoteConfigImpl(getActivity());
        if (remoteConfig.getBoolean(ENABLE_VARIANT)==false) {
            useVariant = false;
        }
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
        headerInfoView = (HeaderInfoView) view.findViewById(R.id.view_header);
        pictureView = (PictureView) view.findViewById(R.id.view_picture);
        ratingTalkCourierView = (RatingTalkCourierView) view.findViewById(R.id.view_rating);
        detailInfoView = (DetailInfoView) view.findViewById(R.id.view_detail);
        newShopView = (NewShopView) view.findViewById(R.id.view_new_shop);
        videoDescriptionLayout = (VideoDescriptionLayout) view.findViewById(R.id.video_layout);
        shopInfoView = (ShopInfoViewV2) view.findViewById(R.id.view_shop_info);
        otherProductsView = (OtherProductsView) view.findViewById(R.id.view_other_products);
        promoWidgetView = (PromoWidgetView) view.findViewById(R.id.view_promo_widget);
        mostHelpfulReviewView = (MostHelpfulReviewView) view.findViewById(R.id.view_most_helpful);
        buttonBuyView = (ButtonBuyView) view.findViewById(R.id.view_buy);
        lastUpdateView = (LastUpdateView) view.findViewById(R.id.view_last_update);
        latestTalkView = (LatestTalkView) view.findViewById(R.id.view_latest_discussion);
        progressBar = (ProgressBar) view.findViewById(R.id.view_progress);
        nestedScrollView = view.findViewById(R.id.nested_scroll_pdp);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);
        collapsingToolbarLayout
                = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        transactionDetailView
                = (TransactionDetailView) view.findViewById(R.id.view_transaction_detail);
        priceSimulationView
                = (PriceSimulationView) view.findViewById(R.id.view_price_simulation);
        fabWishlist = (FloatingActionButton) view.findViewById(R.id.fab_detail);
        rootView = (LinearLayout) view.findViewById(R.id.root_view);

        collapsingToolbarLayout.setTitle("");
        toolbar.setTitle("");
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
    }

    private void collapsedAppBar() {
        initStatusBarLight();
        initToolbarLight();
        fabWishlist.hide();
    }

    private void expandedAppBar() {
        initStatusBarDark();
        initToolbarTransparant();
        if (productData != null && productData.getInfo().getProductAlreadyWishlist() != null) {
            fabWishlist.show();
        }
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
        promoWidgetView.setListener(this);
        mostHelpfulReviewView.setListener(this);
        transactionDetailView.setListener(this);
        priceSimulationView.setListener(this);
        latestTalkView.setListener(this);
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
    public void onProductRatingClicked(String productId, String shopId, String productName) {
        presenter.processToReputation(context, productId, productName);
    }

    @Override
    public void onProductReviewClicked(String productId, String shopId, String productName) {
        presenter.processToReputation(context, productId, productName);
    }

    @Override
    public void onProductManagePromoteClicked(ProductDetailData productData) {
        presenter.requestPromoteProduct(context, productData);
    }

    @Override
    public void onBuyClick(String source) {
        if (SessionHandler.isV4Login(getActivity())) {
            if (onClickBuyWhileRequestingVariant == false && productData != null &&
                    productData.getInfo().getHasVariant() && productVariant == null) {
                onClickBuyWhileRequestingVariant = true;
                buttonBuyView.changeToLoading();
            } else if ( productData != null && !productData.getInfo().getHasVariant() && productVariant==null ||
                    productData.getInfo().getHasVariant() && productVariant!=null && variantLevel1!=null) {
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
                        .setPrice(productData.getInfo().getProductPrice())
                        .setShopType(generateShopType(productData.getShopInfo()))
                        .build();
                pass.setNotes(generateVariantString());
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
                onProductBuySessionLogin(pass);
            } else {
                Bundle bundle = new Bundle();
                bundle.putParcelable(VariantActivity.KEY_VARIANT_DATA, productVariant);
                bundle.putParcelable(VariantActivity.KEY_PRODUCT_DETAIL_DATA, productData);
                onVariantClicked(bundle);
            }
            if (!TextUtils.isEmpty(source) && source.equals(SOURCE_BUTTON_BUY_PDP) && productData.getInfo().getHasVariant() && variantLevel1 != null ) {
                UnifyTracking.eventBuyPDPVariant(generateVariantString());
            } else if (!TextUtils.isEmpty(source) && source.equals(SOURCE_BUTTON_BUY_PDP) && productData.getInfo().getHasVariant()) {
                UnifyTracking.eventBuyPDPVariant("");
            } else if (!TextUtils.isEmpty(source) && source.equals(SOURCE_BUTTON_BUY_PDP) && !productData.getInfo().getHasVariant()) {
                UnifyTracking.eventBuyPDPVariant(NON_VARIANT);
            } else if (!TextUtils.isEmpty(source) && source.equals(SOURCE_BUTTON_BUY_VARIANT) && productData.getInfo().getHasVariant()) {
                Long timestamp = System.currentTimeMillis()/1000;
                UnifyTracking.eventBuyPageVariant(timestamp.toString() + "-" + generateVariantString());
            }
        } else {
            Bundle bundle = new Bundle();
            bundle.putBoolean("login", true);
            onProductBuySessionNotLogin(bundle);
        }
    }

    private String generateShopType(ProductShopInfo productShopInfo) {
        if (productShopInfo.getShopIsOfficial() == 1)
            return "official_store";
        else if(productShopInfo.getShopIsGold() == 1)
            return "gold_merchant";
        else return "reguler";
    }

    @Override
    public void updateButtonBuyListener() {
        buttonBuyView.removeLoading();
        if (onClickBuyWhileRequestingVariant) {
            onBuyClick(SOURCE_BUTTON_BUY_PDP);
        }
    }

    public ArrayList<String> getImageURIPaths() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (ProductImage productImage : productData.getProductImages()) {
            arrayList.add(productImage.getImageSrc());
        }
        if (productData.getInfo() != null && productData.getInfo().getHasVariant()
                && productVariant!=null && productVariant.getChildren()!=null) {
            for (Child child: productVariant.getChildren()) {
                if (!TextUtils.isEmpty(child.getPicture().getOriginal()) && child.getProductId()!=productData.getInfo().getProductId()) {
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
        presenter.processToPicturePreview(context, bundle);
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
        startActivity(ShareActivity.createIntent(getActivity(), data));
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void shareProduct(ShareData data) {
        interactionListener.shareProductInfo(data);
    }

    @Override
    public void onCourierClicked(@NonNull Bundle bundle) {
        Intent intent = new Intent(context, CourierActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        getActivity().overridePendingTransition(0, 0);
    }

    @Override
    public void onWholesaleClicked(@NonNull Bundle bundle) {
        Intent intent = new Intent(context, WholesaleActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        getActivity().overridePendingTransition(0, 0);
    }

    @Override
    public void onVariantClicked(@NonNull Bundle bundle) {
        Intent intent = new Intent(context, VariantActivity.class);
        intent.putExtras(bundle);
        intent.putExtra(KEY_LEVEL1_SELECTED,variantLevel1);
        intent.putExtra(KEY_LEVEL2_SELECTED,variantLevel2);
        if (productData.getShopInfo().getShopIsOwner() == 1
                || (productData.getShopInfo().getShopIsAllowManage() == 1 || GlobalConfig.isSellerApp())) {
            intent.putExtra(VariantActivity.KEY_SELLER_MODE, true);
        }
        startActivityForResult(intent,REQUEST_VARIANT);
        getActivity().overridePendingTransition(com.tokopedia.core.R.anim.pull_up,0);
        if (productData.getInfo().getHasVariant() && productVariant !=null && variantLevel1 != null) {
            UnifyTracking.eventClickVariant(generateVariantString());
        }

    }

    @Override
    public void onDescriptionClicked(@NonNull Bundle bundle) {
        Intent intent = new Intent(context, DescriptionActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        getActivity().overridePendingTransition(com.tokopedia.core.R.anim.pull_up, 0);
    }

    @Override
    public void onInstallmentClicked(@NonNull Bundle bundle) {
        Intent intent = new Intent(context, InstallmentActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        getActivity().overridePendingTransition(com.tokopedia.core.R.anim.pull_up, 0);
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
        this.presenter.startIndexingApp(appIndexHandler, successResult);
        this.refreshMenu();
        this.updateWishListStatus(productData.getInfo().getProductAlreadyWishlist());
    }

    @Override
    public void onOtherProductLoaded(List<ProductOther> productOthers) {
        this.productOthers = productOthers;
        otherProductsView.renderOtherProduct(productOthers);
    }

    @Override
    public void onProductShopMessageClicked(@NonNull Intent intent) {
        presenter.processToSendMessage(context, intent);
    }

    @Override
    public void onProductHasEdited() {
        presenter.requestProductDetail(context, productPass, RE_REQUEST, true, useVariant);
    }

    @Override
    public void onProductTalkUpdated() {
        presenter.requestProductDetail(context, productPass, RE_REQUEST, true, useVariant);
    }

    @Override
    public void onShopFavoriteUpdated(int statFave) {
        shopInfoView.reverseFavorite();
    }

    @Override
    public void onProductShopFaveClicked(String shopId, Integer productId) {
        presenter.requestFaveShop(context, shopId, productId);
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
            fabWishlist.setImageDrawable(getResources().getDrawable(R.drawable.ic_wishlist_red));
        } else {
            fabWishlist.setImageDrawable(getResources().getDrawable(R.drawable.ic_wishlist));
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
        if (productVariant!=null && productVariant.getChildren()!=null ) {
            for (Child child: productVariant.getChildren()) {
                if (child.getProductId()==productData.getInfo().getProductId()) {
                    child.setWishlist(status==1?true:false);
                    break;
                }
            }
        }
    }

    @Override
    public void loadVideo(VideoData data) {
        this.videoDescriptionLayout.renderVideoData(data, youTubeThumbnailLoadInProcessListener);
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
        TextView tv = snack.getView().findViewById(com.tokopedia.core.R.id.snackbar_text);
        tv.setTextColor(ContextCompat.getColor(context,R.color.black_54));
        tv.setMaxLines(5);

        Button snackBarAction = snack.getView().findViewById(android.support.design.R.id.snackbar_action);
        snackBarAction.setTextColor(ContextCompat.getColor(coordinatorLayout.getContext(), R.color.black_70));
        snackBarAction.setAllCaps(false);

        snack.getView().setBackground(getResources().getDrawable(R.drawable.bg_snackbar_variant));
        snack.setAction(getString(R.string.title_retry), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.requestProductDetail(context, productPass, INIT_REQUEST, false, useVariant);
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
            if (webViewHandleListener != null) {
                webViewHandleListener.catchToWebView(pass != null ? pass.getProductUri() : "");
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
        if (context instanceof YoutubeThumbnailViewHolder.YouTubeThumbnailLoadInProcess)
            youTubeThumbnailLoadInProcessListener = (YoutubeThumbnailViewHolder.YouTubeThumbnailLoadInProcess) context;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(context);
        if (activity instanceof DeepLinkWebViewHandleListener) {
            webViewHandleListener = (DeepLinkWebViewHandleListener) activity;
        } else {
            throw new RuntimeException("Activity must implement DeepLinkWebViewHandleListener");
        }
        if (context instanceof YoutubeThumbnailViewHolder.YouTubeThumbnailLoadInProcess)
            youTubeThumbnailLoadInProcessListener = (YoutubeThumbnailViewHolder.YouTubeThumbnailLoadInProcess) context;
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
        presenter.onDestroyView(context);
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
            presenter.requestProductDetail(context, productPass, INIT_REQUEST, false, useVariant);
        }
    }

    @Override
    public void onSaveState(Bundle outState) {
        Log.d(TAG, "onSaveState");
        presenter.saveStateProductDetail(outState, STATE_DETAIL_PRODUCT, productData);
        presenter.saveStateProductVariant(outState, STATE_PRODUCT_VARIANT, productVariant);
        presenter.saveStateProductOthers(outState, STATE_OTHER_PRODUCTS, productOthers);
        presenter.saveStateVideoData(outState, STATE_VIDEO, videoData);
        presenter.saveStatePromoWidget(outState, STATE_PROMO_WIDGET, promoAttributes);
        presenter.saveStateAppBarCollapsed(outState, STATE_APP_BAR_COLLAPSED, isAppBarCollapsed);
    }

    @Override
    public void onRestoreState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreState");
        presenter.processStateData(savedInstanceState,getActivity());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        } else if (i == R.id.action_share) {
            if (productData != null) {
                ShareData shareData = ShareData.Builder.aShareData()
                        .setName(productData.getInfo().getProductName())
                        .setDescription(productData.getInfo().getProductDescription())
                        .setImgUri(productData.getProductImages().get(0).getImageSrc())
                        .setPrice(productData.getInfo().getProductPrice())
                        .setUri(productData.getInfo().getProductUrl())
                        .setType(ShareData.PRODUCT_TYPE)
                        .setId(productData.getInfo().getProductId().toString())
                        .build();
                onProductShareClicked(shareData);
            }
            return true;
        } else if (i == R.id.action_cart) {
            if (!SessionHandler.isV4Login(getActivity())) {
                Intent intent = ((PdpRouter) MainApplication.getAppContext()).getLoginIntent
                        (getActivity());
                navigateToActivityRequest(intent, ProductDetailFragment.REQUEST_CODE_LOGIN);
                if (productData.getInfo().getHasVariant()) UnifyTracking.eventClickCartVariant(generateVariantString());
            } else {
                startActivity(TransactionCartRouter.createInstanceCartActivity(getActivity()));
                if (productData.getInfo().getHasVariant()) UnifyTracking.eventClickCartVariant(generateVariantString());
            }
            return true;
        } else if (i == R.id.action_report) {
            onProductReportClicked();
            return true;
        } else if (i == R.id.action_warehouse) {
            presenter.requestMoveToWarehouse(context, productData.getInfo().getProductId());
            return true;
        } else if (i == R.id.action_etalase) {
            presenter.requestMoveToEtalase(context, productData.getInfo().getProductId());
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
                if (SessionHandler.isV4Login(getActivity())) presenter.requestProductDetail(context, productPass, RE_REQUEST, true, useVariant);
                break;
            case REQUEST_VARIANT:
                if (data.getParcelableExtra(KEY_PRODUCT_DETAIL_DATA) != null) {
                    productData = data.getParcelableExtra(KEY_PRODUCT_DETAIL_DATA);
                }
                if (productVariant == null && data.getParcelableExtra(KEY_VARIANT_DATA) != null) {
                    productVariant = data.getParcelableExtra(KEY_VARIANT_DATA);
                }
                if (data!=null && data.getParcelableExtra(KEY_LEVEL1_SELECTED)!=null && data.getParcelableExtra(KEY_LEVEL1_SELECTED) instanceof Option) {
                    variantLevel1 = data.getParcelableExtra(KEY_LEVEL1_SELECTED);
                    if (data.getParcelableExtra(KEY_LEVEL2_SELECTED)!=null && data.getParcelableExtra(KEY_LEVEL2_SELECTED) instanceof Option) {
                        variantLevel2 = data.getParcelableExtra(KEY_LEVEL2_SELECTED);
                    }
                    priceSimulationView.updateVariant(generateVariantString());
                    if (productVariant != null) {
                        pictureView.renderData(productData);
                        headerInfoView.renderData(productData);
                        shopInfoView.renderData(productData);
                        presenter.updateRecentView(context,productData.getInfo().getProductId());
                        ratingTalkCourierView.renderData(productData);
                        latestTalkView.renderData(productData);
                        buttonBuyView.updateButtonForVariantProduct(productVariant.getChildFromProductId(
                                productData.getInfo().getProductId()).isIsBuyable(),productData);
                        updateWishListStatus(productData.getInfo().getProductAlreadyWishlist());
                        productPass.setProductId(Integer.toString(productData.getInfo().getProductId()));
                    }
                    if (resultCode==VariantActivity.SELECTED_VARIANT_RESULT_TO_BUY) {
                        onBuyClick(SOURCE_BUTTON_BUY_VARIANT);
                    } else if (resultCode==VariantActivity.KILL_PDP_BACKGROUND) {
                        getActivity().finish();
                    }
                }
            default:
                break;
        }
    }

    public String generateVariantString() {
        String variantText = variantLevel1 !=null ? variantLevel1.getValue() : "";
        if (variantLevel2!=null && variantLevel2 instanceof Option) {
            variantText+= (", "+((Option) variantLevel2).getValue());
        }
        return variantText;
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
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.stopIndexingApp(appIndexHandler);
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
                presenter.requestProductDetail(context, productPass, INIT_REQUEST, false, useVariant);
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
        if (getActivity().getApplication() instanceof TkpdCoreRouter) {
            Intent intent = ((TkpdCoreRouter) getActivity().getApplication()).goToEditProduct(context, isEdit, productId);
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
        presenter.onPromoAdsClicked(getActivity(), productData.getShopInfo().getShopId(),
                productData.getInfo().getProductId(), SessionHandler.getLoginID(getActivity()));
    }

    @Override
    public void showPromoWidget(PromoAttributes promoAttributes) {
        this.promoAttributes = promoAttributes;
        this.promoWidgetView.renderData(promoAttributes);
    }

    @Override
    public void onPromoWidgetCopied() {
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, context.getString(R.string.title_copied),
                Snackbar.LENGTH_LONG);
        snackbar.setAction(context.getString(R.string.close), new View.OnClickListener() {
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
            headerInfoView.renderProductCampaign(productData.getCampaign());
        }
    }

    @Override
    public void addProductVariant(ProductVariant productVariant) {
        this.productVariant=productVariant;
        this.priceSimulationView.addProductVariant(productVariant,productData);
        if (variantLevel1!=null && variantLevel1 instanceof Option) {
            priceSimulationView.updateVariant(generateVariantString());
        }
        int defaultChild =  productVariant.getParentId() == productData.getInfo().getProductId()
                ?  productVariant.getDefaultChild() : productData.getInfo().getProductId();
        buttonBuyView.updateButtonForVariantProduct(productVariant.getChildFromProductId(
                defaultChild).isIsBuyable(),productData);

        startShowCase();

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
                    stateCollapsing = FROM_COLLAPSED;
                } else if (intColor < SCROLL_ELEVATION + toolbar.getHeight() && isAdded() && stateCollapsing == FROM_COLLAPSED) {
                    initStatusBarDark();
                    initToolbarTransparant();
                    if (productData != null && productData.getInfo().getProductAlreadyWishlist() != null) {
                        fabWishlist.show();
                    }
                    stateCollapsing = FROM_EXPANDED;
                }
            }
        };
    }

    private void initToolbarLight() {
        if (isAdded()) {
            collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(context, R.color.grey_toolbar_icon));
            collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
            toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.grey_toolbar_icon));
            toolbar.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);
            if (menu != null && menu.size() > 2) {
                menu.findItem(R.id.action_share).setIcon(ContextCompat.getDrawable(context, R.drawable.icon_share));
                LocalCacheHandler Cache = new LocalCacheHandler(getActivity(), DrawerHelper.DRAWER_CACHE);
                int CartCache = Cache.getInt(DrawerNotification.IS_HAS_CART);
                if (CartCache > 0) {
                    menu.findItem(R.id.action_cart).setIcon(ContextCompat.getDrawable(context, R.drawable.icon_cart_notif));
                } else {
                    menu.findItem(R.id.action_cart).setIcon(ContextCompat.getDrawable(context, R.drawable.icon_cart));
                }
            }
            toolbar.setOverflowIcon(ContextCompat.getDrawable(context, R.drawable.icon_more));
        }
    }

    private void initToolbarTransparant() {
        if (isAdded()) {
            collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(context, R.color.white));
            collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
            toolbar.setBackgroundColor(Color.TRANSPARENT);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back_white);
            if (menu != null && menu.size() > 1) {
                menu.findItem(R.id.action_share).setIcon(ContextCompat.getDrawable(context, R.drawable.icon_share_white));
                LocalCacheHandler Cache = new LocalCacheHandler(getActivity(), DrawerHelper.DRAWER_CACHE);
                int CartCache = Cache.getInt(DrawerNotification.IS_HAS_CART);
                if (CartCache > 0) {
                    menu.findItem(R.id.action_cart).setIcon(ContextCompat.getDrawable(context, R.drawable.cart_active_white));
                } else {
                    menu.findItem(R.id.action_cart).setIcon(ContextCompat.getDrawable(context, R.drawable.icon_cart_white));
                }
            }
            toolbar.setOverflowIcon(ContextCompat.getDrawable(context, R.drawable.icon_more_white));
        }
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
    public boolean isSellerApp() {
        return GlobalConfig.isSellerApp();
    }

    private void startShowCase() {
        final String showCaseTag = ProductInfoActivity.class.getName()+TAG_SHOWCASE_VARIANT;
        if (ShowCasePreference.hasShown(getActivity(), showCaseTag) || showCaseDialog != null) {
            return;
        }
        showCaseDialog = createShowCase();
        showCaseDialog.setShowCaseStepListener(new ShowCaseDialog.OnShowCaseStepListener() {
            @Override
            public boolean onShowCaseGoTo(int previousStep, int nextStep, ShowCaseObject showCaseObject) {
                return false;
            }
        });

        Rect rectToShowCase = new Rect();
        priceSimulationView.getGlobalVisibleRect(rectToShowCase);

        ArrayList<ShowCaseObject> showCaseObjectList = new ArrayList<>();
        showCaseObjectList.add(new ShowCaseObject(
                priceSimulationView,
                getResources().getString(R.string.product_variant),
                getResources().getString(R.string.product_variant_onboarding),
                ShowCaseContentPosition.TOP).withCustomTarget(new int[]{ rectToShowCase.left + SHOWCASE_MARGIN,
                rectToShowCase.top - 50, rectToShowCase.right - SHOWCASE_MARGIN, rectToShowCase.top + SHOWCASE_HEIGHT}));
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

}
