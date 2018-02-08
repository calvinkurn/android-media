package com.tokopedia.topads.dashboard.view.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

/**
 * Created by normansyahputa on 3/6/17.
 */

public abstract class BaseTopAdsAddProductListViewHolder extends RecyclerView.ViewHolder {
    protected TextView description;
    protected TextView snippet;
    protected View emptySpace;

    public BaseTopAdsAddProductListViewHolder(View itemView) {
        super(itemView);
        initViews(itemView);
    }

    protected abstract void initViews(View itemView);

    protected void setDescriptionText(String text) {
        if (!TextUtils.isEmpty(text)) {
            description.setText(text);
        }
    }

    protected void setSnippet(String text) {
        if (text != null && !text.isEmpty()) {
            snippet.setVisibility(View.VISIBLE);
            snippet.setText(text);
            emptySpace.setVisibility(View.GONE);
        } else {
            snippet.setVisibility(View.INVISIBLE);
            emptySpace.setVisibility(View.VISIBLE);
        }
    }
}
