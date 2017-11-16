package com.tokopedia.events.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.events.R;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;

import java.util.List;

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

        public Button btnBookticket;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            eventTitle = (TextView) itemView.findViewById(R.id.event_title);
            eventPrice = (TextView) itemView.findViewById(R.id.tv_event_price);
            eventImage = (ImageView) itemView.findViewById(R.id.img_event);
            btnBookticket = (Button) itemView.findViewById(R.id.btn_bookticket);

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
        holder.eventTitle.setText("" + categoryItems.get(position).getTitle());
        holder.eventPrice.setText("" + categoryItems.get(position).getSalesPrice());
        ImageHandler.loadImageCover2(holder.eventImage, categoryItems.get(position).getImageApp());
    }
}
