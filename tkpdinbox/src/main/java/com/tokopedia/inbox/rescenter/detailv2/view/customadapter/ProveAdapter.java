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
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.AttachmentData;
import com.tokopedia.inbox.rescenter.player.VideoPlayerActivity;

import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.AttachmentData.TYPE_IMAGE;
import static com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.AttachmentData.TYPE_VIDEO;

/**
 * Created by yfsx on 10/11/17.
 */

public class ProveAdapter extends RecyclerView.Adapter<ProveAdapter.Holder> {

    private Context context;
    private List<AttachmentData> attachmentDataList = new ArrayList<>();

    public ProveAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context)
                .inflate(R.layout.item_detail_prove,
                        parent,
                        false));
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        AttachmentData attachmentData = attachmentDataList.get(position);
        ImageHandler.LoadImage(holder.ivImage, attachmentData.getImageThumbUrl());

        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (attachmentDataList.get(position).getIsVideo() == TYPE_IMAGE) {
                    openProductPreview(attachmentDataList, position);
                } else if (attachmentDataList.get(position).getIsVideo() == TYPE_VIDEO) {
                    openVideoPlayer(attachmentDataList.get(position).getImageUrl());
                }
            }
        });
    }

    private void openProductPreview(List<AttachmentData> list, int position) {
        ArrayList<String> imageUrls = new ArrayList<>();
        for (AttachmentData model : list) {
            if (attachmentDataList.get(position).getIsVideo() == TYPE_IMAGE) {
                imageUrls.add(model.getImageUrl());
            }
        }
        Intent intent = new Intent(context, PreviewProductImage.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("fileloc", imageUrls);
        bundle.putInt("img_pos", position);
        intent.putExtras(bundle);
        context.startActivity(intent);
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
        return attachmentDataList.size();
    }

    public List<AttachmentData> getAttachmentDataList() {
        return attachmentDataList;
    }

    public void setAttachmentDataList(List<AttachmentData> attachmentDataList) {
        this.attachmentDataList = attachmentDataList;
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView ivImage;

        public Holder(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
        }
    }
}
