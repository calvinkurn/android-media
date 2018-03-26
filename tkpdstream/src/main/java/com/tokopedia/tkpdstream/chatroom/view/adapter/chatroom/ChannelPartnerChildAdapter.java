package com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.chatroom.view.listener.ChannelInfoFragmentListener;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.ChannelPartnerChildViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by milhamj on 26/03/18.
 */

public class ChannelPartnerChildAdapter
        extends RecyclerView.Adapter<ChannelPartnerChildAdapter.ViewHolder> {

    private ChannelInfoFragmentListener.View.ChannelPartnerViewHolderListener listener;
    private List<ChannelPartnerChildViewModel> list;

    private ChannelPartnerChildAdapter(ChannelInfoFragmentListener
                                               .View
                                               .ChannelPartnerViewHolderListener listener) {
        this.listener = listener;
        this.list = new ArrayList<>();
    }

    public static ChannelPartnerChildAdapter createInstance(ChannelInfoFragmentListener
                                                                    .View
                                                                    .ChannelPartnerViewHolderListener
                                                                    listener) {
        return new ChannelPartnerChildAdapter(listener);
    }

    public List<ChannelPartnerChildViewModel> getList() {
        return list;
    }

    public void setList(List<ChannelPartnerChildViewModel> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_channel_partner_child, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.partnerName.setText(list.get(position).getPartnerName());
        ImageHandler.loadImage2(holder.partnerAvatar,
                list.get(position).getPartnerAvatar(),
                R.drawable.loading_page);
        holder.partnerChildLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.channelPartnerClicked(list.get(holder.getAdapterPosition()).getPartnerUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View partnerChildLayout;
        ImageView partnerAvatar;
        TextView partnerName;

        public ViewHolder(View itemView) {
            super(itemView);
            partnerChildLayout = itemView.findViewById(R.id.partner_child_layout);
            partnerAvatar = itemView.findViewById(R.id.partner_avatar);
            partnerName = itemView.findViewById(R.id.partner_name);
        }
    }
}
