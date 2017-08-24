package com.tokopedia.tkpdpdp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.network.entity.variant.Option;
import com.tokopedia.core.network.entity.variant.ProductVariant;
import com.tokopedia.core.network.entity.variant.VariantOption;
import com.tokopedia.tkpdpdp.R;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;

import java.util.List;

import static com.tokopedia.core.network.entity.variant.VariantOption.IDENTIFIER_COLOUR;

/**
 * Created by alifa on 8/21/17.
 */

public class VariantParentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements VariantOptionAdapter.OnVariantOptionChoosedListener {

    public static final int VARIANT_TITLE = -1;

    private Context context;
    private ProductVariant variant;

    public VariantParentAdapter(Context context, @NonNull ProductVariant variant) {
        this.context = context;
        this.variant = variant;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VARIANT_TITLE:
                return new VariantTitleViewHolder(LayoutInflater.from(context).inflate(
                        R.layout.variant_title_item, parent, false));
            default:
                return new VariantOptionsViewHolder(LayoutInflater.from(context).inflate(
                        R.layout.variant_option, parent, false),context);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0) {
            return VARIANT_TITLE;
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VARIANT_TITLE:
                ((VariantTitleViewHolder) holder).bindData(variant);
                break;
            default:
                ((VariantOptionsViewHolder) holder).bindData(variant.getVariantOption().get(position-1),position-1);
        }
    }

    @Override
    public int getItemCount() {
        return (variant.getVariantOption().size()+1);
    }

    @Override
    public void onVariantChosen(Option option, int level) {
        final int MAX_LEVEL =1;
        if (level>MAX_LEVEL)  return;
        List<Integer> combinations = variant.getCombinationFromSelectedVariant(option.getPvoId());
        if (level==0) {
            for (Option otherLevelOption: variant.getVariantOption().get(MAX_LEVEL-level).getOption()) {
                if (combinations.contains(otherLevelOption.getPvoId())) {
                    otherLevelOption.setEnabled(true);
                } else {
                    otherLevelOption.setEnabled(false);
                }
            }
            notifyItemChanged(MAX_LEVEL-level+1);
        }



    }

    static class VariantTitleViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productPrice;

        VariantTitleViewHolder(View itemView) {
            super(itemView);
            productName = (TextView) itemView.findViewById(R.id.variant_product_name);
            productPrice = (TextView) itemView.findViewById(R.id.variant_product_price);
            productImage = (ImageView) itemView.findViewById(R.id.variant_image_title);
        }

        public void bindData(ProductVariant productVariant) {
            productName.setText(productVariant.getProductName());
            productPrice.setText(productVariant.getProductPrice());
            ImageHandler.LoadImage(productImage,productVariant.getProductImageUrl());
        }
    }

     class VariantOptionsViewHolder extends RecyclerView.ViewHolder {
        RecyclerView optionRecyclerView;
        TextView optionName;
        Context context;

        VariantOptionsViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            optionName = (TextView) itemView.findViewById(R.id.text_variant_option);
            optionRecyclerView = (RecyclerView) itemView.findViewById(R.id.rv_variant_option);
        }

        public void bindData(VariantOption variantOption, int level) {
            optionName.setText(variantOption.getName()+" :");

            VariantOptionAdapter variantOptionAdapter
                    = new VariantOptionAdapter(context,variantOption.getOption(),
                    TextUtils.equals(IDENTIFIER_COLOUR,variantOption.getIdentifier()), VariantParentAdapter.this, level);

            ChipsLayoutManager chipsLayoutManager= ChipsLayoutManager.newBuilder(context)
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                    .build();
            optionRecyclerView.setNestedScrollingEnabled(false);
            optionRecyclerView.setLayoutManager(chipsLayoutManager);
            optionRecyclerView.setAdapter(variantOptionAdapter);
        }
    }
}


