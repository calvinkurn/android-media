package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.core.analytics.HomePageTracking;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.data.source.pojo.DynamicHomeChannel;
import com.tokopedia.home.beranda.helper.TextViewHelper;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicChannelViewModel;
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

    private TextView homeChannelTitle;
    private ImageView channelImage1;
    private TextView channelPrice1;
    private TextView channelBeforeDiscPrice1;
    private TextView channelDiscount1;
    private ImageView channelImage2;
    private TextView channelPrice2;
    private TextView channelBeforeDiscPrice2;
    private TextView channelDiscount2;
    private ImageView channelImage3;
    private TextView channelPrice3;
    private TextView channelBeforeDiscPrice3;
    private TextView channelDiscount3;
    private TextView seeAllButton;
    private Context context;
    private HomeCategoryListener listener;
    private View itemContainer1;
    private View itemContainer2;
    private View itemContainer3;
    private CountDownView countDownView;

    public DynamicChannelSprintViewHolder(View itemView, HomeCategoryListener listener) {
        super(itemView);
        context = itemView.getContext();
        this.listener = listener;
        findViews(itemView);
    }

    private void findViews(View itemView) {
        homeChannelTitle = (TextView)itemView.findViewById( R.id.home_channel_title );
        channelImage1 = (ImageView)itemView.findViewById( R.id.channel_image_1 );
        channelPrice1 = (TextView)itemView.findViewById( R.id.channel_price_1 );
        channelBeforeDiscPrice1 = (TextView)itemView.findViewById( R.id.channel_before_disc_price_1 );
        channelDiscount1 = (TextView)itemView.findViewById(R.id.channel_discount_1);
        channelImage2 = (ImageView)itemView.findViewById( R.id.channel_image_2 );
        channelPrice2 = (TextView)itemView.findViewById( R.id.channel_price_2 );
        channelBeforeDiscPrice2 = (TextView)itemView.findViewById( R.id.channel_before_disc_price_2 );
        channelDiscount2 = (TextView)itemView.findViewById(R.id.channel_discount_2);
        channelImage3 = (ImageView)itemView.findViewById( R.id.channel_image_3 );
        channelPrice3 = (TextView)itemView.findViewById( R.id.channel_price_3 );
        channelBeforeDiscPrice3 = (TextView)itemView.findViewById( R.id.channel_before_disc_price_3 );
        channelDiscount3 = (TextView)itemView.findViewById(R.id.channel_discount_3);
        seeAllButton = (TextView)itemView.findViewById(R.id.see_all_button);
        itemContainer1 = itemView.findViewById(R.id.channel_item_container_1);
        itemContainer2 = itemView.findViewById(R.id.channel_item_container_2);
        itemContainer3 = itemView.findViewById(R.id.channel_item_container_3);
        countDownView = itemView.findViewById(R.id.count_down);
    }

    @Override
    public void bind(final DynamicChannelViewModel element) {
        final DynamicHomeChannel.Channels channel = element.getChannel();
        if (isSprintSale(channel)) {
            countDownView.setVisibility(View.VISIBLE);
            countDownView.setup(getExpiredTime(element));
        } else {
            countDownView.setVisibility(View.GONE);
        }
        homeChannelTitle.setText(channel.getHeader().getName());
        ImageHandler.loadImageThumbs(context, channelImage1, channel.getGrids()[0].getImageUrl());
        ImageHandler.loadImageThumbs(context, channelImage2, channel.getGrids()[1].getImageUrl());
        ImageHandler.loadImageThumbs(context, channelImage3, channel.getGrids()[2].getImageUrl());
        channelPrice1.setText(channel.getGrids()[0].getPrice());
        channelPrice2.setText(channel.getGrids()[1].getPrice());
        channelPrice3.setText(channel.getGrids()[2].getPrice());
        TextViewHelper.displayText(channelDiscount1, channel.getGrids()[0].getDiscount());
        TextViewHelper.displayText(channelDiscount2, channel.getGrids()[1].getDiscount());
        TextViewHelper.displayText(channelDiscount3, channel.getGrids()[2].getDiscount());
        channelBeforeDiscPrice1.setText(channel.getGrids()[0].getSlashedPrice());
        channelBeforeDiscPrice2.setText(channel.getGrids()[1].getSlashedPrice());
        channelBeforeDiscPrice3.setText(channel.getGrids()[2].getSlashedPrice());
        channelBeforeDiscPrice1.setPaintFlags(channelBeforeDiscPrice1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        channelBeforeDiscPrice2.setPaintFlags(channelBeforeDiscPrice2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        channelBeforeDiscPrice3.setPaintFlags(channelBeforeDiscPrice3.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        homeChannelTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDynamicChannelClicked(channel.getHeader().getApplink());
            }
        });
        seeAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSprintSale(channel)) {
                    HomePageTracking.eventClickSeeAllProductSprint();
                } else {
                    HomePageTracking.eventClickSeeAllDynamicChannel(channel.getHeader().getApplink());
                }
                listener.onDynamicChannelClicked(channel.getHeader().getApplink());
            }
        });
        itemContainer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSprintSale(channel)) {
                    HomePageTracking.eventEnhancedClickSprintSaleProduct(channel.getEnhanceClickSprintSaleHomePage(0, countDownView.getCurrentCountDown()));
                } else {
                    HomePageTracking.eventEnhancedClickDynamicChannelHomePage(channel.getEnhanceClickDynamicChannelHomePage(channel.getGrids()[0], 1));
                }
                listener.onDynamicChannelClicked(channel.getGrids()[0].getApplink());
            }
        });
        itemContainer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSprintSale(channel)) {
                    HomePageTracking.eventEnhancedClickSprintSaleProduct(channel.getEnhanceClickSprintSaleHomePage(1, countDownView.getCurrentCountDown()));
                } else {
                    HomePageTracking.eventEnhancedClickDynamicChannelHomePage(channel.getEnhanceClickDynamicChannelHomePage(channel.getGrids()[1], 2));
                }
                listener.onDynamicChannelClicked(channel.getGrids()[1].getApplink());
            }
        });
        itemContainer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSprintSale(channel)) {
                    HomePageTracking.eventEnhancedClickSprintSaleProduct(channel.getEnhanceClickSprintSaleHomePage(2, countDownView.getCurrentCountDown()));
                } else {
                    HomePageTracking.eventEnhancedClickDynamicChannelHomePage(channel.getEnhanceClickDynamicChannelHomePage(channel.getGrids()[2], 3));
                }
                listener.onDynamicChannelClicked(channel.getGrids()[2].getApplink());
            }
        });
    }

    private boolean isSprintSale(DynamicHomeChannel.Channels channel) {
        return DynamicHomeChannel.Channels.LAYOUT_SPRINT.equals(channel.getLayout());
    }

    private Date getExpiredTime(DynamicChannelViewModel model) {
        String expiredTimeString = model.getChannel().getHeader().getExpiredTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZZZZ");
        try {
            return format.parse(expiredTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }
}
