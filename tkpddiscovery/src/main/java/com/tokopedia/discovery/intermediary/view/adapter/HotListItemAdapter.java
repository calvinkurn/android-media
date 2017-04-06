package com.tokopedia.discovery.intermediary.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.URLParser;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.network.entity.topPicks.Item;
import com.tokopedia.core.network.entity.topPicks.Toppick;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.intermediary.domain.model.HotListModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by alifa on 3/30/17.
 */

public class HotListItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TOPPICKS_TITLE = 0;

    private List<HotListModel> hotListModelList = new ArrayList<>();
    private final int homeMenuWidth;
    private final Context context;
    private final String categoryId;

    public HotListItemAdapter(List<HotListModel> hotListModelList, int homeMenuWidth,
                              Context context, String categoryId) {
        this.hotListModelList = hotListModelList;
        this.homeMenuWidth = homeMenuWidth;
        this.context = context;
        this.categoryId = categoryId;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.item_hotlist, null
        );
        v.setMinimumWidth(homeMenuWidth);
        return new HotListItemRowHolder(v);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int i) {
        HotListItemRowHolder hotListItemRowHolder = (HotListItemRowHolder) holder;

        final HotListModel hotListModel = hotListModelList.get(i);
        ImageHandler.LoadImage(hotListItemRowHolder.itemImage,hotListModel.getImageUrl());
        hotListItemRowHolder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
        hotListItemRowHolder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnifyTracking.eventHotlistIntermediary(categoryId,hotListModel.getTitle());
                Bundle bundle = new Bundle();
                bundle.putString(BrowseProductRouter.EXTRAS_DISCOVERY_ALIAS,
                        URLParser.getPathSegment(1,hotListModel.getUrl()));
                bundle.putString(BrowseProductRouter.EXTRA_SOURCE,
                        BrowseProductRouter.VALUES_DYNAMIC_FILTER_HOT_PRODUCT);
                Intent intent = BrowseProductRouter.getDefaultBrowseIntent(context);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return hotListModelList.size();
    }


    class HotListItemRowHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.product_image_hoth)
        ImageView itemImage;

        HotListItemRowHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);

        }
    }


}