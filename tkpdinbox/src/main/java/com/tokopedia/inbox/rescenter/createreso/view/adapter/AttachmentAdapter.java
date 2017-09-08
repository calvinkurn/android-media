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
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.attachment.AttachmentViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoasfs on 04/09/17.
 */

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.ItemHolder> {
    private Context context;
    private List<AttachmentViewModel> attachmentViewModelList = new ArrayList<>();
    private AttachmentAdapterListener listener;

    public AttachmentAdapter(Context context, AttachmentAdapterListener attachmentAdapterListener) {
        this.context = context;
        this.listener = attachmentAdapterListener;
    }

    public void updateAdapter(List<AttachmentViewModel> attachmentViewModelList) {
        this.attachmentViewModelList = attachmentViewModelList;
        notifyDataSetChanged();
    }

    public void addAttachment(AttachmentViewModel attachmentViewModel) {
        attachmentViewModelList.add(attachmentViewModel);
        notifyDataSetChanged();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.item_attachment, parent, false));
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, int position) {
        if (position == attachmentViewModelList.size()) {
            //show upload image
            holder.ivImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_attachment));
        } else {
            //show image
            Glide.with(context).load(attachmentViewModelList.get(position).getUrl()).into(holder.ivImage);
        }

        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.getAdapterPosition() == attachmentViewModelList.size()) {
                    listener.onAddAttachmentClicked();
                } else {
                    //image clicked
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return attachmentViewModelList.size() + 1;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        public ItemHolder(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
        }
    }

    public List<AttachmentViewModel> getList() {
        return attachmentViewModelList;
    }
}
