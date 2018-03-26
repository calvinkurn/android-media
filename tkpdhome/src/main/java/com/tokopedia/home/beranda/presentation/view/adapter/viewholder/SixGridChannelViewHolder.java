package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.core.analytics.HomePageTracking;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.image.SquareImageView;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel;
import com.tokopedia.home.beranda.listener.HomeCategoryListener;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicChannelViewModel;

/**
 * Created by henrypriyono on 22/03/18.
 */

public class SixGridChannelViewHolder extends AbstractViewHolder<DynamicChannelViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.home_six_grid_channel;

    private SquareImageView image1;
    private SquareImageView image2;
    private SquareImageView image3;
    private SquareImageView image4;
    private SquareImageView image5;
    private SquareImageView image6;

    private Context context;
    private HomeCategoryListener listener;

    public SixGridChannelViewHolder(View itemView, HomeCategoryListener listener) {
        super(itemView);
        context = itemView.getContext();
        this.listener = listener;
        findViews(itemView);
    }

    private void findViews(View itemView) {
        image1 = (SquareImageView)itemView.findViewById( R.id.image_1 );
        image2 = (SquareImageView)itemView.findViewById( R.id.image_2 );
        image3 = (SquareImageView)itemView.findViewById( R.id.image_3 );
        image4 = (SquareImageView)itemView.findViewById( R.id.image_4 );
        image5 = (SquareImageView)itemView.findViewById( R.id.image_5 );
        image6 = (SquareImageView)itemView.findViewById( R.id.image_6 );
    }

    @Override
    public void bind(DynamicChannelViewModel element) {
        bindImageView(image1, element, 0);
        bindImageView(image2, element, 1);
        bindImageView(image3, element, 2);
        bindImageView(image4, element, 3);
        bindImageView(image5, element, 4);
        bindImageView(image6, element, 5);
    }

    private void bindImageView(SquareImageView imageView, final DynamicChannelViewModel element, final int position) {
        final DynamicHomeChannel.Grid item = element.getChannel().getGrids()[position];

        ImageHandler.loadImageFitCenter(context, imageView, item.getImageUrl());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomePageTracking.eventEnhancedClickDynamicChannelHomePage(
                        element.getChannel().getEnhanceClickLegoBannerHomePage(item, position + 1)
                );
                listener.onSixGridItemClicked(getAvailableLink(item.getApplink(), item.getUrl()),
                        element.getChannel().getHomeAttribution(position + 1, item.getName()));
            }
        });
    }

    private String getAvailableLink(String applink, String url) {
        if (!TextUtils.isEmpty(applink)) {
            return applink;
        } else {
            return url;
        }
    }
}
