package com.tokopedia.inbox.rescenter.createreso.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.view.listener.AttachmentAdapterListener;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.attachment.AttachmentImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoasfs on 04/09/17.
 */

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.ItemHolder> {
    private Context context;
    private List<AttachmentImage> attachmentImageList = new ArrayList<>();
    private AttachmentAdapterListener listener;

    public AttachmentAdapter(Context context, AttachmentAdapterListener attachmentAdapterListener) {
        this.context = context;
        this.listener = attachmentAdapterListener;
    }

    public void updateAdapter(List<AttachmentImage> attachmentImageList) {
        this.attachmentImageList = attachmentImageList;
        notifyDataSetChanged();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.item_attachment, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, final int position) {
        if (position == attachmentImageList.size()) {
            //show upload image
            holder.ivImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_attachment));
        } else {
            //show image
            Glide.with(context).load(attachmentImageList.get(position).realPath).into(holder.ivImage);
        }

        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == attachmentImageList.size()) {
                    //add image clicked
                    listener.onAddAttachmentClicked();
                } else {
                    //image clicked
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return attachmentImageList.size() + 1;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        public ItemHolder(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
        }
    }
}
