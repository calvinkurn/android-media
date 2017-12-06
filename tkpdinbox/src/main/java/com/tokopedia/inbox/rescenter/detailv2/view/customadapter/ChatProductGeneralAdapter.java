package com.tokopedia.inbox.rescenter.detailv2.view.customadapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    public void onBindViewHolder(Holder holder, final int position) {
        final ConversationAttachmentDomain product = productList.get(position);
        ImageHandler.LoadImage(holder.ivImage, product.getThumb());
        holder.tvMore.setVisibility(View.GONE);
        if (maxShowCount - 1 == position && maxShowCount < productList.size()) {
            holder.tvMore.setVisibility(View.VISIBLE);
            holder.tvMore.setText("+" + (productList.size() - position));
        }

        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(productList.get(position).getType().equalsIgnoreCase(ConversationAttachmentDomain.TYPE_IMAGE)) {
                    openProductPreview(productList, position);
                } else if (productList.get(position).getType().equalsIgnoreCase(ConversationAttachmentDomain.TYPE_VIDEO)) {
                    openVideoPlayer(productList.get(position).getFull());
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
        String type;
        try {
            type = model.getType();
        } catch (Exception e) {
            type = "";
            e.printStackTrace();
        }
        return type;
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
