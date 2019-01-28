package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import com.bumptech.glide.Glide;
import com.tokopedia.core.model.share.ShareData;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.ProductImage;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author by Angga.Prasetiyo on 30/10/2015.
 */
public class ButtonShareView extends BaseView<ProductDetailData, ProductDetailView> {
    private Bitmap bitmap;

    public ButtonShareView(Context context) {
        super(context);
    }

    public ButtonShareView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_share_product;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {
        setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull final ProductDetailData data) {
        if (bitmap != null) {
            setVisibility(VISIBLE);
            setOnClickListener(new OnClicked(data));
        } else {
            new BitmapTask(data, getContext()).execute();
        }
    }

    private class OnClicked implements OnClickListener {
        private final ProductDetailData data;

        OnClicked(ProductDetailData data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            listener.onProductShareClicked(data);
        }
    }

    private class BitmapTask extends AsyncTask<String, String, Bitmap> {

        private final ProductDetailData data;
        private Context context;

        BitmapTask(ProductDetailData data, Context context) {
            this.data = data;
            this.context = context;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                List<ProductImage> images = data.getProductImages();
                if (!images.isEmpty()) {
                    String uriImage = images.get(0).getImageSrc300();
                    if (!uriImage.isEmpty())
                        try {
                            return Glide.with(context)
                                    .load(uriImage)
                                    .asBitmap()
                                    .centerCrop()
                                    .into(500, 500)
                                    .get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap s) {
            super.onPostExecute(s);
            if (s != null) {
                bitmap = s;
                setVisibility(VISIBLE);
                setOnClickListener(new OnClicked(data));
            }
        }
    }
}
