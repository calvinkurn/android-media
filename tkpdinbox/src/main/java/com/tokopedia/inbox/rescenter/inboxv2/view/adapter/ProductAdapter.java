package com.tokopedia.inbox.rescenter.inboxv2.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.inbox.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoasfs on 26/10/17.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.Holder> {

    private static final int COUNT_MAX = 3;

    private List<String> attachmentList = new ArrayList<>();

    public ProductAdapter(List<String> attachmentList) {
        this.attachmentList = attachmentList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reso_inbox_product, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        String attachment = attachmentList.get(position);
        ImageHandler.loadImageCenterCrop(holder.ivImage, attachment);
    }

    @Override
    public int getItemCount() {
        return attachmentList.size() > COUNT_MAX ? COUNT_MAX : attachmentList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView ivImage;

        public Holder(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
        }
    }
}
