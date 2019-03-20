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
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.appindexing.Action;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.abstraction.common.network.constant.ErrorNetMessage;
import com.tokopedia.abstraction.common.network.exception.HttpErrorException;
import com.tokopedia.abstraction.common.network.exception.ResponseDataNullException;
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.affiliatecommon.domain.GetProductAffiliateGqlUseCase;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.PaymentTracking;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.Product;
import com.tokopedia.core.analytics.nishikino.model.ProductDetail;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.entity.variant.Campaign;
import com.tokopedia.core.network.entity.variant.Child;
import com.tokopedia.core.network.entity.variant.ProductVariant;
import com.tokopedia.core.product.facade.NetworkParam;
import com.tokopedia.core.product.interactor.CacheInteractor;
import com.tokopedia.core.product.interactor.RetrofitInteractor;
import com.tokopedia.core.product.interactor.RetrofitInteractor.DiscussionListener;
import com.tokopedia.core.product.interactor.RetrofitInteractorImpl;
import com.tokopedia.core.product.model.etalase.Etalase;
import com.tokopedia.core.product.model.goldmerchant.VideoData;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.ShopShipment;
import com.tokopedia.core.product.model.productdetail.discussion.LatestTalkViewModel;
import com.tokopedia.core.product.model.productdetail.promowidget.DataPromoWidget;
import com.tokopedia.core.product.model.productdetail.promowidget.PromoAttributes;
import com.tokopedia.core.product.model.productdetail.promowidget.PromoWidget;
import com.tokopedia.core.product.model.productdink.ProductDinkData;
import com.tokopedia.core.product.model.productother.ProductOther;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.router.discovery.DetailProductRouter;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.router.transactionmodule.TransactionAddToCartRouter;
import com.tokopedia.core.router.transactionmodule.passdata.ProductCartPass;
import com.tokopedia.core.util.AppIndexHandler;
import com.tokopedia.core.util.DeepLinkUtils;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.gallery.domain.GetImageReviewUseCase;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.kotlin.util.ContainNullException;
import com.tokopedia.kotlin.util.NullCheckerKt;
import com.tokopedia.tkpdpdp.BuildConfig;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;
import com.tokopedia.tkpdpdp.PreviewProductImageDetail;
import com.tokopedia.tkpdpdp.ProductInfoActivity;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.courier.CourierViewData;
import com.tokopedia.tkpdpdp.dialog.DialogToEtalase;
import com.tokopedia.tkpdpdp.domain.GetMostHelpfulReviewUseCase;
import com.tokopedia.tkpdpdp.domain.GetWishlistCountUseCase;
import com.tokopedia.tkpdpdp.estimasiongkir.data.model.RatesEstimationModel;
import com.tokopedia.tkpdpdp.estimasiongkir.domain.interactor.GetRateEstimationUseCase;
import com.tokopedia.tkpdpdp.fragment.ProductDetailFragment;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;
import com.tokopedia.tkpdpdp.presenter.subscriber.AffiliateProductDataSubscriber;
import com.tokopedia.tkpdpdp.presenter.subscriber.ImageReviewSubscriber;
import com.tokopedia.tkpdpdp.presenter.subscriber.MostHelpfulReviewSubscriber;
import com.tokopedia.tkpdpdp.presenter.subscriber.WishlistCountSubscriber;
import com.tokopedia.tkpdpdp.revamp.ProductViewData;
import com.tokopedia.tkpdpdp.tracking.ProductPageTracking;
import com.tokopedia.topads.sourcetagging.data.repository.TopAdsSourceTaggingRepositoryImpl;
import com.tokopedia.topads.sourcetagging.data.source.TopAdsSourceTaggingDataSource;
import com.tokopedia.topads.sourcetagging.data.source.TopAdsSourceTaggingLocal;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsAddSourceTaggingUseCase;
import com.tokopedia.topads.sourcetagging.domain.repository.TopAdsSourceTaggingRepository;
import com.tokopedia.track.TrackApp;
import com.tokopedia.transaction.common.sharedata.AddToCartRequest;
import com.tokopedia.transaction.common.sharedata.AddToCartResult;
import com.tokopedia.transactiondata.entity.response.expresscheckout.profile.ProfileListGqlResponse;
import com.tokopedia.transactiondata.usecase.GetProfileListUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.wishlist.common.listener.WishListActionListener;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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

    public static final String OFFICIAL_STORE_TYPE = "os";
    public static final String MERCHANT_TYPE = "merchant";
    private static final String NON_LOGIN_USER_ID = "0";
    public static final int FIRST_PAGE = 0;
    public static final int TOTAL_IMAGE_FOR_PDP = 21;

    private final WishListActionListener wishListActionListener;
    private final GetProductAffiliateGqlUseCase getProductAffiliateGqlUseCase;
    private final GetMostHelpfulReviewUseCase getMostHelpfulReviewUseCase;
    private final GetImageReviewUseCase getImageReviewUseCase;

    private GetWishlistCountUseCase getWishlistCountUseCase;

    private ProductDetailView viewListener;
    private RetrofitInteractor retrofitInteractor;
    private CacheInteractor cacheInteractor;
    private TopAdsSourceTaggingLocal topAdsSourceTaggingLocal;
    private int counter = 0;
    LocalCacheHandler cacheHandler;
    DateFormat df;

    private TopAdsAddSourceTaggingUseCase topAdsAddSourceTaggingUseCase;
    private GetRateEstimationUseCase getRateEstimationUseCase;
    private ToggleFavouriteShopUseCase toggleFavouriteShopUseCase;

    public ProductDetailPresenterImpl(
            GetWishlistCountUseCase getWishlistCountUseCase,
            ProductDetailView viewListener,
            WishListActionListener wishListActionListener,
            RetrofitInteractor retrofitInteractor,
            CacheInteractor cacheInteractor,
            GetProductAffiliateGqlUseCase getProductAffiliateGqlUseCase,
            GetImageReviewUseCase getImageReviewUseCase,
            GetMostHelpfulReviewUseCase getMostHelpfulReviewUseCase,
            ToggleFavouriteShopUseCase toggleFavouriteShopUseCase) {
        this.viewListener = viewListener;
        this.wishListActionListener = wishListActionListener;
        this.retrofitInteractor = retrofitInteractor;
        this.cacheInteractor = cacheInteractor;
        this.getWishlistCountUseCase = getWishlistCountUseCase;
        this.toggleFavouriteShopUseCase = toggleFavouriteShopUseCase;
        this.df = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        this.getProductAffiliateGqlUseCase = getProductAffiliateGqlUseCase;
        this.getImageReviewUseCase = getImageReviewUseCase;
        this.getMostHelpfulReviewUseCase = getMostHelpfulReviewUseCase;
    }

    private void checkWishlistCount(String productId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetWishlistCountUseCase.PRODUCT_ID_PARAM, productId);

        getWishlistCountUseCase.execute(requestParams, new WishlistCountSubscriber(viewListener));
    }

    private void checkImageReview(int productId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putInt(GetImageReviewUseCase.Companion.getKEY_PRODUCT_ID(), productId);
        requestParams.putInt(GetImageReviewUseCase.Companion.getKEY_PAGE(), FIRST_PAGE);
        requestParams.putInt(GetImageReviewUseCase.Companion.getKEY_TOTAL(), TOTAL_IMAGE_FOR_PDP);

        getImageReviewUseCase.execute(requestParams,
                new ImageReviewSubscriber(viewListener));
    }

    @Override
    public void initRetrofitInteractor() {
        this.retrofitInteractor = new RetrofitInteractorImpl();
    }

    @Override
    public void initGetRateEstimationUseCase() {
        getRateEstimationUseCase = new GetRateEstimationUseCase(new GraphqlUseCase());
    }

    @Override
    public void processDataPass(@NonNull ProductPass productPass) {
        if (productPass.haveBasicData()) viewListener.renderTempProductData(productPass);
    }

    @Override
    public void getCostEstimation(@NonNull Context context, float productWeight, String shopDomain) {
        getRateEstimationUseCase.execute(GetRateEstimationUseCase.createRequestParams(GraphqlHelper.loadRawString(context.getResources(), R.raw.gql_pdp_estimasi_ongkir), productWeight, shopDomain),
                new Subscriber<RatesEstimationModel>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        viewListener.onErrorLoadRateEstimation();
                    }

                    @Override
                    public void onNext(RatesEstimationModel ratesEstimationModel) {
                        viewListener.onSuccesLoadRateEstimation(ratesEstimationModel.getRates());
                    }
                });
    }

    @Override
    public void processToShopInfo(@NonNull Context context, @NonNull Bundle bundle) {
        String etalaseName = bundle.getString("etalase_name");
        String etalaseId = bundle.getString("etalase_id");
        Intent intent = null;
        if (!TextUtils.isEmpty(etalaseId)) {
            intent = ((PdpRouter) context.getApplicationContext()).getShoProductListIntent(context, bundle.getString("shop_id"), "", etalaseId);
        } else {
            intent = ((PdpRouter) context.getApplicationContext()).getShopPageIntent(context, bundle.getString("shop_id"));
        }
        viewListener.navigateToActivityRequest(intent, ProductDetailFragment.REQUEST_CODE_SHOP_INFO);
    }

    @Override
    public void processToLogin(@NonNull Context context, @NonNull Bundle bundle) {
        Intent intent = ((PdpRouter) MainApplication.getAppContext()).getLoginIntent(context);
        viewListener.navigateToActivityRequest(intent, ProductDetailFragment.REQUEST_CODE_LOGIN);
    }

    @Override
    public void processToCart(@NonNull Activity context, @NonNull ProductCartPass data) {
        sendAppsFlyerCheckout(context, data);
        boolean skipToCart = data.isSkipToCart();
        Subscriber subscriber = skipToCart ? getBuySubscriber(data.getSourceAtc()) : getCartSubscriber(data.getSourceAtc());
        boolean isOneClickShipment = skipToCart && !data.isBigPromo();
        routeToNewCheckout(context, data, subscriber, isOneClickShipment);
        UnifyTracking.eventPDPCart(context);
    }

    private Subscriber getCartSubscriber(String sourceAtc) {
        return new Subscriber<AddToCartResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                onAtcError(e);
            }

            @Override
            public void onNext(AddToCartResult addToCartResult) {
                NullCheckerKt.isContainNull(addToCartResult, s -> {
                    ContainNullException exception = new ContainNullException("Found " + s + " on " + ProductDetailPresenterImpl.class.getSimpleName());
                    if (!BuildConfig.DEBUG) {
                        Crashlytics.logException(exception);
                    }
                    throw exception;
                });

                viewListener.hideProgressLoading();
                if (addToCartResult.isSuccess()) {
                    addToCartResult.setSource(sourceAtc);
                    viewListener.renderAddToCartSuccess(addToCartResult);
                } else {
                    viewListener.showToastMessage(addToCartResult.getMessage());
                }
            }
        };
    }

    private void onAtcError(Throwable e) {
        viewListener.hideProgressLoading();
        e.printStackTrace();
        if (e instanceof UnknownHostException) {
            /* Ini kalau ga ada internet */
            viewListener.showToastMessage(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL);
        } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
            /* Ini kalau timeout */
            viewListener.showToastMessage(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
        } else if (e instanceof ResponseErrorException) {
            /* Ini kalau error dari API kasih message error */
            viewListener.showToastMessage(e.getMessage());
        } else if (e instanceof ResponseDataNullException) {
            /* Dari Api data null => "data":{}, tapi ga ada message error apa apa */
            viewListener.showToastMessage(e.getMessage());
        } else if (e instanceof HttpErrorException) {
                                /* Ini Http error, misal 403, 500, 404,
                                code http errornya bisa diambil
                                e.getErrorCode */
            viewListener.showToastMessage(e.getMessage());
        } else {
            /* Ini diluar dari segalanya hahahaha */
            viewListener.showToastMessage(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        }
    }

    private Subscriber getBuySubscriber(String sourceAtc) {
        return new Subscriber<AddToCartResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                onAtcError(e);
            }

            @Override
            public void onNext(AddToCartResult addToCartResult) {
                NullCheckerKt.isContainNull(addToCartResult, s -> {
                    ContainNullException exception = new ContainNullException("Found " + s + " on " + ProductDetailPresenterImpl.class.getSimpleName());
                    if (!BuildConfig.DEBUG) {
                        Crashlytics.logException(exception);
                    }
                    throw exception;
                });

                viewListener.hideProgressLoading();
                if (addToCartResult.isSuccess()) {
                    addToCartResult.setSource(sourceAtc);
                    viewListener.renderAddToCartSuccessOpenCheckout(addToCartResult);
                } else {
                    viewListener.showToastMessage(addToCartResult.getMessage());
                }
            }
        };
    }

    private void routeToOldCheckout(@NonNull Activity context, @NonNull ProductCartPass data) {
        viewListener.navigateToActivity(
                TransactionAddToCartRouter.createInstanceAddToCartActivity(context, data)
        );
    }

    private void routeToNewCheckout(@NonNull Activity context, @NonNull ProductCartPass data,
                                    Subscriber subscriber, boolean isOneClickShipment) {
        if (context.getApplication() instanceof PdpRouter) {
            viewListener.showProgressLoading();
            ((PdpRouter) context.getApplication()).addToCartProduct(
                    new AddToCartRequest.Builder()
                            .productId(Integer.parseInt(data.getProductId()))
                            .notes(data.getNotes())
                            .quantity(data.getOrderQuantity())
                            .trackerAttribution(data.getTrackerAttribution())
                            .trackerListName(data.getListName())
                            .shopId(Integer.parseInt(data.getShopId()))
                            .build(), isOneClickShipment
            ).subscribeOn(Schedulers.newThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }

    private void handleCheckoutError(Throwable e) {
        if (e instanceof UnknownHostException) {
            /* Ini kalau ga ada internet */
            viewListener.showToastMessage(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL);
        } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
            /* Ini kalau timeout */
            viewListener.showToastMessage(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
        } else if (e instanceof ResponseErrorException) {
            /* Ini kalau error dari API kasih message error */
            viewListener.showToastMessage(e.getMessage());
        } else if (e instanceof ResponseDataNullException) {
            /* Dari Api data null => "data":{}, tapi ga ada message error apa apa */
            viewListener.showToastMessage(e.getMessage());
        } else if (e instanceof HttpErrorException) {
            /* Ini Http error, misal 403, 500, 404,
            code http errornya bisa diambil
            e.getErrorCode */
            viewListener.showToastMessage(e.getMessage());
        } else {
            /* Ini diluar dari segalanya hahahaha */
            viewListener.showToastMessage(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        }
    }

    @Override
    public void processToTalk(@NonNull Context context, @NonNull Bundle bundle) {
        UnifyTracking.eventPDPTalk(context);
        Intent intent = ((PdpRouter) context.getApplicationContext()).getProductTalk(context,
                bundle.getString("product_id", ""));
        viewListener.navigateToActivityRequest(intent,
                ProductDetailFragment.REQUEST_CODE_TALK_PRODUCT);
    }

    @Override
    public void processToReputation(@NonNull Context context, String productId, String productName) {
        UnifyTracking.eventPDPReputation(context);
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
        viewListener.navigateToActivity(RouteManager.getIntent(context,
                ApplinkConstInternalMarketplace.DISCOVERY_CATEGORY_DETAIL,
                bundle.getString(BrowseProductRouter.DEPARTMENT_ID)));
    }

    @Override
    public void processToCreateShop(@NonNull Context context) {
        Intent intent;
        if (SessionHandler.isV4Login(context)) {
            intent = SellerRouter.getActivityShopCreateEdit(context);
            viewListener.navigateToActivity(intent);
        } else {
            intent = ((PdpRouter) MainApplication.getAppContext()).getLoginIntent(context);
            viewListener.navigateToActivityRequest(intent,
                    ProductDetailFragment.REQUEST_CODE_LOGIN);
        }
    }

    @Override
    public void reportProduct(@NonNull Context context) {
        if (SessionHandler.isV4Login(context)) {
            UnifyTracking.eventPDPReport(context);
            viewListener.showReportDialog();
        } else {
            UnifyTracking.eventPDPReportNotLogin(context);
            Intent intent = ((PdpRouter) MainApplication.getAppContext()).getLoginIntent(context);
            viewListener.navigateToActivityRequest(intent, ProductDetailFragment.REQUEST_CODE_LOGIN);
        }
    }

    @Override
    public void processGetGTMTicker() {
        viewListener.hideTickerGTM();
    }

    @Override
    public void processToSendMessage(@NonNull Context context, @NonNull Intent sendMessageIntent) {
        Intent intent;
        if (SessionHandler.isV4Login(context)) {
            viewListener.navigateToActivity(sendMessageIntent);
        } else {
            intent = ((PdpRouter) MainApplication.getAppContext()).getLoginIntent(context);
            viewListener.navigateToActivityRequest(intent,
                    ProductDetailFragment.REQUEST_CODE_LOGIN);
        }
        UnifyTracking.eventPDPSendMessage(context);
        UnifyTracking.eventPDPSendChat(context);
    }

    @Override
    public void processToEditProduct(@NonNull Context context, @NonNull Bundle bundle) {
        boolean isEdit = bundle.getBoolean("is_edit");
        viewListener.moveToEditFragment(isEdit);
    }

    @Override
    public void sendAppsFlyerCheckout(@NonNull final Context context, @NonNull final ProductCartPass param) {
        PaymentTracking.checkoutEventAppsflyer(context, param);
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

        UnifyTracking.eventPDPDetail(viewListener.getActivityContext(), pdt);


        sendMoEngageOpenProductEvent(viewListener.getActivityContext(), successResult);

        try {
            if (successResult.getShopInfo().getShopIsOfficial() == 1) {
                ScreenTracking.eventOfficialStoreScreenAuth(viewListener.getActivityContext(), successResult.getShopInfo().getShopId(), "official_store", "/product", String.valueOf(successResult.getInfo().getProductId()));
            } else if (successResult.getShopInfo().getShopIsGold() == 1) {
                ScreenTracking.eventOfficialStoreScreenAuth(viewListener.getActivityContext(), successResult.getShopInfo().getShopId(), "gold_merchant", "/product", String.valueOf(successResult.getInfo().getProductId()));
            } else {
                ScreenTracking.eventOfficialStoreScreenAuth(viewListener.getActivityContext(), successResult.getShopInfo().getShopId(), "regular", "/product", String.valueOf(successResult.getInfo().getProductId()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonUtils.dumper("GAv4 error " + e.getMessage());
        }

    }

    private void sendMoEngageOpenProductEvent(Context context, ProductDetailData productData){
        Map<String, Object> value = new HashMap<>();
        if (productData.getBreadcrumb().size() > 1) {
            value.put(AppEventTracking.MOENGAGE.SUBCATEGORY, productData.getBreadcrumb().get(0).getDepartmentName());
            value.put(AppEventTracking.MOENGAGE.SUBCATEGORY_ID, productData.getBreadcrumb().get(0).getDepartmentId());
            value.put(
                    AppEventTracking.MOENGAGE.CATEGORY,
                    productData.getBreadcrumb().get(productData.getBreadcrumb().size() - 1)
                            .getDepartmentName()
            );
            value.put(
                    AppEventTracking.MOENGAGE.CATEGORY_ID,
                    productData.getBreadcrumb().get(productData.getBreadcrumb().size() - 1)
                            .getDepartmentId()
            );
        } else if (productData.getBreadcrumb().size() == 1) {
            value.put(AppEventTracking.MOENGAGE.CATEGORY, productData.getBreadcrumb().get(0).getDepartmentName());
            value.put(AppEventTracking.MOENGAGE.CATEGORY_ID, productData.getBreadcrumb().get(0).getDepartmentId());
        }

        if (productData.getInfo() != null) {
            value.put(AppEventTracking.MOENGAGE.PRODUCT_NAME, MethodChecker.fromHtml(productData.getInfo().getProductName()).toString());
            value.put(AppEventTracking.MOENGAGE.PRODUCT_ID, productData.getInfo().getProductId() + "");
            value.put(AppEventTracking.MOENGAGE.PRODUCT_URL, productData.getInfo().getProductUrl());

            if (productData.getProductImages() != null
                    && productData.getProductImages().size() > 0
                    && productData.getProductImages().get(0) != null) {
                value.put(AppEventTracking.MOENGAGE.PRODUCT_IMAGE_URL, productData.getProductImages().get(0).getImageSrc());
            }
            value.put(AppEventTracking.MOENGAGE.PRODUCT_PRICE, productData.getInfo().getProductPriceUnformatted());
        }

        if (productData.getShopInfo() != null) {
            value.put(AppEventTracking.MOENGAGE.IS_OFFICIAL_STORE, productData.getShopInfo().getShopIsOfficial() == 1);
            value.put(AppEventTracking.MOENGAGE.SHOP_ID, productData.getShopInfo().getShopId());
            value.put(AppEventTracking.MOENGAGE.SHOP_NAME, productData.getShopInfo().getShopName());
        }

        TrackApp.getInstance().getMoEngage().sendTrackEvent(value, AppEventTracking.EventMoEngage.OPEN_PRODUCTPAGE);
    }

    @Override
    public void requestProductDetail(@NonNull final Context context,
                                     @NonNull final ProductPass productPass,
                                     final int type,
                                     final boolean forceNetwork,
                                     final boolean useVariant) {
        if (type == ProductDetailFragment.INIT_REQUEST) viewListener.showProgressLoading();
        if (forceNetwork) {
            getProductDetailFromNetwork(context, productPass, useVariant);
        } else {
            getProductDetailFromCache(productPass,
                    new CacheInteractor.GetProductDetailCacheListener() {
                        @Override
                        public void onSuccess(ProductDetailData productDetailData) {
                            Campaign campaign = productDetailData.getCampaign();
                            viewListener.onProductDetailLoaded(productDetailData, mappingToViewData(productDetailData));
                            viewListener.hideProgressLoading();
                            viewListener.refreshMenu();

                            checkWishlistCount(String.valueOf(productDetailData.getInfo().getProductId()));

                            checkImageReview(productDetailData.getInfo().getProductId());

                            requestAffiliateProductData(productDetailData);

                            requestOtherProducts(context,
                                    NetworkParam.paramOtherProducts(productDetailData));
                            setVideoProduct(context, productDetailData);
                            getTalk(context, productDetailData.getInfo().getProductId().toString(), productDetailData.getShopInfo().getShopId());
                            getMostHelpfulReview(context, productDetailData.getInfo().getProductId
                                    ().toString(), productDetailData.getShopInfo().getShopId());
                            viewListener.loadPromo();
                            if (productDetailData.getInfo().getHasVariant() && useVariant) {
                                getProductVariant(context
                                        , Integer.toString(productDetailData.getInfo().getProductId()));
                            } else {
                                productDetailData.getInfo().setHasVariant(false);
                                getProductStock(context
                                        , Integer.toString(productDetailData.getInfo().getProductId()));
                            }
                            viewListener.trackingEnhanceProductDetail();
                            validateProductDataWithProductPassAndShowMessage(productDetailData, productPass, context);

                            if (campaign.getActive()) {
                                getProductDetailFromNetwork(context, productPass, useVariant);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            getProductDetailFromNetwork(context, productPass, useVariant);
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
            toggleFavouriteShopUseCase.execute(ToggleFavouriteShopUseCase.createRequestParam(shopId), new Subscriber<Boolean>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    viewListener.showFaveShopRetry();
                }

                @Override
                public void onNext(Boolean isValid) {
                    if (isValid) {
                        viewListener.onShopFavoriteUpdated(1);
                        viewListener.actionSuccessAddFavoriteShop(shopId);
                        cacheInteractor.deleteProductDetail(productId);
                    }
                }
            });
        } else {
            Intent intent = ((PdpRouter) MainApplication.getAppContext()).getLoginIntent(context);
            viewListener.navigateToActivityRequest(intent,
                    ProductDetailFragment.REQUEST_CODE_LOGIN);
        }
        UnifyTracking.eventPDPFavorite(context);
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
                            viewListener.onProductHasEdited();
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

    /**
     * this is no longer used in next release (need confirmation)
     * replaced by merchant voucher
     */
    @Deprecated
    public void getPromoWidget(final @NonNull Context context, @NonNull final ProductDetailData productDetailData) {
        String shopType = productDetailData.getShopInfo().isOfficial() ? OFFICIAL_STORE_TYPE : MERCHANT_TYPE;
        cacheInteractor.getPromoWidgetCache(generatePromoTargetType(productDetailData, context),
                SessionHandler.isV4Login(context) ? SessionHandler.getLoginID(context) : NON_LOGIN_USER_ID,
                shopType, new CacheInteractor.GetPromoWidgetCacheListener() {
                    @Override
                    public void onSuccess(PromoAttributes result) {
                        if (result.getCode() != null && result.getCodeHtml() != null && result.getShortCondHtml() != null
                                && result.getShortDescHtml() != null) {
                            viewListener.showPromoWidget(result);
                            ProductPageTracking.eventImpressionWidgetPromo(
                                    context,
                                    result.getShortDesc(),
                                    result.getCustomPromoId(),
                                    result.getCode()
                            );
                        }
                    }

                    @Override
                    public void onError() {
                        retrofitInteractor.getPromo(context,
                                generatePromoTargetType(productDetailData, context),
                                SessionHandler.isV4Login(context) ? SessionHandler.getLoginID(context) : NON_LOGIN_USER_ID, shopType,
                                new RetrofitInteractor.PromoListener() {
                                    @Override
                                    public void onSucccess(DataPromoWidget dataPromoWidget) {
                                        cacheInteractor.storePromoWidget(
                                                generatePromoTargetType(productDetailData, context),
                                                SessionHandler.isV4Login(context) ? SessionHandler.getLoginID(context) : NON_LOGIN_USER_ID,
                                                shopType, dataPromoWidget);
                                        if (!dataPromoWidget.getPromoWidgetList().isEmpty()) {
                                            PromoWidget item = dataPromoWidget.getPromoWidgetList().get(0);
                                            PromoAttributes attributes = item.getPromoAttributes();
                                            attributes.setCustomPromoId(item.getId());
                                            viewListener.showPromoWidget(attributes);
                                            ProductPageTracking.eventImpressionWidgetPromo(
                                                    context,
                                                    attributes.getShortDesc(),
                                                    attributes.getCustomPromoId(),
                                                    attributes.getCode()
                                            );
                                        }
                                    }

                                    @Override
                                    public void onError(String error) {
                                    }
                                });
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

    @Override
    public void processResultTalk(int resultCode, Intent data) {
        if (isResultOK(resultCode) & isIntentOK(data)) {
            if (data.getExtras() != null && data.getBooleanExtra(
                    "RESULT_TALK_HAS_ADDED", false
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
        if (topAdsAddSourceTaggingUseCase != null) {
            topAdsAddSourceTaggingUseCase.unsubscribe();
        }
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
            Intent intent = ((PdpRouter) MainApplication.getAppContext()).getLoginIntent(context);
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
    public void saveStateProductVariant(Bundle outState, String key, ProductVariant value) {
        if (value != null) {
            outState.putParcelable(key, value);
        }
    }

    @Override
    public void saveStateProductStockNonVariant(Bundle outState, String key, Child value) {
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
    public void saveStateProductCampaign(Bundle outState, String key, Campaign value) {
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
    public void processStateData(Bundle savedInstanceState, Context context) {
        ProductDetailData productData = savedInstanceState
                .getParcelable(ProductDetailFragment.STATE_DETAIL_PRODUCT);
        List<ProductOther> productOthers = savedInstanceState
                .getParcelableArrayList(ProductDetailFragment.STATE_OTHER_PRODUCTS);
        VideoData videoData = savedInstanceState.getParcelable(ProductDetailFragment.STATE_VIDEO);
        PromoAttributes promoAttributes = savedInstanceState.getParcelable(ProductDetailFragment.STATE_PROMO_WIDGET);
        ProductVariant productVariant = savedInstanceState.getParcelable(ProductDetailFragment.STATE_PRODUCT_VARIANT);
        Child productStockNonVariant = savedInstanceState.getParcelable(ProductDetailFragment.STATE_PRODUCT_STOCK_NON_VARIANT);
        boolean isAppBarCollapsed = savedInstanceState.getBoolean(ProductDetailFragment.STATE_APP_BAR_COLLAPSED);

        if (productData != null & productOthers != null) {
            viewListener.onProductDetailLoaded(productData, mappingToViewData(productData));
            viewListener.onOtherProductLoaded(productOthers);
            if (videoData != null) {
                viewListener.loadVideo(videoData);
            }
        }

        if (productVariant != null) {
            viewListener.addProductVariant(productVariant);
        } else if (productData != null && productData.getInfo() != null && productData.getInfo().getHasVariant() && productVariant == null) {
            getProductVariant(context, Integer.toString(productData.getInfo().getProductId()));
        } else if (productStockNonVariant != null) {
            viewListener.addProductStock(productStockNonVariant);
        } else if (productData != null && productData.getInfo() != null) {
            getProductStock(context, Integer.toString(productData.getInfo().getProductId()));
        }

        if (productData != null && productData.getCampaign() != null) {
            viewListener.showProductCampaign();
        }

        if (promoAttributes != null) {
            viewListener.showPromoWidget(promoAttributes);
        }

        viewListener.restoreIsAppBarCollapsed(isAppBarCollapsed);
        viewListener.loadPromo();
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
                                            viewListener.onProductHasEdited();
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
        UnifyTracking.eventPDPWishlit(context);

        AddWishListUseCase addWishListUseCase = new AddWishListUseCase(context);
        addWishListUseCase.createObservable(String.valueOf(productId),
                SessionHandler.getLoginID(context), wishListActionListener);

    }

    private void requestRemoveWishList(final Context context, final Integer productId) {
        viewListener.loadingWishList();

        RemoveWishListUseCase removeWishListUseCase = new RemoveWishListUseCase(context);
        removeWishListUseCase.createObservable(String.valueOf(productId),
                SessionHandler.getLoginID(context), wishListActionListener);

    }

    public void getProductDetailFromCache(@NonNull final ProductPass productPass,
                                          @NonNull CacheInteractor.GetProductDetailCacheListener listener) {
        cacheInteractor.getProductDetailCache(productPass.getProductId(), listener);
    }

    public void getProductDetailFromNetwork(@NonNull final Context context,
                                            @NonNull final ProductPass productPass,
                                            final boolean useVariant) {

        retrofitInteractor.getProductDetail(context, NetworkParam.paramProductDetailTest2(productPass),
                new RetrofitInteractor.ProductDetailListener() {
                    @Override
                    public void onSuccess(@NonNull ProductDetailData data) {
                        cacheInteractor.storeProductDetailCache(data.getInfo().getProductId().toString(), data);

                        viewListener.onProductDetailLoaded(data, mappingToViewData(data));

                        checkWishlistCount(String.valueOf(data.getInfo().getProductId()));

                        checkImageReview(data.getInfo().getProductId());

                        requestAffiliateProductData(
                                data
                        );

                        viewListener.hideProgressLoading();
                        viewListener.refreshMenu();
                        requestOtherProducts(context, NetworkParam.paramOtherProducts(data));
                        setVideoProduct(context, data);
                        getMostHelpfulReview(context, data.getInfo().getProductId().toString(),
                                data.getShopInfo().getShopId());
                        getTalk(context, data.getInfo().getProductId().toString(), data.getShopInfo().getShopId());
                        viewListener.loadPromo();
                        if (data.getInfo().getHasVariant() && useVariant) {
                            getProductVariant(context
                                    , Integer.toString(data.getInfo().getProductId()));
                        } else {
                            data.getInfo().setHasVariant(false);
                            getProductStock(context
                                    , Integer.toString(data.getInfo().getProductId()));
                        }
                        viewListener.trackingEnhanceProductDetail();
                        validateProductDataWithProductPassAndShowMessage(data, productPass, context);
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

    private ProductViewData mappingToViewData(ProductDetailData data) {
        ProductViewData viewData = new ProductViewData();
        viewData.setProductId(String.valueOf(data.getInfo().getProductId()));
        viewData.setCourierList(mappingToListCourierViewData(data));
        return viewData;
    }

    private ArrayList<CourierViewData> mappingToListCourierViewData(ProductDetailData data) {
        ArrayList<CourierViewData> list = new ArrayList<>();
        for (ShopShipment shopShipment : data.getShopInfo().getShopShipments()) {
            CourierViewData items = new CourierViewData();
            items.setCourierId(shopShipment.getShippingId());
            items.setLogo(shopShipment.getLogo());
            if (shopShipment.getPackageNames() != null) {
                items.setPackageName(shopShipment.getPackageNames());
            } else {
                items.setPackageName(new ArrayList<>());
            }
            items.setCourierName(shopShipment.getShippingName());
            list.add(items);
        }
        return list;
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
        ScreenTracking.sendAFPDPEvent(context, successResult, eventName);
    }

    @Override
    public void sendButtonClickEvent(@NonNull Context context, @NonNull ProductDetailData successResult) {
        UnifyTracking.eventPDPAddToWishlist(context, successResult.getInfo().getProductName());
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

    private void setVideoProduct(Context context, ProductDetailData productDetailData) {
        requestVideo(context, productDetailData.getInfo().getProductId().toString());
    }

    public void getMostHelpfulReview(@NonNull Context context, @NonNull String productId, String
            shopId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetMostHelpfulReviewUseCase.PRODUCT_ID_PARAM, productId);
        requestParams.putString(GetMostHelpfulReviewUseCase.SHOP_ID, shopId);

        getMostHelpfulReviewUseCase.execute(
                requestParams,
                new MostHelpfulReviewSubscriber(viewListener)
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
        if (topAdsSourceTaggingLocal == null) {
            topAdsSourceTaggingLocal = new TopAdsSourceTaggingLocal(context);
        }
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

    @Override
    public void updateRecentView(@NonNull Context context, int productId) {
        retrofitInteractor.updateRecentView(context, Integer.toString(productId));
    }

    @Override
    public void openPromoteAds(Context context, String url) {
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
            UnifyTracking.eventTopAdsSwitcher(context, AppEventTracking.Category.SWITCHER);
        }
    }

    @Override
    public void initTopAdsSourceTaggingUseCase(Context context) {
        TopAdsSourceTaggingLocal topAdsSourceTaggingLocal = new TopAdsSourceTaggingLocal(context);
        TopAdsSourceTaggingDataSource topAdsSourceTaggingDataSource = new TopAdsSourceTaggingDataSource(topAdsSourceTaggingLocal);
        TopAdsSourceTaggingRepository topAdsSourceTaggingRepository = new TopAdsSourceTaggingRepositoryImpl(topAdsSourceTaggingDataSource);
        topAdsAddSourceTaggingUseCase = new TopAdsAddSourceTaggingUseCase(topAdsSourceTaggingRepository);
    }

    @Override
    public void saveSource(String source) {
        topAdsAddSourceTaggingUseCase.execute(TopAdsAddSourceTaggingUseCase.createRequestParams(source), new Subscriber<Void>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Void aVoid) {
                //do nothing
            }
        });
    }

    @Override
    public void requestAffiliateProductData(ProductDetailData productDetailData) {
        ArrayList<Integer> productList = new ArrayList<>();
        productList.add(productDetailData.getInfo().getProductId());
        getProductAffiliateGqlUseCase.execute(
                GetProductAffiliateGqlUseCase.Companion.createRequestParams(
                        productList,
                        Integer.parseInt(productDetailData.getShopInfo().getShopId())
                ),
                new AffiliateProductDataSubscriber(viewListener)
        );
    }

    public void getProductVariant(@NonNull Context context, @NonNull String id) {
        if (!viewListener.isFromExploreAffiliate()) {
            retrofitInteractor.getProductVariant(context, id,
                    new RetrofitInteractor.ProductVariantListener() {
                        @Override
                        public void onSucccess(final ProductVariant productVariant) {
                            if (productVariant != null && productVariant.getVariant() != null && productVariant.getVariant().size() > 0
                                    && productVariant.getChildren() != null && productVariant.getChildren().size() > 0) {
                                viewListener.addProductVariant(productVariant);
                            } else {
                                viewListener.setVariantFalse();
                            }
                            viewListener.updateButtonBuyListener();
                        }

                        @Override
                        public void onError(String error) {
                            viewListener.showErrorVariant();
                        }
                    }
            );
        }
    }

    public void getProductStock(@NonNull Context context, @NonNull String id) {
        retrofitInteractor.getProductStock(context, id,
                new RetrofitInteractor.ProductStockListener() {
                    @Override
                    public void onSucccess(final Child productStock) {
                        if (productStock != null) {
                            viewListener.addProductStock(productStock);
                        }
                    }

                    @Override
                    public void onError(String error) {
                    }
                }
        );
    }

    private void validateProductDataWithProductPassAndShowMessage(ProductDetailData data, ProductPass productPass, @NonNull Context context) {
        if (productPass == null)
            return;
        if (productPass.getDateTimeInMilis() != 0) {
            try {
                Date date = new Date(productPass.getDateTimeInMilis());
                String lastUpdate = data.getInfo().getProductLastUpdate().replace(" WIB", "");
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy, HH:mm", Locale.ENGLISH);
                Date lastUpdateDate = df.parse(lastUpdate);
                if (lastUpdateDate.after(date)) {
                    viewListener.showToastMessage(context.getString(R.string.product_updated_on_message_container, lastUpdate));
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
                return;
            }
        }
    }

    @Override
    public void checkExpressCheckoutProfile(Activity activity) {
        GetProfileListUseCase useCase = new GetProfileListUseCase(activity);
        useCase.execute(RequestParams.create(), new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (viewListener != null) {
                    viewListener.navigateToOneClickShipment();
                }
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                ProfileListGqlResponse profileResponse = graphqlResponse.getData(ProfileListGqlResponse.class);
                if (profileResponse != null && profileResponse.getData() != null &&
                        profileResponse.getData().getStatus() != null &&
                        profileResponse.getData().getStatus().equals("OK") &&
                        profileResponse.getData().getData().getDefaultProfileId() != 0) {
                    viewListener.navigateToExpressCheckout();
                } else {
                    viewListener.navigateToOneClickShipment();
                }
            }
        });
    }
}
