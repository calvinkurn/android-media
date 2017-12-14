package com.tokopedia.inbox.rescenter.detailv2.view.customadapter;

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
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationAttachmentDomain;
import com.tokopedia.inbox.rescenter.player.VideoPlayerActivity;

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
    public void onBindViewHolder(Holder holder, final int position) {
        ConversationAttachmentDomain attachment = attachmentList.get(position);
        ImageHandler.LoadImage(holder.ivImage, attachment.getThumb());

        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getTypeFromModel(attachmentList.get(position)).equalsIgnoreCase(ConversationAttachmentDomain.TYPE_IMAGE)) {
                    openProductPreview(attachmentList, position);
                } else if (getTypeFromModel(attachmentList.get(position)).equalsIgnoreCase(ConversationAttachmentDomain.TYPE_VIDEO)) {
                    openVideoPlayer(attachmentList.get(position).getFull());
                }
            }
        });
    }

    private void openProductPreview(List<ConversationAttachmentDomain> list, int position) {
        ArrayList<String> imageUrls = new ArrayList<>();
        for (ConversationAttachmentDomain model : list) {
            if(getTypeFromModel(model).equalsIgnoreCase(ConversationAttachmentDomain.TYPE_IMAGE)) {
                imageUrls.add(model.getFull());
            }
        }
        Intent intent = new Intent(context, PreviewProductImage.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("fileloc", imageUrls);
        bundle.putInt("img_pos", position);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private String getTypeFromModel(ConversationAttachmentDomain model) {
        return (model.getType() != null) ? model.getType() : "";
    }

    private void openVideoPlayer(String urlVideo) {
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(VideoPlayerActivity.PARAMS_URL_VIDEO, urlVideo);
        intent.putExtras(bundle);
        context.startActivity(intent);
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
