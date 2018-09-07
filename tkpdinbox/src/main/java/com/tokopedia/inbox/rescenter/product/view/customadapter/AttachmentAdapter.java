package com.tokopedia.inbox.rescenter.product.view.customadapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.PreviewProductImage;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.player.VideoPlayerActivity;
import com.tokopedia.inbox.rescenter.product.view.model.Attachment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangnadi on 3/28/17.
 */

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.ViewHolder> {

    private final List<Attachment> attachment;

    public AttachmentAdapter(List<Attachment> attachment) {
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ImageHandler.LoadImage(
                holder.placeHolder,
                getAttachmentItem(position).getThumbUrl());
        holder.placeHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getAttachmentItem(position).isVideo()) {
                    openVideo(view.getContext(), holder.getAdapterPosition());
                } else {
                    openAttachmentPreview(view.getContext(), holder.getAdapterPosition());
                }
            }
        });
    }

    private void openVideo(Context context, int adapterPosition) {
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(VideoPlayerActivity.PARAMS_URL_VIDEO, getAttachmentItem(adapterPosition).getUrl());
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private void openAttachmentPreview(Context context, int adapterPosition) {
        ArrayList<String> imageUrls = new ArrayList<>();
        for (Attachment attachment : getAttachment()) {
            imageUrls.add(attachment.getUrl());
        }
        Intent intent = new Intent(context, PreviewProductImage.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("fileloc", imageUrls);
        bundle.putInt("img_pos", adapterPosition);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public List<Attachment> getAttachment() {
        return attachment;
    }

    public Attachment getAttachmentItem(int position) {
        return getAttachment().get(position);
    }

    @Override
    public int getItemCount() {
        return attachment.size();
    }

}
