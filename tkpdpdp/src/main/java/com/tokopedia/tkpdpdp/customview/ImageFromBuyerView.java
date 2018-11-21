package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.design.image.SquareImageView;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;
import com.tokopedia.tkpdpdp.viewmodel.ImageReviewItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Angga.Prasetiyo on 26/10/2015.
 */
public class ImageFromBuyerView extends BaseView<List<ImageReviewItem>, ProductDetailView> {

    private Context context;
    private RecyclerView rv_image;

    public ImageFromBuyerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    public void setListener(ProductDetailView listener) {
        this.listener = listener;
    }

    public ImageFromBuyerView(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_image_from_buyer;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
        rv_image = findViewById(R.id.rv_image);
    }

    @Override
    protected void setViewListener() {
//        ivToggle.setImageResource(R.drawable.chevron_down);
//        tvDesc.setMaxLines(5);
//        isExpand = false;
//        ivToggle.setOnClickListener(new ClickToggle());
//        setVisibility(GONE);
    }

    @Override
    public void renderData(@NonNull List<ImageReviewItem> imageReviewItems) {
        ItemAdapter adapter = new ItemAdapter();
        adapter.setData(imageReviewItems);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 4);
        rv_image.setLayoutManager(gridLayoutManager);
        rv_image.setAdapter(adapter);
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
        private static final int VIEW_TYPE_IMAGE = 77;
        private static final int VIEW_TYPE_IMAGE_WITH_SEE_ALL_LAYER = 88;
        private List<ImageReviewItem> data;

        public ItemAdapter() {
            this.data = new ArrayList<>();
        }

        public void setData(List<ImageReviewItem> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            if(position == (data.size() - 1)){
                return VIEW_TYPE_IMAGE_WITH_SEE_ALL_LAYER;
            }
            return VIEW_TYPE_IMAGE;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView;
            if (viewType == VIEW_TYPE_IMAGE){
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_image_review_item, parent, false);
            } else {
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_image_review_item_with_layer, parent, false);
            }
            return new ItemViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(ItemViewHolder holder, final int position) {
            holder.bind(data.get(position));
        }
        @Override
        public int getItemCount() {
            if(data == null) return 0; return data.size();
        }
        public class ItemViewHolder extends RecyclerView.ViewHolder {
            ImageView reviewImage;
            public ItemViewHolder(View itemView) {
                super(itemView);
                reviewImage = itemView.findViewById(R.id.review_image);
            }
            public void bind(final ImageReviewItem item) {
                ImageHandler.loadImageAndCache(reviewImage, item.getImageUrlThumbnail());
            }
        }
    }
}
