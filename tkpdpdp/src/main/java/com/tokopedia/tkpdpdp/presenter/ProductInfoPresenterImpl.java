package com.tokopedia.tkpdpdp.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.database.model.CategoryDB;
import com.tokopedia.core.database.model.CategoryDB_Table;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.share.ShareActivity;
import com.tokopedia.core.share.fragment.ProductShareFragment;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.tkpdpdp.ProductInfoActivity;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.fragment.ProductDetailFragment;
import com.tokopedia.tkpdpdp.listener.ProductInfoView;

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

        boolean isAddingProduct = bundle.getBoolean(ProductInfoActivity.IS_ADDING_PRODUCT);

        ShareData shareDa = bundle.getParcelable(ProductInfoActivity.SHARE_DATA);
        // [variable for add product before share]
        if(isAddingProduct){
            viewListener.navigateToActivity(ShareActivity.createIntent(context,shareDa,isAddingProduct));
            viewListener.closeView();
            // [variable for add product before share]
        }else if(shareDa !=null){
            viewListener.navigateToActivity(ShareActivity.createIntent(context,shareDa));
            viewListener.closeView();
        } else if (bundle !=null && uri !=null && uri.getPathSegments().size() == 2) {
            viewListener.inflateFragment(ProductDetailFragment.newInstanceForDeeplink(ProductPass.Builder.aProductPass()
                            .setProductKey(uri.getPathSegments().get(1))
                            .setShopDomain(uri.getPathSegments().get(0))
                            .setProductUri(uri.toString())
                            .build()),
                    ProductDetailFragment.class.getSimpleName());
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
            Intent moveIntent = BrowseProductRouter.getIntermediaryIntent(context,iden);

            viewListener.navigateToActivity(moveIntent);
        }
    }

    public void processToShareProduct(Context context, @NonNull ShareData shareData) {
        UnifyTracking.eventShareProduct();
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
                        .build();
            } else if (productItem != null) {
                productPass = ProductPass.Builder.aProductPass()
                        .setProductPrice(productItem.getPrice())
                        .setProductId(productItem.getId())
                        .setProductName(productItem.getName())
                        .setProductImage(productItem.getImgUri())
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
