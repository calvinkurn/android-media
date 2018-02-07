package com.tokopedia.inbox.rescenter.detailv2.view.customadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationProductDomain;
import com.tokopedia.inbox.rescenter.product.ListProductActivity;
import com.tokopedia.inbox.rescenter.product.ProductDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoasfs on 26/10/17.
 */

public class ChatProductAdapter extends RecyclerView.Adapter<ChatProductAdapter.Holder> {

    private final DetailResChatFragmentListener.View mainView;
    private final Context context;
    private List<ConversationProductDomain> productList = new ArrayList<>();
    private int maxShowCount = 0;

    public ChatProductAdapter(DetailResChatFragmentListener.View mainView,
                              Context context,
                              List<ConversationProductDomain> productList,
                              int maxShowCount) {
        this.mainView = mainView;
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
        final ConversationProductDomain product = productList.get(position);
        ImageHandler.LoadImage(holder.ivImage, product.getImage().get(0).getThumb());
        holder.tvMore.setVisibility(View.GONE);
        if (maxShowCount - 1 == position && maxShowCount < productList.size()) {
            holder.tvMore.setVisibility(View.VISIBLE);
            int plusNumber = productList.size() - position;
            String plusString = "+" + plusNumber;
            holder.tvMore.setText(plusString);
            holder.tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainView.goToProductList(product);
                }
            });
        }

        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainView.goToProductDetail(product);
            }
        });
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
