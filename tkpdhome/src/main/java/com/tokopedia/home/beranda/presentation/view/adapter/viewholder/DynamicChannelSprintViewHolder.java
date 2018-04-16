package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.core.analytics.HomePageTracking;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.component.TextViewCompat;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel;
import com.tokopedia.home.beranda.helper.DateHelper;
import com.tokopedia.home.beranda.helper.DynamicLinkHelper;
import com.tokopedia.home.beranda.helper.TextViewHelper;
import com.tokopedia.home.beranda.listener.GridItemClickListener;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.GridSpacingItemDecoration;
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.SpacingItemDecoration;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicChannelViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.LayoutSections;
import com.tokopedia.home.beranda.presentation.view.compoundview.CountDownView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by henrypriyono on 31/01/18.
 */

public class DynamicChannelSprintViewHolder extends AbstractViewHolder<DynamicChannelViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.home_channel_3_image;
    private static final String TAG = DynamicChannelSprintViewHolder.class.getSimpleName();
    private TextView homeChannelTitle;
    private TextView seeAllButton;
    private HomeCategoryListener listener;
    private static CountDownView countDownView;
    private String sprintSaleExpiredText;
    private CountDownView.CountDownListener countDownListener;
    private ItemAdapter itemAdapter;
    private RecyclerView recyclerView;
    private static final int spanCount = 3;

    public DynamicChannelSprintViewHolder(View itemView, HomeCategoryListener listener, CountDownView.CountDownListener countDownListener) {
        super(itemView);
        this.countDownListener = countDownListener;
        this.listener = listener;
        initResources(itemView.getContext());
        findViews(itemView);
        itemAdapter = new ItemAdapter(itemView.getContext(), listener);
        recyclerView.setAdapter(itemAdapter);
    }

    private void initResources(Context context) {
        sprintSaleExpiredText = context.getString(R.string.sprint_sale_expired_text);
    }

    private void findViews(View itemView) {
        homeChannelTitle = (TextView) itemView.findViewById(R.id.home_channel_title);
        seeAllButton = (TextView) itemView.findViewById(R.id.see_all_button);
        countDownView = itemView.findViewById(R.id.count_down);
        recyclerView = itemView.findViewById(R.id.recycleList);
        recyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(), spanCount,
                GridLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount,
                itemView.getContext().getResources().getDimensionPixelSize(R.dimen.page_margin), true));
    }

    @Override
    public void bind(final DynamicChannelViewModel element) {
        try {
            final DynamicHomeChannel.Channels channel = element.getChannel();
            itemAdapter.setChannel(channel);
            homeChannelTitle.setText(channel.getHeader().getName());

            if (!TextUtils.isEmpty(DynamicLinkHelper.getActionLink(channel.getHeader()))) {
                seeAllButton.setVisibility(View.VISIBLE);
            } else {
                seeAllButton.setVisibility(View.GONE);
            }
            setupClickListeners(channel);

            if (isSprintSale(channel)) {
                Date expiredTime = DateHelper.getExpiredTime(channel.getHeader().getExpiredTime());
                countDownView.setup(expiredTime, countDownListener);
                countDownView.setVisibility(View.VISIBLE);
            } else {
                countDownView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Crashlytics.log(0, TAG, e.getLocalizedMessage());
        }
    }

    private void setupClickListeners(final DynamicHomeChannel.Channels channel) {
        seeAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSprintSale(channel)) {
                    HomePageTracking.eventClickSeeAllProductSprint();
                } else {
                    HomePageTracking.eventClickSeeAllDynamicChannel(DynamicLinkHelper.getActionLink(channel.getHeader()));
                }
                listener.onDynamicChannelClicked(DynamicLinkHelper.getActionLink(channel.getHeader()), "");
            }
        });
    }

    private static boolean isSprintSale(DynamicHomeChannel.Channels channel) {
        return DynamicHomeChannel.Channels.LAYOUT_SPRINT.equals(channel.getLayout());
    }

    private static class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        private final HomeCategoryListener listener;
        private DynamicHomeChannel.Channels channel;
        private DynamicHomeChannel.Grid[] list;


        public ItemAdapter(Context context, HomeCategoryListener listener) {
            this.listener = listener;
            this.list = new DynamicHomeChannel.Grid[0];
        }

        public void setChannel(DynamicHomeChannel.Channels channel) {
            this.channel = channel;
            this.list = channel.getGrids();
            notifyDataSetChanged();
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sprint_product_item_simple, parent, false);
            return new ItemViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, final int position) {
            try {
                final DynamicHomeChannel.Grid grid = list[position];
                if (grid != null) {
                    ImageHandler.loadImageThumbs(holder.getContext(),
                            holder.channelImage1, grid.getImageUrl());
                    holder.channelPrice1.setText(grid.getPrice());
                    TextViewHelper.displayText(holder.channelDiscount1, grid.getDiscount());
                    holder.channelBeforeDiscPrice1.setText(grid.getSlashedPrice());

                    holder.itemContainer1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isSprintSale(channel)) {
                                HomePageTracking.eventEnhancedClickSprintSaleProduct(channel.getEnhanceClickSprintSaleHomePage(position, countDownView.getCurrentCountDown()));
                            } else {
                                HomePageTracking.eventEnhancedClickDynamicChannelHomePage(channel.getEnhanceClickDynamicChannelHomePage(grid, position + 1));
                            }
                            listener.onDynamicChannelClicked(DynamicLinkHelper.getActionLink(grid),
                                    channel.getHomeAttribution(position + 1, grid.getAttribution())
                            );
                        }
                    });
                }
            } catch (Exception e) {
                Crashlytics.log(0, TAG, e.getLocalizedMessage());
            }
        }

        @Override
        public int getItemCount() {
            return list.length;
        }
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView channelImage1;
        private TextView channelPrice1;
        private TextView channelDiscount1;
        private TextView channelBeforeDiscPrice1;
        private RelativeLayout itemContainer1;
        private View view;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            channelImage1 = (ImageView) itemView.findViewById(R.id.channel_image_1);
            channelPrice1 = (TextView) itemView.findViewById(R.id.channel_price_1);
            channelDiscount1 = (TextView) itemView.findViewById(R.id.channel_discount_1);
            channelBeforeDiscPrice1 = (TextView) itemView.findViewById(R.id.channel_before_disc_price_1);
            itemContainer1 = itemView.findViewById(R.id.channel_item_container_1);
            channelBeforeDiscPrice1.setPaintFlags(channelBeforeDiscPrice1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        public Context getContext() {
            return view.getContext();
        }
    }

}
