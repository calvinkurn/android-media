package com.tokopedia.discovery.newdiscovery.search.fragment.shop.adapter.viewholder

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import com.tokopedia.discovery.R;

public class ItemPreviewViewHolder extends RecyclerView.ViewHolder {

    public ImageView img_preview;

    public ItemPreviewViewHolder(View itemView) {
        super(itemView);
        this.img_preview = itemView.findViewById(R.id.img_preview);
    }
}