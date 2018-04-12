package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.core.analytics.HomePageTracking;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel;
import com.tokopedia.home.beranda.helper.DynamicLinkHelper;
import com.tokopedia.home.beranda.helper.TextViewHelper;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.GridSpacingItemDecoration;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicChannelViewModel;

/**
 * Created by henrypriyono on 31/01/18.
 */

public class DynamicChannelHeroViewHolder extends AbstractViewHolder<DynamicChannelViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.home_channel_hero_4_image;

    private TextView channelTitle;
    private ImageView channelHeroImage;
    private TextView seeAllButton;
    private static Context context;
    private static HomeCategoryListener listener;
    private View channelTitleContainer;
    private ItemAdapter itemAdapter;
    private RecyclerView recyclerView;
    private static final int spanCount = 2;

    public DynamicChannelHeroViewHolder(View itemView, HomeCategoryListener listener) {
        super(itemView);
        context = itemView.getContext();
        this.listener = listener;
        findViews(itemView);
        itemAdapter = new ItemAdapter();
        recyclerView.setAdapter(itemAdapter);
    }

    private void findViews(View itemView) {
        channelTitle = (TextView) itemView.findViewById(R.id.channel_title);
        channelTitleContainer = itemView.findViewById(R.id.channel_title_container);
        channelHeroImage = (ImageView) itemView.findViewById(R.id.channel_hero_image);
        seeAllButton = (TextView) itemView.findViewById(R.id.see_all_button);
        recyclerView = itemView.findViewById(R.id.recycleList);
        recyclerView.setLayoutManager(new GridLayoutManager(context, spanCount,
                GridLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount,
                context.getResources().getDimensionPixelSize(R.dimen.dp_5), true));
    }

    @Override
    public void bind(final DynamicChannelViewModel element) {
        try {
            final DynamicHomeChannel.Channels channel = element.getChannel();
            String titleText = element.getChannel().getHeader().getName();
            if (!TextUtils.isEmpty(titleText)) {
                channelTitleContainer.setVisibility(View.VISIBLE);
                channelTitle.setText(titleText);
            } else {
                channelTitleContainer.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(DynamicLinkHelper.getActionLink(channel.getHeader()))) {
                seeAllButton.setVisibility(View.VISIBLE);
            } else {
                seeAllButton.setVisibility(View.GONE);
            }
            itemAdapter.setChannel(channel);
            if (channel.getHero() != null) {
                ImageHandler.loadImageThumbs(context, channelHeroImage, channel.getHero()[0].getImageUrl());
            }
            seeAllButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onDynamicChannelClicked(DynamicLinkHelper.getActionLink(channel.getHeader()), "");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        private DynamicHomeChannel.Grid[] list;
        DynamicHomeChannel.Channels channel;

        public ItemAdapter() {
            this.list = new DynamicHomeChannel.Grid[0];
        }

        public void setChannel(DynamicHomeChannel.Channels channel) {
            this.channel = channel;
            this.list = channel.getGrids();
            notifyDataSetChanged();
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_hero_product_item, parent, false);
            return new ItemViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, final int position) {
            try {
                final DynamicHomeChannel.Grid grid = list[position];
                if (grid != null) {
                    holder.channelCaption1.setText(grid.getName());
                    ImageHandler.loadImageThumbs(context, holder.channelImage1, grid.getImageUrl());
                    TextViewHelper.displayText(holder.channelBadge1, grid.getLabel());
                    holder.channelImage1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            HomePageTracking.eventEnhancedClickDynamicChannelHomePage(
                                    channel.getEnhanceClickDynamicChannelHomePage(grid, position + 1)
                            );
                            listener.onDynamicChannelClicked(DynamicLinkHelper.getActionLink(grid),
                                    channel.getHomeAttribution(position + 1, grid.getAttribution()));
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return list.length;
        }
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView channelCaption1;
        private ImageView channelImage1;
        private TextView channelBadge1;

        public ItemViewHolder(View itemView) {
            super(itemView);
            channelImage1 = (ImageView) itemView.findViewById(R.id.channel_image_1);
            channelCaption1 = (TextView) itemView.findViewById(R.id.channel_caption_1);
            channelBadge1 = (TextView) itemView.findViewById(R.id.channel_badge_1);
        }
    }
}
