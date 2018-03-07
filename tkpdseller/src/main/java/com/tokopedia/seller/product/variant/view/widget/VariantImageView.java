package com.tokopedia.seller.product.variant.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.common.utils.UrlUtils;
import com.tokopedia.seller.product.edit.view.model.edit.BasePictureViewModel;

import java.io.File;


/**
 * Created by hendry on 06/03/18.
 */

public class VariantImageView extends FrameLayout {

    private ImageView ivVariant;
    private BasePictureViewModel basePictureViewModel;

    private OnImageClickListener onImageClickListener;
    public interface OnImageClickListener{
        void onImageVariantClicked();
    }

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    public VariantImageView(@NonNull Context context) {
        super(context);
    }

    public VariantImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        apply(null, 0);
        init();
    }

    public VariantImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        apply(null, 0);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VariantImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        apply(null, defStyleAttr);
        init();
    }

    @SuppressWarnings("ResourceType")
    private void apply(AttributeSet attrs, int defStyleAttr) {
        /*TypedArray a = getContext().obtainStyledAttributes(attrs, com.tokopedia.design.R.styleable.LoadingStateView);
        loadingLayoutRes = a.getResourceId(com.tokopedia.design.R.styleable.LoadingStateView_lsv_loading_layout, DEFAULT_LOADING_LAYOUT_RES);
        errorLayoutRes = a.getResourceId(com.tokopedia.design.R.styleable.LoadingStateView_lsv_error_layout, VIEW_NOT_AVAILABLE);
        emptyLayoutRes = a.getResourceId(com.tokopedia.design.R.styleable.LoadingStateView_lsv_empty_layout, VIEW_NOT_AVAILABLE);
        a.recycle();*/
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_variant_image_view, this);
        ivVariant = view.findViewById(R.id.image_view);
        ivVariant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onImageClickListener!= null) {
                    onImageClickListener.onImageVariantClicked();
                }
            }
        });
    }

    public void setImage(BasePictureViewModel basePictureViewModel){
        setImage(basePictureViewModel, "");
    }

    public void setImage(BasePictureViewModel basePictureViewModel, String defaultHexColor){
        this.basePictureViewModel = basePictureViewModel;
        if (basePictureViewModel!= null && !TextUtils.isEmpty(basePictureViewModel.getUriOrPath())) {
            ivVariant.setBackgroundColor(Color.TRANSPARENT);
            // ImageHandler.LoadImage(ivVariant, urlOrPath);
            if (!TextUtils.isEmpty(basePictureViewModel.getId()) ) {
                if ( basePictureViewModel.getX() != 0 && basePictureViewModel.getY()!= 0) {
                    ImageHandler.loadImageFitCenter(getContext(), ivVariant,
                            basePictureViewModel.getUrlOriginal());
                } else { // we want to load image, meanwhile also to know its width/height
                    ImageHandler.loadImageWithTarget(getContext(), basePictureViewModel.getUrlOriginal(), new SimpleTarget<Bitmap>() {
                        @Override
                        public void onLoadStarted(Drawable placeholder) {
                            super.onLoadStarted(placeholder);
                            ivVariant.setImageDrawable(placeholder);
                        }

                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            ivVariant.setImageBitmap(resource);
                            VariantImageView.this.basePictureViewModel.setX(resource.getWidth());
                            VariantImageView.this.basePictureViewModel.setY(resource.getHeight());
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            ivVariant.setImageDrawable(errorDrawable);
                        }
                    });
                }
            } else { // local Uri
                ImageHandler.loadImageFromFileFitCenter(
                        getContext(),
                        ivVariant,
                        new File(basePictureViewModel.getFilePath())
                );
            }
        } else if (!TextUtils.isEmpty(defaultHexColor)) {
            ivVariant.setBackgroundColor(Color.parseColor(defaultHexColor));
            ivVariant.setImageDrawable(null);
        } else {
            ivVariant.setBackgroundColor(Color.LTGRAY);
            ivVariant.setImageDrawable(null);
        }

    }

    public BasePictureViewModel getBasePictureViewModel() {
        return basePictureViewModel;
    }
}
