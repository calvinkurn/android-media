package com.tokopedia.tkpdpdp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.network.entity.variant.Option;
import com.tokopedia.tkpdpdp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alifa on 8/21/17.
 */

public class VariantOptionAdapter extends RecyclerView.Adapter<VariantOptionAdapter.VariantOptionViewHolder> {

    private final Context context;
    private final List<Option> variantOptions;
    private final boolean isColor;
    private int selectedPosition = 0;
    private final OnVariantOptionChoosedListener variantChosenListener;
    private final int level;

    public VariantOptionAdapter(Context context, List<Option> variantOptions,
                                boolean isColor, OnVariantOptionChoosedListener variantChosenListener, int level) {
        this.context = context;
        this.variantOptions = variantOptions;
        this.isColor = isColor;
        this.variantChosenListener = variantChosenListener;
        this.level = level;
    }

    @Override
    public VariantOptionAdapter.VariantOptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VariantOptionAdapter.VariantOptionViewHolder(LayoutInflater.from(context).inflate(
                R.layout.item_variant_option, parent, false));
    }

    @Override
    public void onBindViewHolder(VariantOptionAdapter.VariantOptionViewHolder holder, final int position) {
        holder.bindData(variantOptions.get(position),context,isColor,position==selectedPosition);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (variantOptions.get(position).isEnabled()) {
                    selectedPosition = position;
                    notifyDataSetChanged();
                    variantChosenListener.onVariantChosen(variantOptions.get(selectedPosition),level);
                }
            }
        });
    }

    public void notifyItemSelectedChange() {
        variantChosenListener.onVariantChosen(variantOptions.get(selectedPosition),level);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return variantOptions.size();
    }


    class VariantOptionViewHolder extends RecyclerView.ViewHolder {
        TextView textOption;
        ImageView imageColor;
        protected View view;
        protected LinearLayout container;

        VariantOptionViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            container = (LinearLayout) itemView.findViewById(R.id.variant_container);
            textOption = (TextView) itemView.findViewById(R.id.text_variant);
            imageColor = (ImageView) itemView.findViewById(R.id.image_color);
        }

        public void bindData(Option variantOption, Context context, Boolean isColor, Boolean isSelected) {
            textOption.setText(variantOption.getValue());
            if (isColor && !TextUtils.isEmpty(variantOption.getHex())) {
                Drawable background = imageColor.getBackground();
                if (background instanceof GradientDrawable) {
                    ((GradientDrawable)background).setColor(Color.parseColor(variantOption.getHex()));
                }
                imageColor.setVisibility(View.VISIBLE);
            }
            Drawable background = container.getBackground();
            if (!variantOption.isEnabled()) {
                if (background instanceof GradientDrawable) {
                    ((GradientDrawable)background).setColor(ContextCompat.getColor(context,R.color.grey_hint));
                    ((GradientDrawable)background).setStroke(1,ContextCompat.getColor(context,R.color.grey_hint));
                }
                textOption.setTextColor(ContextCompat.getColor(context,R.color.black_12));
            } else {
                textOption.setTextColor(ContextCompat.getColor(context,R.color.black_70));
                if (isSelected) {
                    if (background instanceof GradientDrawable) {
                        ((GradientDrawable)background).setColor(ContextCompat.getColor(context,R.color.green_variant_choosed));
                        ((GradientDrawable)background).setStroke(1,ContextCompat.getColor(context,R.color.medium_green));
                    }
                } else {
                    if (background instanceof GradientDrawable) {
                        ((GradientDrawable)background).setColor(ContextCompat.getColor(context,R.color.white));
                        ((GradientDrawable)background).setStroke(1,ContextCompat.getColor(context,R.color.grey_hint));
                    }
                }
            }
        }
    }

    public List<Option> getVariantOptions() {
        return variantOptions;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    public Option getSelectedOption() {
        return variantOptions.get(selectedPosition);
    }

    public interface OnVariantOptionChoosedListener{
        void onVariantChosen(Option option, int level);
    }
}

