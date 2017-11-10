package com.tokopedia.inbox.rescenter.createreso.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.view.listener.AttachmentAdapterListener;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.attachment.AttachmentViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoasfs on 04/09/17.
 */

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.ItemHolder> {
    private Context context;
    private List<AttachmentViewModel> attachmentViewModelList = new ArrayList<>();
    private AttachmentAdapterListener listener;
    private int maxAttachmentCount;

    public AttachmentAdapter(Context context, int maxAttachmentCount, AttachmentAdapterListener attachmentAdapterListener) {
        this.context = context;
        this.maxAttachmentCount = maxAttachmentCount;
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

    public void deleteAttachment(int position) {
        attachmentViewModelList.remove(position);
        if (attachmentViewModelList.size() == 0) {
            listener.onEmptyAdapter();
        }
        notifyDataSetChanged();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.item_attachment, parent, false));
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, int position) {
        final int itemPos = attachmentViewModelList.size() < maxAttachmentCount ? position - 1 : position;
        if (attachmentViewModelList.size() < maxAttachmentCount && position == 0) {
            holder.ivClose.setVisibility(View.GONE);
            holder.ivImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_attachment));
        } else {
            //show without upload image icon
            holder.ivClose.setVisibility(View.VISIBLE);
            File file = new File(attachmentViewModelList.get(itemPos).getFileLoc());
            holder.ivImage.setImageURI(Uri.fromFile(file));
            Glide.with(context).load(attachmentViewModelList.get(itemPos).getFileLoc()).into(holder.ivImage);
            holder.ivImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.getAdapterPosition() == 0) {
                    listener.onAddAttachmentClicked();
                } else {
                    //image clicked
                }
            }
        });

        holder.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAttachment(itemPos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return attachmentViewModelList.size() < maxAttachmentCount ? attachmentViewModelList.size() + 1 : attachmentViewModelList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        ImageView ivImage, ivClose;

        public ItemHolder(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
            ivClose = (ImageView) itemView.findViewById(R.id.iv_close);
        }
    }

    public List<AttachmentViewModel> getList() {
        return attachmentViewModelList;
    }
}
