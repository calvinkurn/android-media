package com.tokopedia.discovery.intermediary.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.intermediary.domain.model.HotListModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alifa on 3/30/17.
 */

public class HotListItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int BANNER_HOTLIST = -1;
    public static final int SHORT_HEIGHT_HOTLIST = -2;

    private List<HotListModel> hotListModelList = new ArrayList<>();
    private final int homeMenuWidth;
    private final Context context;
    private final String categoryId;
    private HotlistItemListener hotlistItemListener;

    public HotListItemAdapter(List<HotListModel> hotListModelList, int homeMenuWidth,
                              Context context, String categoryId) {
        this.hotListModelList = hotListModelList;
        this.homeMenuWidth = homeMenuWidth;
        this.context = context;
        this.categoryId = categoryId;
    }

    @Override
    public int getItemViewType(int position) {
        if (hotListModelList.size() >= 3 && position == 0) {
            return BANNER_HOTLIST;
        } else if (hotListModelList.size() >= 3 && position < 3) {
            return SHORT_HEIGHT_HOTLIST;
        } else {
            return position;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case SHORT_HEIGHT_HOTLIST:
                @SuppressLint("InflateParams") View v = LayoutInflater.from(
                        viewGroup.getContext()).inflate(R.layout.item_hotlist_square, null
                );
                v.setMinimumWidth(homeMenuWidth);
                return new HotListItemRowHolder(v);
            case BANNER_HOTLIST:
                @SuppressLint("InflateParams") View vBanner = LayoutInflater.from(
                        viewGroup.getContext()).inflate(R.layout.item_hotlist_banner, null
                );
                vBanner.setMinimumWidth(homeMenuWidth);
                return new HotListItemRowHolder(vBanner);
            default:
                @SuppressLint("InflateParams") View vDefault = LayoutInflater.from(
                        viewGroup.getContext()).inflate(R.layout.item_hotlist, null
                );
                vDefault.setMinimumWidth(homeMenuWidth);
                return new HotListItemRowHolder(vDefault);
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int i) {
        HotListItemRowHolder hotListItemRowHolder = (HotListItemRowHolder) holder;

        final HotListModel hotListModel = hotListModelList.get(i);
        switch (getItemViewType(i)) {
            case SHORT_HEIGHT_HOTLIST:
                ImageHandler.LoadImage(hotListItemRowHolder.itemImage, hotListModel.getImageUrlSquare());
                break;
            case BANNER_HOTLIST:
                ImageHandler.LoadImage(hotListItemRowHolder.itemImage, hotListModel.getImageUrlBanner());
                break;
            default:
                ImageHandler.LoadImage(hotListItemRowHolder.itemImage, hotListModel.getImageUrl());
        }
        hotListItemRowHolder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hotlistItemListener.sendHotlistClickEvent(hotListModel, i);
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        int position = holder.getAdapterPosition();
        HotListModel model = hotListModelList.get(position);
        if (!model.isTracked()) {
            hotlistItemListener.sendHotlistImpressionEvent(model, position);
            model.setTracked(true);
        }
        super.onViewAttachedToWindow(holder);
    }

    private void moveToSearchActivity(String url, Context context) {
        Uri uriData = Uri.parse(url);
        Bundle bundle = new Bundle();

        String departmentId = uriData.getQueryParameter("sc");
        String searchQuery = uriData.getQueryParameter("q");

        bundle.putString(BrowseProductRouter.DEPARTMENT_ID, departmentId);
        bundle.putString(BrowseProductRouter.EXTRAS_SEARCH_TERM, searchQuery);

        Intent intent = BrowseProductRouter.getSearchProductIntent(context);
        intent.putExtras(bundle);

        context.startActivity(intent);
    }

    public void registerListener(HotlistItemListener listener) {
        if (listener != null) {
            hotlistItemListener = listener;
        } else {
            throw new RuntimeException("HotlistItemListener implementation is null");
        }
    }

    @Override
    public int getItemCount() {
        return hotListModelList.size();
    }


    class HotListItemRowHolder extends RecyclerView.ViewHolder {

        private ImageView itemImage;

        HotListItemRowHolder(View view) {
            super(view);
            initView(view);
        }

        private void initView(View view) {
            itemImage = view.findViewById(R.id.product_image_hoth);
        }
    }

    public interface HotlistItemListener {
        void sendHotlistImpressionEvent(HotListModel model, int pos);

        void sendHotlistClickEvent(HotListModel model, int pos);
    }

}