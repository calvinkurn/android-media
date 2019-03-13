package com.tokopedia.tkpdpdp.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.applink.internal.ApplinkConstInternal;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.linker.model.LinkerData;
import com.tokopedia.tkpdpdp.fragment.ProductDetailFragment;
import com.tokopedia.tkpdpdp.listener.ProductInfoView;
import com.tokopedia.core.analytics.*;

import java.util.List;

/**
 * @author Angga.Prasetiyo on 09/11/2015.
 */
public class ProductInfoPresenterImpl implements ProductInfoPresenter {
    private static final String TAG = ProductInfoPresenterImpl.class.getSimpleName();

    private final ProductInfoView viewListener;

    public ProductInfoPresenterImpl(ProductInfoView viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void initialFragment(@NonNull Context context, Uri uri, Bundle bundle) {
        if (bundle !=null && uri !=null && uri.getPathSegments().size() == 2) {
            if (bundle.getBoolean(DeepLink.IS_DEEP_LINK, false)
                    && !TextUtils.isEmpty(bundle.getString("product_id", ""))) {
                viewListener.inflateFragment(
                        ProductDetailFragment.newInstance(generateProductPass(bundle, uri)),
                        ProductDetailFragment.class.getSimpleName()
                );
            } else {
                viewListener.inflateFragment(ProductDetailFragment.newInstanceForDeeplink(ProductPass.Builder.aProductPass()
                                .setProductKey(uri.getPathSegments().get(1))
                                .setShopDomain(uri.getPathSegments().get(0))
                                .setProductUri(uri.toString())
                                .build()),
                        ProductDetailFragment.class.getSimpleName());
            }
        } else if (isProductDetail(uri, bundle)) {
            viewListener.inflateFragment(ProductDetailFragment
                            .newInstance(generateProductPass(bundle, uri)),
                    ProductDetailFragment.class.getSimpleName());
        } else {
            if (uri == null) {
                return;
            }

            List<String> uriSegments = uri.getPathSegments();
            String iden = uriSegments.get(1);
            for (int i = 2; i < uriSegments.size(); i++) {
                iden = iden + "_" + uriSegments.get(i);
            }
            Intent moveIntent = RouteManager.getIntentInternal(context,
                    UriUtil.buildUri(ApplinkConstInternal.Marketplace.DISCOVERY_CATEGORY_DETAIL,iden));
            viewListener.navigateToActivity(moveIntent);
        }
    }

    public void processToShareProduct(Context context, @NonNull LinkerData shareData) {
        UnifyTracking.eventShareProduct(context);
    }

    private ProductPass generateProductPass(Bundle bundleData, Uri uriData) {
        ProductPass productPass;
        if (bundleData != null) {
            productPass = bundleData.getParcelable(ProductDetailRouter.EXTRA_PRODUCT_PASS);
            ProductItem productItem = bundleData
                    .getParcelable(ProductDetailRouter.EXTRA_PRODUCT_ITEM);
            if (productPass == null && productItem == null) {
                productPass = ProductPass.Builder.aProductPass()
                        .setProductId(bundleData.getString("product_id", ""))
                        .setProductName(bundleData.getString("product_key", ""))
                        .setProductPrice(bundleData.getString("product_price", ""))
                        .setShopDomain(bundleData.getString("shop_domain", ""))
                        .setTrackerAttribution(bundleData.getString("tracker_attribution", ""))
                        .setTrackerListName(bundleData.getString("tracker_list_name", ""))
                        .setFromExploreAffiliate(bundleData.getBoolean("is_from_explore_affiliate", false))
                        .build();
            } else if (productItem != null) {
                productPass = ProductPass.Builder.aProductPass()
                        .setProductPrice(productItem.getPrice())
                        .setProductId(productItem.getId())
                        .setProductName(productItem.getName())
                        .setProductImage(productItem.getImgUri())
                        .setTrackerAttribution(productItem.getTrackerAttribution())
                        .setTrackerListName(productItem.getTrackerListName())
                        .setWishlist(productItem.getIsWishlist())
                        .setDiscountedPrice(productItem.getOriginalPrice())
                        .setDiscountPercentage(productItem.getDiscountPercentage())
                        .setCountReview(!TextUtils.isEmpty(productItem.getReviewCount()) && productItem.getReviewCount().matches("\\d+")
                                ? Integer.parseInt(productItem.getReviewCount()) : 0)
                        .setCountCourrier(productItem.getCountCourier())
                        .setRating(!TextUtils.isEmpty(productItem.getRating()) && productItem.getRating().matches("\\d+")
                                ? Integer.parseInt(productItem.getRating()) : 0)
                        .setCashback(productItem.getCashback())
                        .setOfficial(productItem.getOfficial())
                        .setShopName(productItem.getShop())
                        .build();
            }
        } else {
            List<String> uriSegments = uriData.getPathSegments();
            String prodName = "";
            String shopDomain = "";
            if (uriSegments.size() >= 2) {
                prodName = uriSegments.get(1);
                shopDomain = uriSegments.get(0);
            }
            productPass = ProductPass.Builder.aProductPass()
                    .setProductName(prodName)
                    .setShopDomain(shopDomain)
                    .build();
        }
        return productPass;
    }

    private boolean isProductDetail(Uri uriData, Bundle bundleData) {
        if (uriData != null & bundleData == null) {
            List<String> uriSegments = uriData.getPathSegments();
            return !uriSegments.get(0).equals("p");
        } else {
            return true;
        }
    }
}
