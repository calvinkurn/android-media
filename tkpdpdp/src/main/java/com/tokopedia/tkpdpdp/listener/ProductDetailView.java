package com.tokopedia.tkpdpdp.listener;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.core.model.share.ShareData;
import com.tokopedia.core.network.entity.variant.Child;
import com.tokopedia.core.network.entity.variant.ProductVariant;
import com.tokopedia.core.product.listener.ViewListener;
import com.tokopedia.core.product.model.goldmerchant.VideoData;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.discussion.LatestTalkViewModel;
import com.tokopedia.core.product.model.productdetail.mosthelpful.Review;
import com.tokopedia.core.product.model.productdetail.mosthelpful.ReviewImageAttachment;
import com.tokopedia.core.product.model.productdetail.promowidget.PromoAttributes;
import com.tokopedia.core.product.model.productother.ProductOther;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.router.transactionmodule.passdata.ProductCartPass;
import com.tokopedia.gallery.viewmodel.ImageReviewItem;
import com.tokopedia.linker.model.LinkerData;
import com.tokopedia.tkpdpdp.courier.CourierViewData;
import com.tokopedia.tkpdpdp.estimasiongkir.data.model.RatesModel;
import com.tokopedia.tkpdpdp.revamp.ProductViewData;
import com.tokopedia.tkpdpdp.viewmodel.AffiliateInfoViewModel;
import com.tokopedia.transaction.common.sharedata.AddToCartResult;
import com.tokopedia.transactiondata.entity.shared.expresscheckout.AtcRequestParam;

import java.util.ArrayList;
import java.util.List;

import model.TradeInParams;

/**
 * @author ANGGA on 11/2/2015.
 */
public interface ProductDetailView extends ViewListener {

    String SOURCE_BUTTON_BUY_PDP = "BUTTON_BUY_PDP";
    String SOURCE_BUTTON_CART_PDP = "BUTTON_CART_PDP";
    String SOURCE_BUTTON_TRADEIN= "BUTTON_TRADE_IN";
    String SOURCE_BUTTON_BUY_VARIANT = "BUTTON_BUY_VARIANT";
    String SOURCE_BUTTON_CART_VARIANT = "SOURCE_BUTTON_CART_VARIANT";
    String SOURCE_BUTTON_CHAT_PDP = "SOURCE_BUTTON_CHAT_PDP";

    void onByMeClicked(AffiliateInfoViewModel affiliate, boolean isRegularPdp);

    void renderAffiliateButton(AffiliateInfoViewModel affiliate);

    void onWishlistCountLoaded(String wishlistCountText);

    void onImageReviewLoaded(List<ImageReviewItem> data);

    void onProductInfoShortClicked(Intent intent);
    /**
     * Saat salah satu kategori product di klik.
     *
     * @param bundle bundle data yang dikirim
     */
    void onProductDepartmentClicked(@NonNull Bundle bundle);

    /**
     * Saat product katalog di klik.
     *
     * @param catalogId nama dari product katalog
     */
    void onProductCatalogClicked(@NonNull String catalogId);

    /**
     * Pada saat product etalase diklik
     *
     * @param bundle bundle data yang dikirim
     */
    void onProductEtalaseClicked(@NonNull Bundle bundle);

    /**
     * Pada saat ulasan product diklik
     *
     * @param bundle bundle data yang dikirim
     */
    void onProductTalkClicked(@NonNull Bundle bundle);

    /**
     * Pada saat diskusi produk diklik
     *
     */
    void onProductReviewClicked(String productId, String shopId, String productName);

    /**
     * Pada saat promosikan produk diklik
     *
     * @param productData product data model
     */
    void onProductManagePromoteClicked(ProductDetailData productData);


    void onBuyClick(String source);

    void onImageZoomClick(int position);

    /**
     * Pada saat gambar toko diklik
     *
     * @param bundle data yang dikirim
     */
    void onProductShopAvatarClicked(@NonNull Bundle bundle);


    /**
     * Pada saat produk lainnya diklik
     *
     * @param productPass bundle data yang dikirim
     */
    void onProductOtherClicked(@NonNull ProductPass productPass);

    /**
     * Pada saat nama toko diklik
     *
     * @param bundle bundle data yang dikirim
     */
    void onProductShopNameClicked(@NonNull Bundle bundle);

    /**
     * Pada saat tombol ke etalase diklik
     *
     * @param productId product id
     */
    void onProductManageToEtalaseClicked(int productId);

    /**
     * Pada saat tombol edit diklik
     *
     * @param bundle bundle data yang dikirim
     */
    void onProductManageEditClicked(@NonNull Bundle bundle);

    /**
     * Pada saat tombol soldout dklik
     *
     * @param productId id product
     */
    void onProductManageSoldOutClicked(int productId);

    /**
     * Pada saat tombol share diklik
     *
     * @param data  data yang dikirim
     */
    void onProductShareClicked(@NonNull ProductDetailData data);

    /**
     * Pada saat rating product diklik
     */
    void onProductRatingClicked(String productId, String shopId, String productName);

    @Deprecated
    void onCourierClicked(@NonNull Bundle bundle);

    void onCourierClicked(@NonNull String productId,
                          @Nullable ArrayList<CourierViewData> arrayList);

    void onWholesaleClicked(@NonNull Bundle bundle);

    void openVariantPage(int source);

    void onInstallmentClicked(@NonNull Bundle bundle);

    void onDescriptionClicked(@NonNull Bundle bundle);

    void onDescriptionClicked(@NonNull Intent intent);

    /**
     * Pada saat ada error pada toko
     */
    void onProductShopInfoError();

    /**
     * Pada saat status error pada product
     */
    void onProductStatusError();

    /**
     * Pada saat status tombol buat toko baru diklik
     */
    void onProductNewShopClicked();

    /**
     * Pada saat tombol beli di klik
     * user dalam keadaan login
     *
     * @param data model yang dikirim
     * @param source button mana yg mentrigger
     */
    void onProductBuySessionLogin(@NonNull ProductCartPass data, String source);

    /**
     * Pada saat tombol beli di klik
     * user dalam keadaan tidak login
     *
     * @param bundle bundle yang dikirim
     */
    void onProductBuySessionNotLogin(@NonNull Bundle bundle);

    /**
     * mengisi UI dengan data sementara
     *
     * @param productPass data product sementara, nama, harga, foto
     */
    void renderTempProductData(ProductPass productPass);

    /**
     * ngisi/mengupdate UI dari full data product detail yang diterima
     *
     * @param successResult data product detail
     * @param viewData
     */
    void onProductDetailLoaded(@NonNull ProductDetailData successResult, ProductViewData viewData);

    /**
     * Megisi/mengupdate UI dengan data product lainnya yang diterima
     *
     * @param productOthers list data produk lainnya
     */
    void onOtherProductLoaded(List<ProductOther> productOthers);

    /**
     * Pada saat tombol pesan di info toko diklik
     *
     * @param intent intent send Message
     */
    void onProductShopMessageClicked(@NonNull Intent intent);

    /**
     * Setelah product di edit
     */
    void onProductHasEdited();

    /**
     * setelah ulasan product diupdate
     */
    void onProductTalkUpdated();

    /**
     * Mengupdate satatus toko
     * difavorite kan atau tidak difavorite kan
     *
     * @param statFave status favorite
     */
    void onShopFavoriteUpdated(int statFave);

    /**
     * Pada saat tombol favorite toko diklik
     *
     * @param shopId id toko tersebut
     * @param productId
     */
    void onProductShopFaveClicked(String shopId, Integer productId);


    /**
     * Pada saat reputation di shop info diklik
     *
     * @param bundle bundle data yang dikirim
     */
    void onProductShopRatingClicked(Bundle bundle);

    /**
     * hilangkan loading request whislist
     */
    void finishLoadingWishList();

    /**
     * tampilkan progress bar loading whislist
     */
    void loadingWishList();

    /**
     * update wishlist status di UI
     *
     * @param status status
     */
    void updateWishListStatus(int status);

    void loadVideo(VideoData data);

    /**
     * refresh options menu di action bar
     */
    void refreshMenu();

    void showProductDetailRetry();

    void showErrorVariant();

    void showProductOthersRetry();

    void showFaveShopRetry();

    void showWishListRetry(String errorMessage);

    void showPromoteRetry();

    void onNullData();

    void showReportDialog();

    void onProductReportClicked();

    void showTickerGTM(String message);

    void hideTickerGTM();

    void showFullScreenError();

    void moveToEditFragment(boolean isEdit);

    void showSuccessWishlistSnackBar();

    void onPromoWidgetCopied();

    void showProductCampaign();

    void showMostHelpfulReview(List<Review> reviews);

    void showLatestTalkView(LatestTalkViewModel discussion);

    void addProductVariant(ProductVariant productVariant);

    void setVariantFalse();

    void addProductStock(Child productStock);

    void actionSuccessAddToWishlist(Integer productId);

    void actionSuccessRemoveFromWishlist(Integer productId);

    void actionSuccessAddFavoriteShop(String shopId);

    void showDinkSuccess(String productName);

    void showDinkFailed(String productName, String expired);

    void onPromoAdsClicked();

    void restoreIsAppBarCollapsed(boolean isAppBarCollapsed);

    void loadPromo();

    boolean isSellerApp();

    void renderAddToCartSuccess(AddToCartResult addToCartResult);

    void renderAddToCartSuccessOpenCheckout(AddToCartResult addToCartResult);

    void openLoginPage();

    int generateStateVariant(String source);

    void updateButtonBuyListener();

    void trackingEnhanceProductDetail();

    Context getActivityContext();

    void refreshData();

    void onSuccesLoadRateEstimation(RatesModel ratesModel);

    void onErrorLoadRateEstimation();

    void moveToEstimationDetail();

    void showErrorAffiliate(String message);

    void showPromoWidget(PromoAttributes promoAttributes);

    boolean isFromExploreAffiliate();

    void onImageFromBuyerClick(int viewType, String reviewId);

    void onMostHelpfulImageClicked(List<ReviewImageAttachment> data, int position);

    void checkTradeIn(TradeInParams tradeInParams);

    void navigateToOneClickShipment();

    void navigateToExpressCheckout();
}
