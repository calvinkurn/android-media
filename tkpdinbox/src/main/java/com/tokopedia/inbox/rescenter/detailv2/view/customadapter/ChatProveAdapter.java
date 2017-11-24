package com.tokopedia.inbox.rescenter.detailv2.view.customadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationAttachmentDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoasfs on 26/10/17.
 */

public class ChatProveAdapter extends RecyclerView.Adapter<ChatProveAdapter.Holder> {

    private Context context;
    private List<ConversationAttachmentDomain> attachmentList = new ArrayList<>();

    public ChatProveAdapter(Context context, List<ConversationAttachmentDomain> attachmentList) {
        this.context = context;
        this.attachmentList = attachmentList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_prove_image, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        ConversationAttachmentDomain attachment = attachmentList.get(position);
        Glide.with(context).load(attachment.getThumb()).into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return attachmentList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView ivImage;

        public Holder(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
        }
    }
}
