package com.tokopedia.tkpdstream.chatroom.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.tokopedia.tkpdstream.R;

import java.util.ArrayList;

/**
 * @author by stevenfredian on 2/21/17.
 */

public class ShareFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ShareItem> list;

    public ShareFeedAdapter() {
        this.list = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sheet_groupchat_item, parent, false);
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