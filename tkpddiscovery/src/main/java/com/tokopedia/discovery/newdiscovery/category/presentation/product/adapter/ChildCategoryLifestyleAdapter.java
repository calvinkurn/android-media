package com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tagmanager.DataLayer;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.ChildCategoryModel;

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
        setupAttrCardView(holder, position);
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
                TrackingUtils.eventCategoryLifestyleClick(model.getCategoryUrl(), list);
                listener.onCategoryRevampClick(model);
            }
        });
    }

    private void setupAttrCardView(ViewHolder holder, int position) {
        if (listCategory.size() == 3) {
            setForFitToWindow(holder, position);
        } else {
            setDefaultAttr(holder, position);
        }
    }

    private void setDefaultAttr(ViewHolder holder, int position) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.container.getLayoutParams();
        setMargin(params, position);
        holder.container.setLayoutParams(params);
    }

    private void setForFitToWindow(ViewHolder holder, int position) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                holder.container.getLayoutParams().height
        );
        setMargin(params, position);
        holder.container.setLayoutParams(params);
    }

    private void setMargin(RelativeLayout.LayoutParams params, int position) {
        if (position != listCategory.size() - 1) {
            params.setMargins(
                    context.getResources().getDimensionPixelSize(R.dimen.margin_small), 0, 0, 0);
        } else {
            params.setMargins(
                    context.getResources().getDimensionPixelSize(R.dimen.margin_small), 0,
                    context.getResources().getDimensionPixelSize(R.dimen.margin_small), 0
            );
        }
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
