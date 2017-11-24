package com.tokopedia.inbox.rescenter.detailv2.view.customadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.AttachmentData;

import java.util.ArrayList;
import java.util.List;

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
    public void onBindViewHolder(Holder holder, int position) {
        AttachmentData attachmentData = attachmentDataList.get(position);
        ImageHandler.LoadImage(holder.ivImage, attachmentData.getImageThumbUrl());
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
