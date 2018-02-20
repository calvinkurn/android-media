package com.tokopedia.tkpdpdp.presenter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.appsflyer.AFInAppEventType;
import com.google.android.gms.appindexing.Action;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.PaymentTracking;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.Product;
import com.tokopedia.core.analytics.nishikino.model.ProductDetail;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.product.facade.NetworkParam;
import com.tokopedia.core.product.interactor.CacheInteractor;
import com.tokopedia.core.product.interactor.CacheInteractorImpl;
import com.tokopedia.core.product.interactor.RetrofitInteractor;
import com.tokopedia.core.product.interactor.RetrofitInteractor.DiscussionListener;
import com.tokopedia.core.product.interactor.RetrofitInteractor.MostHelpfulListener;
import com.tokopedia.core.product.interactor.RetrofitInteractorImpl;
import com.tokopedia.core.product.model.etalase.Etalase;
import com.tokopedia.core.product.model.goldmerchant.VideoData;
import com.tokopedia.core.product.model.productdetail.ProductCampaign;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.discussion.LatestTalkViewModel;
import com.tokopedia.core.product.model.productdetail.mosthelpful.Review;
import com.tokopedia.core.product.model.productdetail.promowidget.DataPromoWidget;
import com.tokopedia.core.product.model.productdetail.promowidget.PromoAttributes;
import com.tokopedia.core.product.model.productdink.ProductDinkData;
import com.tokopedia.core.product.model.productother.ProductOther;
import com.tokopedia.core.router.OldSessionRouter;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.router.discovery.DetailProductRouter;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.router.transactionmodule.passdata.ProductCartPass;
import com.tokopedia.core.router.transactionmodule.sharedata.AddToCartRequest;
import com.tokopedia.core.router.transactionmodule.sharedata.AddToCartResult;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.talk.talkproduct.activity.TalkProductActivity;
import com.tokopedia.core.util.AppIndexHandler;
import com.tokopedia.core.util.DeepLinkUtils;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.tkpdpdp.PreviewProductImageDetail;
import com.tokopedia.tkpdpdp.ProductInfoActivity;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.dialog.DialogToEtalase;
import com.tokopedia.tkpdpdp.fragment.ProductDetailFragment;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.tokopedia.core.network.apiservices.galadriel.GaladrielApi.VALUE_TARGET_GOLD_MERCHANT;
import static com.tokopedia.core.network.apiservices.galadriel.GaladrielApi.VALUE_TARGET_GUEST;
import static com.tokopedia.core.network.apiservices.galadriel.GaladrielApi.VALUE_TARGET_LOGIN_USER;
import static com.tokopedia.core.network.apiservices.galadriel.GaladrielApi.VALUE_TARGET_MERCHANT;
import static com.tokopedia.core.product.model.productdetail.ProductInfo.PRD_STATE_ACTIVE;
import static com.tokopedia.core.product.model.productdetail.ProductInfo.PRD_STATE_PENDING;
import static com.tokopedia.core.product.model.productdetail.ProductInfo.PRD_STATE_WAREHOUSE;

/**
 * ProductDetailPresenterImpl
 * Created by ANGGA on 11/2/2015.
 */
public class ProductDetailPresenterImpl implements ProductDetailPresenter {

    public static final String TAG = ProductDetailPresenterImpl.class.getSimpleName();
    public static final String CACHE_PROMOTION_PRODUCT = "CACHE_PROMOTION_PRODUCT";
    private static final String PRODUCT_NAME = "CACHE_PRODUCT_NAME";
    private static final String DATE_EXPIRE = "CACHE_EXPIRED_DATE";
    private static final String IS_UNPROMOTED_PRODUCT = "0";

    private static final int SHOP_IS_OFFICIAL_TRUE = 1;
    private static final String NON_LOGIN_USER_ID = "0";
    private static final String OFFICIAL_STORE_TYPE = "os";
    private static final String MERCHANT_TYPE = "merchant";


    private ProductDetailView viewListener;
    private RetrofitInteractor retrofitInteractor;
    private CacheInteractor cacheInteractor;
    private int counter = 0;
    LocalCacheHandler cacheHandler;
    DateFormat df;

    public ProductDetailPresenterImpl(ProductDetailView viewListener) {
        this.viewListener = viewListener;
        this.retrofitInteractor = new RetrofitInteractorImpl();
        this.cacheInteractor = new CacheInteractorImpl();
        this.df = new SimpleDateFormat("dd/MM/yyyy hh:mm");
    }

    @Override
    public void initRetrofitInteractor() {
        this.retrofitInteractor = new RetrofitInteractorImpl();
    }

    @Override
    public void processDataPass(@NonNull ProductPass productPass) {
        if (productPass.haveBasicData()) viewListener.renderTempProductData(productPass);
    }

    @Override
    public void processToShopInfo(@NonNull Context context, @NonNull Bundle bundle) {
        Intent intent = new Intent(context, ShopInfoActivity.class);
        intent.putExtras(bundle);
        viewListener.navigateToActivityRequest(intent,
                ProductDetailFragment.REQUEST_CODE_SHOP_INFO);
    }

    @Override
    public void processToLogin(@NonNull Context context, @NonNull Bundle bundle) {
        Intent intent = OldSessionRouter.getLoginActivityIntent(context);
        intent.putExtra(Session.WHICH_FRAGMENT_KEY,
                TkpdState.DrawerPosition.LOGIN);
        viewListener.navigateToActivityRequest(intent, ProductDetailFragment.REQUEST_CODE_LOGIN);
    }

    @Override
    public void processToCart(@NonNull Activity context, @NonNull ProductCartPass data) {
        sendAppsFlyerCheckout(context, data);

        if (context.getApplication() instanceof PdpRouter) {
            ((PdpRouter) context.getApplication()).addToCartProduct(
                    new AddToCartRequest.Builder()
                            .productId(Integer.parseInt(data.getProductId()))
                            .notes("")
                            .quantity(data.getMinOrder())
                            .shopId(Integer.parseInt(data.getShopId()))
                            .build()
            ).subscribeOn(Schedulers.newThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<AddToCartResult>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            viewListener.showToastMessage(e.getMessage());
                        }

                        @Override
                        public void onNext(AddToCartResult addToCartResult) {
                            if (addToCartResult.isSuccess())
                                viewListener.renderAddToCartSuccess(addToCartResult.getMessage());
                            else
                                viewListener.showToastMessage(addToCartResult.getMessage());
                        }
                    });
        }

//        viewListener.navigateToActivity(
//                TransactionAddToCartRouter.createInstanceAddToCartActivity(context, data)
//        );
        UnifyTracking.eventPDPCart();
    }

    @Override
    public void processToTalk(@NonNull Context context, @NonNull Bundle bundle) {
        UnifyTracking.eventPDPTalk();
        Intent intent = new Intent(context, TalkProductActivity.class);
        intent.putExtras(bundle);
        viewListener.navigateToActivityRequest(intent,
                ProductDetailFragment.REQUEST_CODE_TALK_PRODUCT);
    }

    @Override
    public void processToReputation(@NonNull Context context, String productId, String productName) {
        UnifyTracking.eventPDPReputation();
        if (context.getApplicationContext() instanceof PdpRouter) {
            Intent intent = ((PdpRouter) context.getApplicationContext())
                    .getProductReputationIntent(context, productId, productName);
            viewListener.navigateToActivity(intent);
        }
    }

    @Override
    public void processToProductInfo(@NonNull Context activity, @NonNull Bundle bundle) {
        Intent intent = new Intent(activity, ProductInfoActivity.class);
        intent.putExtras(bundle);
        viewListener.navigateToActivity(intent);
        viewListener.closeView();
    }

    @Override
    public void processToPicturePreview(@NonNull Context context, @NonNull Bundle bundle) {
        Intent intent = new Intent(context, PreviewProductImageDetail.class);
        intent.putExtras(bundle);
        viewListener.navigateToActivity(intent);
    }

    @Override
    public void processToBrowseProduct(@NonNull Context context, @NonNull Bundle bundle) {
        Intent intent = BrowseProductRouter.getIntermediaryIntent(context);
        intent.putExtras(bundle);
        viewListener.navigateToActivity(intent);
    }

    @Override
    public void processToCreateShop(@NonNull Context context) {
        Intent intent;
        if (SessionHandler.isV4Login(context)) {
            intent = SellerRouter.getActivityShopCreateEdit(context);
            viewListener.navigateToActivity(intent);
        } else {
            intent = OldSessionRouter.getLoginActivityIntent(context);
            intent.putExtra(Session.WHICH_FRAGMENT_KEY,
                    TkpdState.DrawerPosition.LOGIN);
            viewListener.navigateToActivityRequest(intent,
                    ProductDetailFragment.REQUEST_CODE_LOGIN);
        }
    }

    @Override
    public void reportProduct(@NonNull Context context) {
        if (SessionHandler.isV4Login(context)) {
            UnifyTracking.eventPDPReport();
            viewListener.showReportDialog();
        } else {
            UnifyTracking.eventPDPReportNotLogin();
            Intent intent = OldSessionRouter.getLoginActivityIntent(context);
            intent.putExtra(Session.WHICH_FRAGMENT_KEY,
                    TkpdState.DrawerPosition.LOGIN);
            viewListener.navigateToActivityRequest(intent, ProductDetailFragment.REQUEST_CODE_LOGIN);
        }
    }

    @Override
    public void processGetGTMTicker() {
        if (TrackingUtils.getGtmString(AppEventTracking.GTM.TICKER_PDP).equalsIgnoreCase("true")) {
            String message = TrackingUtils.getGtmString(AppEventTracking.GTM.TICKER_PDP_TEXT);
            viewListener.showTickerGTM(message);
        } else {
            viewListener.hideTickerGTM();
        }
    }

    @Override
    public void processToSendMessage(@NonNull Context context, @NonNull Intent sendMessageIntent) {
        Intent intent;
        if (SessionHandler.isV4Login(context)) {
            viewListener.navigateToActivity(sendMessageIntent);
        } else {
            intent = OldSessionRouter.getLoginActivityIntent(context);
            intent.putExtra(Session.WHICH_FRAGMENT_KEY,
                    TkpdState.DrawerPosition.LOGIN);
            viewListener.navigateToActivityRequest(intent,
                    ProductDetailFragment.REQUEST_CODE_LOGIN);
        }
        UnifyTracking.eventPDPSendMessage();
        UnifyTracking.eventPDPSendChat();
    }

    @Override
    public void processToEditProduct(@NonNull Context context, @NonNull Bundle bundle) {
        boolean isEdit = bundle.getBoolean("is_edit");
        String productId = bundle.getString("product_id");
        viewListener.moveToEditFragment(isEdit, productId);
    }

    @Override
    public void sendAppsFlyerCheckout(@NonNull final Context context, @NonNull final ProductCartPass param) {
        PaymentTracking.checkoutEventAppsflyer(param);
    }

    @Override
    public void sendAnalytics(@NonNull ProductDetailData successResult) {
        Product product = new Product();
        product.setProductID("" + successResult.getInfo().getProductId());
        product.setProductName("" + successResult.getInfo().getProductName());
        product.setPrice(CurrencyFormatHelper.convertRupiahToInt(successResult.getInfo().getProductPrice()));
        product.setQty(successResult.getInfo().getProductCatalogName());

        ProductDetail pdt = new ProductDetail();
        pdt.addProduct(product.getProduct());

        UnifyTracking.eventPDPDetail(pdt);
        TrackingUtils.sendMoEngageOpenProductEvent(successResult);

        if (successResult.getShopInfo().getShopIsOfficial() == 1) {
            ScreenTracking.eventOfficialStoreScreenAuth(successResult.getShopInfo().getShopId(), AppScreen.SCREEN_OFFICIAL_STORE);
        }

    }

    @Override
    public void requestProductDetail(@NonNull final Context context,
                                     @NonNull final ProductPass productPass,
                                     final int type,
                                     final boolean forceNetwork) {
        if (type == ProductDetailFragment.INIT_REQUEST) viewListener.showProgressLoading();
        if (forceNetwork) {
            getProductDetailFromNetwork(context, productPass);
        } else {
            getProductDetailFromCache(productPass,
                    new CacheInteractor.GetProductDetailCacheListener() {
                        @Override
                        public void onSuccess(ProductDetailData productDetailData) {
                            viewListener.onProductDetailLoaded(productDetailData);
                            viewListener.hideProgressLoading();
                            viewListener.refreshMenu();
                            requestOtherProducts(context,
                                    NetworkParam.paramOtherProducts(productDetailData));
                            setGoldMerchantFeatures(context, productDetailData);
                            getProductCampaign(context, productDetailData.getInfo().getProductId().toString());
                            getTalk(context, productDetailData.getInfo().getProductId().toString(), productDetailData.getShopInfo().getShopId());
                            getMostHelpfulReview(context, productDetailData.getInfo().getProductId
                                    ().toString(), productDetailData.getShopInfo().getShopId());
                            String shopType = productDetailData.getShopInfo().getShopIsOfficial() == SHOP_IS_OFFICIAL_TRUE ? OFFICIAL_STORE_TYPE : MERCHANT_TYPE;
                            if (!GlobalConfig.isSellerApp()) {
                                getPromoWidget(context, generatePromoTargetType(productDetailData, context),
                                        SessionHandler.isV4Login(context) ? SessionHandler.getLoginID(context) : NON_LOGIN_USER_ID, shopType);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            getProductDetailFromNetwork(context, productPass);
                            e.printStackTrace();
                        }
                    });
        }
    }

    @Override
    public void requestOtherProducts(@NonNull final Context context, final Map<String, String> param) {
        cacheInteractor.getOtherProductCache(param.get("-id"),
                new CacheInteractor.ProductOtherCacheListener() {
                    @Override
                    public void onSuccess(List<ProductOther> data) {
                        viewListener.onOtherProductLoaded(data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getOtherProductFromNetwork(context, param);
                    }
                });
    }

    @Override
    public void requestFaveShop(@NonNull Context context, @NonNull final String shopId, final Integer productId) {
        if (SessionHandler.isV4Login(context)) {
            retrofitInteractor.favoriteShop(context,
                    NetworkParam.paramFaveShop(shopId),
                    new RetrofitInteractor.FaveListener() {
                        @Override
                        public void onSuccess(boolean status) {
                            if (status) {
                                viewListener.onShopFavoriteUpdated(1);
                                viewListener.actionSuccessAddFavoriteShop(shopId);
                                cacheInteractor.deleteProductDetail(productId);
                            }
                        }

                        @Override
                        public void onError(String error) {
                            viewListener.showFaveShopRetry();
                        }
                    });
        } else {
            Intent intent = OldSessionRouter.getLoginActivityIntent(context);
            intent.putExtra(Session.WHICH_FRAGMENT_KEY,
                    TkpdState.DrawerPosition.LOGIN);
            viewListener.navigateToActivityRequest(intent,
                    ProductDetailFragment.REQUEST_CODE_LOGIN);
        }
        UnifyTracking.eventPDPFavorite();
    }

    @Override
    public void requestMoveToEtalase(@NonNull final Context context, final int productId) {
        final TkpdProgressDialog mProgressDialog = new TkpdProgressDialog(context,
                TkpdProgressDialog.NORMAL_PROGRESS);
        mProgressDialog.showDialog();
        retrofitInteractor.getShopEtalase(context, new HashMap<String, String>(),
                new RetrofitInteractor.GetEtalaseListener() {
                    @Override
                    public void onSuccess(List<Etalase> etalases) {
                        mProgressDialog.dismiss();
                        viewListener.showDialog(createToEtalaseDialog(context, productId, etalases));
                    }

                    @Override
                    public void onError(String error) {
                        mProgressDialog.dismiss();
                    }
                });
    }


    @Override
    public void requestMoveToWarehouse(@NonNull final Context context, final int productId) {
        final TkpdProgressDialog mProgressDialog = new TkpdProgressDialog(context,
                TkpdProgressDialog.NORMAL_PROGRESS);
        mProgressDialog.showDialog();
        retrofitInteractor.moveToWarehouse(context, NetworkParam.paramToWarehouse(productId),
                new RetrofitInteractor.ToWarehouseListener() {
                    @Override
                    public void onSuccess(boolean success) {
                        mProgressDialog.dismiss();
                        if (success) {
                            viewListener.showToastMessage(context
                                    .getString(R.string.title_sold_out_action));
                            requestProductDetail(context, ProductPass.Builder.aProductPass()
                                            .setProductId(productId).build(),
                                    ProductDetailFragment.RE_REQUEST, true);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        viewListener.showToastMessage(error);
                        mProgressDialog.dismiss();
                    }
                });
    }

    @Override
    public void requestPromoteProduct(@NonNull final Context context,
                                      @NonNull final ProductDetailData product) {
        cacheHandler = new LocalCacheHandler(context, CACHE_PROMOTION_PRODUCT);

        if (cacheHandler.isExpired()) {
            viewListener.showProgressLoading();
            retrofitInteractor.promoteProduct(context,
                    NetworkParam.paramPromote(product.getInfo().getProductId()),
                    new RetrofitInteractor.PromoteProductListener() {
                        @Override
                        public void onSuccess(ProductDinkData data) {
                            viewListener.hideProgressLoading();
                            String productName = data.getProductName();
                            if (productName == null) productName = "WS BELUM SIAP";
                            cacheHandler.putString(PRODUCT_NAME, productName);
                            if (data.getIsDink() == 1) {
                                viewListener.showDinkSuccess(MethodChecker.fromHtml(productName).toString());
                            } else {
                                viewListener.showDinkFailed(MethodChecker.fromHtml(productName).toString(),
                                        data.getExpiry());
                            }

                            String expireDate = data.getExpiry();
                            cacheHandler.putString(DATE_EXPIRE, data.getExpiry());
                            Date expireD;

                            try {
                                expireD = df.parse(expireDate);
                                Long curr_time = System.currentTimeMillis() / 1000;
                                Long expireTime = expireD.getTime() / 1000;
                                cacheHandler.setExpire(Integer.parseInt(String.valueOf(expireTime - curr_time)));

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(String error) {
                            viewListener.hideProgressLoading();
                            viewListener.showToastMessage(error);
                        }
                    });
        } else {
            viewListener.showDinkFailed(MethodChecker.fromHtml(cacheHandler.getString(PRODUCT_NAME, "")).toString(),
                    cacheHandler.getString(DATE_EXPIRE, ""));
        }
    }

    @Override
    public void processResultEdit(int resultCode, Intent data) {
        if (isResultOK(resultCode)) viewListener.onProductHasEdited();
    }

    @Override
    public void processResultShop(int resultCode, Intent data) {
        if (isIntentOK(data) & isResultOK(resultCode)) {
            int statFave = data.getIntExtra("state_fav", 5);
            viewListener.onShopFavoriteUpdated(statFave);
        }
    }

    @Override
    public void processResultTalk(int resultCode, Intent data) {
        if (isResultOK(resultCode) & isIntentOK(data)) {
            if (data.getExtras() != null && data.getBooleanExtra(
                    TalkProductActivity.RESULT_TALK_HAS_ADDED, false
            )) {
                viewListener.onProductTalkUpdated();
            }
        }
    }

    @Override
    public void startIndexingApp(@NonNull AppIndexHandler handler, @NonNull ProductDetailData data) {
        handler.setAction(Action.TYPE_VIEW,
                MethodChecker.fromHtml(data.getInfo().getProductName()).toString(),
                Uri.parse(data.getInfo().getProductUrl()),
                DeepLinkUtils.generateAppUri(data.getInfo().getProductUrl()));
        handler.startIndexing();
    }

    @Override
    public void stopIndexingApp(@NonNull AppIndexHandler handler) {
        handler.stopIndexing();
    }

    @Override
    public void onDestroyView(@NonNull Context context) {
        retrofitInteractor.unSubscribeObservable();
        cacheHandler = null;
        df = null;
    }

    @Override
    public void prepareOptionMenu(Menu menu, Context context, ProductDetailData productData) {
        MenuItem menuShare = menu.findItem(R.id.action_share);
        MenuItem menuCart = menu.findItem(R.id.action_cart);
        MenuItem report = menu.findItem(R.id.action_report);
        MenuItem warehouse = menu.findItem(R.id.action_warehouse);
        MenuItem etalase = menu.findItem(R.id.action_etalase);
        boolean isSellerApp = GlobalConfig.isSellerApp();
        if (productData != null) {
            menuShare.setVisible(true);
            menuShare.setEnabled(true);
            if (!productData.getShopInfo().getShopId().equals(SessionHandler.getShopID(context))) {
                if (isSellerApp) {
                    menuCart.setVisible(false);
                    menuCart.setEnabled(false);
                } else {
                    menuCart.setVisible(true);
                    menuCart.setEnabled(true);
                    if (SessionHandler.isV4Login(context)) {
                        LocalCacheHandler Cache = new LocalCacheHandler(context, TkpdCache.NOTIFICATION_DATA);
                        int CartCache = Cache.getInt(TkpdCache.Key.IS_HAS_CART);
                        if (CartCache > 0 && menuCart != null) {
                            menuCart.setIcon(R.drawable.cart_active_white);
                        }
                    }
                }

                if (productData.getInfo().getProductStatus().equals(PRD_STATE_WAREHOUSE)) {
                    report.setVisible(false);
                    report.setEnabled(false);
                } else {
                    report.setVisible(true);
                    report.setEnabled(true);
                }

                warehouse.setVisible(false);
                warehouse.setEnabled(false);
                etalase.setVisible(false);
                etalase.setEnabled(false);
            } else {
                menuCart.setVisible(false);
                menuCart.setEnabled(false);
                report.setVisible(false);
                report.setEnabled(false);
                switch (productData.getInfo().getProductStatus()) {
                    case PRD_STATE_ACTIVE:
                        etalase.setVisible(false);
                        etalase.setEnabled(false);
                        break;
                    case PRD_STATE_WAREHOUSE:
                        warehouse.setVisible(false);
                        warehouse.setEnabled(false);
                        break;
                    case PRD_STATE_PENDING:
                        etalase.setVisible(false);
                        etalase.setEnabled(false);
                        warehouse.setVisible(false);
                        warehouse.setEnabled(false);
                        break;
                }
            }
        } else {
            menuShare.setVisible(false);
            menuShare.setEnabled(false);
            menuCart.setVisible(false);
            menuCart.setEnabled(false);
            report.setVisible(false);
            report.setEnabled(false);
            warehouse.setVisible(false);
            warehouse.setEnabled(false);
            etalase.setVisible(false);
            etalase.setEnabled(false);
            warehouse.setVisible(false);
            warehouse.setEnabled(false);
        }
    }

    @Override
    public void processWishList(@NonNull Context context, @NonNull ProductDetailData product) {
        if (SessionHandler.isV4Login(context)) {
            if (counter < 6) {
                switch (product.getInfo().getProductAlreadyWishlist()) {
                    case 0:
                        requestAddWishList(context, product.getInfo().getProductId());
                        sendAppsFlyerData(context, product, AFInAppEventType.ADD_TO_WISH_LIST);
                        sendButtonClickEvent(context, product);
                        break;
                    case 1:
                        requestRemoveWishList(context, product.getInfo().getProductId());
                        break;
                }
                counter++;
            } else {
                viewListener.showToastMessage("Tunggu beberapa saat lagi");
            }
        } else {
            cacheInteractor.deleteProductDetail(product.getInfo().getProductId());
            Intent intent = OldSessionRouter.getLoginActivityIntent(context);
            intent.putExtra(Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
            intent.putExtra("product_id", String.valueOf(product.getInfo().getProductId()));
            viewListener.navigateToActivityRequest(intent, ProductDetailFragment.REQUEST_CODE_LOGIN);
        }
    }

    @Override
    public void saveStateProductDetail(Bundle outState, String key, ProductDetailData value) {
        if (value != null) {
            outState.putParcelable(key, value);
        }
    }

    @Override
    public void saveStateProductOthers(Bundle outState, String key, List<ProductOther> values) {
        if (values != null) outState.putParcelableArrayList(key, new ArrayList<Parcelable>(values));
    }

    @Override
    public void saveStateVideoData(Bundle outState, String key, VideoData value) {
        if (value != null) outState.putParcelable(key, value);
    }

    @Override
    public void saveStateProductCampaign(Bundle outState, String key, ProductCampaign value) {
        if (value != null) outState.putParcelable(key, value);
    }

    @Override
    public void saveStatePromoWidget(Bundle outState, String key, PromoAttributes promoAttributes) {
        if (promoAttributes != null) outState.putParcelable(key, promoAttributes);
    }

    @Override
    public void saveStateAppBarCollapsed(Bundle outState, String key, boolean isAppBarCollapsed) {
        outState.putBoolean(key, isAppBarCollapsed);
    }

    @Override
    public void processStateData(Bundle savedInstanceState) {
        ProductDetailData productData = savedInstanceState
                .getParcelable(ProductDetailFragment.STATE_DETAIL_PRODUCT);
        List<ProductOther> productOthers = savedInstanceState
                .getParcelableArrayList(ProductDetailFragment.STATE_OTHER_PRODUCTS);
        VideoData videoData = savedInstanceState.getParcelable(ProductDetailFragment.STATE_VIDEO);
        ProductCampaign productCampaign = savedInstanceState.getParcelable(ProductDetailFragment.STATE_PRODUCT_CAMPAIGN);
        PromoAttributes promoAttributes = savedInstanceState.getParcelable(ProductDetailFragment.STATE_PROMO_WIDGET);
        boolean isAppBarCollapsed = savedInstanceState.getBoolean(ProductDetailFragment.STATE_APP_BAR_COLLAPSED);

        if (productData != null & productOthers != null) {
            viewListener.onProductDetailLoaded(productData);
            viewListener.onOtherProductLoaded(productOthers);
            if (videoData != null) {
                viewListener.loadVideo(videoData);
            }
        }

        if (productCampaign != null) {
            viewListener.showProductCampaign(productCampaign);
        }

        if (promoAttributes != null) {
            viewListener.showPromoWidget(promoAttributes);
        }

        viewListener.restoreIsAppBarCollapsed(isAppBarCollapsed);
    }

    @Override
    public void processToCatalog(Context context, String catalogId) {
        viewListener.navigateToActivity(DetailProductRouter.getCatalogDetailActivity(context, catalogId));
    }

    private static int getWishListIcon(int status) {
        if (status == 1) {
            return R.drawable.ic_wishlist_orange;
        } else {
            return R.drawable.ic_wishlist_white;
        }
    }

    private boolean isResultOK(int result) {
        return result == Activity.RESULT_OK;
    }

    private boolean isIntentOK(Intent data) {
        return data != null;
    }

    private Dialog createToEtalaseDialog(final Context context, final int productId,
                                         List<Etalase> etalases) {
        DialogToEtalase.Listener dialogListener =
                new DialogToEtalase.Listener() {
                    @Override
                    public void onNotSelected() {
                        viewListener.showToastMessage(context.getString(R.string.error_etalase));
                    }

                    @Override
                    public void onRequestAction(Map<String, String> param, final int productId) {
                        final TkpdProgressDialog mProgressDialog = new TkpdProgressDialog(context,
                                TkpdProgressDialog.NORMAL_PROGRESS);
                        mProgressDialog.showDialog();
                        retrofitInteractor.moveToEtalase(context, param,
                                new RetrofitInteractor.ToEtalaseListener() {
                                    @Override
                                    public void onSuccess(boolean success) {
                                        mProgressDialog.dismiss();
                                        if (success) {
                                            viewListener.showToastMessage
                                                    (context.getString(R.string.title_move_etalase));
                                            requestProductDetail(context,
                                                    ProductPass.Builder.aProductPass()
                                                            .setProductId(productId)
                                                            .build(),
                                                    ProductDetailFragment.RE_REQUEST, true);
                                        }
                                    }

                                    @Override
                                    public void onError(String error) {
                                        mProgressDialog.dismiss();
                                        viewListener.showToastMessage(error);
                                    }
                                });
                    }
                };
        return DialogToEtalase.Builder.aDialogToEtalase()
                .setContext(context)
                .setProductId(productId)
                .setEtalases(etalases)
                .setListener(dialogListener)
                .build();
    }

    private void requestAddWishList(final Context context, final Integer productId) {
        viewListener.loadingWishList();
        UnifyTracking.eventPDPWishlit();
        retrofitInteractor.addToWishList(context, productId,
                new RetrofitInteractor.AddWishListListener() {
                    @Override
                    public void onSuccess() {
                        viewListener.finishLoadingWishList();
                        viewListener.showSuccessWishlistSnackBar();
                        viewListener.updateWishListStatus(1);
                        viewListener.actionSuccessAddToWishlist(productId);
                        cacheInteractor.deleteProductDetail(productId);
                    }

                    @Override
                    public void onError(String error) {
                        viewListener.finishLoadingWishList();
                        viewListener.showWishListRetry(error);
                    }
                });
    }

    private void requestRemoveWishList(final Context context, final Integer productId) {
        viewListener.loadingWishList();
        retrofitInteractor.removeFromWishList(context, productId,
                new RetrofitInteractor.RemoveWishListListener() {
                    @Override
                    public void onSuccess() {
                        viewListener.finishLoadingWishList();
                        viewListener.showToastMessage(context
                                .getString(R.string.msg_remove_wishlist));
                        viewListener.updateWishListStatus(ProductDetailFragment.STATUS_NOT_WISHLIST);
                        viewListener.actionSuccessRemoveFromWishlist(productId);
                        cacheInteractor.deleteProductDetail(productId);
                    }

                    @Override
                    public void onError(String error) {
                        viewListener.finishLoadingWishList();
                        viewListener.showWishListRetry(error);
                    }
                });
    }

    public void getProductDetailFromCache(@NonNull final ProductPass productPass,
                                          @NonNull CacheInteractor.GetProductDetailCacheListener listener) {
        cacheInteractor.getProductDetailCache(productPass.getProductId(), listener);
    }

    public void getProductDetailFromNetwork(@NonNull final Context context,
                                            @NonNull final ProductPass productPass) {

        retrofitInteractor.getProductDetail(context, NetworkParam.paramProductDetailTest2(productPass),
                new RetrofitInteractor.ProductDetailListener() {
                    @Override
                    public void onSuccess(@NonNull ProductDetailData data) {
                        cacheInteractor.storeProductDetailCache(data.getInfo().getProductId().toString(), data);
                        viewListener.onProductDetailLoaded(data);
                        viewListener.hideProgressLoading();
                        viewListener.refreshMenu();
                        requestOtherProducts(context, NetworkParam.paramOtherProducts(data));
                        setGoldMerchantFeatures(context, data);
                        getProductCampaign(context, data.getInfo().getProductId().toString());
                        getMostHelpfulReview(context, data.getInfo().getProductId().toString(),
                                data.getShopInfo().getShopId());
                        getTalk(context, data.getInfo().getProductId().toString(), data.getShopInfo().getShopId());
                        String shopType = data.getShopInfo().getShopIsOfficial() == SHOP_IS_OFFICIAL_TRUE ? OFFICIAL_STORE_TYPE : MERCHANT_TYPE;
                        if (!GlobalConfig.isSellerApp()) {
                            getPromoWidget(context, generatePromoTargetType(data, context),
                                    SessionHandler.isV4Login(context) ? SessionHandler.getLoginID(context) : NON_LOGIN_USER_ID, shopType
                            );
                        }
                    }

                    @Override
                    public void onTimeout() {
                        viewListener.showProductDetailRetry();
                        viewListener.hideProgressLoading();
                    }

                    @Override
                    public void onError(String error) {
                        viewListener.showProductDetailRetry();
                        viewListener.hideProgressLoading();
                    }

                    @Override
                    public void onNullData() {
                        viewListener.hideProgressLoading();
                        viewListener.onNullData();
                    }

                    @Override
                    public void onReportServerProblem() {
                        viewListener.hideProgressLoading();
                        viewListener.showFullScreenError();
                    }
                });
    }

    public String generatePromoTargetType(ProductDetailData productData, Context context) {
        if (productData.getShopInfo().getShopIsOwner() == 1 && productData.getShopInfo().getShopIsGold() == 1) {
            return VALUE_TARGET_GOLD_MERCHANT;
        } else if (productData.getShopInfo().getShopIsOwner() == 1) {
            return VALUE_TARGET_MERCHANT;
        } else if (!TextUtils.isEmpty(SessionHandler.getLoginID(context))) {
            return VALUE_TARGET_LOGIN_USER;
        }
        return VALUE_TARGET_GUEST;
    }

    private void getOtherProductFromNetwork(Context context, final Map<String, String> param) {
        retrofitInteractor.getOtherProducts(context, param,
                new RetrofitInteractor.OtherProductListener() {
                    @Override
                    public void onSuccess(@NonNull List<ProductOther> datas) {
                        viewListener.onOtherProductLoaded(datas);
                        cacheInteractor.storeOtherProductCache(param.get("-id"), datas);
                    }

                    @Override
                    public void onTimeout(String message) {
                        viewListener.showProductOthersRetry();
                    }

                    @Override
                    public void onError(String error) {
                        viewListener.showProductOthersRetry();
                    }
                });
    }

    @Override
    public void sendAppsFlyerData(@NonNull final Context context, @NonNull final ProductDetailData successResult, @NonNull final String eventName) {
        ScreenTracking.sendAFPDPEvent(successResult, eventName);
    }

    @Override
    public void sendButtonClickEvent(@NonNull Context context, @NonNull ProductDetailData successResult) {
        UnifyTracking.eventPDPAddToWishlist(successResult.getInfo().getProductName());
        TrackingUtils.sendMoEngageAddWishlistEvent(successResult);
    }

    private void requestVideo(@NonNull Context context, @NonNull String productID) {
        retrofitInteractor.requestProductVideo(context, productID,
                new RetrofitInteractor.VideoLoadedListener() {
                    @Override
                    public void onSuccess(@NonNull VideoData data) {
                        viewListener.loadVideo(data);
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    private void setGoldMerchantFeatures(Context context, ProductDetailData productDetailData) {
        if (productDetailData.getShopInfo().getShopIsGold() == 1) {
            requestVideo(context, productDetailData.getInfo().getProductId().toString());
        }
    }

    public void getPromoWidget(final @NonNull Context context, @NonNull final String targetType, @NonNull final String userId, @NonNull final String shopType) {
        cacheInteractor.getPromoWidgetCache(targetType, userId, shopType, new CacheInteractor.GetPromoWidgetCacheListener() {
            @Override
            public void onSuccess(PromoAttributes result) {
                if (result.getCode() != null && result.getCodeHtml() != null && result.getShortCondHtml() != null
                        && result.getShortDescHtml() != null) {
                    viewListener.showPromoWidget(result);
                }
            }

            @Override
            public void onError() {
                retrofitInteractor.getPromo(context, targetType, userId, shopType,
                        new RetrofitInteractor.PromoListener() {
                            @Override
                            public void onSucccess(DataPromoWidget dataPromoWidget) {
                                cacheInteractor.storePromoWidget(targetType, userId, shopType, dataPromoWidget);
                                if (!dataPromoWidget.getPromoWidgetList().isEmpty()) {
                                    viewListener.showPromoWidget(dataPromoWidget.getPromoWidgetList().get(0).getPromoAttributes());
                                }
                            }

                            @Override
                            public void onError(String error) {
                            }
                        });
            }
        });
    }

    public void getProductCampaign(@NonNull Context context, @NonNull String id) {
        retrofitInteractor.getProductCampaign(context, id,
                new RetrofitInteractor.ProductCampaignListener() {
                    @Override
                    public void onSucccess(ProductCampaign productCampaign) {
                        viewListener.showProductCampaign(productCampaign);
                    }

                    @Override
                    public void onError(String error) {
                    }
                }
        );
    }

    public void getMostHelpfulReview(@NonNull Context context, @NonNull String productId, String
            shopId) {
        retrofitInteractor.getMostHelpfulReview(context, productId, shopId,
                new MostHelpfulListener() {
                    @Override
                    public void onSucccess(List<Review> reviews) {
                        if (reviews != null && reviews.size() > 0)
                            viewListener.showMostHelpfulReview(reviews);
                    }

                    @Override
                    public void onError(String error) {

                    }
                }
        );
    }

    public void getTalk(@NonNull final Context context, @NonNull final String productId, final String shopId) {
        retrofitInteractor.getProductDiscussion(context, productId, shopId,
                new DiscussionListener() {
                    @Override
                    public void onSucccess(LatestTalkViewModel latestTalkViewModel) {
                        if (latestTalkViewModel != null) {
                            if (latestTalkViewModel.getTalkCounterComment() > 0) {
                                String talkId = latestTalkViewModel.getTalkId();
                                getTalkComment(context, talkId, shopId, latestTalkViewModel);
                            } else {
                                viewListener.showLatestTalkView(latestTalkViewModel);
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    public void getTalkComment(@NonNull Context context,
                               @NonNull String talkId,
                               @NonNull String shopId,
                               final LatestTalkViewModel latestTalkViewModel) {
        retrofitInteractor.getProductTalkComment(context, talkId, shopId,
                new DiscussionListener() {
                    @Override
                    public void onSucccess(LatestTalkViewModel talkComment) {

                        if (talkComment != null) {
                            latestTalkViewModel.setCommentId(talkComment.getCommentId());
                            latestTalkViewModel.setCommentId(talkComment.getCommentId());
                            latestTalkViewModel.setCommentMessage(talkComment.getCommentMessage());
                            latestTalkViewModel.setCommentDate(talkComment.getCommentDate());
                            latestTalkViewModel.setCommentUserId(talkComment.getCommentUserId());
                            latestTalkViewModel.setCommentUserName(talkComment.getCommentUserName());
                            latestTalkViewModel.setCommentUserLabel(talkComment.getCommentUserLabel());
                            latestTalkViewModel.setCommentUserAvatar(talkComment.getCommentUserAvatar());

                            viewListener.showLatestTalkView(latestTalkViewModel);
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    @Override
    public void onPromoAdsClicked(final Context context, String shopId, final int itemId, final String userId) {
        retrofitInteractor.checkPromoAds(shopId, itemId, userId, new RetrofitInteractor.CheckPromoAdsListener() {
            @Override
            public void onSuccess(String adsId) {
                if (adsId.equals(IS_UNPROMOTED_PRODUCT)) {
                    openPromoteAds(context, String.format("%s?user_id=%s&item_id=%s", Constants.Applinks.SellerApp.TOPADS_PRODUCT_CREATE, userId, itemId));
                } else {
                    openPromoteAds(context, String.format("%s/%s?user_id=%s", Constants.Applinks.SellerApp.TOPADS_PRODUCT_DETAIL_CONSTS, adsId, userId));
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openPromoteAds(Context context, String url) {
        Intent topadsIntent = context.getPackageManager()
                .getLaunchIntentForPackage(GlobalConfig.PACKAGE_SELLER_APP);
        if (topadsIntent != null) {
            Intent intentActionView = new Intent(Intent.ACTION_VIEW);
            intentActionView.setData(Uri.parse(url));
            intentActionView.putExtra(Constants.EXTRA_APPLINK, url);
            PackageManager manager = context.getPackageManager();
            List<ResolveInfo> infos = manager.queryIntentActivities(intentActionView, 0);
            if (infos.size() > 0) {
                topadsIntent = intentActionView;
            }
            context.startActivity(topadsIntent);
        } else if (context.getApplicationContext() instanceof TkpdCoreRouter) {
            ((TkpdCoreRouter) context.getApplicationContext()).goToCreateMerchantRedirect(context);
            UnifyTracking.eventTopAdsSwitcher(AppEventTracking.Category.SWITCHER);
        }
    }
}
