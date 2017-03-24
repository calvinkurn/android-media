package com.tokopedia.inbox.rescenter.history.customadapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.PreviewProductImage;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.history.viewmodel.ShippingAttachment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangnadi on 3/23/17.
 */

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.ViewHolder> {

    private final List<ShippingAttachment> attachment;

    public AttachmentAdapter(List<ShippingAttachment> attachment) {
        this.attachment = attachment;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView placeHolder;

        public ViewHolder(View itemView) {
            super(itemView);
            placeHolder = (ImageView) itemView.findViewById(R.id.attachment);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_attachment_rescenter_create, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ImageHandler.loadImage2(holder.placeHolder, getAttachmentItem(position).getUrlThumbnail(), R.drawable.ic_action_attachment);
        holder.placeHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                ArrayList<String> imageUrls = new ArrayList<>();
                for (ShippingAttachment attachment : getAttachment()) {
                    imageUrls.add(attachment.getUrl());
                }
                Intent intent = new Intent(context, PreviewProductImage.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("fileloc", imageUrls);
                bundle.putInt("img_pos", holder.getAdapterPosition());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    public List<ShippingAttachment> getAttachment() {
        return attachment;
    }

    public ShippingAttachment getAttachmentItem(int position) {
        return getAttachment().get(position);
    }

    @Override
    public int getItemCount() {
        return attachment.size();
    }

}
