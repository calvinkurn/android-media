package com.tokopedia.inbox.rescenter.createreso.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.view.listener.ProductProblemItemListener;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoasfs on 14/08/17.
 */

public class ProductProblemAdapter extends RecyclerView.Adapter<ProductProblemAdapter.ItemHolder> {
    private Context context;
    private List<ProductProblemViewModel> productProblemList = new ArrayList<>();
    private List<Object> itemList = new ArrayList<>();
    private ProductProblemItemListener listener;


    public ProductProblemAdapter(Context context, ProductProblemItemListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void updateAdapter(List<ProductProblemViewModel> productProblemList) {
        this.productProblemList = productProblemList;
        itemList = new ArrayList<>();
        int type = 0;
        for (ProductProblemViewModel productProblem : productProblemList) {
            if (type != productProblem.getProblem().getType()) {
                type = productProblem.getProblem().getType();
                itemList.add(productProblem.getProblem().getName());
            }
            itemList.add(productProblem);
        }
        notifyDataSetChanged();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.item_product_problem, null));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        if (itemList.get(position) instanceof ProductProblemViewModel) {
            final ProductProblemViewModel productProblem = (ProductProblemViewModel) itemList.get(position);
            holder.tvTitleSection.setVisibility(View.GONE);
            holder.llItem.setVisibility(View.VISIBLE);
            if (productProblem.getProblem().getType() == 1) {
                holder.tvProductName.setText(productProblem.getProblem().getName());
            } else {
                if (productProblem.getOrder() != null) {
                    if (productProblem.getOrder().getProduct() != null) {
                        if (productProblem.getOrder().getProduct().getThumb() != null) {
                            Glide.with(context).load(productProblem.getOrder().getProduct().getThumb()).into(holder.ivProduct);
                        }
                        holder.tvProductName.setText(productProblem.getOrder().getProduct().getName());
                    }
                }
            }
            holder.llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (productProblem.getProblem().getType() == 2) {
                        listener.onItemClicked(productProblem);
                    }
                }
            });
        } else if(itemList.get(position) instanceof String) {
            String title = (String) itemList.get(position);
            holder.llItem.setVisibility(View.GONE);
            holder.tvTitleSection.setVisibility(View.VISIBLE);
            holder.tvTitleSection.setText(title);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        ImageView ivProduct;
        LinearLayout llItem;
        TextView tvProductName, tvTitleSection;
        public ItemHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            ivProduct = (ImageView) itemView.findViewById(R.id.iv_product_image);
            tvProductName = (TextView) itemView.findViewById(R.id.tv_product_name);
            tvTitleSection = (TextView) itemView.findViewById(R.id.tv_title_section);
            llItem = (LinearLayout) itemView.findViewById(R.id.ll_item_view);
        }
    }
}
