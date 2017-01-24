package com.tokopedia.seller.topads.view.adapter.viewholder;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.tokopedia.core.customadapter.NoResultDataBinder;
import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.seller.R;

/**
 * Created by Nisie on 2/26/16.
 */
public class TopAdsEmptyAdDataBinder extends NoResultDataBinder {

    public interface Callback {

        void onEmptyContentItemTextClicked();

    }

    private String emptyTitleText;
    private String emptyContentText;
    private String emptyContentItemText;
    private Callback callback;

    public TopAdsEmptyAdDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    public static class EmptyViewHolder extends ViewHolder {
        TextView emptyTitleTextView;
        TextView emptyContentTextView;
        TextView emptyContentItemTextView;

        public EmptyViewHolder(View view) {
            super(view);
            emptyTitleTextView = (TextView) view.findViewById(R.id.text_view_empty_title_text);
            emptyContentTextView = (TextView) view.findViewById(R.id.text_view_empty_content_text);
            emptyContentItemTextView = (TextView) view.findViewById(R.id.text_view_empty_content_item_text);
        }
    }

    public void setEmptyTitleText(String emptyTitleText) {
        this.emptyTitleText = emptyTitleText;
    }

    public void setEmptyContentText(String emptyContentText) {
        this.emptyContentText = emptyContentText;
    }

    public void setEmptyContentItemText(String emptyContentItemText) {
        this.emptyContentItemText = emptyContentItemText;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_top_ads_empty_ad_list, null);
        if (isFullScreen) {
            view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } else {
            view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        return new EmptyViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        EmptyViewHolder emptyViewHolder = (EmptyViewHolder) holder;
        if (!TextUtils.isEmpty(emptyTitleText)) {
            emptyViewHolder.emptyTitleTextView.setText(emptyTitleText);
        }
        if (!TextUtils.isEmpty(emptyContentText)) {
            emptyViewHolder.emptyContentTextView.setText(emptyContentText);
        }
        if (!TextUtils.isEmpty(emptyContentText)) {
            emptyViewHolder.emptyContentTextView.setText(emptyContentText);
        }
        if (!TextUtils.isEmpty(emptyContentItemText)) {
            emptyViewHolder.emptyContentItemTextView.setText(emptyContentItemText);
            emptyViewHolder.emptyContentItemTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (callback != null) {
                        callback.onEmptyContentItemTextClicked();
                    }
                }
            });
        }
    }
}