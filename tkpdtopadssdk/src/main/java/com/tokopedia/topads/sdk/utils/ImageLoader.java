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
        imageFetcher.setLoadingImage(R.drawable.ic_loading_toped);
        imageFetcher.addImageCache(cacheParams);
    }

    public void loadImage(String ecs, ImageView imageView) {
        loadImage(ecs, null, imageView);
    }

    public void loadImage(String ecs, final String url, ImageView imageView) {
        imageFetcher.loadImage(ecs, imageView, new ImageWorker.OnImageLoadedListener() {
            @Override
            public void onImageLoaded(boolean success) {
                if (success && url != null && url.contains(PATH_VIEW)) {
                    new ImpresionTask().execute(url);
                }
            }
        }, false);
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

}
