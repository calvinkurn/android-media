package com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.chatroom.view.listener.ChannelInfoFragmentListener;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.ChannelPartnerViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by milhamj on 26/03/18.
 */

public class ChannelPartnerAdapter extends RecyclerView.Adapter<ChannelPartnerAdapter.ViewHolder> {

    private ChannelInfoFragmentListener.View.ChannelPartnerViewHolderListener listener;
    private List<ChannelPartnerViewModel> list;

    private ChannelPartnerAdapter(ChannelInfoFragmentListener
                                          .View
                                          .ChannelPartnerViewHolderListener listener) {
        this.listener = listener;
        this.list = new ArrayList<>();
    }

    public static ChannelPartnerAdapter createInstance(ChannelInfoFragmentListener
                                                               .View
                                                               .ChannelPartnerViewHolderListener
                                                               listener) {
        return new ChannelPartnerAdapter(listener);
    }

    public List<ChannelPartnerViewModel> getList() {
        return list;
    }

    public void setList(List<ChannelPartnerViewModel> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_channel_partner, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.partnerTitle.setText(list.get(position).getPartnerTitle());

        holder.partnerChildren.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                holder.partnerChildren.getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        holder.partnerChildren.setLayoutManager(linearLayoutManager);

        if (list.get(position).getChild() != null
            && !list.get(position).getChild().isEmpty()) {
            ChannelPartnerChildAdapter childAdapter =
                    ChannelPartnerChildAdapter.createInstance(listener);
            childAdapter.setList(list.get(position).getChild());
            holder.partnerChildren.setAdapter(childAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView partnerTitle;
        RecyclerView partnerChildren;

        public ViewHolder(View itemView) {
            super(itemView);
            partnerTitle = itemView.findViewById(R.id.partner_title);
            partnerChildren = itemView.findViewById(R.id.partner_children);
        }
    }
}
