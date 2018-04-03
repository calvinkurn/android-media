package com.tokopedia.events.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.view.activity.EventDetailsActivity;
import com.tokopedia.events.view.utils.CurrencyUtil;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pranaymohapatra on 02/04/18.
 */

public class EventCategoryAdapterRevamp extends RecyclerView.Adapter<EventCategoryAdapterRevamp.ViewHolder> {

    private List<CategoryItemsViewModel> categoryItems;
    private Context context;

    public EventCategoryAdapterRevamp(Context context, List<CategoryItemsViewModel> categoryItems) {
        this.context = context;
        this.categoryItems = categoryItems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.tv_add_to_wishlist)
        public TextView tvAddToWishlist;
        @BindView(R2.id.tv_event_share)
        public TextView tvEventShare;
        @BindView(R2.id.tv3_sold_cnt)
        public TextView tv3SoldCnt;
        @BindView(R2.id.event_category_cardview)
        public CardView eventCategoryCardview;
        @BindView(R2.id.tv4_event_title)
        public TextView eventTitle;
        @BindView(R2.id.tv1_price)
        public TextView eventPrice;
        @BindView(R2.id.iv_event_thumb)
        public ImageView eventImage;
        @BindView(R2.id.tv4_location)
        public TextView eventLocation;
        @BindView(R2.id.tv4_date_time)
        public TextView eventTime;
        @BindView(R2.id.tv3_tag)
        public TextView tvDisplayTag;
        int index;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            ButterKnife.bind(this, itemLayoutView);
        }

        public void setViewHolder(CategoryItemsViewModel data, int position) {
            index = position;

            eventTitle.setText(data.getDisplayName());
            eventPrice.setText("Rp" + " " + CurrencyUtil.convertToCurrencyString(data.getSalesPrice()));
            eventLocation.setText(data.getCityName());
            if (data.getMinStartDate() == 0) {
                eventTime.setVisibility(View.GONE);
            } else {
                if (data.getMinStartDate() == data.getMaxEndDate())
                    eventTime.setText(Utils.convertEpochToString(data.getMinStartDate()));
                else
                    eventTime.setText(Utils.convertEpochToString(data.getMinStartDate())
                            + " - " + Utils.convertEpochToString(data.getMaxEndDate()));
            }

            if (data.getDisplayTags() != null && data.getDisplayTags().length() > 3) {
                tvDisplayTag.setText(data.getDisplayTags());
                tvDisplayTag.setVisibility(View.VISIBLE);
            } else {
                tvDisplayTag.setVisibility(View.GONE);
            }

            ImageHandler.loadImageCover2(eventImage, categoryItems.get(position).getThumbnailApp());

        }

        @OnClick({
                R2.id.tv4_event_title,
                R2.id.iv_event_thumb,
                R2.id.tv4_location,
                R2.id.tv4_date_time})
        public void openEventDetails() {
            Intent detailsIntent = new Intent(context, EventDetailsActivity.class);
            detailsIntent.putExtra(EventDetailsActivity.FROM, EventDetailsActivity.FROM_HOME_OR_SEARCH);
            detailsIntent.putExtra("homedata", categoryItems.get(index));
            context.startActivity(detailsIntent);
        }

        public int getIndex() {
            return this.index;
        }

    }

    @Override
    public int getItemCount() {
        if (categoryItems != null) {
            return categoryItems.size();
        }
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_category_item_revamp, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CategoryItemsViewModel model = categoryItems.get(position);
        holder.setViewHolder(model, position);
    }

    class CategoryItemViewListener implements View.OnClickListener {

        ViewHolder mViewHolder;

        public CategoryItemViewListener(ViewHolder holder) {
            this.mViewHolder = holder;
        }

        @Override
        public void onClick(View view) {
            Intent detailsIntent = new Intent(context, EventDetailsActivity.class);
            detailsIntent.putExtra(EventDetailsActivity.FROM, EventDetailsActivity.FROM_HOME_OR_SEARCH);
            detailsIntent.putExtra("homedata", categoryItems.get(mViewHolder.getIndex()));
            context.startActivity(detailsIntent);
        }
    }

}
