package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.inboxreputation.model.ShareItem;

import java.util.ArrayList;

/**
 * Created by stevenfredian on 2/21/17.
 */

public class ShareFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ShareItem> list;

    public ShareFeedAdapter() {
        this.list = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(com.tokopedia.tkpd.tkpdfeed.R.layout.sheet_feed_grid_item, parent, false);
        return new ShareFeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder temp, int position) {
        ShareFeedViewHolder holder = (ShareFeedViewHolder) temp;
        ShareItem item = list.get(position);
        if (item.getIcon() != null) {
            holder.icon.setBackgroundDrawable(item.getIcon());
        }
        holder.label.setText(item.getName());

        holder.itemView.setOnClickListener(item.getOnClickListener());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(ArrayList<ShareItem> list) {
        this.list.clear();
        this.list = list;
        notifyDataSetChanged();
    }

    private class ShareFeedViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView label;

        public ShareFeedViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            label = (TextView) itemView.findViewById(R.id.label);
        }
    }
}