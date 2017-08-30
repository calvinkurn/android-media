package com.tokopedia.seller.product.view.adapter.addurlvideo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;

import com.tokopedia.core.customadapter.NoResultDataBinder;
import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.dashboard.view.adapter.viewholder.TopAdsEmptyAdDataBinder;

/**
 * @author normansyahputa on 4/27/17.
 */

public class EmptyAddUrlVideoDataBinder extends NoResultDataBinder {
    private TopAdsEmptyAdDataBinder.Callback callback;

    public EmptyAddUrlVideoDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    public void setCallback(TopAdsEmptyAdDataBinder.Callback callback) {
        this.callback = callback;
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_video_empty, null);
        if (isFullScreen) {
            view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } else {
            view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        return new EmptyAddUrlVideoViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        ((EmptyAddUrlVideoViewHolder) holder).
                textAddUrlVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onEmptyContentItemTextClicked();
                }
            }
        });
    }

    public static class EmptyAddUrlVideoViewHolder extends TopAdsEmptyAdDataBinder.EmptyViewHolder {

        private final Button textAddUrlVideo;

        public EmptyAddUrlVideoViewHolder(View view) {
            super(view);
            textAddUrlVideo = (Button) view.findViewById(R.id.text_add_url_video);
        }
    }
}
