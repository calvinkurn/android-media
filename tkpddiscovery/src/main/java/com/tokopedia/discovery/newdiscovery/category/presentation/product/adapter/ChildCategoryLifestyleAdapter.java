package com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tagmanager.DataLayer;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.ChildCategoryModel;
import com.tokopedia.track.TrackApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nakama on 1/5/18.
 */

public class ChildCategoryLifestyleAdapter extends RecyclerView.Adapter<ChildCategoryLifestyleAdapter.ViewHolder> {

    private final RevampCategoryAdapter.CategoryListener listener;
    private List<ChildCategoryModel> listCategory;
    private Context context;
    private String headerName;

    public ChildCategoryLifestyleAdapter(RevampCategoryAdapter.CategoryListener listener,
                                         String headerName) {
        this.headerName = headerName;
        this.listener = listener;
        this.listCategory = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(
                LayoutInflater.from(context)
                        .inflate(R.layout.item_lifestyle_category, null)
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final ChildCategoryModel model = listCategory.get(position);
        holder.title.setText(model.getCategoryName().toUpperCase());
        ImageHandler.LoadImage(
                holder.imageView,
                model.getCategoryImageUrl()
        );
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Object> list = new ArrayList<>();
                list.add(
                        DataLayer.mapOf(
                                "id", model.getCategoryId(),
                                "name", String.format("category %s - subcategory banner", headerName.toLowerCase()),
                                "position", String.valueOf(position + 1),
                                "creative", model.getCategoryName()
                        )
                );
                eventCategoryLifestyleClick(model.getCategoryUrl(), list);
                listener.onCategoryRevampClick(model);
            }
        });
    }

    public static void eventCategoryLifestyleClick(String categoryUrl, List<Object> list) {
        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(
                DataLayer.mapOf("event", "promoClick",
                        "eventCategory", "category page",
                        "eventAction", "click subcategory",
                        "eventLabel", categoryUrl,
                        "ecommerce", DataLayer.mapOf(
                                "promoClick", DataLayer.mapOf(
                                        "promotions", DataLayer.listOf(list.toArray(new Object[list.size()])))),
                        "destinationURL", categoryUrl
                )
        );
    }


    public void setListCategory(List<ChildCategoryModel> listCategory) {
        this.listCategory = listCategory;
    }

    @Override
    public int getItemCount() {
        return listCategory.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final View container;
        private final ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            this.title = view.findViewById(R.id.categoryTitle);
            this.container = view.findViewById(R.id.linWrapper);
            this.imageView = view.findViewById(R.id.thumbnail);
        }
    }
}
