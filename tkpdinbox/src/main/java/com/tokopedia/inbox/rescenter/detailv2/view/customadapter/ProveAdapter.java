package com.tokopedia.inbox.rescenter.detailv2.view.customadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.AttachmentData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfsx on 10/11/17.
 */

public class ProveAdapter extends RecyclerView.Adapter<ProveAdapter.Holder> {

    Context context;
    private List<AttachmentData> attachmentDataList = new ArrayList<>();

    public ProveAdapter(Context context) {
        this.context = context;
    }

    public List<AttachmentData> getAttachmentDataList() {
        return attachmentDataList;
    }

    public void setAttachmentDataList(List<AttachmentData> attachmentDataList) {
        this.attachmentDataList = attachmentDataList;
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_detail_prove, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        AttachmentData attachmentData = attachmentDataList.get(position);
        Glide.with(context).load(attachmentData.getImageThumbUrl()).into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        if (attachmentDataList != null) {
            return attachmentDataList.size();
        }
        return 0;
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        public Holder(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
        }
    }
}
