package com.tokopedia.events.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.events.R;
import com.tokopedia.events.view.activity.EventDetailsActivity;
import com.tokopedia.events.view.utils.CurrencyUtil;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by ashwanityagi on 16/11/17.
 */

public class EventCategoryAdapter extends RecyclerView.Adapter<EventCategoryAdapter.ViewHolder> {

    private List<CategoryItemsViewModel> categoryItems;
    private Context context;

    public EventCategoryAdapter(Context context, List<CategoryItemsViewModel> categoryItems) {
        this.context = context;
        this.categoryItems = categoryItems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView eventTitle;
        public TextView eventPrice;
        public ImageView eventImage;
        //public TextView btnBookticket;
        public TextView eventLocation;
        public TextView eventTime;
        public LinearLayout eventTimeLayout;
        public TextView tvDisplayTag;
        int index;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            eventTitle = (TextView) itemView.findViewById(R.id.event_title);
            eventPrice = (TextView) itemView.findViewById(R.id.tv_event_price);
            eventImage = (ImageView) itemView.findViewById(R.id.img_event);
            //btnBookticket = (TextView) itemView.findViewById(R.id.btn_bookticket);
            eventLocation = (TextView) itemView.findViewById(R.id.tv_location);
            eventTime = (TextView) itemView.findViewById(R.id.tv_time);
            eventTimeLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout2);
            tvDisplayTag = (TextView) itemView.findViewById(R.id.text_view_display_tag);

        }

        public void setIndex(int position) {
            this.index = position;
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
                .inflate(R.layout.home_event_category_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CategoryItemsViewModel model = categoryItems.get(position);
        holder.eventTitle.setText(model.getDisplayName());
        holder.eventPrice.setText("Rp" + " " + CurrencyUtil.convertToCurrencyString(model.getSalesPrice()));
        holder.eventLocation.setText(model.getCityName());
        if (model.getMinStartDate() == 0) {
            holder.eventTimeLayout.setVisibility(View.GONE);
        } else {
            if (model.getMinStartDate().equals(model.getMaxEndDate()))
                holder.eventTime.setText(convertEpochToString(model.getMinStartDate()));
            else
                holder.eventTime.setText(convertEpochToString(model.getMinStartDate())
                        + " - " + convertEpochToString(model.getMaxEndDate()));
        }
        holder.setIndex(position);

        if (model.getDisplayTags() != null && model.getDisplayTags().length() > 3) {
            holder.tvDisplayTag.setText(model.getDisplayTags());
            holder.tvDisplayTag.setVisibility(View.VISIBLE);
        } else {
            holder.tvDisplayTag.setVisibility(View.GONE);
        }

        ImageHandler.loadImageCover2(holder.eventImage, categoryItems.get(position).getThumbnailApp());

        CategoryItemViewListener listener = new CategoryItemViewListener(holder);

        holder.itemView.setOnClickListener(listener);
    }

    class CategoryItemViewListener implements View.OnClickListener {

        ViewHolder mViewHolder;

        public CategoryItemViewListener(ViewHolder holder) {
            this.mViewHolder = holder;
        }

        @Override
        public void onClick(View view) {
            Intent detailsIntent = new Intent(context, EventDetailsActivity.class);
            detailsIntent.putExtra("homedata", categoryItems.get(mViewHolder.getIndex()));
            context.startActivity(detailsIntent);
        }
    }

    public String convertEpochToString(int time) {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        Long epochTime = time * 1000L;
        Date date = new Date(epochTime);
        String dateString = sdf.format(date);
        return dateString;
    }
}
