package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.core.analytics.HomePageTracking;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel;
import com.tokopedia.home.beranda.helper.DynamicLinkHelper;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicChannelViewModel;

/**
 * Created by henrypriyono on 31/01/18.
 */

public class DynamicChannelHeroViewHolder extends AbstractViewHolder<DynamicChannelViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.home_channel_hero_4_image;

    private TextView channelTitle;
    private ImageView channelHeroImage;
    private ImageView channelImage1;
    private TextView channelCaption1;
    private ImageView channelImage2;
    private TextView channelCaption2;
    private ImageView channelImage3;
    private TextView channelCaption3;
    private ImageView channelImage4;
    private TextView channelCaption4;
    private TextView seeAllButton;
    private Context context;
    private HomeCategoryListener listener;
    private View channelTitleContainer;

    public DynamicChannelHeroViewHolder(View itemView, HomeCategoryListener listener) {
        super(itemView);
        context = itemView.getContext();
        this.listener = listener;
        findViews(itemView);
    }

    private void findViews(View itemView) {
        channelTitle = (TextView)itemView.findViewById( R.id.channel_title );
        channelTitleContainer = itemView.findViewById(R.id.channel_title_container);
        channelHeroImage = (ImageView)itemView.findViewById( R.id.channel_hero_image );
        channelImage1 = (ImageView)itemView.findViewById( R.id.channel_image_1 );
        channelCaption1 = (TextView)itemView.findViewById( R.id.channel_caption_1 );
        channelImage2 = (ImageView)itemView.findViewById( R.id.channel_image_2 );
        channelCaption2 = (TextView)itemView.findViewById( R.id.channel_caption_2 );
        channelImage3 = (ImageView)itemView.findViewById( R.id.channel_image_3 );
        channelCaption3 = (TextView)itemView.findViewById( R.id.channel_caption_3 );
        channelImage4 = (ImageView)itemView.findViewById( R.id.channel_image_4 );
        channelCaption4 = (TextView)itemView.findViewById( R.id.channel_caption_4 );
        seeAllButton = (TextView)itemView.findViewById(R.id.see_all_button);
    }

    @Override
    public void bind(final DynamicChannelViewModel element) {
        final DynamicHomeChannel.Channels channel = element.getChannel();
        String titleText = element.getChannel().getHeader().getName();
        if (!TextUtils.isEmpty(titleText)) {
            channelTitleContainer.setVisibility(View.VISIBLE);
            channelTitle.setText(titleText);
        } else {
            channelTitleContainer.setVisibility(View.GONE);
        }
        channelCaption1.setText(element.getChannel().getGrids()[0].getName());
        channelCaption2.setText(element.getChannel().getGrids()[1].getName());
        channelCaption3.setText(element.getChannel().getGrids()[2].getName());
        channelCaption4.setText(element.getChannel().getGrids()[3].getName());
        ImageHandler.loadImageThumbs(context, channelHeroImage, channel.getHero()[0].getImageUrl());
        ImageHandler.loadImageThumbs(context, channelImage1, channel.getGrids()[0].getImageUrl());
        ImageHandler.loadImageThumbs(context, channelImage2, channel.getGrids()[1].getImageUrl());
        ImageHandler.loadImageThumbs(context, channelImage3, channel.getGrids()[2].getImageUrl());
        ImageHandler.loadImageThumbs(context, channelImage4, channel.getGrids()[3].getImageUrl());

        if (!TextUtils.isEmpty(DynamicLinkHelper.getActionLink(channel.getHeader()))) {
            seeAllButton.setVisibility(View.VISIBLE);
        } else {
            seeAllButton.setVisibility(View.GONE);
        }

        seeAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDynamicChannelClicked(DynamicLinkHelper.getActionLink(channel.getHeader()));
            }
        });

        channelHeroImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomePageTracking.eventEnhancedClickDynamicChannelHomePage(
                        element.getChannel().getEnhanceClickDynamicChannelHomePage(element.getChannel().getHero()[0], 1)
                );
                listener.onDynamicChannelClicked(DynamicLinkHelper.getActionLink(element.getChannel().getHero()[0]));
            }
        });
        channelImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomePageTracking.eventEnhancedClickDynamicChannelHomePage(
                        element.getChannel().getEnhanceClickDynamicChannelHomePage(element.getChannel().getGrids()[0], 2)
                );
                listener.onDynamicChannelClicked(DynamicLinkHelper.getActionLink(element.getChannel().getGrids()[0]));
            }
        });
        channelImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomePageTracking.eventEnhancedClickDynamicChannelHomePage(
                        element.getChannel().getEnhanceClickDynamicChannelHomePage(element.getChannel().getGrids()[1], 3)
                );
                listener.onDynamicChannelClicked(DynamicLinkHelper.getActionLink(element.getChannel().getGrids()[1]));
            }
        });
        channelImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomePageTracking.eventEnhancedClickDynamicChannelHomePage(
                        element.getChannel().getEnhanceClickDynamicChannelHomePage(element.getChannel().getGrids()[2], 4)
                );
                listener.onDynamicChannelClicked(DynamicLinkHelper.getActionLink(element.getChannel().getGrids()[2]));
            }
        });
        channelImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomePageTracking.eventEnhancedClickDynamicChannelHomePage(
                        element.getChannel().getEnhanceClickDynamicChannelHomePage(element.getChannel().getGrids()[3], 5)
                );
                listener.onDynamicChannelClicked(DynamicLinkHelper.getActionLink(element.getChannel().getGrids()[3]));
            }
        });
    }
}
