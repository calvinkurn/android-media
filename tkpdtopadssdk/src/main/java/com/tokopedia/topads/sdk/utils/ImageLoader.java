package com.tokopedia.topads.sdk.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.domain.model.Badge;
import com.tokopedia.topads.sdk.imageutils.ImageCache;
import com.tokopedia.topads.sdk.imageutils.ImageFetcher;
import com.tokopedia.topads.sdk.imageutils.ImageWorker;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import android.graphics.Bitmap;

import java.util.List;

/**
 * @author by errysuprayogi on 4/3/17.
 */

public class ImageLoader {

    private static final String IMAGE_CACHE_DIR = "images";

    private Context context;
    private ImageCache.ImageCacheParams cacheParams;
    private ImageFetcher imageFetcher;
    private final String PATH_VIEW = "views";

    public ImageLoader(Context context) {
        this.context = context;
        cacheParams = new ImageCache.ImageCacheParams(context, IMAGE_CACHE_DIR);
        cacheParams.setMemCacheSizePercent(0.25f);
        imageFetcher = new ImageFetcher(context,
                context.getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size));
        imageFetcher.setLoadingImage(R.drawable.loading_page);
        imageFetcher.addImageCache(cacheParams);
    }

    public void loadImage(String ecs, ImageView imageView) {
        loadImage(ecs, null, imageView);
    }

    public void loadImage(String ecs, final String url, ImageView imageView) {
        Glide.with(context)
                .load(ecs)
                .asBitmap()
                .placeholder(R.drawable.loading_page)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageView.setImageBitmap(resource);
                        if (url.contains(PATH_VIEW)) {
                            new ImpresionTask().execute(url);
                        }
                    }
                });
    }

    public void loadImageWithMemoryCache(String url, ImageView imageView){
        imageFetcher.loadImage(url, imageView);
    }

    public void loadBadge(final LinearLayout container, List<Badge> badges) {
        container.removeAllViews();
        for (Badge badge : badges) {
            final View view = LayoutInflater.from(context).inflate(R.layout.layout_badge, null);
            final ImageView imageView = (ImageView) view.findViewById(R.id.badge);
            imageFetcher.loadImage(badge.getImageUrl(), imageView, new ImageWorker.OnImageLoadedListener() {
                @Override
                public void onImageLoaded(boolean success) {
                    if (success) {
                        container.addView(view);
                    }
                }
            }, true);
        }
    }

    public static int getRatingDrawable(int param) {
        switch (param) {
            case 0:
                return R.drawable.ic_star_none;
            case 1:
                return R.drawable.icon_star_one;
            case 2:
                return R.drawable.icon_star_two;
            case 3:
                return R.drawable.icon_star_three;
            case 4:
                return R.drawable.icon_star_four;
            case 5:
                return R.drawable.icon_star_five;
            default:
                return R.drawable.ic_star_none;
        }
    }

}
