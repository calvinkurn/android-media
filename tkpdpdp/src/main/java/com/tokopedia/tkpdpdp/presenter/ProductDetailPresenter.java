package com.tokopedia.tkpdpdp.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;

import com.tokopedia.core.network.entity.variant.Campaign;
import com.tokopedia.core.network.entity.variant.Child;
import com.tokopedia.core.network.entity.variant.ProductVariant;
import com.tokopedia.core.product.model.goldmerchant.VideoData;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.promowidget.PromoAttributes;
import com.tokopedia.core.product.model.productother.ProductOther;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.router.transactionmodule.passdata.ProductCartPass;
import com.tokopedia.core.util.AppIndexHandler;

import java.util.List;
import java.util.Map;

/**
 * ProductDetailPresenter
 * Created by Angga.Prasetiyo on 18/11/2015.
 */
public interface ProductDetailPresenter {

    void initGetRateEstimationUseCase();

    void processDataPass(@NonNull ProductPass productPass);

    void processToProductInfo(@NonNull Context context, @NonNull Bundle bundle);

    void processToBrowseProduct(@NonNull Context context, @NonNull Bundle bundle);

    void processToCreateShop(@NonNull Context context);

    void getCostEstimation(@NonNull Context context, float productWeight, String shopDomain);

    void processToShopInfo(@NonNull Context context, @NonNull Bundle bundle);

    void processToTalk(@NonNull Context context, @NonNull Bundle bundle);

    void processToReputation(@NonNull Context context, String productId, String productName);

    void requestPromoteProduct(final @NonNull Context context, @NonNull ProductDetailData product);

    void requestOtherProducts(final @NonNull Context context, final Map<String, String> param);

    void requestMoveToEtalase(final @NonNull Context context, final int productId);

    void processToEditProduct(@NonNull Context context, @NonNull Bundle bundle);

    void requestMoveToWarehouse(final @NonNull Context context, final int productId);

    void processToLogin(@NonNull Context context, @NonNull Bundle bundle);

    void processToCart(@NonNull Activity context, @NonNull ProductCartPass data);

    void sendAnalytics(@NonNull ProductDetailData successResult);

    void processToPicturePreview(@NonNull Context context, @NonNull Bundle bundle);

    void processToSendMessage(@NonNull Context context, @NonNull Intent intent);

    void requestProductDetail(final @NonNull Context context, final @NonNull ProductPass productPass, int type, boolean forceNetwork, boolean useVariant);

    void requestFaveShop(@NonNull Context context, @NonNull String shopId, Integer productId);

    void processResultEdit(int resultCode, Intent data);

    void processResultShop(int resultCode, Intent data);

    void processResultTalk(int resultCode, Intent data);

    void startIndexingApp(@NonNull AppIndexHandler handler, @NonNull ProductDetailData productData);

    void stopIndexingApp(@NonNull AppIndexHandler appIndexHandler);

    void onDestroyView(@NonNull Context context);

    void prepareOptionMenu(Menu menu, Context context, ProductDetailData productData);

    void processWishList(@NonNull Context context, @NonNull ProductDetailData productData);

    void saveStateProductDetail(Bundle outState, String key, ProductDetailData value);

    void saveStateProductVariant(Bundle outState, String key, ProductVariant value);

    void saveStateProductStockNonVariant(Bundle outState, String key, Child value);

    void saveStateProductOthers(Bundle outState, String key, List<ProductOther> values);

    void saveStateVideoData(Bundle outState, String key, VideoData value);

    void saveStateProductCampaign(Bundle outState, String key, Campaign productCampaign);

    void saveStatePromoWidget(Bundle outState, String key, PromoAttributes promoAttributes);

    void saveStateAppBarCollapsed(Bundle outState, String key, boolean isAppBarCollapsed);

    void processStateData(Bundle savedInstanceState, Context context);

    void processToCatalog(Context context, String catalogId);

    void sendAppsFlyerData(@NonNull Context context, @NonNull ProductDetailData successResult, @NonNull String eventName);

    void sendButtonClickEvent(@NonNull Context context, @NonNull ProductDetailData successResult);

    void sendAppsFlyerCheckout(@NonNull Context context, @NonNull ProductCartPass param);

    void initRetrofitInteractor();

    void reportProduct(@NonNull Context context);

    void processGetGTMTicker();

    void onPromoAdsClicked(Context context, String shopId, int itemId, String userId);

    void updateRecentView(final @NonNull Context context, final int productId);

    void openPromoteAds(Context context, String url);

    void initTopAdsSourceTaggingUseCase(Context context);

    void saveSource(String source);

    void requestAffiliateProductData(ProductDetailData productDetailData);

    void getPromoWidget(final @NonNull Context context, @NonNull ProductDetailData productDetailData);

    void checkExpressCheckoutProfile(Activity activity);
}
