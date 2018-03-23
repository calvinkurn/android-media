package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.component.TextViewCompat;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel;
import com.tokopedia.home.beranda.helper.DateHelper;
import com.tokopedia.home.beranda.helper.DynamicLinkHelper;
import com.tokopedia.home.beranda.helper.StartSnapHelper;
import com.tokopedia.home.beranda.listener.GridItemClickListener;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.SpacingItemDecoration;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicChannelViewModel;
import com.tokopedia.home.beranda.presentation.view.compoundview.CountDownView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by errysuprayogi on 3/22/18.
 */

public class SprintSaleCarouselViewHolder extends AbstractViewHolder<DynamicChannelViewModel>
        implements GridItemClickListener {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_sprint_card_item;
    private static final String TAG = SprintSaleCarouselViewHolder.class.getSimpleName();
    private RelativeLayout container;
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private Context context;
    private TextView title;
    private TextView seeMore;
    private ImageView headerBg;
    private CountDownView countDownView;
    private HomeCategoryListener listener;

    public SprintSaleCarouselViewHolder(View itemView, HomeCategoryListener listener) {
        super(itemView);
        this.context = itemView.getContext();
        this.listener = listener;
        itemAdapter = new ItemAdapter();
        countDownView = itemView.findViewById(R.id.count_down);
        container = itemView.findViewById(R.id.container);
        headerBg = itemView.findViewById(R.id.header_bg);
        title = itemView.findViewById(R.id.title);
        seeMore = itemView.findViewById(R.id.see_more);
        recyclerView = itemView.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SpacingItemDecoration(context.getResources().getDimensionPixelSize(R.dimen.item_space), SpacingItemDecoration.HORIZONTAL));
        SnapHelper snapHelper = new StartSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onGridItemClick(int pos, DynamicHomeChannel.Grid grid) {
        listener.onDynamicChannelClicked(DynamicLinkHelper.getActionLink(grid));
    }

    @Override
    public void bind(DynamicChannelViewModel element) {
        final DynamicHomeChannel.Channels channels = element.getChannel();
        title.setText(channels.getHeader().getName());
        Glide.with(context).load(channels.getHeader().getBackImage()).into(headerBg);
        recyclerView.setBackgroundColor(Color.parseColor(channels.getHeader().getBackColor()));
        itemAdapter.setList(channels.getGrids());
        itemAdapter.setGridItemClickListener(this);
        Date expiredTime = DateHelper.getExpiredTime(channels.getHeader().getExpiredTime());
        countDownView.setup(expiredTime, new CountDownView.CountDownListener() {
            @Override
            public void onCountDownFinished() {
                itemAdapter.setGridItemClickListener(null);
            }
        });
        seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDynamicChannelClicked(DynamicLinkHelper.getActionLink(channels.getHeader()));
            }
        });
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        private DynamicHomeChannel.Grid[] list;
        private GridItemClickListener gridItemClickListener;

        public ItemAdapter() {
            this.list = new DynamicHomeChannel.Grid[0];
        }

        public void setGridItemClickListener(GridItemClickListener gridItemClickListener) {
            this.gridItemClickListener = gridItemClickListener;
        }

        public void setList(DynamicHomeChannel.Grid[] list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sprint_product_item, parent, false);
            return new ItemViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, final int position) {
            try {
                final DynamicHomeChannel.Grid grid = list[position];
                ImageHandler.loadImageThumbs(context, holder.imageView, grid.getImageUrl());
                holder.price1.setText(grid.getSlashedPrice());
                holder.price1.setPaintFlags(holder.price1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.price2.setText(grid.getPrice());
                holder.stockStatus.setText(grid.getLabel());
                if (grid.getDiscount().isEmpty()) {
                    holder.channelDiscount.setVisibility(View.GONE);
                } else {
                    holder.channelDiscount.setVisibility(View.VISIBLE);
                    holder.channelDiscount.setText(grid.getDiscount());
                }
                if (grid.getLabel().equalsIgnoreCase(context.getString(R.string.hampir_habis))) {
                    holder.stockStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_flame, 0, 0, 0);
                } else {
                    holder.stockStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
                if (grid.getSoldPercentage() > 0) {
                    holder.stockProgress.setVisibility(View.VISIBLE);
                    holder.stockProgress.setProgress(grid.getSoldPercentage());
                } else {
                    holder.stockProgress.setVisibility(View.INVISIBLE);
                }
                if (gridItemClickListener != null) {
                    holder.countainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            gridItemClickListener.onGridItemClick(position, grid);
                        }
                    });
                } else {
                    holder.countainer.setOnClickListener(null);
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

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        public CardView countainer;
        public ImageView imageView;
        public TextView channelDiscount;
        public TextView price1;
        public TextView price2;
        public TextViewCompat stockStatus;
        public ProgressBar stockProgress;

        public ItemViewHolder(View itemView) {
            super(itemView);
            countainer = itemView.findViewById(R.id.container);
            imageView = itemView.findViewById(R.id.image);
            channelDiscount = itemView.findViewById(R.id.channel_discount);
            price1 = itemView.findViewById(R.id.price1);
            price2 = itemView.findViewById(R.id.price2);
            stockStatus = itemView.findViewById(R.id.stock_status);
            stockProgress = itemView.findViewById(R.id.stock_progress);
        }
    }

}
