package com.tokopedia.tkpdpdp.customview;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.viewpagerindicator.CirclePageIndicator;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.ProductImage;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpdpdp.PreviewProductImageDetail;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.adapter.ImagePagerAdapter;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Angga.Prasetiyo on 29/10/2015.
 */
public class PictureView extends BaseView<ProductDetailData, ProductDetailView> {

    private ViewPager vpImage;
    private CirclePageIndicator indicator;
    private LinearLayout errorProductContainer;
    private TextView errorProductTitle;
    private TextView errorProductSubitle;

    private ImagePagerAdapter imagePagerAdapter;

    public PictureView(Context context) {
        super(context);
    }

    public PictureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }


    @Override
    protected int getLayoutView() {
        return R.layout.view_picture_product_pdp;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {
        imagePagerAdapter = new ImagePagerAdapter(context, new ArrayList<ProductImage>());
    }

    @Override
    protected void setViewListener() {
        vpImage.setAdapter(imagePagerAdapter);
        indicator.setViewPager(vpImage);
    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
        vpImage = (ViewPager) findViewById(R.id.view_pager);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator_picture);
        errorProductContainer = (LinearLayout) findViewById(R.id.error_product_container);
        errorProductTitle = (TextView) findViewById(R.id.error_product_title);
        errorProductSubitle = (TextView) findViewById(R.id.error_product_subtitle);

    }

    @Override
    public void renderData(@NonNull final ProductDetailData data) {
        imagePagerAdapter = new ImagePagerAdapter(getContext(), new ArrayList<ProductImage>());
        vpImage.setAdapter(imagePagerAdapter);
        List<ProductImage> productImageList = data.getProductImages();
        if (productImageList.isEmpty()) {
            int resId = R.drawable.product_no_photo_default;
            Resources res = getContext().getResources();
            Uri resUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + res.getResourcePackageName(resId)
                    + '/' + res.getResourceTypeName(resId)
                    + '/' + res.getResourceEntryName(resId));
            productImageList.add(ProductImage.Builder.aProductImage()
                    .setImageSrc300(resUri.toString())
                    .setImageDescription("").build());
            imagePagerAdapter.addAll(productImageList);
            indicator.notifyDataSetChanged();
        } else {
            imagePagerAdapter.addAll(productImageList);
            indicator.notifyDataSetChanged();
            imagePagerAdapter.setActionListener(new PagerAdapterAction(data));
        }
        if (data.getInfo().getProductStatus().equals("-1")) {
            if (data.getInfo().getProductStatusMessage() != null && data.getInfo().getProductStatusTitle() != null) {
                errorProductContainer.setVisibility(VISIBLE);
                errorProductTitle.setText(data.getInfo().getProductStatusTitle());
                errorProductSubitle.setText(data.getInfo().getProductStatusMessage());
            }
            listener.onProductStatusError();
        } else if (data.getInfo().getProductStatus().equals("3") &
                data.getShopInfo().getShopStatus() == 1 && data.getInfo().getProductStatusTitle().length()>1) {
            errorProductContainer.setVisibility(VISIBLE);
            errorProductTitle.setText(data.getInfo().getProductStatusTitle());
            errorProductSubitle.setText(data.getInfo().getProductStatusMessage());
        } else if (!data.getInfo().getProductStatus().equals("1")) {
            listener.onProductStatusError();
        } else if (data.getShopInfo().getShopStatus()!=1) {
            errorProductContainer.setVisibility(VISIBLE);
            errorProductTitle.setText(data.getShopInfo().getShopStatusTitle() != null
                    && !data.getShopInfo().getShopStatusTitle().isEmpty()
                    ? data.getShopInfo().getShopStatusTitle() : "");
            errorProductSubitle.setText(data.getShopInfo().getShopStatusMessage() != null
                    && !data.getShopInfo().getShopStatusMessage().isEmpty()
                    ? data.getShopInfo().getShopStatusMessage() : "");

        }else {
            errorProductContainer.setVisibility(GONE);
        }
    }

    public void renderTempData(ProductPass productPass) {
        ProductImage productImage = new ProductImage();
        productImage.setImageSrc300(productPass.getProductImage());
        productImage.setImageSrc(productPass.getProductImage());
        productImage.setImageDescription("");
        imagePagerAdapter.addFirst(productImage);
        indicator.notifyDataSetChanged();
    }

    private class PagerAdapterAction implements ImagePagerAdapter.OnActionListener {
        private final ProductDetailData data;

        PagerAdapterAction(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onItemImageClicked(ProductImage productImage, int position) {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(PreviewProductImageDetail.FILELOC, imagePagerAdapter.getImageURIPaths());
            bundle.putString("product_name", MethodChecker.fromHtml(data.getInfo().getProductName()).toString());
            bundle.putString("product_price", MethodChecker.fromHtml(data.getInfo().getProductPrice()).toString());
            bundle.putStringArrayList("image_desc", imagePagerAdapter.getImageDescs());
            bundle.putInt(PreviewProductImageDetail.IMG_POSITION, position);
            listener.onProductPictureClicked(bundle);
        }
    }
}
