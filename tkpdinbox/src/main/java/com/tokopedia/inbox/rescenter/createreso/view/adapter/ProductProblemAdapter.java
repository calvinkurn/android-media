package com.tokopedia.inbox.rescenter.createreso.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ProblemResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoasfs on 14/08/17.
 */

public class ProductProblemAdapter extends RecyclerView.Adapter<ProductProblemAdapter.ItemHolder> {

    public static final int FREE_RETURN = 3;

    public static final int ITEM_TEXT = 1;
    public static final int ITEM_PRODUCT = 2;

    private Context context;
    private List<ProblemResult> selectedProblemResultList = new ArrayList<>();
    private List<Object> itemList = new ArrayList<>();
    private ProductProblemItemListener listener;


    public ProductProblemAdapter(Context context, ProductProblemItemListener listener) {
        this.context = context;
        this.listener = listener;
        selectedProblemResultList = new ArrayList<>();
    }

    public void updateAdapter(List<ProductProblemViewModel> productProblemList) {
        itemList = new ArrayList<>();
        int type = 0;
        for (ProductProblemViewModel productProblem : productProblemList) {
            if (type != productProblem.getProblem().getType()) {
                type = productProblem.getProblem().getType();
                if (type == ITEM_PRODUCT) {
                    itemList.add(context.getResources().getString(R.string.string_adapter_product_problem));
                } else {
                    itemList.add(context.getResources().getString(R.string.string_adapter_money));
                }
            }
            itemList.add(productProblem);
        }
        notifyDataSetChanged();
    }

    public void clearAndUpdateSelectedItem(List<ProblemResult> selectedProblemResultList) {
        this.selectedProblemResultList = selectedProblemResultList;
        notifyDataSetChanged();
    }


    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.item_product_problem, parent, false));
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, int position) {
        if (itemList.get(position) instanceof ProductProblemViewModel) {
            final ProductProblemViewModel productProblem = (ProductProblemViewModel) itemList.get(position);
            holder.tvTitleSection.setVisibility(View.GONE);
            holder.llItem.setVisibility(View.VISIBLE);
            holder.llFreeReturn.setVisibility(View.GONE);
            holder.checkBox.setChecked(false);
            for (ProblemResult problemResult : selectedProblemResultList) {
                if (productProblem.getProblem().getType() == ITEM_TEXT) {
                    if (problemResult.name.equals(productProblem.getProblem().getName())) {
                        holder.checkBox.setChecked(true);
                    }
                } else {
                    if (problemResult.id == productProblem.getOrder().getDetail().getId()) {
                        holder.checkBox.setChecked(true);
                    }
                }
            }
            if (productProblem.getProblem().getType() == ITEM_TEXT) {
                holder.tvProductName.setText(productProblem.getProblem().getName());
                holder.ivProduct.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_ongkir));
            } else {
                if (productProblem.getOrder() != null) {
                    if (productProblem.getOrder().getDetail().getReturnable() == FREE_RETURN) {
                        holder.llFreeReturn.setVisibility(View.VISIBLE);
                    }
                    if (productProblem.getOrder().getProduct() != null) {
                        if (productProblem.getOrder().getProduct().getThumb() != null) {
                            Glide.with(context).load(productProblem.getOrder().getProduct().getThumb()).into(holder.ivProduct);
                        }
                        holder.tvProductNameFreeReturn.setText(productProblem.getOrder().getProduct().getName());
                        holder.tvProductName.setText(productProblem.getOrder().getProduct().getName());
                    }
                }
            }
            holder.llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (productProblem.getProblem().getType() == ITEM_PRODUCT) {
                        listener.onItemClicked(productProblem);
                    } else {
                        listener.onStringProblemClicked(productProblem);
                    }
                }
            });

            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.checkBox.isChecked()) {
                        holder.checkBox.setChecked(false);
                        if (productProblem.getProblem().getType() == ITEM_PRODUCT) {
                            listener.onItemClicked(productProblem);
                        } else {
                            listener.onStringProblemClicked(productProblem);
                        }
                    } else {
                        listener.onRemoveProductProblem(productProblem);
                    }
                }
            });
        } else if (itemList.get(position) instanceof String) {
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
        LinearLayout llItem, llFreeReturn;
        TextView tvProductName, tvTitleSection, tvFreeReturn, tvProductNameFreeReturn;

        public ItemHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            ivProduct = (ImageView) itemView.findViewById(R.id.iv_product_image);
            tvProductName = (TextView) itemView.findViewById(R.id.tv_product_name);
            tvTitleSection = (TextView) itemView.findViewById(R.id.tv_title_section);
            tvProductNameFreeReturn = (TextView) itemView.findViewById(R.id.tv_product_name_free_return);
            tvFreeReturn = (TextView) itemView.findViewById(R.id.tv_free_return);
            llItem = (LinearLayout) itemView.findViewById(R.id.ll_item_view);
            llFreeReturn = (LinearLayout) itemView.findViewById(R.id.ll_free_return);
        }
    }
}
