package com.tokopedia.tkpdpdp.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.product.model.productdetail.ProductImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author by Angga.Prasetiyo on 23/10/2015.
 */
public class ImagePagerAdapter extends PagerAdapter {
    private Context context;
    private List<ProductImage> productImages;

    private OnActionListener actionListener;
    private String urlTemporary;

    public ImagePagerAdapter(Context context) {
        this.context = context;
        this.productImages = new ArrayList<>();
    }

    public ImagePagerAdapter(Context context, String urlTemporary) {
        this.context = context;
        this.productImages = new ArrayList<>();
        this.urlTemporary = urlTemporary;
    }

    @Override
    public int getCount() {
        return productImages.size();
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final ImageView imageView = new ImageView(context);
        final String urlImage = productImages.get(position).getImageSrc();

        int currentOrientation = context.getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setAdjustViewBounds(true);
            if (!TextUtils.isEmpty(urlTemporary) && position==0) {
                Glide.with(context)
                        .load(urlImage)
                        .dontAnimate()
                        .dontTransform()
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .thumbnail(
                                Glide.with(context)
                                        .load(urlTemporary)
                                        .dontAnimate()
                                        .dontTransform()
                                        .fitCenter()
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE))
                        .into(imageView);

            } else{
                if(!TextUtils.isEmpty(urlImage)){
                    if (!TextUtils.isEmpty(urlTemporary) && urlImage.equals(urlTemporary)) {
                        ImageHandler.loadImageSourceSizeFitCenter(context, imageView, urlTemporary);
                    } else {
                        ImageHandler.loadImageSourceSizeFitCenter(context, imageView, urlImage);
                    }
                }
            }
        }
        else {
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setAdjustViewBounds(true);
            if (!TextUtils.isEmpty(urlTemporary) && position==0) {
                Glide.with(context)
                        .load(urlImage)
                        .dontAnimate()
                        .dontTransform()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .thumbnail(
                                Glide.with(context)
                                        .load(urlTemporary)
                                        .dontAnimate()
                                        .dontTransform()
                                        .centerCrop()
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE))
                        .into(imageView);

            } else if (!TextUtils.isEmpty(urlTemporary) && urlImage.equals(urlTemporary)) {
                ImageHandler.loadImageSourceSizeCenterCrop(context, imageView, urlTemporary);
            } else {
                ImageHandler.loadImageSourceSizeCenterCrop(context, imageView, urlImage);
            }
        }
        imageView.setOnClickListener(new OnClickImage(position));
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void addAll(List<ProductImage> productImages) {
        this.productImages.clear();
        this.productImages.addAll(productImages);
        Collections.sort(this.productImages, new Comparator<ProductImage>() {
            @Override
            public int compare(ProductImage lhs, ProductImage rhs) {
                return rhs.getImagePrimary().compareTo(lhs.getImagePrimary());
            }
        });
        notifyDataSetChanged();
    }

    public void addAllWithoutSort(List<ProductImage> productImages) {
        this.productImages.clear();
        this.productImages.addAll(productImages);
        notifyDataSetChanged();
    }

    public void addFirst(ProductImage productImage) {
        this.productImages.clear();
        this.productImages.add(productImage);
        urlTemporary = productImage.getImageSrc();
        notifyDataSetChanged();
    }

    public void setActionListener(OnActionListener actionListener) {
        this.actionListener = actionListener;
    }

    private class OnClickImage implements View.OnClickListener {
        private final int position;

        OnClickImage(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (actionListener != null)
                actionListener.onItemImageClicked(position);
        }
    }

    public interface OnActionListener {

        void onItemImageClicked(int position);

    }
}