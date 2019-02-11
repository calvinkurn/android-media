package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.gallery.viewmodel.ImageReviewItem;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

import java.util.ArrayList;
import java.util.List;

public class ImageFromBuyerView extends BaseView<List<ImageReviewItem>, ProductDetailView> {

    private Context context;
    private RecyclerView rv_image;

    public static final int VIEW_TYPE_IMAGE = 77;
    public static final int VIEW_TYPE_IMAGE_WITH_SEE_ALL_LAYER = 88;

    public static final int DEFAULT_START_ROW = 0;
    public static final int DEFAULT_END_ROW = 4;

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
    }

    @Override
    public void renderData(@NonNull List<ImageReviewItem> imageReviewItems) {
        ItemAdapter adapter = new ItemAdapter();
        int endRow = imageReviewItems.size() > 4 ? DEFAULT_END_ROW :
                (imageReviewItems.size() - 1);
        adapter.setData(imageReviewItems.subList(DEFAULT_START_ROW, endRow));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 4);
        rv_image.setLayoutManager(gridLayoutManager);
        rv_image.setAdapter(adapter);
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
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
            ImageReviewItem imageReviewItem = data.get(position);
            holder.bind(imageReviewItem);
            holder.reviewImage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.getItemViewType() == VIEW_TYPE_IMAGE){

                    }
                    listener.onImageFromBuyerClick(holder.getItemViewType(),
                            imageReviewItem.getReviewId());
                }
            });
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
