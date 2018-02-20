package com.tokopedia.tkpdpdp.listener;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.core.product.listener.ViewListener;
import com.tokopedia.core.product.model.goldmerchant.VideoData;
import com.tokopedia.core.product.model.productdetail.ProductCampaign;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.discussion.LatestTalkViewModel;
import com.tokopedia.core.product.model.productdetail.mosthelpful.Review;
import com.tokopedia.core.product.model.productdetail.promowidget.PromoAttributes;
import com.tokopedia.core.product.model.productother.ProductOther;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.router.transactionmodule.passdata.ProductCartPass;

import java.util.List;

/**
 * @author ANGGA on 11/2/2015.
 */
public interface ProductDetailView extends ViewListener {

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
    void onProductShareClicked(@NonNull ShareData data);

    /**
     * Pada saat rating product diklik
     */
    void onProductRatingClicked(String productId, String shopId, String productName);

    void onCourierClicked(@NonNull Bundle bundle);

    void onWholesaleClicked(@NonNull Bundle bundle);

    void onInstallmentClicked(@NonNull Bundle bundle);

    void onDescriptionClicked(@NonNull Bundle bundle);

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
     */
    void onProductBuySessionLogin(@NonNull ProductCartPass data);

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
     */
    void onProductDetailLoaded(@NonNull ProductDetailData successResult);

    /**
     * Pada saat salah satu gambar product diklik
     *
     * @param bundle model yang dikirim
     */
    void onProductPictureClicked(@NonNull Bundle bundle);

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

    void moveToEditFragment(boolean isEdit, String productId);

    void showSuccessWishlistSnackBar();

    void showPromoWidget(PromoAttributes promoAttributes);

    void onPromoWidgetCopied();

    void showProductCampaign(ProductCampaign productCampaign);

    void showMostHelpfulReview(List<Review> reviews);

    void showLatestTalkView(LatestTalkViewModel discussion);

    void actionSuccessAddToWishlist(Integer productId);

    void actionSuccessRemoveFromWishlist(Integer productId);

    void actionSuccessAddFavoriteShop(String shopId);

    void showDinkSuccess(String productName);

    void showDinkFailed(String productName, String expired);

    void onPromoAdsClicked();

    void restoreIsAppBarCollapsed(boolean isAppBarCollapsed);

    boolean isSellerApp();

    void renderAddToCartSuccess(String message);
}
