package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.Button;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.EmptyFeedBeforeLoginModel;

/**
 * Created by meyta on 1/29/18.
 */

public class EmptyFeedBeforeLoginViewHolder extends AbstractViewHolder<EmptyFeedBeforeLoginModel> {

    @LayoutRes public static final int LAYOUT = R.layout.list_feed_before_login;

    public EmptyFeedBeforeLoginViewHolder(View itemView, final FeedPlus.View viewListener) {
        super(itemView);

        Button button = itemView.findViewById(R.id.btn_login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onGoToLogin();
            }
        });
    }

    @Override
    public void bind(EmptyFeedBeforeLoginModel element) {}
}