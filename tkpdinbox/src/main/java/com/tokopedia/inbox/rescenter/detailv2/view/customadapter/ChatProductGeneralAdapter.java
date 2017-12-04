package com.tokopedia.inbox.rescenter.detailv2.view.customadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationAttachmentDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationProductDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoasfs on 26/10/17.
 */

public class ChatProductGeneralAdapter extends RecyclerView.Adapter<ChatProductGeneralAdapter.Holder> {

    private Context context;
    private List<ConversationAttachmentDomain> productList = new ArrayList<>();
    private int maxShowCount = 0;

    public ChatProductGeneralAdapter(Context context, List<ConversationAttachmentDomain> productList, int maxShowCount) {
        this.context = context;
        this.productList = productList;
        this.maxShowCount = maxShowCount;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_product_image, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        ConversationAttachmentDomain product = productList.get(position);
        ImageHandler.LoadImage(holder.ivImage, product.getThumb());
        holder.tvMore.setVisibility(View.GONE);
        if (maxShowCount - 1 == position && maxShowCount < productList.size()) {
            holder.tvMore.setVisibility(View.VISIBLE);
            holder.tvMore.setText("+" + (productList.size() - position));
        }
    }

    @Override
    public int getItemCount() {
        return productList.size() < maxShowCount ?
                productList.size() :
                maxShowCount;
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvMore;

        public Holder(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
            tvMore = (TextView) itemView.findViewById(R.id.tv_more);
        }
    }
}
